import java.util.ArrayList;

/**
 * Dijkstra class to perform all pathfinding operations in a node repository utilizing Dijkstra's Algorithm
 *
 * @author ustunyilmaz
 */
public class Dijkstra {
    private final NodeRepository repo;
    private final int radiusOfSight;
    private final ArrayList<Node> shortestPath;
    private Node currentSource;
    private final GenericHashTable<Integer, Integer> chosenNumbers;

    /**
     * Constructor method to initialize the pathfinder
     *
     * @param repo          Node repository we want to operate on
     * @param radiusOfSight Radius of sight the "player" can see
     */
    public Dijkstra(NodeRepository repo, int radiusOfSight) {
        this.repo = repo;
        this.radiusOfSight = radiusOfSight;
        this.shortestPath = new ArrayList<>();
        this.chosenNumbers = new GenericHashTable<>();
    }

    /**
     * Method to modify the shortestPath arrayList of nodes so that it gives the shortest path between two nodes, using Dijkstra's Algorithm
     *
     * @param source      Node we want to start from
     * @param destination Node we want to reach in the shortest distance
     */
    public void dijkstraVoid(Node source, Node destination) {
        shortestPath.clear(); // Always clear the shortestPath arrayList to ensure integrity
        // Initialize the needed structures
        MinHeap<Node> minHeap = new MinHeap<>();
        GenericHashTable<Node, Double> distances = new GenericHashTable<>();
        GenericHashTable<Node, Node> previous = new GenericHashTable<>();

        // Initialize the distances of the nodes
        for (Node node : repo.getAllNodes()) {
            if (node.isPassable()) {
                distances.insert(node, Double.MAX_VALUE); // Set all initial distances to infinity
            }
        }
        distances.insert(source, 0.0); // Distance to the source is 0
        minHeap.insert(new HeapNode<>(source, 0.0)); // Only add the source to the heap initially

        // Dijkstra's algorithm starts here
        while (!minHeap.isEmpty()) {
            Node currentNode = minHeap.deleteMin().data(); // Pop the "unchecked" node from the heap

            // Stop if we reach the destination
            if (currentNode.equals(destination)) {
                break;
            }

            for (Node neighbor : currentNode.getEdges().getAllKeys()) {
                if (neighbor.isPassable()) {
                    double currentDistance = distances.get(currentNode) + currentNode.getEdges().get(neighbor); // Calculate the distance between them

                    // For each passable neighbor, if the distance is shorter, update it and add the distance into the heap, also record it in the shortest path
                    if (currentDistance < distances.get(neighbor)) {
                        distances.insert(neighbor, currentDistance);
                        previous.insert(neighbor, currentNode);

                        minHeap.insert(new HeapNode<>(neighbor, currentDistance));
                    }
                }
            }
        }

        Node currentNode = destination;
        // Starting from the destination, form the path by going in reverse to complete the shortestPath arrayList
        while (currentNode != null) {
            shortestPath.addFirst(currentNode);
            currentNode = previous.get(currentNode); // Reverse the "previous" hashTable to form the path
        }
    }

    /**
     * Method to return the shortest path available from a source node to a destination node, using Dijkstra's algorithm
     *
     * @param source      Node we want to start from
     * @param destination Node we want to reach in the shortest distance
     * @return ArrayList of nodes that make up the shortest path
     * @see Dijkstra#dijkstraVoid(Node, Node) dijkstraVoid for implementation details
     */
    public ArrayList<Node> dijkstra(Node source, Node destination) {
        // The implementation of this method is identical to the method dijkstraVoid, please check its comments
        ArrayList<Node> path = new ArrayList<>();
        MinHeap<Node> minHeap = new MinHeap<>();
        GenericHashTable<Node, Double> distances = new GenericHashTable<>();
        GenericHashTable<Node, Node> previous = new GenericHashTable<>();

        for (Node node : repo.getAllNodes()) {
            if (node.isPassable()) {
                distances.insert(node, Double.MAX_VALUE);
            }
        }
        distances.insert(source, 0.0);
        minHeap.insert(new HeapNode<>(source, 0.0));

        while (!minHeap.isEmpty()) {
            Node currentNode = minHeap.deleteMin().data();

            if (currentNode.equals(destination)) {
                break;
            }

            for (Node neighbor : currentNode.getEdges().getAllKeys()) {
                if (neighbor.isPassable()) {
                    double currentDistance = distances.get(currentNode) + currentNode.getEdges().get(neighbor);

                    if (currentDistance < distances.get(neighbor)) {
                        distances.insert(neighbor, currentDistance);
                        previous.insert(neighbor, currentNode);

                        minHeap.insert(new HeapNode<>(neighbor, currentDistance));
                    }
                }
            }
        }

        Node currentNode = destination;
        while (currentNode != null) {
            path.addFirst(currentNode);
            currentNode = previous.get(currentNode);
        }

        // Return null if there is no shortest path
        if (path.isEmpty() || !path.getFirst().equals(source)) {
            return null;
        }
        return path;
    }

