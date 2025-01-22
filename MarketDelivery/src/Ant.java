import java.util.ArrayList;

/**
 * Ant class to be used particularly in an ant-colony optimization algorithm for a TSP (Travelling Salesman Problem)
 *
 * @author ustunyilmaz
 * @version 1.0.0
 */
public class Ant {

    public int startingHouse; // ID of the house an ant starts its path
    public int houseAt; // ID of the house an ant is at
    public ArrayList<Integer> housesBeen = new ArrayList<>(); // Path traversed by the ant

    /**
     * Constructor that assigns the starting house to proper data-fields for an ant
     *
     * @param houseAt starting house
     */
    public Ant(int houseAt) {
        startingHouse = houseAt;
        this.houseAt = houseAt;
        housesBeen.add(houseAt);
    }

}
