/**
 * AVLNode class in which the nodes hold the required data-fields of parking lots, with parent-child relationship
 * @author ustunyilmaz
 */
public class AVLNode {
    Integer capacityConstraint; // Capacity constraint of the parking lot, stored separately for easy access
    TruckQueue parkingLot;
    int height;
    AVLNode left; // Left child
    AVLNode right; // Right child
    AVLNode parent; // Parent

    /**
     * Constructor to initialize the relevant fields
     * @param capacityConstraint Key of the parking lot
     * @param parkingLot Parking lot
     */
    public AVLNode(Integer capacityConstraint, TruckQueue parkingLot) {
        this.capacityConstraint = capacityConstraint;
        this.parkingLot = parkingLot;
        this.height = 1; // Set height to 1
    }
}