    /**
     * Method to check if a path includes impassable nodes and adds it to the output
     *
     * @param stringBuilder The output
     * @param currentNode   The node to start from
     * @return True if path is impassable, false else
     */
    public boolean pathImpassable(StringBuilder stringBuilder, Node currentNode) {
        // Iterate through the nodes in the shortestPath arrayList
        for (int i = 0; i < shortestPath.size(); i++) {
            Node nextNode = shortestPath.get(i);

            // Return true if the next node is in radius and is impassable, also add it to the output
            if (repo.nodeInRadius(currentNode, nextNode, radiusOfSight) && !nextNode.isPassable()) {
                stringBuilder.append("Path is impassable!").append("\n");
                setCurrentSource(currentNode); // Update the current source to start from the node where the path was first seen as impassable
                return true;
            }
        }
        return false;
    }

    /**
     * Method to calculate the sum of edgeWeights of a path
     *
     * @param path ArrayList of nodes that are in the path
     * @return The total distance of the path
     */
    public double calculatePathDistance(ArrayList<Node> path) {
        double distance = 0;
        for (int i = 0; i < path.size() - 1; i++) {
            distance += path.get(i).getEdges().get(path.get(i + 1));
        }
        return distance;
    }

    /**
     * Method to run Dijkstra's algorithm until an objective is exhausted, also adding to the output
     *
     * @param objectiveNo   The number of the objective
     * @param stringBuilder The output
     * @param source        Node we want to start from
     * @param destination   Node we want to reach
     */
    public void algorithm(int objectiveNo, StringBuilder stringBuilder, Node source, Node destination) {
        setCurrentSource(source); // Set the current source as source
        revealNodesWithinRadius(currentSource); // Reveal all nodes within its radius before starting

        // Repeatedly run dijkstra until the objective is reached and the path is passable
        while (true) {
            dijkstraVoid(currentSource, destination); // Calculate path from source to destination

            // If no path is found, terminate the algorithm; which will never be the case in a deterministic input
            if (shortestPath.isEmpty()) {
                return;
            }

            // Traverse the shortestPath to check if its passable and concatenate its nodes to the output
            for (int i = 1; i < shortestPath.size(); i++) {
                Node currentNode = shortestPath.get(i);
                stringBuilder.append("Moving to ").append(currentNode.getX()).append("-").append(currentNode.getY()).append("\n"); // Concatenate the node to the output
                revealNodesWithinRadius(currentNode); // Reveal nodes within radius

                // Terminate the algorithm if objective is reached
                if (currentNode.equals(destination)) {
                    stringBuilder.append("Objective ").append(objectiveNo).append(" reached!").append("\n");
                    return; // Exit the loop and complete the algorithm
                }
                // Check if the path is impassable, break the loop to recalculate the shortestPath if it is
                if (pathImpassable(stringBuilder, currentNode)) {
                    break;
                }
            }
        }
    }

    /**
     * Setter method to set the current source
     *
     * @param currentSource The node we want to set as current source
     */
    public void setCurrentSource(Node currentSource) {
        this.currentSource = currentSource;
    }

    /**
     * Method to reveal all nodes within the radius of a center node
     *
     * @param center Node located at the center
     */
    public void revealNodesWithinRadius(Node center) {
        ArrayList<Node> nodesWithinRadius = repo.getNodesWithinRadius(center, radiusOfSight);
        for (Node nodeWithinRadius : nodesWithinRadius) {
            nodeWithinRadius.reveal();
        }
    }

    /**
     * Method to calculate the best possible choice out of an arrayList of integers consisting of choices; using what the "player" knows so far
     * Also concatenates the chosen number to the output
     *
     * @param stringBuilder The output
     * @param source        Node we want to start from
     * @param destination   Node we want to reach in the shortest distance
     * @param choices       ArrayList of integers
     */
    public void bestPossibleChoice(StringBuilder stringBuilder, Node source, Node destination, ArrayList<Integer> choices) {
        // Initialize the minimum distance as infinity and best choice as nonexistent (-1)
        double minDistance = Double.MAX_VALUE;
        int bestChoice = -1;

        // Iterate through all choices in the choices arrayList to calculate the choice with the shortest distance
        for (int choice : choices) {
            // Only iterate if the choice wasn't made before
            if (chosenNumbers.get(choice) == null) {
                ArrayList<Node> changedNodes = repo.changeAllTypes(choice); // Change all types to that choice
                ArrayList<Node> path = dijkstra(source, destination); // Calculate the path
                double pathDistance = calculatePathDistance(path); // Calculate the distance

                // If it is a minimum distance, update the best choice and minimum distance
                if (pathDistance < minDistance) {
                    minDistance = pathDistance;
                    bestChoice = choice;
                }
                repo.revertAllNodes(changedNodes, choice); // Revert all nodes that were changed
            }
        }
        repo.changeAllTypes(bestChoice); // Make the "best choice" permanently
        chosenNumbers.insert(bestChoice, bestChoice); // Insert the choice into the chosenNumbers hashTable so that it's never chosen twice
        stringBuilder.append("Number ").append(bestChoice).append(" is chosen!").append("\n"); // Add it to the output
    }
}