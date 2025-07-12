import java.util.ArrayList;
import java.util.Arrays;

/**
 * Node repository class to hold all nodes in a map and perform updates on them
 *
 * @author ustunyilmaz
 */
public class NodeRepository {
    private final int width;
    private final int height;
    private final Node[][] nodeMatrix;

    /**
     * Constructor method to initialize the grid of a map
     *
     * @param width  Width of the grid
     * @param height Height of the grid
     */
    public NodeRepository(int width, int height) {
        this.width = width;
        this.height = height;
        nodeMatrix = new Node[width][height]; // Initialize the "hash map" of the repository
    }

    /**
     * Method to add a node to the repository
     *
     * @param x    x-coordinate of the node
     * @param y    y-coordinate of the node
     * @param type Type of the node
     */
    public void addNode(int x, int y, int type) {
        nodeMatrix[x][y] = new Node(x, y, type);
    }

    /**
     * Method to get a specific node from the repository
     *
     * @param x x-coordinate of the node
     * @param y y-coordinate of the node
     * @return If exists, the node we are searching for; else null
     */
    public Node getNode(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            return null;
        }
        return nodeMatrix[x][y];
    }

    /**
     * Method to get all nodes in the repository as an arrayList
     *
     * @return ArrayList containing all the nodes of the repository
     */
    public ArrayList<Node> getAllNodes() {
        ArrayList<Node> allNodes = new ArrayList<>();
        // Add all nodes in the repository to the arrayList
        for (int x = 0; x < width; x++) {
            allNodes.addAll(Arrays.asList(nodeMatrix[x]).subList(0, height));
        }
        return allNodes;
    }

    /**
     * Method to reveal and get all nodes in a specific radius around a specific node in the repository as an arrayList
     *
     * @param center The node we want to search around
     * @param radius The radius we want to check
     * @return ArrayList containing all the nodes in that radius
     */
    public ArrayList<Node> getNodesWithinRadius(Node center, int radius) {
        ArrayList<Node> nodesInRadius = new ArrayList<>();

        // Get the coordinates of the center node
        int centerX = center.getX();
        int centerY = center.getY();

        // Search only within the square the size of the radius to reduce runtime
        int startX = Math.max(0, centerX - radius);
        int endX = Math.min(width - 1, centerX + radius);
        int startY = Math.max(0, centerY - radius);
        int endY = Math.min(height - 1, centerY + radius);

        // Add all the nodes in the corresponding square that are not null / uninitialized / outside the repository's bounds
        for (int x = startX; x <= endX; x++) {
            for (int y = startY; y <= endY; y++) {
                try {
                    Node node = nodeMatrix[x][y];
                    if (node != null) {
                        double distance = Math.sqrt(Math.pow(x - centerX, 2) + Math.pow(y - centerY, 2));
                        // Reveal and add all the nodes in the radius to the arrayList
                        if (distance <= radius) {
                            node.reveal();
                            nodesInRadius.add(node);
                        }
                    }
                }
                // Ignore the exception for easier handling
                catch (ArrayIndexOutOfBoundsException ignored) {

                }
            }
        }
        return nodesInRadius;
    }

    /**
     * Method to check if two nodes are in the same "circle" centered at one node
     *
     * @param center    Center node
     * @param otherNode Other node
     * @param radius    The radius of that circle, can also be thought simply as distance
     * @return True if they are in the same circle centered at one node, false else
     */
    public boolean nodeInRadius(Node center, Node otherNode, int radius) {
        int dx = center.getX() - otherNode.getX();
        int dy = center.getY() - otherNode.getY();
        double distance = Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));

        return distance <= radius;
    }

    /**
     * Method to change all the nodes with a certain type to type-0 nodes in the whole repository
     *
     * @param type The type we want to change
     * @return ArrayList consisting of changed nodes
     */
    public ArrayList<Node> changeAllTypes(int type) {
        ArrayList<Node> changedNodes = new ArrayList<>();
        for (Node node : getAllNodes()) {
            if (node.getType() == type) {
                node.setType(0);
                changedNodes.add(node);
            }
        }
        return changedNodes;
    }

    /**
     * Method to revert all nodes in an arrayList to their previous type
     *
     * @param changedNodes ArrayList containing previously changed nodes
     * @param type         Previous type of changed nodes
     */
    public void revertAllNodes(ArrayList<Node> changedNodes, int type) {
        for (Node node : changedNodes) {
            node.setType(type);
        }
    }

}
