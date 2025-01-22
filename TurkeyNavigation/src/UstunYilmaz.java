import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * A java program which takes two input files, one containing 'cities' and the latter containing connections.
 * User will input the city names, one source and one destination.
 * The program will draw the shortest path calculated by my variant of 'Floyd-Warshall' algorithm using the StdDraw library if one exists, then tell the path and the distance as output.
 * If no path exists, the canvas won't be drawn and the code will exit.
 *
 * @author ustunyilmaz
 * @version 1.0
 */
public class UstunYilmaz {
    public static void main(String[] args) throws FileNotFoundException {
        String coordinateFileName = "city_coordinates.txt"; // name of the coordinate file
        String connectionFileName = "city_connections.txt"; // name of the connections file

        File coordinateFile = new File(coordinateFileName); // file handler for coordinates
        File connectionFile = new File(connectionFileName); // file handler for connections

        ArrayList<ArrayList<Integer>> coordinates = new ArrayList<>(); // arrayList storing coordinate tuples
        ArrayList<String> cityNames = new ArrayList<>(); // arrayList storing names
        ArrayList<City> cities = new ArrayList<>(); // arrayList storing cities
        ArrayList<ArrayList<City>> connections = new ArrayList<>(); // arrayList storing connections

        Scanner scanCoordinates = new Scanner(coordinateFile); // scanner object for coordinate file
        while (scanCoordinates.hasNextLine()) {
            String[] line = scanCoordinates.nextLine().split(", "); // get next line as an array
            cityNames.add(line[0]); // add the name of the city
            ArrayList<Integer> tuple = new ArrayList<>(); // initialize dummy tuple
            tuple.add(Integer.parseInt(line[1])); // x-coordinate
            tuple.add(Integer.parseInt(line[2])); // y-coordinate
            coordinates.add(tuple); // add the x and y coordinates
        }
        scanCoordinates.close(); // close the file

        for (int i = 0; i < cityNames.size(); i++) {
            cities.add(new City(cityNames.get(i), coordinates.get(i).getFirst(), coordinates.get(i).getLast(), i)); // add the cities
        }

        Scanner scanConnections = new Scanner(connectionFile); // scanner object for connections file
        while (scanConnections.hasNextLine()) {
            String[] line = scanConnections.nextLine().split(","); // get next line as an array
            ArrayList<City> tuple = new ArrayList<>(); // initialize dummy tuple
            for (City city : cities) {
                if (line[0].equals(city.cityName)) {
                    tuple.add(city); // add first city
                } else if (line[1].equals(city.cityName)) {
                    tuple.add(city); // add the city to be connected to
                }
            }
            connections.add(tuple); // add the connection to connections
        }
        scanConnections.close(); // close the file

        Scanner userInput = new Scanner(System.in); // scanner object to take user input
        String startingCity;
        String destinationCity;
        while (true) {
            System.out.print("Enter starting city: ");
            startingCity = userInput.nextLine();
            if (cityNames.contains(startingCity)) {
                break;
            } else {
                System.out.println("City named '" + startingCity + "' not found. Please enter a valid city name.");
            }
        }
        while (true) {
            System.out.print("Enter destination city: ");
            destinationCity = userInput.nextLine();
            if (cityNames.contains(destinationCity)) {
                break;
            } else {
                System.out.println("City named '" + destinationCity + "' not found. Please enter a valid city name.");
            }
        }
        ArrayList<Integer> path = pathfindingAlgorithm(cities, connections, getCity(startingCity, cities), getCity(destinationCity, cities));

        // StdDraw ----------------------------------------------------------------------------------------------------------------
        int pictureWidth = 2377; // width of the picture
        int pictureHeight = 1055; // height of the picture
        int canvasWidth = (int) (pictureWidth / 1.5); // width of the canvas (roughly equal to /1.5 of picture width)
        int canvasHeight = (int) (pictureHeight / 1.5); // height of the canvas (roughly equal to /1.5 of picture height)
        StdDraw.setCanvasSize(canvasWidth, canvasHeight);
        StdDraw.setXscale(0, pictureWidth); // scale of x-coordinates on canvas
        StdDraw.setYscale(0, pictureHeight); // scale of y-coordinates on canvas
        StdDraw.enableDoubleBuffering();
        StdDraw.picture(pictureWidth / 2.0, pictureHeight / 2.0, "map.png", pictureWidth, pictureHeight); // draw the map
        StdDraw.setPenColor(StdDraw.GRAY);
        Font font = new Font("Arial", Font.PLAIN, 15);
        StdDraw.setFont(font);
        for (City city : cities) {
            if (!path.contains(city.cityNumber)) {
                StdDraw.filledCircle(city.x, city.y, 6);
                StdDraw.text(city.x, city.y + 15, city.cityName);
            } else {
                StdDraw.setPenColor(StdDraw.BOOK_LIGHT_BLUE);
                StdDraw.filledCircle(city.x, city.y, 6);
                StdDraw.text(city.x, city.y + 15, city.cityName);
                StdDraw.setPenColor(StdDraw.GRAY);
            }
        }
        for (ArrayList<City> connection : connections) {
            StdDraw.line(connection.getFirst().x, connection.getFirst().y, connection.getLast().x, connection.getLast().y);
        }
        StdDraw.setPenColor(StdDraw.BOOK_LIGHT_BLUE);
        StdDraw.setPenRadius(0.01);
        if (path.size() == 1) {
            StdDraw.filledCircle(getCity(path.getFirst(), cities).x, getCity(path.getFirst(), cities).y, 5);
        }
        for (int i = 0; i < path.size() - 1; i++) {
            StdDraw.line(getCity(path.get(i), cities).x, getCity(path.get(i), cities).y, getCity(path.get(i + 1), cities).x, getCity(path.get(i + 1), cities).y);
        }
        StdDraw.show();
        // StdDraw ----------------------------------------------------------------------------------------------------------------
    }

