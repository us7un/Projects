/**
 * ParkingLotHashTable class implementing all operations needed in the hash table.
 * Some parts may be implemented using pseudocode of hash table structure from resources in the public domain
 * @author ustunyilmaz
 */
public class ParkingLotHashTable {

    private HashNode[] hashTable; // Array of linkedLists holding each head node
    private int capacity = 20; // Initial capacity of the hashTable
    int size = 0; // Initial size of the hashTable

    /**
     * Constructor method to initialize the array of linkedLists
     */
    public ParkingLotHashTable() {
        this.hashTable = new HashNode[capacity];
    }

    /**
     * Hash function that uses the builtin hashCode() method to efficiently hash keys (capacity constraints)
     * @param capacityConstraint Capacity constraint of parking lot
     * @return The hash value
     * @see Object#hashCode()
     */
    private int hashFunction(Integer capacityConstraint) {
        return Math.abs(capacityConstraint.hashCode()) % capacity; // Always take the mod to ensure no OOB exception
    }

    /**
     * Method to efficiently insert a parkingLot to the hash table
     * @param capacityConstraint Capacity constraint of parking lot, held separately for easy access
     * @param parkingLot Parking lot (queue) to be inserted
     */
    public void insert(Integer capacityConstraint, TruckQueue parkingLot) {
        if ((size + 1) / (double) capacity > 0.75){
            resize(); // Resize if loadFactor > 3/4
        }

        int i = hashFunction(capacityConstraint); // Get the index (hash) of the node
        if (hashTable[i] == null) {
            hashTable[i] = new HashNode(capacityConstraint, parkingLot, null); // If it does not exist in the table, simply insert it
        }
        // If a collision exists at that index, insert it at the end of the linkedList
        else {
            HashNode collision = hashTable[i]; // The collision at the index
            while (collision != null) { // Traverse through the linkedList until null
                if (collision.capacityConstraint.equals(capacityConstraint)) { // If that parkingLot already exists, do nothing
                    return;
                }
                if (collision.next == null) {
                    break; // End of linked list reached
                }
                collision = collision.next; // Get to the next node if there are more collisions
            }
            if (collision != null)
                collision.next = new HashNode(capacityConstraint, parkingLot, null); // Insert the node at the end of linkedList
        }
        size++;
    }

    /**
     * Method to efficiently delete a parkingLot to the hash table
     * @param capacityConstraint Capacity constraint of the lot
     */
    public void delete(Integer capacityConstraint) {
        int i = hashFunction(capacityConstraint); // Get the index (hash) of the node
        HashNode collision = hashTable[i]; // The collision at the index, can be thought as current node
        HashNode previous = null; // Previous node of the collision
        while (collision != null) {
            if (collision.capacityConstraint.equals(capacityConstraint)) {
                if (previous == null) {
                    hashTable[i] = collision.next; // Set next to head
                } else {
                    previous.next = collision.next; // Lazy deletion
                }
                size--;
                return;
            }
            // If it is not equal to our constraint, continue traversing
            previous = collision;
            collision = collision.next;
        }
    }

    /**
     * Method that resizes the hash table if a certain load factor threshold is exceeded
     */
    private void resize() {
        int finalCapacity = 2 * capacity; // Double the capacity of the hash table
        HashNode[] finalTable = new HashNode[finalCapacity];

        // Rehash every element in the initial table to its corresponding index in the final one
        for (int i = 0; i < capacity; i++) {
            HashNode current = hashTable[i];
            while (current != null) {
                HashNode next = current.next;
                int reHash = Math.abs(current.capacityConstraint.hashCode()) % finalCapacity;
                current.next = finalTable[reHash];
                finalTable[reHash] = current;
                current = next;
            }
        }
        hashTable = finalTable; // Set the reference of hashTable to the final state of the table
        capacity = finalCapacity; // Assign the doubled capacity
    }

    /**
     * Method that returns the value (parkingLot) of the capacity constraint entered
     * @param capacityConstraint Key of the parking lot
     * @return If found parkingLot else null
     */
    public TruckQueue search(Integer capacityConstraint) {
        int i = hashFunction(capacityConstraint); // Get the index of the lot
        HashNode current = hashTable[i]; // Get the head of that index

        // Traverse the linkedList until a match has been found and return it
        while (current != null) {
            if (current.capacityConstraint.equals(capacityConstraint)) {
                return current.parkingLot;
            }
            current = current.next;
        }
        return null; // Not found
    }

    /**
     * Method that tells if a key exists in our hash table
     * @param capacityConstraint Key of the parking lot
     * @return If found true else false
     */
    public boolean has(Integer capacityConstraint) {
        return search(capacityConstraint) != null;
    }

}
