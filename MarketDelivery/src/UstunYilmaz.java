import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

/**
 * Java code consisting of two separate algorithms to calculate and show(using StdDraw) the shortest route
 * starting from an initial mart and traversing every house one by one then returning. One being the
 * not-so-efficient bruteForce algorithm and the latter being the more efficient antColony optimization algorithm.
 *
 * @author ustunyilmaz
 * @version 1.0.0
 */
public class UstunYilmaz {
    public static void main(String[] args) throws FileNotFoundException {

        // Constants to be used in ACO algorithm
        final int iterationCount = 100; // Count of iterations
        final int antCount = 50; // Size of colonies to be iterated over
        final double degradationFactor = 0.6; // Evaporation rate
        final double ALPHA = 0.8; // Alpha constant
        final double BETA = 1.2; // Beta constant
        final double initialPheromone = 0.1; // Initial pheromone on roads
        final double Q = 0.0001; // Q constant

        int chosenMethod = 1; // Set to 1 to use bruteforce, 2 to use ACO

        int antColonyOutput = 1; // Set to 1 to see the shortest path, 2 to see pheromone densities

        // Reading input
        String fileName = "input01.txt"; // Selected file-name for reading
        File inputFile = new File(fileName); // Create new file
        Scanner fileHandler = new Scanner(inputFile); // Scanner to traverse file content
        ArrayList<House> houses = new ArrayList<>(); // Create an arrayList of houses
        int houseIndex = 1; // Start house ID's from 1
        while (fileHandler.hasNext()) {
            String[] coordinateTuple = fileHandler.nextLine().split(","); // Store house coordinates in a tuple
            double[] coordinates = new double[2];
            coordinates[0] = Double.parseDouble(coordinateTuple[0]);
            coordinates[1] = Double.parseDouble(coordinateTuple[1]);
            houses.add(new House(houseIndex, coordinates[0], coordinates[1])); // Create new house using values parsed from the tuple and the house ID
            houseIndex++; // Increment house ID
        }
        fileHandler.close(); // Close scanner for safety

        // Indexing variables
        ArrayList<Road> roads = new ArrayList<>(); // Create an array of roads between houses

        for (int i = 0; i < houses.size(); i++) {
            for (int j = i + 1; j < houses.size(); j++) {
                roads.add(new Road(houses.get(i), houses.get(j), initialPheromone)); // Initialize each combination once, as a road
            }
        }

        //------StdDraw-----------------------------------------------------------------------

        int canvasWidth = 800; // Width of our canvas
        int canvasHeight = 800; // Height of our canvas
        Color houseColor = StdDraw.LIGHT_GRAY; // Color of 'houses'
        Color martColor = StdDraw.PRINCETON_ORANGE; // Color of our 'mart'
        double buildingRadius = 0.022; // Radius of each 'building'
        Font font = new Font("Arial", Font.PLAIN, 16); // Font to write house ID's

        StdDraw.enableDoubleBuffering(); // Draw everything on screen only once
        StdDraw.setCanvasSize(canvasWidth, canvasHeight); // Set canvas size
        StdDraw.setFont(font); // Set font

        // Drawing the shortest path
        StdDraw.setPenRadius(0.007);
        if (chosenMethod == 1 || (chosenMethod == 2 && antColonyOutput == 1)) {
            // Shortest path for iterative brute-force algorithm
            if (chosenMethod == 1) {
                int[] bruteForcePath = bruteForce(houses); // Path that also executes bruteForce method
                for (int i = 0; i < bruteForcePath.length - 1; i++) {
                    StdDraw.line(getHouse(houses, bruteForcePath[i]).houseX(), getHouse(houses, bruteForcePath[i]).houseY(), getHouse(houses, bruteForcePath[i + 1]).houseX(), getHouse(houses, bruteForcePath[i + 1]).houseY()); // Roads of shortest path between buildings
                }
            }
            // Shortest path for efficient ant colony optimization algorithm
            else {
                int[] antColonyPath = antColony(houses, roads, ALPHA, BETA, iterationCount, antCount, degradationFactor, Q); // Path that also executes antColony method
                for (int i = 0; i < antColonyPath.length - 1; i++) {
                    StdDraw.line(getHouse(houses, antColonyPath[i]).houseX(), getHouse(houses, antColonyPath[i]).houseY(), getHouse(houses, antColonyPath[i + 1]).houseX(), getHouse(houses, antColonyPath[i + 1]).houseY()); // Roads of shortest path between buildings
                }
            }
            for (House house : houses) {
                // Set color according to house / mart
                if (house.houseNumber() != 1) {
                    StdDraw.setPenColor(houseColor);
                } else {
                    StdDraw.setPenColor(martColor);
                }
                StdDraw.filledCircle(house.houseX(), house.houseY(), buildingRadius); // Draw buildings
                StdDraw.setPenColor(); // Set color to default(black)
                StdDraw.text(house.houseX(), house.houseY(), Integer.toString(house.houseNumber())); // Write house ID's

            }
        }
        // Drawing ant-colony pheromone-densities
        else {
            antColony(houses, roads, ALPHA, BETA, iterationCount, antCount, degradationFactor, Q); // Execute antColony method to update roads
            for (Road road : roads) {
                StdDraw.setPenRadius(road.getPheromoneLevel() * 1.5); // Set road thickness according to its pheromone level
                StdDraw.line(getHouse(houses, road.houses.getFirst()).houseX(), getHouse(houses, road.houses.getFirst()).houseY(), getHouse(houses, road.houses.getLast()).houseX(), getHouse(houses, road.houses.getLast()).houseY()); // Draw road
            }
            // Draw all buildings in default color
            for (House house : houses) {
                StdDraw.setPenColor(houseColor);
                StdDraw.filledCircle(house.houseX(), house.houseY(), buildingRadius); // Draw buildings
                StdDraw.setPenColor(); // Set color to default(black)
                StdDraw.text(house.houseX(), house.houseY(), Integer.toString(house.houseNumber())); // Write house ID's

            }
        }
        StdDraw.show(); // Show canvas
        //------StdDraw----------------------------------------------------------------------
    }