    /**
     * Method that gets a city using its specified city number.
     *
     * @param cityNo the city number (the index of the city in 'cities' arrayList)
     * @param cities the cities arrayList which holds the cities
     * @return returns the corresponding city of given number, if not found, returns a default 'city'
     */
    public static City getCity(int cityNo, ArrayList<City> cities) {
        for (City city : cities) {
            if (city.cityNumber == cityNo)
                return city;
        }
        return new City("default", 0, 0, -1);
    }

    /**
     * Method that gets a city using its specified city number.
     *
     * @param name   the city name (the name of the city in 'cityNames' arrayList)
     * @param cities the cities arrayList which holds the cities
     * @return returns the corresponding city of given name, if not found, returns a default 'city'
     */
    public static City getCity(String name, ArrayList<City> cities) {
        for (City city : cities) {
            if (city.cityName.equals(name))
                return city;
        }
        return new City("default", 0, 0, -1);
    }

    /**
     * Method that creates an adjacency matrix for easier use in the 'Floyd-Warshall' algorithm
     *
     * @param cities      the cities arrayList which holds the cities
     * @param connections the connections arrayList which holds the connections
     * @return returns the adjacency matrix created
     */
    public static ArrayList<ArrayList<Double>> createAdjacencyMatrix(ArrayList<City> cities, ArrayList<ArrayList<City>> connections) {
        ArrayList<ArrayList<Double>> adjacencyMatrix = new ArrayList<>();
        for (int i = 0; i < cities.size(); i++) {
            ArrayList<Double> initialCondition = new ArrayList<>();
            for (int j = 0; j < cities.size(); j++) {
                initialCondition.add(Double.MAX_VALUE);
            }
            adjacencyMatrix.add(initialCondition);
        }
        for (int i = 0; i < cities.size(); i++) {
            adjacencyMatrix.get(i).set(i, 0.0);
        }
        for (int i = 0; i < cities.size(); i++) {
            for (City city : cities) {
                if (city.hasConnectionWith(connections, getCity(i, cities))) {
                    adjacencyMatrix.get(i).set(city.cityNumber, city.distanceTo(connections, getCity(i, cities)));
                }
            }
        }
        return adjacencyMatrix;
    }

    /**
     * A simple application of 'Floyd-Warshall' pathfinding algorithm for this case, written by me.
     * This method prints the desired output path if there exists one, if there is no path, it exits the code.
     *
     * @param cities      the cities arrayList which holds the cities
     * @param connections the connections arrayList which holds the connections
     * @param a           the 'source' city
     * @param b           the 'destination' city
     * @return returns the shortestPath arrayList containing the indices of the cities in the 'cities' arrayList
     */
    public static ArrayList<Integer> pathfindingAlgorithm(ArrayList<City> cities, ArrayList<ArrayList<City>> connections, City a, City b) {
        ArrayList<ArrayList<Double>> distances = createAdjacencyMatrix(cities, connections);
        int[][] cityPath = new int[cities.size()][cities.size()];
        for (int i = 0; i < cities.size(); i++) {
            for (int j = 0; j < cities.size(); j++) {
                if (distances.get(i).get(j) == Double.MAX_VALUE) {
                    cityPath[i][j] = -1;
                } else cityPath[i][j] = j;
            }
        }
        for (int k = 0; k < cities.size(); k++) {
            for (int i = 0; i < cities.size(); i++) {
                for (int j = 0; j < cities.size(); j++) {
                    if (distances.get(i).get(k) == Double.MAX_VALUE || distances.get(k).get(j) == Double.MAX_VALUE) {
                        continue;
                    }
                    if (distances.get(i).get(k) + distances.get(k).get(j) < distances.get(i).get(j)) {
                        distances.get(i).set(j, (distances.get(i).get(k) + distances.get(k).get(j)));
                        cityPath[i][j] = cityPath[i][k];
                    }
                }
            }
        }
        int index1 = a.cityNumber;
        int index2 = b.cityNumber;
        if (cityPath[index1][index2] == -1) {
            System.out.println("No path could be found.");
            System.exit(0);
        }
        ArrayList<Integer> shortestPath = new ArrayList<>();
        shortestPath.add(index1);
        while (index1 != index2) {
            index1 = cityPath[index1][index2];
            shortestPath.add(index1);
        }
        System.out.print("Total distance: " + String.format("%.2f", distances.get(a.cityNumber).get(b.cityNumber)) + ". Path: ");
        for (int i = 0; i < shortestPath.size(); i++) {
            System.out.print(getCity(shortestPath.get(i), cities).cityName);
            if (i != shortestPath.size() - 1) {
                System.out.print(" -> ");
            }
        }
        return shortestPath;
    }
}