/**
 * Node class to hold and update corresponding properties of the nodes in the project "Magical Map"
 *
 * @author ustunyilmaz
 */
public class Node {
    private final int x;
    private final int y;
    private int type;
    private boolean revealed; // If the player can see the node or not
    private final GenericHashTable<Node, Double> edges; // Adjacency list of the node also containing the edge weights

    /**
     * Constructor method to initialize corresponding properties of a new node
     *
     * @param x    x-coordinate of the node
     * @param y    y-coordinate of the node
     * @param type Type of the node
     */
    public Node(int x, int y, int type) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.revealed = type == 0 || type == 1; // Initially, the "player" can see the true nature of the node if its types are 0 and 1
        this.edges = new GenericHashTable<>();
    }

    /**
     * Method to reveal the node
     */
    public void reveal() {
        this.revealed = true;
    }

    /**
     * Method to determine if a node is seen as passable
     *
     * @return True if the node is seen as passable, false else
     */
    public boolean isPassable() {
        return type == 0 || !revealed; // Type 0 and >=2 nodes are always seen as passable at first
    }

    /**
     * Method to add an edge and its corresponding adjacent node to the adjacency list
     *
     * @param neighbor   Adjacent node to be connected to
     * @param travelTime Edge weight (i.e. the distance between the nodes)
     */
    public void addEdge(Node neighbor, double travelTime) {
        if (neighbor != null) {
            edges.insert(neighbor, travelTime);
        }
    }

    /**
     * Getter method to get the x-coordinate of the node
     *
     * @return x-coordinate of the node
     */
    public int getX() {
        return x;
    }

    /**
     * Getter method to get the y-coordinate of the node
     *
     * @return y-coordinate of the node
     */
    public int getY() {
        return y;
    }

    /**
     * Getter method to get the type of the node
     *
     * @return Type of the node
     */
    public int getType() {
        return type;
    }

    /**
     * Setter method to set the type of the node
     *
     * @param type Type we want to set
     */
    public void setType(int type) {
        this.type = type;
    }

    /**
     * Getter method to get the adjacency list of the node
     *
     * @return Adjacency list hash table of the node
     */
    public GenericHashTable<Node, Double> getEdges() {
        return edges;
    }

    @Override
    public String toString() {
        return "Node{" + "x=" + x + ", y=" + y + '}';
    }
}
