public class City {
    public int population;
    public double x, y;
    public String name;

    public City(int population, double x, double y, String name) {
        this.population = population;
        this.x = x;
        this.y = y;
        this.name = name;
    }

    public double distanceTo(City city) {
        return Math.sqrt(Math.pow(this.x - city.x, 2) + Math.pow(this.y - city.y, 2));
    }

    @Override
    public String toString() {
        return "City: " + name +
                " Population: " + population +
                " X-coordinate: " + x +
                " Y-coordinate: " + y;
    }

    public int getPopulation() {
        return population;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public String getName() {
        return name;
    }
}
