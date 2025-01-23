/**
 * HashNode class in which the nodes hold the required data-fields of parking lots, with separate chaining implementation
 * @author ustunyilmaz
 */
public class HashNode {
    Integer capacityConstraint; // Capacity constraint of the parking lot, stored separately for easy access
    TruckQueue parkingLot;
    HashNode next; // Next node, for separate chaining implementation

    /**
     * Constructor method for HashNode
     * @param capacityConstraint Maximum capacity of the parking lot of node, held separately for easy access
     * @param parkingLot Parking lot of node
     * @param next Next pointer to next linkedList node in separate chaining implementation
     */
    public HashNode(Integer capacityConstraint, TruckQueue parkingLot, HashNode next) {
        this.capacityConstraint = capacityConstraint;
        this.parkingLot = parkingLot;
        this.next = next;
    }
}
