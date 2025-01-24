/**
 * Max heap class to get the post with the most likes which has a lexicographically bigger ID than the posts
 * with the same amount of likes, efficiently.
 * This implementation is directly inspired from pseudocode in CMPE250 lecture slides of Bogazici University.
 *
 * @author ustunyilmaz
 */
public class PostMaxHeap {
    private final PostNode[] heap;
    private int currentSize;

    /**
     * Constructor method to build the heap from a given array of PostNodes
     *
     * @param heap Array of PostNodes to be built on
     */
    public PostMaxHeap(PostNode[] heap) {
        currentSize = heap.length;
        this.heap = new PostNode[currentSize + 1];

        int i = 1;
        for (PostNode postNode : heap) {
            this.heap[i++] = postNode;
        }
        buildHeap();
    }

    /**
     * Method to efficiently delete the "maximum" element according to the properties said in the class description
     *
     * @return The "maximum" element
     */
    public PostNode deleteMax() {
        PostNode maxItem = findMax(); // Get the maximum element in the heap (i.e. heap[1])
        heap[1] = heap[currentSize--];
        percolateDown(1);
        return maxItem;
    }

    /**
     * Method to percolate down in the heap to restore the heap property
     *
     * @param hole Index of the "hole" i.e. where percolation begins
     */
    private void percolateDown(int hole) {
        int child; // Index of the child to compare
        PostNode temp = heap[hole]; // Value to percolate down
        // Percolate down until a leaf is reached
        for (; hole * 2 <= currentSize; hole = child) {
            child = hole * 2;
            // Move to the larger child
            if (child != currentSize && postComparator(heap[child + 1], heap[child]) > 0) {
                child++;
            }
            // Compare the two
            if (postComparator(heap[child], temp) > 0) {
                heap[hole] = heap[child];
            }
            // When heap property is restored stop percolating
            else break;
        }
        heap[hole] = temp; // Place the value in its new position
    }

    /**
     * Method to return if the heap is empty or not
     *
     * @return If heap is empty true else false
     */
    public boolean isEmpty() {
        return currentSize == 0;
    }

    /**
     * Method to get the "maximum" value in the heap if it has any
     *
     * @return The "maximum" post
     */
    public PostNode findMax() {
        if (isEmpty()) {
            return null;
        }
        return heap[1];
    }

    /**
     * Method to build the heap from an array by percolating down until leaves are reached
     */
    private void buildHeap() {
        for (int i = currentSize / 2; i > 0; i--) {
            percolateDown(i);
        }
    }

    /**
     * Method to compare two posts according to their likes and if they have the same likes, the lexicographically greater one prevails
     *
     * @param post1 Unique key of the first post
     * @param post2 Unique key of the second post
     * @return A positive value if post1 is "greater than" post2 in any sense
     */
    private int postComparator(PostNode post1, PostNode post2) {
        int likeDifference = post1.getLikes() - post2.getLikes();
        if (likeDifference != 0) {
            return likeDifference;
        }
        return post1.getID().compareTo(post2.getID());
    }
}
