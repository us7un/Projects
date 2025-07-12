import java.util.ArrayList;

/**
 * Generic implementation of a linear probing hash table for multipurpose usage
 *
 * @param <K> Key we want to hash according to
 * @param <V> Value corresponding to that key
 * @author ustunyilmaz
 */
public class GenericHashTable<K, V> {
    private final int INITIAL_CAPACITY = 20; // Initial capacity of the hash table
    private final float MAX_LOAD_FACTOR = 0.70F; // The load factor we want to resize at
    private int size = 0;
    private Pair<K, V>[] hashTable; // Array of "Pair"s i.e. the hash table array

    /**
     * Constructor method to initialize the hash table
     */
    public GenericHashTable() {
        hashTable = new Pair[INITIAL_CAPACITY];
    }

    /**
     * Method to hash and probe the key to its respective position in the hash table array
     *
     * @param key Key we want to hash according to
     * @return The position of that key in the hash table array
     */
    private int probe(K key) {
        int hashCode = key.hashCode(); // Get the hashCode corresponding to the key
        int index = Math.abs(hashCode) % hashTable.length; // Hash the key accordingly

        // Linearly probe the index until there is no collusion
        while (hashTable[index] != null && (hashTable[index].full && !hashTable[index].key.equals(key))) {
            index = (index + 1) % hashTable.length;
        }
        return index;
    }

    /**
     * Method to double the size of the hash table and rehash if it becomes "too full"
     */
    private void resize() {
        Pair<K, V>[] temp = hashTable;
        hashTable = new Pair[temp.length * 2]; // The new hash table array
        size = 0;

        // Rehash all existing pairs and insert them into the new hash table
        for (Pair<K, V> pair : temp) {
            if (pair != null && pair.full && !pair.deleted) {
                insert(pair.key, pair.value);
            }
        }
    }

    /**
     * Method to insert a new pair into the hash table
     *
     * @param key   Key we want to hash according to
     * @param value Value corresponding to that key
     */
    public void insert(K key, V value) {
        // Resize and rehash if the table becomes too full
        if (size >= hashTable.length * MAX_LOAD_FACTOR) {
            resize();
        }

        // Get the index and probe until there is no collision to finish the insertion
        int index = probe(key);
        // If the key does not exist in the table, simply insert it
        if (hashTable[index] == null || hashTable[index].deleted) {
            hashTable[index] = new Pair<>(key, value);
            size++; // Increment size
        }
        // Inserting an already existing key will modify its value (this is an implementation specific choice!)
        else {
            hashTable[index].value = value;
        }
    }

    /**
     * Method to get the value corresponding to a specific key we want to search
     *
     * @param key Key we want to find the corresponding value of
     * @return That value if the key exists, null else.
     */
    public V get(K key) {
        // Get the index and probe until there is no collision to find the exact value
        int index = probe(key);
        if (hashTable[index] != null && hashTable[index].full && !hashTable[index].deleted) {
            return hashTable[index].value;
        }
        return null;
    }

    /**
     * Method to turn the hash table into an arrayList of its keys
     *
     * @return ArrayList consisting of all the keys
     */
    public ArrayList<K> getAllKeys() {
        ArrayList<K> allKeys = new ArrayList<>();
        for (Pair<K, V> pair : hashTable) {
            if (pair != null && pair.full && !pair.deleted) {
                allKeys.add(pair.key);
            }
        }
        return allKeys;
    }

    /**
     * Pair class to use in the GenericHashTable implementation
     *
     * @param <K> Key we want to hash according to
     * @param <V> Value corresponding to that key
     * @author ustunyilmaz
     */
    private static class Pair<K, V> {
        K key;
        V value;
        boolean full;
        boolean deleted;

        /**
         * Constructor method for Pair class
         *
         * @param key   Key we want to hash according to
         * @param value Value corresponding to that key
         */
        public Pair(K key, V value) {
            this.key = key;
            this.value = value;
            this.full = true;
            this.deleted = false;
        }
    }

}
