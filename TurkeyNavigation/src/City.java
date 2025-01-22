import java.util.ArrayList;

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

    public double euclideanDistance(double x0, double y0, double x1, double y1) {
        return Math.sqrt(Math.pow(Math.abs(x1 - x0), 2) + Math.pow(Math.abs(y1 - y0), 2));
    }

    @Override
    public String toString() {
        return "City{" +
                "cityName='" + cityName + '\'' +
                '}';
    }

    public boolean hasConnectionWith(ArrayList<ArrayList<City>> connections, City otherCity) {
        for (ArrayList<City> connection : connections) {
            if (connection.contains(this) && connection.contains(otherCity)) {
                return true;
            }
        }
        return false;
    }

    public double distanceTo(ArrayList<ArrayList<City>> connections, City otherCity) {
        if (hasConnectionWith(connections, otherCity)) {
            return euclideanDistance(this.x, this.y, otherCity.x, otherCity.y);
        } else return Double.MAX_VALUE;
    }
}
