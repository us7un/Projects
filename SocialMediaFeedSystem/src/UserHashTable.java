import java.util.ArrayList;
import java.util.NoSuchElementException;

/**
 * Linear probing hashTable to store UserNode objects with needed operations implemented efficiently.
 *
 * @author ustunyilmaz
 */
public class UserHashTable {
    private static final UserNode TOMBSTONE = new UserNode("abcdef&$)(/?-!fedcba"); // Mark the deleted entries as a tombstone
    private UserNode[] hashTable;
    private int capacity = 20;
    private int size = 0;

    /**
     * Constructor method to initialize the hashTable
     */
    public UserHashTable() {
        this.hashTable = new UserNode[capacity];
    }

    /**
     * Method that returns a unique index corresponding to the "hash" of the key
     *
     * @param ID Unique key to identify a user
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
     * Method to insert a UserNode into the hashTable efficiently
     *
     * @param user UserNode to be inserted
     * @throws IllegalArgumentException If the key already exists, log the error
     */
    public void insert(UserNode user) throws IllegalArgumentException {
        // Resize after a certain loadFactor is reached
        if ((size + 1) / (double) capacity > 0.3) {
            resize();
        }
        int index = hashFunction(user.getID()); // Index of the node to be inserted at
        // If there exists a collision, probe until a suitable spot is found
        while (hashTable[index] != null && hashTable[index] != TOMBSTONE) {
            if (hashTable[index].getID().equals(user.getID())) {
                throw new IllegalArgumentException(); // User already exists in the hashTable
            }
            index = probe(index);
        }
        hashTable[index] = user; // Insert the user into the hashTable
        size++;
    }

    /**
     * Method to delete a UserNode from the hashTable efficiently
     *
     * @param user UserNode to be deleted
     * @throws NoSuchElementException If the key does not exist, log the error
     */
    public void delete(UserNode user) throws NoSuchElementException {
        int index = hashFunction(user.getID()); // Proposed index of the node we want to delete
        int originalIndex = index; // Save the original index to detect a full cycle
        // Traverse the hashTable until the user is found
        while (hashTable[index] != null) {
            if (hashTable[index] != TOMBSTONE && hashTable[index].getID().equals(user.getID())) {
                hashTable[index] = TOMBSTONE; // Mark the deleted place as tombstone
                size--;
                return;
            }
            index = probe(index);
            // Break the loop if the whole table has been searched but the user was not found
            if (index == originalIndex) {
                break;
            }
        }
        throw new NoSuchElementException(); // User not found in the hashTable
    }

    /**
     * Method to resize and rehash the hashTable abiding by linear probing implementation
     */
    private void resize() {
        int finalCapacity = 2 * capacity; // Double the capacity
        UserNode[] finalTable = new UserNode[finalCapacity];

        // Rehash all non-deleted users into the new table
        for (int i = 0; i < capacity; i++) {
            if (hashTable[i] != null && hashTable[i] != TOMBSTONE) {
                int index = Math.abs(hashTable[i].getID().hashCode()) % finalCapacity; // Rehash the element
                while (finalTable[index] != null) {
                    index = (index + 1) % finalCapacity; // The probe method is not suitable here since we are dealing with finalCapacity
                }
                finalTable[index] = hashTable[i];
            }
        }
        // Assign the newly resized table array to the hashTable
        hashTable = finalTable;
        capacity = finalCapacity;
    }

    /**
     * Method to efficiently get an element from the hashTable
     *
     * @param ID Unique key corresponding to the user
     * @return The UserNode we want to get that has the corresponding ID
     * @throws NoSuchElementException If the key does not exist, log the error
     */
    public UserNode get(String ID) throws NoSuchElementException {
        int index = hashFunction(ID);
        int originalIndex = index;
        // Traverse the hashTable until the user is found
        while (hashTable[index] != null) {
            if (hashTable[index] != TOMBSTONE && hashTable[index].getID().equals(ID)) {
                return hashTable[index];
            }
            index = probe(index);
            // Break the loop if the whole table has been searched but the user was not found
            if (index == originalIndex) {
                break;
            }
        }
        throw new NoSuchElementException(); // User not found in the hashTable
    }

    /**
     * Method to determine if the hashTable has a certain element corresponding to the given key without throwing any exception
     *
     * @param ID Unique key of the user we want to check
     * @return If it has the user, true else false
     */
    public boolean has(String ID) {
        int index = hashFunction(ID);
        int originalIndex = index;
        // Traverse the table until the element is found
        while (hashTable[index] != null) {
            if (hashTable[index] != TOMBSTONE && hashTable[index].getID().equals(ID)) {
                return true;
            }
            index = probe(index);
            // Break the loop if the whole table has been searched but the user was not found
            if (index == originalIndex) {
                break;
            }
        }
        return false; // User not found in the table
    }

    /**
     * Method to get the size of the hashTable
     *
     * @return Size of the hashTable
     */
    public int getSize() {
        return size;
    }

    /**
     * Method to convert the hashTable into an array of UserNode objects
     *
     * @return UserNode[] of all the non-deleted users in the hashTable
     */
    public UserNode[] getAllContents() {
        ArrayList<UserNode> resultList = new ArrayList<>(); // Create an arrayList to hold suitable users

        // Traverse through the hashTable and get every non-null or non-deleted entry
        for (int i = 0; i < capacity; i++) {
            if (hashTable[i] != null && hashTable[i] != TOMBSTONE) {
                resultList.add(hashTable[i]); // Add these entries to the arrayList
            }
        }
        return resultList.toArray(new UserNode[0]); // Convert the arrayList to array
    }
}