    //---------Methods used in both algorithms-----------------------------------------------

    /**
     * Method to get a house by its house ID
     *
     * @param houses  arrayList containing houses
     * @param houseId ID of the house to return
     * @return house corresponding to ID, if not found, default house
     */
    public static House getHouse(ArrayList<House> houses, int houseId) {
        // Search and return house if found
        for (House house : houses) {
            if (house.houseNumber() == houseId) return house;
        }
        // Return default house if not found
        return new House(-1, 0, 0);
    }

    /**
     * Method to re-sort an array in such a way that it will start with the pointer,
     * not touching the order of other elements in the loop
     *
     * @param array   input array
     * @param pointer the value that we want the array to start with
     * @return a modified separate array
     */
    public static int[] reSortArray(int[] array, int pointer) {
        int i = -1; // Initial index of our pointer, to be found, assumed -1 at first.

        // Find pointer and update index if found
        for (int j = 0; j < array.length; j++) {
            if (array[j] == pointer) {
                i = j;
                break;
            }
        }
        int[] sortedArray = new int[array.length]; // Create new array
        System.arraycopy(array, i, sortedArray, 0, array.length - i); // Copy elements after the pointer
        System.arraycopy(array, 0, sortedArray, array.length - i, i); // Copy elements before the pointer
        return sortedArray; // Return the modified array
    }

    //---------Methods used in both algorithms-------------------------------------------------

    //-----------BRUTE FORCE STARTS HERE-------------------------------------------------------------------------------

