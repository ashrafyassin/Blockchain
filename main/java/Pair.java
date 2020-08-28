import java.util.Map;

public class Pair<K,V> implements Map.Entry{
    private K key;
    private V val;
    public Pair(K key,V val){
        this.key = key;
        this.val = val;
    }
    @Override
    public Object getKey() {
        return key;
    }

    @Override
    public Object getValue() {
        return val;
    }

    @Override
    public Object setValue(Object value) {
        val = (V) value;
        return null;
    }
    @Override
    public String toString(){
        return key.toString()+"," +val.toString();
    }
}
