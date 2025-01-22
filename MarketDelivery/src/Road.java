import java.util.ArrayList;

/**
 * Road class to be used particularly in an ant-colony optimization algorithm for a TSP (Travelling Salesman Problem)
 *
 * @author ustunyilmaz
 * @version 1.0.0
 */
public class Road {
    public final double distance; // Distance of the road
    public ArrayList<Integer> houses = new ArrayList<>(2); // Houses the road contains, always of size 2 in this problem
    private double pheromoneLevel; // Pheromone level of the road

    /**
     * Constructor that assigns the houses, the pheromone level and the distance of the road
     *
     * @param house1           first house
     * @param house2           second house
     * @param initialPheromone initial pheromone level
     */
    public Road(House house1, House house2, double initialPheromone) {
        pheromoneLevel = initialPheromone;
        houses.add(house1.houseNumber());
        houses.add(house2.houseNumber());
        distance = house1.euclideanDistance(house2); // Calculate distance using the euclideanDistance method in House class
    }

    /**
     * Getter for pheromone level
     *
     * @return pheromone level
     */
    public double getPheromoneLevel() {
        return pheromoneLevel;
    }

    /**
     * Setter for pheromone level
     *
     * @param pheromoneLevel value to be assigned
     */
    public void setPheromoneLevel(double pheromoneLevel) {
        this.pheromoneLevel = pheromoneLevel;
    }

    /**
     * Method to evaporate pheromones after each iteration
     *
     * @param evaporationFactor factor to be multiplied with
     */
    public void evaporate(double evaporationFactor) {
        this.pheromoneLevel *= evaporationFactor;
    }


}