    /**
     * Method that searches for all permutations of an array iteratively using Heap's algorithm, and returns the shortest
     * path as an array of indices of houses in that path, also prints out the distance and time consumed.
     * Worse time complexity than a recursive Heap's algorithm, but much better space complexity.
     *
     * @param houses arrayList of houses to be permuted
     * @return the shortest path as an integer array consisting of house IDs
     */
    public static int[] bruteForce(ArrayList<House> houses) {
        long initiation = System.currentTimeMillis(); // Record the initiation time
        int[] shortestPath = new int[houses.size()]; // Initialize the shortest-path array
        double minimumDistance = Double.MAX_VALUE; // Initialize the minimumDistance variable
        int[] input = new int[houses.size()]; // Initialize the input array
        for (int i = 0; i < houses.size(); i++) {
            input[i] = i + 1; // (important!) Initialize according to houseIDs because houseIDs start from 1
        }
        int[] stackState = new int[houses.size()]; // Initialize the stack-state array for iterated recursion-simulation
        double distance = 0; // Initialize total distance

        // Check distance of initial condition of input
        for (int i = 0; i < input.length - 1; i++) {
            distance += getHouse(houses, input[i]).euclideanDistance(getHouse(houses, input[i + 1]));
        }
        distance += getHouse(houses, input[input.length - 1]).euclideanDistance(getHouse(houses, input[0]));

        // Update minimum distance if total distance is smaller, then update the shortest path
        if (distance < minimumDistance) {
            minimumDistance = distance;
            shortestPath = Arrays.copyOf(input, houses.size());
        }

        int i = 2; // (important!) Initialize stack pointer from 2 because we know we are traversing a loop, first element can always be the same

        // Heap's iterative algorithm
        while (i < houses.size()) { // While there still are permutations to generate
            if (stackState[i] < i) {
                if (i % 2 == 0) {
                    swap(input, 1, i); // Swap first element with ith element if i is even
                } else {
                    swap(input, stackState[i], i); // Swap element at current stack state with ith element if i is odd
                }

                distance = 0; // Reset total distance for each permutation

                // Check distance of each permutation
                for (int j = 0; j < input.length - 1; j++) {
                    distance += getHouse(houses, input[j]).euclideanDistance(getHouse(houses, input[j + 1]));
                }
                distance += getHouse(houses, input[input.length - 1]).euclideanDistance(getHouse(houses, input[0]));

                // Update minimum distance if total distance is smaller, then update the shortest path
                if (distance < minimumDistance) {
                    minimumDistance = distance;
                    shortestPath = Arrays.copyOf(input, houses.size());
                }

                stackState[i]++; // Increment the stack state
                i = 2; // Reset i for another round of permutation
            } else {
                stackState[i] = 0; // Reset stack state of ith element
                i++; // Move on to the next element
            }
        }

        // Re-sort array for desired output starting with 1
        if (shortestPath[0] != 1) {
            shortestPath = reSortArray(shortestPath, 1);
        }

        // Put 1 at the end of our desired output
        shortestPath = Arrays.copyOf(shortestPath, shortestPath.length + 1);
        shortestPath[shortestPath.length - 1] = 1;


        double elapsedTime = (System.currentTimeMillis() - initiation) / 1000.0; // Record elapsed time during the algorithm

        // Print desired outputs
        System.out.println("Method: Brute-Force Method");
        System.out.printf("Shortest Distance: %.5f\n", minimumDistance);
        System.out.println("Shortest Path: " + Arrays.toString(shortestPath));
        System.out.printf("Time it takes to find the shortest path: %.2f seconds.", elapsedTime);

        return shortestPath; // Return the answer
    }

    /**
     * Method to swap two elements in given input array
     *
     * @param input  array to be modified
     * @param index1 index of the first element to be swapped
     * @param index2 index of the second element to be swapped
     */
    public static void swap(int[] input, int index1, int index2) {
        int temp = input[index1]; // Store first element's value in a dummy variable

        // Swap the elements
        input[index1] = input[index2];
        input[index2] = temp;
    }

