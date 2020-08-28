import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.apache.log4j.varia.NullAppender;
import protos.TransactionsGrpc;
import protos.TransactionsProto;

import java.util.Random;

public class TransactionsClient {
    private TransactionsGrpc.TransactionsBlockingStub stub;
    private ManagedChannel channel;
    public TransactionsClient(Address addres){
        channel = ManagedChannelBuilder
                .forAddress(addres.GetIp(), addres.GetPort())
                .intercept(new Decorator(0))
                .usePlaintext()
                .build();
        stub = TransactionsGrpc.newBlockingStub(channel);
    }
    public void shutdown() {
        System.out.println("finished sending transactions ..... bye");
        channel.shutdown();
    }

    public void sendTransaction(Transaction tr) {
        TransactionsProto.Transaction req = TransactionsProto.Transaction.newBuilder()
                .setFromId(tr.GetFromId())
                .setToId(tr.GetToId())
                .setFromName(tr.GetFromName())
                .setToName(tr.GetToName())
                .setAmount(tr.GetAmount())
                .build();
        TransactionsProto.Reply rep = stub.sendTransaction(req);
        System.out.println("got reply from: " + rep.getServerAddress());
    }

    public static void main(String[] args) throws Exception {
        org.apache.log4j.BasicConfigurator.configure(new NullAppender());
        Address addr = new Address("localhost:50051");
        if (args.length > 0) {
            addr = new Address(args[0]);
            System.out.println("port is: " + addr.GetPort());
            System.out.println("ip is: " + addr.GetIp());

        }
        TransactionsClient client = new TransactionsClient(addr);
        try {
            // Access a service running on the local machine on port 50051
            Random rand = new Random();
            for(int i=0;i<10;i++){
                client.sendTransaction(new Transaction(10,11,"Qasem","Ashraf", 1));
            }
//            Thread.sleep(1000);
//            client.sendTransaction(new Transaction(12,13,"Qasem","Ashraf", 10001));
//            Thread.sleep(2000);
//            client.sendTransaction(new Transaction(1,12,"Qasem","Ashraf", 1));
//            //new Random().nextDouble()*100)
        } finally {
            client.shutdown();
        }
    }
}
