/**
 * House class(record) storing data about houses
 *
 * @param houseNumber ID of a house
 * @param houseX      x-coordinate of a house
 * @param houseY      y-coordinate of a house
 * @author ustunyilmaz
 * @version 1.0.0
 * @see java.lang.Record
 */
public record House(int houseNumber, double houseX, double houseY) {
    /**
     * Method that calculates the Euclidean distance between two houses using the Pythagorean Theorem
     *
     * @param house2 second house
     * @return the Euclidean distance between the two houses
     */
    public double euclideanDistance(House house2) {

        return Math.sqrt(Math.pow(Math.abs(this.houseX() - house2.houseX()), 2) + Math.pow(Math.abs(this.houseY() - house2.houseY()), 2));
    }

}