    //-----------BRUTE FORCE ENDS HERE-------------------------------------------------------------------------------

    //-----------ANT COLONY STARTS HERE-------------------------------------------------------------------------------

    /**
     * Ant colony optimization algorithm that computes a shortest-path using a heuristic approach to the Travelling Salesman Problem (TSP)
     * More information can be found on <a href="https://en.wikipedia.org/wiki/Ant_colony_optimization_algorithms">Wikipedia</a>
     *
     * @param houses            arrayList containing houses to be traversed
     * @param roads             arrayList containing roads between said houses
     * @param ALPHA             alpha constant used when calculating edge weights
     * @param BETA              beta constant used when calculating edge weights
     * @param iterationCount    count of times to generate the colony
     * @param antCount          count of ants in each colony
     * @param degradationFactor evaporation factor to degrade pheromones on roads
     * @param Q                 q constant used when increasing pheromones
     * @return the shortest path generated
     */
    public static int[] antColony(ArrayList<House> houses, ArrayList<Road> roads, double ALPHA, double BETA, int iterationCount, int antCount, double degradationFactor, double Q) {
        Random randomizer = new Random(); // Random number generator
        ArrayList<Integer> shortestPath = new ArrayList<>(); // Initialize arrayList to store shortest path
        int[] finalAnswer = new int[houses.size()]; // Initialize the integer array to store the formatted shortest path to be output
        double shortestDistance = Double.MAX_VALUE; // Initialize the shortest distance variable
        long initiation = System.currentTimeMillis(); // Record the initiation time
        ArrayList<Ant> colony = new ArrayList<>(); // Initialize the colony arrayList to store ants

        for (int k = 0; k < iterationCount; k++) {
            colony.clear(); // Reset the colony at the start of each iteration

            // Refill the colony with new ants starting at random house
            for (int i = 0; i < antCount; i++) {
                colony.add(new Ant(randomizer.nextInt(1, houses.size() + 1)));
            }

            // Traverse each ant
            for (Ant ant : colony) {
                double antDistance = 0; // Initialize total distance
                ArrayList<Road> antPath = new ArrayList<>(); // Initialize the path of the ant

                // Apply probabilistic choosing algorithm until ant travels all houses
                while (ant.housesBeen.size() < houses.size()) {
                    double totalWeight = 0; // Initialize total weight

                    // Accumulate total weight with respect to roads only that the ant can travel along
                    for (Road road : roads) {
                        if (road.houses.contains(ant.houseAt) && noCommonElements(road.houses, ant.housesBeen, ant.houseAt)) {
                            totalWeight += getEdgeWeight(road, ALPHA, BETA);
                        }
                    }
                    double accumulatedProbability = 0; // Initialize the accumulated probability to reach the threshold

                    // Choose a road probabilistically
                    for (Road road : roads) {
                        if (road.houses.contains(ant.houseAt) && noCommonElements(road.houses, ant.housesBeen, ant.houseAt)) {
                            double normalizedWeight = getEdgeWeight(road, ALPHA, BETA) / totalWeight; // Normalize each road's probability by dividing with totalWeight
                            accumulatedProbability += normalizedWeight; // Start accumulating probability

                            // If the probability reaches the random threshold, travel through the final accumulating road
                            if (randomizer.nextDouble(1.0) < accumulatedProbability) {
                                antPath.add(road); // Add road to path
                                // Travel to the next house
                                if (ant.houseAt != road.houses.getFirst()) {
                                    ant.houseAt = road.houses.getFirst();
                                } else {
                                    ant.houseAt = road.houses.getLast();
                                }
                                ant.housesBeen.add(ant.houseAt); // Add the travelled house to ant's housesBeen
                                antDistance += road.distance; // Increment the total distance travelled by ant
                                break;
                            }
                        }
                    }
                }

                // Increment distance on the final, returning road
                antDistance += getHouse(houses, ant.houseAt).euclideanDistance(getHouse(houses, ant.startingHouse));

                // At the end of each traversal, leave pheromones on roads travelled according to the formula given
                for (Road road : antPath) {
                    if (antDistance > 0) { // Check if antDistance is non-zero to avoid possible errors
                        road.setPheromoneLevel(road.getPheromoneLevel() + (Q / antDistance));
                    }
                }

                // Leave pheromone on the final, returning road manually to avoid possible errors
                getRoad(ant.houseAt, ant.startingHouse, roads).setPheromoneLevel(getRoad(ant.houseAt, ant.startingHouse, roads).getPheromoneLevel() + (Q / antDistance));

                // Set shortest path and distance if the path found is the shortest
                if (antDistance > 0 && antDistance < shortestDistance) { // Check if antDistance is non-zero to avoid possible errors
                    shortestPath = ant.housesBeen;
                    shortestDistance = antDistance;
                }
            }

            // Evaporate pheromones at the end of each iteration
            for (Road road : roads) {
                road.evaporate(degradationFactor);
            }
        }

        double elapsedTime = (System.currentTimeMillis() - initiation) / 1000.0; // Record elapsed time to be printed in the output

        // Prepare the final answer array by converting shortestPath arrayList to an int[], then sorting it to get the desired output
        for (int i = 0; i < shortestPath.size(); i++) {
            finalAnswer[i] = shortestPath.get(i); // Convert shortestPath to int[]
        }
        if (finalAnswer[0] != 1) {
            finalAnswer = reSortArray(finalAnswer, 1); // Re-sort finalAnswer accordingly
        }
        // Add 1 at the end of finalAnswer
        finalAnswer = Arrays.copyOf(finalAnswer, finalAnswer.length + 1);
        finalAnswer[finalAnswer.length - 1] = 1;

        // Print the desired outputs
        System.out.println("Method: Ant-Colony Method");
        System.out.printf("Shortest Distance: %.5f\n", shortestDistance);
        System.out.println("Shortest Path: " + Arrays.toString(finalAnswer));
        System.out.printf("Time it takes to find the shortest path: %.2f seconds.", elapsedTime);

        return finalAnswer; // Return the shortest path
    }

