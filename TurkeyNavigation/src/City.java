import java.util.ArrayList;

/**
 * City class to represent a city with its name, coordinates and ID.
 * @author ustunyilmaz
 */
public class City {
    public String cityName;
    public int x;
    public int y;
    public int cityNumber;

    public City(String cityName, int x, int y, int cityNumber) {
        this.cityName = cityName;
        this.x = x;
        this.y = y;
        this.cityNumber = cityNumber;
    }

    /**
     * Method to compute distance between two cities
     * @param x0 x-coordinate of source
     * @param y0 y-coordinate of source
     * @param x1 x-coordinate of destination
     * @param y1 y-coordinate of destination
     * @return Euclidean distance between two cities
     */
    public double euclideanDistance(double x0, double y0, double x1, double y1) {
        return Math.sqrt(Math.pow(Math.abs(x1 - x0), 2) + Math.pow(Math.abs(y1 - y0), 2));
    }

    /**
     * Method to check if two cities have a connection between them
     * @param connections ArrayList of city tuples
     * @param otherCity The supposedly adjacent city
     * @return True if adjacent, else false
     */
    public boolean hasConnectionWith(ArrayList<ArrayList<City>> connections, City otherCity) {
        for (ArrayList<City> connection : connections) {
            if (connection.contains(this) && connection.contains(otherCity)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Method to compute distance between two cities
     * @param connections ArrayList of city tuples
     * @param otherCity The second city
     * @return The distance if two cities are adjacent, else infinity
     */
    public double distanceTo(ArrayList<ArrayList<City>> connections, City otherCity) {
        if (hasConnectionWith(connections, otherCity)) {
            return euclideanDistance(this.x, this.y, otherCity.x, otherCity.y);
        } else return Double.MAX_VALUE;
    }
}
