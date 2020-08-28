import protos.TransactionsProto;

import java.util.*;

public class Block {
    private List<Transaction> transactions;
    private int blockId = -1;

    public Block(List<Transaction> transactions,int blockId) {
        this.transactions = new ArrayList<>(transactions);
        this.blockId = blockId;
    }

    public Block(TransactionsProto.Block blk) {
        this.blockId = blk.getBlockID();
        this.transactions = new ArrayList<>();
        for (TransactionsProto.Transaction tr : blk.getVList()) {
            this.transactions.add(new Transaction(tr.getFromId(), tr.getToId(), tr.getFromName(), tr.getToName(), tr.getAmount()));
        }
        this.blockId = blk.getBlockID();
    }
    public  Block (Block blk , int id){
        transactions = new ArrayList<>(blk.transactions);
        blockId = id;
    }
    public List<Transaction> getTransactionsList() {
        return new ArrayList<>(transactions);
    }

    public List<TransactionsProto.Transaction> getTransactions() {
        List<TransactionsProto.Transaction> trans = new ArrayList<>();
        for (Transaction tr : transactions) {
            TransactionsProto.Transaction new_tr = TransactionsProto.Transaction.newBuilder()
                    .setFromId(tr.GetFromId())
                    .setToId(tr.GetToId())
                    .setFromName(tr.GetFromName())
                    .setToName(tr.GetToName())
                    .setAmount(tr.GetAmount())
                    .build();
            trans.add(new_tr);
        }
        return trans;
    }

    public int getBlockId() {
        return blockId;
    }

    public void setBlockId(int blockId) {
        this.blockId = blockId;
    }

    @Override
    public String toString() {
        return "BlockId:" + blockId+" BlockSize:"+transactions.size() ;
    }
}