    /**
     * Method that checks if there is a common element in two input arrayLists, and an exception can be made
     *
     * @param list1     first arrayList
     * @param list2     second arrayList
     * @param exception value to be excluded from the search
     * @return true if no common elements found, false otherwise
     */
    public static boolean noCommonElements(ArrayList<Integer> list1, ArrayList<Integer> list2, int exception) {
        // Traverse through both lists to check if any common elements exist
        for (int element : list1) {
            if (element != exception) { // Exclude the exception
                if (list2.contains(element)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Method to get a road using its starting and terminal houses, in an arrayList of roads
     *
     * @param house1 first house
     * @param house2 second house
     * @param roads  arrayList containing roads
     * @return the road with said houses if exists, a default road otherwise(which will never be the case if done properly)
     */
    public static Road getRoad(int house1, int house2, ArrayList<Road> roads) {
        // Traverse through roads in the arrayList
        for (Road road : roads) {
            if (road.houses.contains(house1) && road.houses.contains(house2)) {
                return road; // Return the road if found
            }
        }
        return new Road(new House(-1, -1, -1), new House(-1, -1, -1), 0); // Return a default road if not found
    }

    /**
     * Method that returns a road's weight for use in ant-colony optimization algorithms
     *
     * @param road  the input road
     * @param ALPHA alpha constant used to calculate edge weights
     * @param BETA  beta constant used to calculate edge weights
     * @return weight of the road according to the given formula
     */
    public static double getEdgeWeight(Road road, double ALPHA, double BETA) {
        return Math.pow(road.getPheromoneLevel(), ALPHA) / Math.pow(road.distance, BETA);
    }

    //-----------ANT COLONY ENDS HERE-------------------------------------------------------------------------------

}