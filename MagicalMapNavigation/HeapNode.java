/**
 * Generic HeapNode class to use in binary heap implementations
 * The reason this is not an inner static class of MinHeap is that this class can also be used in MaxHeap implementations without any modifications
 *
 * @param <E> The type of Object we want to insert into our heap
 * @author ustunyilmaz
 */
public record HeapNode<E>(E data, double priority) implements Comparable<HeapNode<E>> {
    /**
     * Constructor method to initialize the node's data and its corresponding priority
     *
     * @param data     The main object the node holds
     * @param priority Priority of the node
     */
    public HeapNode {
    }

    /**
     * Getter method to get a node's corresponding data
     *
     * @return Data of the node
     */
    @Override
    public E data() {
        return data;
    }

    /**
     * Getter method to get a node's corresponding priority
     *
     * @return Priority of the node
     */
    @Override
    public double priority() {
        return priority;
    }

    /**
     * Method to compare two nodes according to their priorities
     *
     * @param o The object to be compared
     * @return A positive value if priority>o.priority, 0 if they are the same, a negative value else
     */
    @Override
    public int compareTo(HeapNode o) {
        return Double.compare(this.priority, o.priority);
    }
}
