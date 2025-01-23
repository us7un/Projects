/**
 * Class to comprehensively create a truckStop where trucks will be loaded and their tasks will be managed
 * @author ustunyilmaz
 */
public class TruckStop {
    // Declare and initialize the table and the so-called "AVL forest"
    ParkingLotHashTable parkingLotTable = new ParkingLotHashTable(); // HashTable to store parking lots
    ParkingLotAVLTree parkingLotTree = new ParkingLotAVLTree(); // AVL tree to store Parking lots
    ParkingLotAVLTree notFull = new ParkingLotAVLTree(); // Not full parking lots
    ParkingLotAVLTree waitingNonEmpty = new ParkingLotAVLTree(); // Parking lots with waiting trucks
    ParkingLotAVLTree readyNonEmpty = new ParkingLotAVLTree(); // Parking lots with ready trucks
    ParkingLotAVLTree notEmpty = new ParkingLotAVLTree(); // Parking lots with trucks

    /**
     * Method to create a parking lot and add it to its corresponding structures
     *
     * @param capacityConstraint Minimum capacity of trucks needed, also maximum capacity of loading one truck
     * @param truckLimit         Maximum number of trucks that can be added
     */
    public void createParkingLot(int capacityConstraint, int truckLimit) {
        TruckQueue parkingLot = new TruckQueue(capacityConstraint, truckLimit);
        // Insert the parking lot
        parkingLotTable.insert(capacityConstraint, parkingLot);
        AVLNode newNodeForTree = new AVLNode(capacityConstraint, parkingLot);
        AVLNode newNodeForNotFull = new AVLNode(capacityConstraint, parkingLot);
        parkingLotTree.root = parkingLotTree.insert(parkingLotTree.root, newNodeForTree);
        notFull.root = notFull.insert(notFull.root, newNodeForNotFull);
    }

    /**
     * Method to delete a parking lot from its corresponding structures
     *
     * @param capacityConstraint Key of the parking lot
     */
    public void deleteParkingLot(int capacityConstraint) {
        // Delete the parking lot
        parkingLotTable.delete(capacityConstraint);
        parkingLotTree.root = parkingLotTree.delete(parkingLotTree.root, capacityConstraint);
        notFull.root = notFull.delete(notFull.root, capacityConstraint);
        waitingNonEmpty.root = waitingNonEmpty.delete(waitingNonEmpty.root, capacityConstraint);
        readyNonEmpty.root = readyNonEmpty.delete(readyNonEmpty.root, capacityConstraint);
        notEmpty.root = notEmpty.delete(notEmpty.root, capacityConstraint);
    }

    /**
     * Method to add a truck with given properties to the parking lot with given constraints, if no such lot exists,
     * the method tries to move on to the next largest parking lot
     *
     * @param ID          Truck ID
     * @param maxCapacity Maximum capacity the truck can handle
     * @param load        Portion of the truck that's already loaded/unavailable
     * @return If found, "capacityConstraint" else "-1"
     */
    public String addTruck(int ID, int maxCapacity, int load) {
        int requiredCapacity = maxCapacity - load;
        if (parkingLotTable.has(requiredCapacity)) {
            TruckQueue parkingLot = parkingLotTable.search(requiredCapacity); // Get that exact parking lot
            if (!parkingLot.isFull()) {
                int constraint = parkingLot.capacityConstraint;
                Truck newTruck = new Truck(ID, maxCapacity, load); // Truck to be added
                parkingLot.enQueueWaiting(newTruck); // Enqueue the truck in the waiting line
                if (!waitingNonEmpty.has(waitingNonEmpty.root, constraint)) {
                    AVLNode newNodeForWaitingNonEmpty = new AVLNode(constraint, parkingLot); // Add the parking lot to waitingNonEmpty
                    waitingNonEmpty.root = waitingNonEmpty.insert(waitingNonEmpty.root, newNodeForWaitingNonEmpty);
                }
                if (!notEmpty.has(notEmpty.root, constraint)){
                    AVLNode newNodeForNotEmpty = new AVLNode(constraint,parkingLot); // Add the parking lot to notEmpty
                    notEmpty.root = notEmpty.insert(notEmpty.root, newNodeForNotEmpty);
                }

                if (parkingLot.isFull()) {
                    notFull.root = notFull.delete(notFull.root, constraint); // Delete from notFull tree
                }
                return Integer.toString(constraint);
            }
        }
        AVLNode largestSmaller = notFull.findOneSmaller(notFull.root, requiredCapacity); // Get the largestSmaller parking lot that is not full
        if (largestSmaller != null) {
            int constraint = largestSmaller.capacityConstraint;
            TruckQueue largestSmallerLot = largestSmaller.parkingLot; // This step is crucial so wrong assignments are done
            Truck newTruck2 = new Truck(ID, maxCapacity, load); // Truck to be added
            largestSmallerLot.enQueueWaiting(newTruck2); // Enqueue the truck in the waiting line
            if (!waitingNonEmpty.has(waitingNonEmpty.root, constraint)) {
                AVLNode newNodeForWaitingNonEmpty2 = new AVLNode(constraint, largestSmallerLot); // Add the parking lot to waitingNonEmpty
                waitingNonEmpty.root = waitingNonEmpty.insert(waitingNonEmpty.root, newNodeForWaitingNonEmpty2);
            }
            if (!notEmpty.has(notEmpty.root, constraint)){
                AVLNode newNodeForNotEmpty = new AVLNode(constraint,largestSmallerLot); // Add the parking lot to notEmpty
                notEmpty.root = notEmpty.insert(notEmpty.root, newNodeForNotEmpty);
            }

            if (largestSmaller.parkingLot.isFull()) {
                notFull.root = notFull.delete(notFull.root, constraint); // Delete from notFull tree
            }
            return Integer.toString(constraint);
        }
        return "-1"; // No available lot exists
    }

