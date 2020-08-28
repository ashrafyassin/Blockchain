import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import org.apache.log4j.varia.NullAppender;
import org.apache.zookeeper.*;
import protos.TransactionsGrpc;
import protos.TransactionsProto;

import java.io.IOException;
import java.util.*;


public class TransactionsServer extends TransactionsGrpc.TransactionsImplBase implements Watcher {
    private List<Transaction> transactionsList;
    private List<Block> blocksChain;
    private HashMap<String, List<Map.Entry<Block, Integer>>> receivedBlocks;//TODO validate that every access is synchronized
    private static Integer my_block_seq = -1;//TODO validate that every access is synchronized
    private String max_seq_in_chain = "";
    private Address myAddress;
    private Integer threshold = 100;//starting point

    private HashMap<String, TransactionsGrpc.TransactionsBlockingStub> stubs;
    private static ZooKeeper zk = null;
    private static String blocks_root = "/BLOCKS";
    private static String servers_root = "/OMEGA";
    private HashMap<Integer, Double> users;
    private static Address elected;
    private Server server;
    private boolean startCheckingServersFail = false;
    private Object lock = new Object();
    private Thread adaptive_block_sending_thread, resend_block_thread, zoo_blocks_cleaning_thread;
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";

    public TransactionsServer(Address myAddress, Set<Address> othersAddresses, String zkHost) throws InterruptedException {
        this.myAddress = myAddress;
        transactionsList = new ArrayList<>();
        blocksChain = new ArrayList<>();
        receivedBlocks = new HashMap<>();
        zoo_blocks_cleaning_thread = new Thread();
        resend_block_thread = new Thread();
        zoo_blocks_cleaning_thread = new Thread();

        CreateUsers();
        stubs = new HashMap<>();
        for (Address otherAddress : othersAddresses) {
            String ip = otherAddress.GetIp();
            int port = otherAddress.GetPort();
            ManagedChannel channel = ManagedChannelBuilder
                    .forAddress(ip, port)
                    .intercept(new Decorator(0))
                    .usePlaintext()
                    .build();
            stubs.put(otherAddress.toString(), TransactionsGrpc.newBlockingStub(channel));
        }

        try {
            server = ServerBuilder.forPort(myAddress.GetPort())
                    .addService(this)
                    .intercept(new Interceptor())
                    .build()
                    .start();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        try {
            zk = new ZooKeeper(zkHost, 3000, this);
            elected = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            if (zk.exists(servers_root, true) == null) {
                zk.create(servers_root, new byte[]{}, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }
            if (zk.exists(blocks_root, true) == null) {
                zk.create(blocks_root, new byte[]{}, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }
            zk.getChildren(blocks_root, true);
            zk.getChildren(servers_root, true);
            zk.create(servers_root + "/", myAddress.toString().getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,
                    CreateMode.EPHEMERAL_SEQUENTIAL);
            electLeader();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }
        Thread.sleep(10 * 1000);
        startCheckingServersFail = true;
        startAdapativeBlockSending(1);

    }

    private int countRecveivedBlocks() {
        int count = 0;
        for (List<Map.Entry<Block, Integer>> lst : receivedBlocks.values()) {
            count += lst.size();
        }
        return count;
    }

    private void addBlockByAddress(String addr, Map.Entry<Block, Integer> p) {
        if (receivedBlocks.containsKey(addr)) {
            receivedBlocks.get(addr).add(p);
        } else {
            List<Map.Entry<Block, Integer>> lst = new ArrayList<>();
            lst.add(p);
            receivedBlocks.put(addr, lst);
        }
    }

    @Override
    public void sendTransaction(TransactionsProto.Transaction tr, StreamObserver<TransactionsProto.Reply> responseObserver) {
        Transaction new_tr = new Transaction(tr.getFromId(), tr.getToId(), tr.getFromName(), tr.getToName(), tr.getAmount());
        synchronized (transactionsList) {
            transactionsList.add(new_tr);
        }
        TransactionsProto.Reply rep = TransactionsProto.Reply
                .newBuilder()
                .setServerAddress(myAddress.toString())
                .build();
 //       System.out.println("got a new transaction: " + new_tr);
//        System.out.println("new transactions list: " + transactionsList.toString());
        responseObserver.onNext(rep);
        responseObserver.onCompleted();
    }

    void startAdapativeBlockSending(final int seconds) {
        adaptive_block_sending_thread = new Thread(new Runnable() {
            @Override
            public void run() {
                int count = 0;
                while (true) {
                    boolean empty_list = false;
                    synchronized (transactionsList) {
                        if (transactionsList.size() == 0) empty_list = true;
                    }
                    if (!empty_list) {
                        try {
                            Thread.sleep(10);
                            //adaptive patching
                            if (count++ > 100 * seconds) {
                                System.out.println(ANSI_RED + "passed one sec : "+ threshold +ANSI_RESET);
                                decrease_threshold();
                                count = 0;
                            } else{
                                int size ;
                                synchronized (transactionsList) {
                                     size = transactionsList.size();
                                }
                                if (threshold < size) {
                                    System.out.println(ANSI_GREEN+ "passed the threshold: "+ threshold+ANSI_RESET);
                                    increase_threshold();
                                    count = 0;
                                } else continue;
                            }

                            synchronized (transactionsList) {
                                Integer blockId = ++my_block_seq;

                                Block blk = new Block(transactionsList, blockId);
                                transactionsList.clear();

                                sendBlockToAll(blk);
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        continue;
                    }
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        adaptive_block_sending_thread.start();
    }

    private void sendBlockToAll(Block blk) {
        final Block Theblk = blk;
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (String toAddr : stubs.keySet()) {
                    sendBlockToServer(Theblk, new Address(toAddr));
                }
                addBlockByAddress(myAddress.toString(), new Pair(Theblk, 0));
                System.out.println("Done sending Block to All");
                try {
                    zk.create(blocks_root + "/", (myAddress.toString() + ":" + my_block_seq.toString()).getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,
                            CreateMode.PERSISTENT_SEQUENTIAL);
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void sendBlockToServer(Block blk, Address toAddr) {
        System.out.println("sending the block to port: " + toAddr);
        System.out.println(blk);
        TransactionsProto.Block req = TransactionsProto.Block
                .newBuilder()
                .addAllV(blk.getTransactions())
                .setBlockID(blk.getBlockId())
                .setServerAddress(myAddress.toString())
                .build();
        TransactionsProto.Reply rep = stubs.get(toAddr.toString()).addReceivedBlock(req);
        System.out.println("block added successfully to: " + rep.getServerAddress());
    }

    @Override
    public void addReceivedBlock(protos.TransactionsProto.Block block,
                                 StreamObserver<protos.TransactionsProto.Reply> responseObserver) {
        Block blk = new Block(block);
        System.out.println("got a new block from: " + block.getServerAddress());
        addBlockByAddress(block.getServerAddress(), new Pair<>(blk, 0));

        TransactionsProto.Reply rep = TransactionsProto.Reply
                .newBuilder()
                .setServerAddress(myAddress.toString())
                .build();
        responseObserver.onNext(rep);
        responseObserver.onCompleted();
    }

    @Override
    public void getBlockSeq(protos.TransactionsProto.maxBlockSeq seq,
                            StreamObserver<protos.TransactionsProto.maxBlockSeq> responseObserver) {
        TransactionsProto.maxBlockSeq rep = TransactionsProto.maxBlockSeq
                .newBuilder()
                .setBlockSeq(max_seq_in_chain)
                .build();
        responseObserver.onNext(rep);
        responseObserver.onCompleted();
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        final Event.EventType eventType = watchedEvent.getType();
        System.out.println("\nGot Event from path: " + watchedEvent.getPath() + "\n Event Type: " + eventType);
        String EventPath = watchedEvent.getPath();
        try {
            if (zk.exists(blocks_root, true) != null) {
                zk.getChildren(blocks_root, true);
            }
            if (zk.exists(servers_root, true) != null) {
                zk.getChildren(servers_root, true);
            }
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (EventPath.startsWith(servers_root)) {
            if (startCheckingServersFail) {
                System.out.println("update Subset ....");
                updateStubsSet();
            }

            try {
                if (!myAddress.equals(getLeader())) {
                    electLeader();
                }
            } catch (KeeperException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        } else if (EventPath.equals(blocks_root)) {
            synchronized (lock) {
                processBlockEvent();
            }
        }
    }

    private void processBlockEvent() {
        try {
            System.out.println("Proccessing Block Event");
            List<String> children = zk.getChildren(blocks_root, true);
            Collections.sort(children);
            byte[] src = null;
            String block_seq = "";
            // get the next block
            for (String block : children) {
                if (block.compareTo(max_seq_in_chain) > 0) {//new block seq is greater than mine
                    src = zk.getData(blocks_root + "/" + block, false, null);
                    if (src != null) {
                        block_seq = block;
                        break;
                    }
                }
            }
            // add it to the chain and remove it from the received
            if (src != null) {
                System.out.println("got a block from Zk returns to the server: " + new String(src));
                validate_block_and_add_to_chain(new String(src), block_seq);
            }
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private String getAdressfromData(String src) {
        return src.split(":")[0] + ":" + src.split(":")[1];
    }

    private String getIdfromData(String src) {
        return src.split(":")[2];
    }

    private Map.Entry getReceivedBlock(String key) {
        String addr = getAdressfromData(key);
        Integer id = Integer.parseInt(getIdfromData(key));
        if (!receivedBlocks.containsKey(addr)) {
            System.out.println("didn't find the list of the server to validate");
        }
        System.out.println(receivedBlocks.get(addr));
        for (Map.Entry<Block, Integer> BP : receivedBlocks.get(addr)) {
            if (BP.getKey().getBlockId() == id) {
                return BP;
            }
        }
        return null;
    }

    private boolean validate_block_and_add_to_chain(String key, String block_seq) {
        Block blk;
        Integer first_try;
        System.out.println("validate_block_and_add_to_chain function with key :" + key);
        max_seq_in_chain = block_seq;//TODO check when to really update this field
        synchronized (receivedBlocks) {
            Map.Entry<Block, Integer> BP = getReceivedBlock(key);
            if (BP != null) {
                blk = BP.getKey();
                first_try = BP.getValue();
                receivedBlocks.get(getAdressfromData(key)).remove(BP);
            } else {
                System.out.println("error no block to validate!");
                return false;
            }
        }
        if (validateBlock(blk)) {
            blk.setBlockId(blocksChain.size());
            System.out.println("\n new block in chain : " + blk.toString() + " from:" + key);
            blocksChain.add(blk);
            System.out.println("\n new chain is: " + blocksChain);
            return true;
        } else if (first_try == 0 && getAdressfromData(key).equals(myAddress.toString())) {
            resendRevokedBlock(blk);
            return false;
        }
        return false;
    }

    private boolean validateBlock(Block blk) {
        HashMap<Integer, Double> usersCopy = new HashMap<>(users);
        System.out.println("validating the block ....");

        for (Transaction tr : blk.getTransactionsList()) {
            int fromId = tr.GetFromId();
            int toId = tr.GetToId();
            double amount = tr.GetAmount();
            if (!usersCopy.containsKey(fromId) || !usersCopy.containsKey(toId)) {
                System.out.println("Block not accepted due to wrong IDs");
                return false;
            }
            double fromBalance = usersCopy.get(fromId);
            if (fromBalance - amount >= 0) {
                usersCopy.put(fromId, fromBalance - amount);
                double toBalance = usersCopy.get(toId);
                usersCopy.put(toId, toBalance + amount);
            } else {
                System.out.println("Block not accepted due to high Amount");
                return false;
            }
        }
        users = usersCopy;
        System.out.println(".... block is valid");
        return true;
    }

    void resendRevokedBlock(Block blk) {
        final Block oldblk = blk;
        resend_block_thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Integer blockId = ++my_block_seq;
                Block new_blk = new Block(oldblk, blockId);
                addBlockByAddress(myAddress.toString(), new Pair<Block,Integer>(new_blk, 1));
                sendBlockToAll(new_blk);
            }
        });
        resend_block_thread.start();
    }

    private void periodically_clean_the_zoo() {
        if (zoo_blocks_cleaning_thread.isAlive()) {
            return;
        }
        zoo_blocks_cleaning_thread = new Thread(new Runnable() {
            @Override
            public void run() {
                int seconds = 60;
                int ZOO_MAX_SIZE = 20;
                int count = 0;
                while (true) {
                    try {
                        Thread.sleep(1000);
                        count++;
                        if (count < seconds && zk.getChildren(blocks_root, true).size() < ZOO_MAX_SIZE) continue;
                        count = 0;
                        if (!startCheckingServersFail) {
                            continue;
                        }
                        List<String> blocks_seq = new ArrayList<>();
                        blocks_seq.add(max_seq_in_chain);
                        for (String fromAddr : stubs.keySet()) {
                            String seq = block_seq_progress(new Address(fromAddr));
                            blocks_seq.add(seq);
                        }
                        Collections.sort(blocks_seq, Collections.reverseOrder());
                        delete_zoo_nodes(blocks_seq.get(0));

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (KeeperException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        zoo_blocks_cleaning_thread.start();
    }

    private String block_seq_progress(Address from_addr) {
        TransactionsProto.maxBlockSeq req = TransactionsProto.maxBlockSeq
                .newBuilder()
                .setBlockSeq(max_seq_in_chain)
                .build();
        TransactionsProto.maxBlockSeq rep = stubs.get(from_addr.toString()).getBlockSeq(req);
        System.out.println("got block sequence:" + rep.getBlockSeq() + " got successfully from:" + from_addr);
        return rep.getBlockSeq();
    }

    private void cleanDeadServerReceivedBlocks(String addr) throws KeeperException, InterruptedException {
        List<String> children = zk.getChildren(blocks_root, true);
        List<String> blocksList = new ArrayList<>();
        for (String child : children) {
            byte[] data = zk.getData(blocks_root + "/" + child, true, null);
            if (data != null) {
                String BlockAddr = new String(data);
                if (BlockAddr.equals(addr)) {
                    blocksList.add(getIdfromData(BlockAddr));
                }
            }
        }
        if (receivedBlocks.containsKey(addr)) {
            List<Map.Entry<Block, Integer>> lst = receivedBlocks.get(addr);
            List<Map.Entry<Block, Integer>> newLst = new ArrayList<>();
            for (Map.Entry<Block, Integer> P : lst) {
                if (blocksList.contains(P.getKey().getBlockId())) {
                    newLst.add(P);
                }
            }
            System.out.println("throwing " + Integer.toString(lst.size() - newLst.size()) + " blocks from: " + addr);
            receivedBlocks.put(addr, newLst);
        }
    }

    private void delete_zoo_nodes(String seq) throws KeeperException, InterruptedException {

        List<String> children = zk.getChildren(blocks_root, true);
        Collections.sort(children);

        for (String block : children) {
            if (block.compareTo(seq) > 0) break;//stop when getting to the seq block
            zk.delete(blocks_root + "/" + block, -1);
        }
    }

    private void decrease_threshold() {
        if (threshold > 1) threshold /= 2;
    }

    private void increase_threshold() {
        //threshold = countRecveivedBlocks();
        if (threshold < 1600) threshold *= 2;
        else threshold += 100;
    }

    public void electLeader() throws KeeperException, InterruptedException {
        synchronized (lock) {
            List<String> children = zk.getChildren(servers_root, false);
            Collections.sort(children);
            System.out.println("the children are: " + children.toString());
            byte[] data = null;
            for (String leader : children) {
                data = zk.getData(servers_root + "/" + leader, true, null);
                if (data != null) {
                    break;
                }
            }
            if (data != null) {
                elected = new Address(new String(data));
                System.out.println("leader is " + elected);
                if (myAddress.equals(getLeader())) {
                    periodically_clean_the_zoo();
                }
            }
        }

    }

    public Address getLeader() {
        synchronized (lock) {
            return elected;
        }
    }

    private void CreateUsers() {
        users = new HashMap<>();
        for (int i = 0; i < 50; i++) {
            users.put(i, 10000.0);
        }
    }

    private void updateStubsSet() {
        try {
            List<String> children = zk.getChildren(servers_root, true);
            Set<String> currentAdrresses = new HashSet<>();
            for (String child : children) {
                byte[] data = zk.getData(servers_root + "/" + child, true, null);
                if (data != null) {
                    currentAdrresses.add(new String(data));
                }
            }
            Set<String> addresses = new HashSet<>(stubs.keySet());
            for (String addr : addresses) { // removes stubs of servers that are down
                if (!currentAdrresses.contains(addr)) {
                    System.out.println("removing from" + addresses + "  addr = " + addr);
                    cleanDeadServerReceivedBlocks(addr);
                    stubs.remove(addr);
                }
            }
            System.out.println("new set of addresses: \n" + stubs.keySet());
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    void shutdown() {
        System.out.println("server shutting down ..... bye");
        server.shutdown();
        if (adaptive_block_sending_thread.isAlive()) adaptive_block_sending_thread.interrupt();
        if (resend_block_thread.isAlive()) resend_block_thread.interrupt();
        if (zoo_blocks_cleaning_thread.isAlive()) zoo_blocks_cleaning_thread.interrupt();
    }

    private void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        org.apache.log4j.BasicConfigurator.configure(new NullAppender());
        Address myAddress = new Address("localhost:50051");
        Set<Address> othersAddresses = new HashSet<>();
        System.out.println(args[0]);
        if (args.length > 0) {
            myAddress = new Address(args[1]); /* Use the arg as the name to greet if provided */
            int i = 2;
            while (i < args.length) {
                othersAddresses.add(new Address(args[i]));
                i++;
            }
        }

        final TransactionsServer server = new TransactionsServer(myAddress, othersAddresses, args[0]);

        try {
            server.blockUntilShutdown();
        } finally {
            server.shutdown();
        }
    }

}