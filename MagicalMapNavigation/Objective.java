import java.util.ArrayList;

/**
 * Objective class to hold source and destination nodes of an objective, along with the choices it has (if it has any).
 *
 * @author ustunyilmaz
 */
public class Objective {
    private final Node source;
    private final Node destination;
    private final ArrayList<Integer> choices;
    private final boolean choicer;

    /**
     * Constructor method to initialize corresponding properties of an objective of type-1 (no choices)
     *
     * @param source      Source node of the objective
     * @param destination Destination node of the objective
     */
    public Objective(Node source, Node destination) {
        this.source = source;
        this.destination = destination;
        this.choices = new ArrayList<>();
        choicer = false;
    }

    /**
     * Constructor method to initialize corresponding properties of an objective of type-2 (having choices)
     *
     * @param source      Source node of the objective
     * @param destination Destination node of the objective
     * @param choices     ArrayList consisting of choices
     */
    public Objective(Node source, Node destination, ArrayList<Integer> choices) {
        this.source = source;
        this.destination = destination;
        this.choices = choices;
        choicer = true;
    }

    /**
     * Getter method to get the source node of the objective
     *
     * @return Source node of the objective
     */
    public Node getSource() {
        return source;
    }

    /**
     * Getter method to get the destination node of the objective
     *
     * @return Destination node of the objective
     */
    public Node getDestination() {
        return destination;
    }

    /**
     * Getter method to get the choices arrayList of the objective
     *
     * @return ArrayList of its choices
     */
    public ArrayList<Integer> getChoices() {
        return choices;
    }

    /**
     * Getter method to see if an objective has choices or not
     *
     * @return True if it has, false else
     */
    public boolean isChoicer() {
        return choicer;
    }
}