    /**
     * Method to mark the earliest waiting truck in the lot with the given constraint as ready,
     * if no such lot exists, the method moves on to the next smallest lot
     *
     * @param capacityConstraint Key of the parking lot
     * @return If found, "TruckID capacityConstraint" else "-1"
     */

    public String[] ready(int capacityConstraint) {
        TruckQueue parkingLot = parkingLotTable.search(capacityConstraint); // Get a possibly eligible lot from the hashTable
        if (parkingLot != null && !parkingLot.waitingEmpty()) {
            int constraint = parkingLot.capacityConstraint;
            Truck earliestWaiting = parkingLot.deQueueWaiting(); // Dequeue the truck from the waiting section
            parkingLot.enQueueReady(earliestWaiting); // Move the truck to the ready section
            if (!readyNonEmpty.has(readyNonEmpty.root, constraint)) {
                AVLNode newNodeForReadyNonEmpty = new AVLNode(capacityConstraint, parkingLot); // Add the parking lot to readyNonEmpty tree
                readyNonEmpty.root = readyNonEmpty.insert(readyNonEmpty.root, newNodeForReadyNonEmpty);
            }
            if (parkingLot.waitingEmpty()) {
                waitingNonEmpty.root = waitingNonEmpty.delete(waitingNonEmpty.root, constraint); // Delete from waitingNonEmpty tree
            }
            return new String[]{Integer.toString(earliestWaiting.ID), Integer.toString(constraint)};
        }
        AVLNode smallestLarger = waitingNonEmpty.findOneLarger(waitingNonEmpty.root, capacityConstraint); // Get the smallest larger lot from the waitingNonEmpty tree
        if (smallestLarger != null) { // If no smallestLarger is found then we're done
            int constraint = smallestLarger.capacityConstraint;
            TruckQueue smallestLargerLot = smallestLarger.parkingLot; // This step is crucial so no wrong assignments are done
            Truck earliestWaiting = smallestLargerLot.deQueueWaiting(); // Dequeue the truck from the waiting section
            smallestLargerLot.enQueueReady(earliestWaiting); // Move the truck to the ready section
            if (!readyNonEmpty.has(readyNonEmpty.root, constraint)) {
                AVLNode newNodeForReadyNonEmpty = new AVLNode(constraint, smallestLargerLot); // Add the parking lot to readyNonEmpty tree
                readyNonEmpty.root = readyNonEmpty.insert(readyNonEmpty.root, newNodeForReadyNonEmpty);
            }
            if (smallestLargerLot.waitingEmpty()) {
                waitingNonEmpty.root = waitingNonEmpty.delete(waitingNonEmpty.root, constraint); // Delete from waitingNonEmpty tree
            }
            return new String[]{Integer.toString(earliestWaiting.ID), Integer.toString(constraint)};
        }
        return new String[]{"-1"}; // No available lot exists
    }

