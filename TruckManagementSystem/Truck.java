/**
 * Truck class that stores corresponding fields of trucks to be used in the truckStop.
 * @author ustunyilmaz
 */
public class Truck {
    int ID;
    int maxCapacity;
    int load;

    /**
     * Constructor method to initialize the truck
     * @param ID Unique identifying number
     * @param maxCapacity How much the truck can handle
     * @param load How much it currently has
     */
    public Truck(int ID, int maxCapacity, int load) {
        this.ID = ID;
        this.maxCapacity = maxCapacity;
        this.load = load;
    }

    /**
     * Method to load the truck
     * @param load How much we want to load the truck
     * @return How much it can be loaded
     */
    public int loadTruck(int load) {
        if (this.load + load <= maxCapacity) {
            this.load += load; // Fill the truck
            return load; // Return how much it's loaded
        } else { // Load is more than the truck can handle
            int initialLoad = this.load;
            this.load = maxCapacity; // Load the truck to the brink of its capacity
            return maxCapacity - initialLoad; // Return how much it's loaded
        }
    }

    /**
     * Method to unload the truck, resetting its load back to 0
     */
    public void unloadTruck() {
        this.load = 0;
    }

    /**
     * Method to check if truck is fully loaded
     * @return If full true else false
     */
    public boolean isFull() {
        return load == maxCapacity;
    }
}
