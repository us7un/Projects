import java.io.*;
import java.util.*;

/**
 * @author ulaskemec, ustunyilmaz
 * @version w.i.p
 * Code that takes input from a text file and displays closest/farthest cities as circles
 */

public class Main {
    /**
     *
     * @param args Main arguments are not used
     * @throws FileNotFoundException if no input.txt
     */
    public static void main(String[] args) throws FileNotFoundException {
        File file = new File("input.txt");
        Random random = new Random();
        Scanner fileInput = new Scanner(file);
        ArrayList<City> cities = new ArrayList<>();
        while (fileInput.hasNext()) {
            String[] line = fileInput.nextLine().split(" ");
            int population = Integer.parseInt(line[3]);
            double x = Double.parseDouble(line[1]);
            double y = Double.parseDouble(line[2]);
            String name = line[0];
            cities.add(new City(population, x, y, name));
        }
        int height = 800;
        int width = 800;
        StdDraw.setCanvasSize(width, height);
        StdDraw.setYscale(10, 35);
        StdDraw.setXscale(30, 55);
        for (City city : cities) {
            StdDraw.setPenColor(random.nextInt(100, 255), random.nextInt(100, 255), random.nextInt(100, 225));
            StdDraw.filledCircle(city.getX(), city.getY(), Math.sqrt(city.getPopulation()) / 120.0);
        }
        double minimumDistance = Double.MAX_VALUE;
        double maximumDistance = Double.MIN_VALUE;
        int[] minimumIndex = {-1, -1};
        int[] maximumIndex = {-1, -1};
        for (int i = 0; i < cities.size(); i++) {
            for (int j = 0; j < cities.size(); j++) {
                if (i != j) {
                    if (cities.get(i).distanceTo(cities.get(j)) < minimumDistance) {
                        minimumDistance = cities.get(i).distanceTo(cities.get(j));
                        minimumIndex[0] = i;
                        minimumIndex[1] = j;
                    }
                    if (cities.get(i).distanceTo(cities.get(j)) > maximumDistance) {
                        maximumDistance = cities.get(i).distanceTo(cities.get(j));
                        maximumIndex[0] = i;
                        maximumIndex[1] = j;
                    }
                }
            }
        }
        StdDraw.setPenRadius(0.006);
        StdDraw.setPenColor(random.nextInt(0, 255), random.nextInt(0, 255), random.nextInt(0, 255));
        StdDraw.line(cities.get(minimumIndex[0]).getX(), cities.get(minimumIndex[0]).getY(), cities.get(minimumIndex[1]).getX(), cities.get(minimumIndex[1]).getY());
        StdDraw.text(cities.get(minimumIndex[0]).getX() - 5, cities.get(minimumIndex[0]).getY() - 3, "Min Distance: " + String.format("%.2f", minimumDistance));
        StdDraw.setPenColor(random.nextInt(0, 255), random.nextInt(0, 255), random.nextInt(0, 255));
        StdDraw.line(cities.get(maximumIndex[0]).getX(), cities.get(maximumIndex[0]).getY(), cities.get(maximumIndex[1]).getX(), cities.get(maximumIndex[1]).getY());
        StdDraw.text(cities.get(maximumIndex[0]).getX() + 5, cities.get(maximumIndex[0]).getY() - 3, "Max Distance: " + String.format("%.2f", maximumDistance));
        for (City city : cities) {
            StdDraw.setPenColor();
            StdDraw.text(city.getX(), city.getY(), city.getName());
        }
    }
}