    /**
     * Method that loads the first ready truck in the parking lot with given capacity constraint, with the given load
     *
     * @param capacityConstraint Key of the parking lot
     * @param remaining          // Load to be loaded
     * @return If loaded, "TruckID capacityConstraint - TruckID2 capacityConstraint2 - ..." else "-1"
     */
    public String load(int capacityConstraint, int remaining) {
        StringBuilder stringBuilder = new StringBuilder(); // StringBuilder for output
        TruckQueue parkingLot1 = parkingLotTable.search(capacityConstraint); // Get the exact parking lot
        if (parkingLot1 != null) {
            int constraint = parkingLot1.capacityConstraint;
            while (remaining > 0 && !parkingLot1.readyEmpty()) {
                Truck earliestReady = parkingLot1.deQueueReady(); // Get the earliest ready truck
                int maxLoadPossible = Math.min(remaining, constraint); // You can only load what the parking lot can handle
                int loaded = earliestReady.loadTruck(maxLoadPossible); // Load the truck with the loadTruck method which also returns how much you've loaded in it
                remaining -= loaded;
                if (earliestReady.isFull()) {
                    earliestReady.unloadTruck(); // Unload the truck, i.e. its load is reset to zero
                }
                if (!notFull.has(notFull.root, constraint)){
                    AVLNode newNodeForNotFull = new AVLNode(constraint, parkingLot1); // Add the parking lot to notFull tree
                    notFull.root = notFull.insert(notFull.root, newNodeForNotFull);
                }
                if (parkingLot1.readyEmpty()){
                    readyNonEmpty.root = readyNonEmpty.delete(readyNonEmpty.root, constraint); // Delete from readyNonEmpty tree
                }
                if (parkingLot1.isEmpty()){
                    notEmpty.root = notEmpty.delete(notEmpty.root, constraint); // Delete from notEmpty tree
                }
                String newLot = addTruck(earliestReady.ID, earliestReady.maxCapacity, earliestReady.load); // Find and return the capacity constraint of the new parking lot of the truck
                stringBuilder.append(earliestReady.ID).append(" ").append(newLot).append(" - "); // Build the output in the format "truckID newLotConstraint - "(note that if no lot is found this constraint will be -1)
                if (remaining == 0) {
                    stringBuilder.delete(stringBuilder.length() - 3, stringBuilder.length()); // Remove the last extra " - " from output
                    return stringBuilder.toString();
                }
            }
        }
        AVLNode smallestLarger = readyNonEmpty.findOneLarger(readyNonEmpty.root, capacityConstraint); // Get the smallestLarger node in readyNonEmpty tree
        while (smallestLarger != null && remaining > 0){
            TruckQueue parkingLot2 = smallestLarger.parkingLot; // This step is crucial so no wrong assignments are done
            int constraint = parkingLot2.capacityConstraint;
            while (remaining > 0 && !parkingLot2.readyEmpty()){
                Truck earliestReady = parkingLot2.deQueueReady(); // Get the earliest ready truck
                int maxLoadPossible = Math.min(remaining, constraint); // You can only load what the parking lot can handle
                int loaded = earliestReady.loadTruck(maxLoadPossible); // Load the truck with the loadTruck method which also returns how much you've loaded in it
                remaining -= loaded;
                if (earliestReady.isFull()){
                    earliestReady.unloadTruck(); // Unload the truck, i.e. its load is reset to zero
                }
                if (!notFull.has(notFull.root, constraint)){
                    AVLNode newNodeForNotFull = new AVLNode(constraint, parkingLot2); // Add the parking lot to notFull tree
                    notFull.root = notFull.insert(notFull.root, newNodeForNotFull);
                }
                if (parkingLot2.readyEmpty()){
                    readyNonEmpty.root = readyNonEmpty.delete(readyNonEmpty.root, constraint); // Delete from readyNonEmpty tree
                }
                if (parkingLot2.isEmpty()){
                    notEmpty.root = notEmpty.delete(notEmpty.root, constraint); // Delete from notEmpty tree
                }
                String newLot = addTruck(earliestReady.ID, earliestReady.maxCapacity, earliestReady.load); // Find and return the capacity constraint of the new parking lot of the truck
                stringBuilder.append(earliestReady.ID).append(" ").append(newLot).append(" - "); // Build the output in the format "truckID newLotConstraint -  "(note that if no lot is found this constraint will be -1)
                if (remaining == 0) {
                    stringBuilder.delete(stringBuilder.length() - 3, stringBuilder.length()); // Remove the last extra " - " from output
                    return stringBuilder.toString();
                }
            }
            smallestLarger = readyNonEmpty.findOneLarger(readyNonEmpty.root, constraint); // Parking lot does not have trucks ready to load, proceed to the next smallestLarger
            }
        if (stringBuilder.length() > 1) { // All load has not been exhausted but still some trucks got loaded
            stringBuilder.delete(stringBuilder.length() - 3, stringBuilder.length()); // Remove the extra " - " from output
            return stringBuilder.toString();
        }
        return "-1"; // Return -1 if no such lot is found and no truck is loaded
    }

    /**
     * Method that returns the number of total trucks in parking lots that have bigger capacity constraints than given
     *
     * @param capacityConstraint Key of the parking lot
     * @return The truck count
     */
    public String count(int capacityConstraint) {
        int truckCount = 0;
        AVLNode smallestLarger = notEmpty.findOneLarger(parkingLotTree.root, capacityConstraint); // Look at all larger parking lots
        while (smallestLarger != null) {
            truckCount += smallestLarger.parkingLot.getSize();
            smallestLarger = notEmpty.inorderSuccessor(smallestLarger);
        }
        return Integer.toString(truckCount);
    }

}
