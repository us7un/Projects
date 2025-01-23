/**
 * TruckQueue class implementing all operations needed in the flexible double circular queue.
 * Some parts may be implemented using pseudocode of circular queue structure from resources in the public domain
 * @author ustunyilmaz
 */
public class TruckQueue {

    int capacityConstraint; // Capacity constraint of the parking lot
    int truckLimit; // Maximum number of trucks it can hold
    private final Truck[] waitingTrucks; // Array holding the waiting line
    private final Truck[] readyTrucks; // Array holding the ready line
    // Front/rear pointers of waiting and ready lines
    private int frontWaiting;
    private int rearWaiting;
    private int frontReady;
    private int rearReady;
    // Sizes of the lines
    private int waitingSize;
    private int readySize;

    /**
     * Constructor that initializes all the required fields
     * @param capacityConstraint Key of the parking lot
     * @param truckLimit Maximum number of trucks it can hold
     */
    public TruckQueue(int capacityConstraint, int truckLimit) {
        this.capacityConstraint = capacityConstraint;
        this.truckLimit = truckLimit;
        this.waitingTrucks = new Truck[truckLimit];
        this.readyTrucks = new Truck[truckLimit];
        // Set pointer indices to -1 since it is empty
        this.frontWaiting = -1;
        this.frontReady = -1;
        this.rearWaiting = -1;
        this.rearReady = -1;
        // Set sizes to 0 since it is empty
        this.waitingSize = 0;
        this.readySize = 0;
    }

    /**
     * Method that returns if the queue is full or not
     * @return if full true else false
     */
    public boolean isFull() {
        return waitingSize + readySize == truckLimit;
    }

    /**
     * Method that returns if the queue is empty or not
     * @return If empty true else false
     */
    public boolean isEmpty() {
        return waitingSize == 0 && readySize == 0;
    }

    /**
     * Method that returns the size of the queue
     * @return How many trucks are in the parking lot i.e. size
     */
    public int getSize() {
        return waitingSize + readySize;
    }

    /**
     * Method that returns if the waiting line is empty or not
     * @return If empty true else false
     */
    public boolean waitingEmpty() {
        return waitingSize == 0;
    }

    /**
     * Method that returns if the ready line is empty or not
     * @return If empty true else false
     */
    public boolean readyEmpty() {
        return readySize == 0;
    }

    /**
     * Method that enqueues a truck in the waiting line of the queue
     * @param truck Truck to be added
     */
    public void enQueueWaiting(Truck truck) {
        if (isFull()) {
            return; // Do nothing
        }
        if (waitingEmpty()) {
            frontWaiting = 0; // Move front pointer from -1 to 0
        }
        rearWaiting = (rearWaiting + 1) % truckLimit; // Modify the rear pointer to abide by circular queue properties
        waitingTrucks[rearWaiting] = truck; // Put the truck in the rear of the waiting line
        waitingSize++;
    }

    /**
     * Method that enqueues a truck in the ready line of the queue
     * @param truck Truck to be added
     */
    public void enQueueReady(Truck truck) {
        if (isFull()) {
            return; // Do nothing
        }
        if (readyEmpty()) {
            frontReady = 0; // Move front pointer from -1 to 0

        }
        rearReady = (rearReady + 1) % truckLimit; // Modify the rear pointer to abide by circular queue properties
        readyTrucks[rearReady] = truck; // Put the truck in the rear of the ready line
        readySize++;
    }

    /**
     * Method that dequeues a truck in the waiting line of the queue
     * @return The truck
     */
    public Truck deQueueWaiting() {
        if (waitingEmpty()) {
            return null;
        } else {
            Truck truck;
            truck = waitingTrucks[frontWaiting]; // Assign the earliest truck to the declaration
            waitingTrucks[frontWaiting] = null; // Remove the truck from the line
            waitingSize--;
            if (waitingEmpty()) {
                // Set the indices back to -1
                frontWaiting = -1;
                rearWaiting = -1;
            } else {
                frontWaiting = (frontWaiting + 1) % truckLimit; // Modify the front pointer to abide by circular queue properties
            }
            return truck;
        }
    }

    /**
     * Method that dequeues a truck in the ready line of the queue
     * @return The truck
     */
    public Truck deQueueReady() {
        if (readyEmpty()) {
            return null;
        } else {
            Truck truck;
            truck = readyTrucks[frontReady]; // Assign the earliest truck to the declaration
            readyTrucks[frontReady] = null; // Remove the truck from the line
            readySize--;
            if (readyEmpty()) {
                // Set the indices back to -1
                frontReady = -1;
                rearReady = -1;
            } else {
                frontReady = (frontReady + 1) % truckLimit; // Modify the front pointer to abide by circular queue properties
            }
            return truck;
        }
    }
}
