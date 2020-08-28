public class Transaction {
    private int fromId;
    private int toId;
    private double amount;
    private String fromName;
    private String toName;

    Transaction(int fromId,int toId,String fromName,String toName,double amount){
        this.fromId = fromId;
        this.toId = toId;
        this.amount = amount;
        this.fromName = fromName;
        this.toName = toName;
    }
    int GetFromId(){
        return fromId;
    }
    int GetToId(){
        return toId;
    }
    String GetFromName(){
        return fromName;
    }
    String GetToName(){
        return toName;

    }
    double GetAmount(){
        return amount;
    }
    @Override
    public String toString(){
        return "{from:" + fromName + ", to:" + toName + ", amount:" +String.format("%.2f", amount)+ "}";
    }
}
