import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class HashTable {
    private List<LinkedList<Entry>> table;
    private int size;

    public HashTable(int size) {
        this.size = size;
        table = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            table.add(new LinkedList<>());
        }
    }

    public void put(String key, String value) {
        int index = hash(key) % size;
        LinkedList<Entry> chain = table.get(index);
        for (Entry entry : chain) {
            if (entry.key.equals(key)) {
                entry.value = value;
                return;
            }
        }
        chain.add(new Entry(key, value));
    }

    public String get(String key) {
        int index = hash(key) % size;
        LinkedList<Entry> chain = table.get(index);
        for (Entry entry : chain) {
            if (entry.key.equals(key)) {
                return entry.value;
            }
        }
        return null;
    }

    public boolean delete(String key) {
        int index = hash(key) % size;
        LinkedList<Entry> chain = table.get(index);
        for (int i = 0; i < chain.size(); i++) {
            if (chain.get(i).key.equals(key)) {
                chain.remove(i);
                return true;
            }
        }
        return false;
    }

    private int hash(String key) {
        return key.hashCode();
    }

}