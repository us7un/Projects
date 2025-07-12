/**
 * Generic MinHeap implementation with only corresponding methods needed specifically for the project implemented (i.e. no buildHeap)
 *
 * @param <E> The type of Object we want to insert into our heap
 * @author ustunyilmaz
 */
public class MinHeap<E> {
    private int size;
    private HeapNode<E>[] heap;

    /**
     * Constructor method to initialize the heap and its size
     */
    public MinHeap() {
        size = 0;
        heap = new HeapNode[size + 1];
    }

    /**
     * Method to delete and retrieve the element with the minimum priority from the heap
     *
     * @return The element with the minimum priority if exists, null else
     */
    public HeapNode<E> deleteMin() {
        // If the heap is empty, print empty and return null for debugging purposes
        if (isEmpty()) {
            System.out.println("empty");
            return null;
        }
        // Delete the "minimum" item from the heap and return it
        HeapNode<E> minItem = findMin();
        heap[1] = heap[size--]; // Decrement size
        percolateDown(1); // Percolate down to ensure heap property is satisfied
        return minItem;
    }

    /**
     * Method to return if the heap is empty or not
     *
     * @return If empty true else false
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Method to return the element with the minimum priority from the heap
     *
     * @return The element with the minimum priority if exists, null else
     */
    public HeapNode<E> findMin() {
        // If the heap is empty, print empty and return null for debugging purposes
        if (isEmpty()) {
            System.out.println("empty");
            return null;
        }
        return heap[1];
    }

    /**
     * Method to percolate down starting from the "hole" to restore heap property
     *
     * @param hole Index we want to start percolating from
     */
    private void percolateDown(int hole) {
        int child;
        HeapNode<E> temp = heap[hole];
        // Percolate down until the element in the "hole" becomes a leaf node or heap property is restored (the latter happens either way)
        for (; hole * 2 <= size; hole = child) {
            child = hole * 2;
            // If the right child is "smaller" than the left child, move to the right child
            if (child != size && heap[child + 1].priority() < heap[child].priority()) {
                child++;
            }
            // Check if heap property is violated
            if (heap[child].priority() < temp.priority()) {
                heap[hole] = heap[child];
            } else break;
        }
        heap[hole] = temp;
    }

    /**
     * Method to enlarge the heap array to some given size
     *
     * @param newSize The new size of the heap array
     */
    private void enlargeArray(int newSize) {
        HeapNode<E>[] finalHeap = new HeapNode[newSize];
        System.arraycopy(heap, 1, finalHeap, 1, size); // Copy the initial heap array's elements into the new array
        heap = finalHeap;
    }

    /**
     * Method to insert a new HeapNode to the minimum heap
     *
     * @param newNode The node we want to insert
     */
    public void insert(HeapNode<E> newNode) {
        // If the heap is getting full, double its array size
        if (size + 1 == heap.length) {
            enlargeArray(heap.length * 2);
        }
        heap[++size] = newNode; // Increment size
        percolateUp(size); // Make sure heap property is satisfied
    }

    /**
     * Method to percolate up, starting from the "hole" to restore heap property
     *
     * @param hole Index we want to start percolating from
     */
    private void percolateUp(int hole) {
        HeapNode<E> temp = heap[hole];
        // Percolate up until the current element in the hole is smaller than the initial element in the hole
        for (; hole > 1 && heap[hole / 2].priority() > temp.priority(); hole /= 2) {
            heap[hole] = heap[hole / 2];
        }
        heap[hole] = temp;
    }
}
