import java.util.NoSuchElementException;

/**
 * Linear probing hashTable to store PostNode objects with needed operations implemented efficiently.
 *
 * @author ustunyilmaz
 */
public class PostHashTable {
    private static final PostNode TOMBSTONE = new PostNode("abcdef&$)(/?-!fedcba", null, null); // Mark the deleted entries as a tombstone
    private PostNode[] hashTable;
    private int capacity = 20;
    private int size = 0;

    /**
     * Constructor method to initialize the hashTable
     */
    public PostHashTable() {
        this.hashTable = new PostNode[capacity];
    }

    /**
     * Method that returns a unique index corresponding to the "hash" of the key
     *
     * @param ID Unique key to identify a post
     * @return The corresponding "hash" i.e. index
     */
    private int hashFunction(String ID) {
        return Math.abs(ID.hashCode()) % capacity;
    }

    /**
     * Method to linearly probe the index if the initial place is full
     *
     * @param index Current index
     * @return The linearly probed index
     */
    private int probe(int index) {
        return (index + 1) % capacity;
    }

    /**
     * Method to insert a PostNode into the hashTable efficiently
     *
     * @param post PostNode to be inserted
     * @throws IllegalArgumentException If the key already exists, log the error
     */
    public void insert(PostNode post) throws IllegalArgumentException {
        // Resize after a certain loadFactor is reached
        if ((size + 1) / (double) capacity > 0.3) {
            resize();
        }
        int index = hashFunction(post.getID()); // Index of the node to be inserted at
        // If there exists a collision, probe until a suitable spot is found
        while (hashTable[index] != null && hashTable[index] != TOMBSTONE) {
            if (hashTable[index].getID().equals(post.getID())) {
                throw new IllegalArgumentException(); // Post already exists in the hashTable
            }
            index = probe(index);
        }
        hashTable[index] = post; // Insert the post into the hashTable
        size++;
    }

    /**
     * Method to resize and rehash the hashTable abiding by linear probing implementation
     */
    private void resize() {
        int finalCapacity = 2 * capacity; // Double the capacity
        PostNode[] finalTable = new PostNode[finalCapacity];

        // Rehash all non-deleted posts into the new table
        for (int i = 0; i < capacity; i++) {
            if (hashTable[i] != null && hashTable[i] != TOMBSTONE) {
                int index = Math.abs(hashTable[i].getID().hashCode()) % finalCapacity; // Rehash the element
                while (finalTable[index] != null) {
                    index = (index + 1) % finalCapacity; // The probe method is not suitable here since we are dealing with finalCapacity
                }
                finalTable[index] = hashTable[i]; // Rehash post into new table
            }
        }
        // Assign the newly resized table array to the hashTable
        hashTable = finalTable;
        capacity = finalCapacity;
    }

    /**
     * Method to efficiently get an element from the hashTable
     *
     * @param ID Unique key corresponding to the post
     * @return The PostNode we want to get that has the corresponding ID
     * @throws NoSuchElementException If the key does not exist, log the error
     */
    public PostNode get(String ID) throws NoSuchElementException {
        int index = hashFunction(ID);
        int originalIndex = index;
        // Traverse the hashTable until the post is found
        while (hashTable[index] != null) {
            if (hashTable[index] != TOMBSTONE && hashTable[index].getID().equals(ID)) {
                return hashTable[index];
            }
            index = probe(index);
            // Break the loop if the whole table has been searched but the post was not found
            if (index == originalIndex) {
                break;
            }
        }
        throw new NoSuchElementException(); // Post not found in the hashTable
    }
}