/**
 * ParkingLotAVLTree class implementing all operations needed in the AVL tree.
 * Some parts may be implemented using pseudocode of AVL tree structure from resources in the public domain
 * @author ustunyilmaz
 */
public class ParkingLotAVLTree {
    AVLNode root;

    /**
     * Method that returns the height of a node
     * @param node Node to get the height of
     * @return If exists height of that node else 0
     */
    public int height(AVLNode node) {
        if (node != null) return node.height;
        else return 0;
    }

    /**
     * Method to rotate the AVL tree to left
     * @param root Root of the tree
     * @return New root of the tree
     */
    public AVLNode leftRotation(AVLNode root) {
        AVLNode right = root.right; // Root of right subtree of root
        AVLNode left = right.left; // Root of left subtree of right root

        right.left = root; // Root is rotated to the left, i.e. right becomes the new root
        root.right = left; // Left subtree of right becomes the right subtree of old root

        right.parent = root.parent; // New root's parent becomes old root's parent
        root.parent = right; // Old root's parent becomes the new root
        if (left != null){
            left.parent = root; // Old root becomes its new parent
        }

        // Update heights
        root.height = Math.max(height(root.left), height(root.right)) + 1;
        right.height = Math.max(height(right.left), height(right.right)) + 1;

        return right; // Return new root
    }

    /**
     * Method to rotate the AVL tree to right
     * @param root Root of the tree
     * @return New root of the tree
     */
    public AVLNode rightRotation(AVLNode root) {
        AVLNode left = root.left; // Root of left subtree of root
        AVLNode right = left.right; // Root of right subtree of left root

        left.right = root; // Root is rotated to the right, i.e. left becomes the new root
        root.left = right; // Right subtree of left becomes the left subtree of old root

        left.parent = root.parent; // New root's parent becomes old root's parent
        root.parent = left; // Old root's parent becomes the new root
        if (right != null){
            right.parent = root; // Old root becomes its new parent
        }

        // Update heights
        root.height = Math.max(height(root.left), height(root.right)) + 1;
        left.height = Math.max(height(left.left), height(left.right)) + 1;

        return left; // Return new root

    }

    /**
     * Method to perform double rotation (left-right case) on the AVL tree
     * @param root Root of the tree
     * @return New root of the tree
     */
    public AVLNode leftRightRotation(AVLNode root) {
        root.left = leftRotation(root.left); // Perform left rotation on left subtree
        if (root.left != null){
            root.left.parent = root; // Set its parent to the root to ensure integrity
        }
        return rightRotation(root); // Return the new root after performing a right rotation on the root
    }

    /**
     * Method to perform double rotation (right-left case) on the AVL tree
     * @param root Root of the tree
     * @return New root of the tree
     */
    public AVLNode rightLeftRotation(AVLNode root) {
        root.right = rightRotation(root.right); // Perform right rotation on right subtree
        if (root.right != null){
            root.right.parent = root; // Set its parent to the root to ensure integrity
        }
        return leftRotation(root); // Return the new root after performing a left rotation on the root
    }

    /**
     * Method that returns the balance factor of a node
     * @param node Node to get the balance factor of
     * @return If exists balanceFactor else 0
     */
    public int balanceFactor(AVLNode node) {
        if (node != null) return height(node.left) - height(node.right);
        else return 0;
    }

    /**
     * Method to get the smallest node in a tree/subtree
     * @param root Root of the tree
     * @return If found smallest node else null
     */
    public AVLNode smallest(AVLNode root) {
        AVLNode current = root; // Set current node to root
        while (current.left != null) { // Go to the leftmost node
            current = current.left;
        }
        return current; // Return the leftmost node
    }

    /**
     * Method to find the largest smaller node than a given key in the AVL tree
     * @param root Root of the tree
     * @param maxCapacity Key to search other keys smaller than
     * @return If exists largest smaller node else null
     */
    public AVLNode findOneSmaller(AVLNode root, int maxCapacity){
        AVLNode oneSmaller = null;
        while (root != null){ // Root is set to our "current" node
            if (root.capacityConstraint < maxCapacity){
                oneSmaller = root; // Set root to oneSmaller if it is smaller
                root = root.right; // Look for the largest smaller node in the root's right subtree
            }
            else{
                root = root.left; // If root is larger we need to look in its left subtree until we get the largest smaller node
            }
        }
        return oneSmaller; // Return the oneSmaller
    }

    /**
     * Method to find the smallest larger node than a given key in the AVL tree
     * @param root Root of the tree
     * @param minCapacity Key to search other keys larger than
     * @return If exists smallest larger node else null
     */
    public AVLNode findOneLarger(AVLNode root, int minCapacity){
        AVLNode oneLarger = null;
        while (root != null){ // Root is set to our "current" node
            if (root.capacityConstraint > minCapacity){
                oneLarger = root; // Set root to oneLarger if it is larger
                root = root.left; // Look for the smallest larger node in the root's left subtree
            }
            else{
                root = root.right; // If root is smaller we need to look in its right subtree until we get the smallest larger node
            }
        }
        return oneLarger; // Return the oneLarger
    }

    /**
     * Method to return the inorder successor of a node if it exists in the tree, this method will assume findOneLarger
     * has been run and will start from that node for a more efficient approach
     * @param node Any node in the AVL tree
     * @return If exists the inorder successor of that node else null
     */
    public AVLNode inorderSuccessor(AVLNode node){
        if (node == null) return null; // No tree
        if (node.right != null){
            AVLNode successorNode = node.right; // Go to the right subtree
            // Get and return the smallest key in its right subtree
            while (successorNode.left != null){
                successorNode = successorNode.left;
            }
            return successorNode;
        }
        // Else get the first parent node that is a left child and return its parent
        AVLNode parentNode = node.parent;
        while (parentNode != null && node == parentNode.right){
            node = parentNode;
            parentNode = parentNode.parent;
        }
        return parentNode;
    }

    /**
     * Method to insert a new node in the AVL tree, must be used in the manner "tree.root = tree.insert(tree.root, node);"
     * @param root Root of the tree
     * @param inserted The node we want to insert
     * @return The new root
     */
    public AVLNode insert(AVLNode root, AVLNode inserted) {
        // Binary search tree insertion
        if (root == null) {
            return inserted; // Base case, can be thought as finalizing the insertion
        }
        // If key is smaller, search left subtree; else search right subtree
        if (inserted.capacityConstraint < root.capacityConstraint) {
            root.left = insert(root.left, inserted); // Insert to its left subtree
            root.left.parent = root; // Set the new parent
        } else if (inserted.capacityConstraint > root.capacityConstraint) {
            root.right = insert(root.right, inserted); // Insert to its right subtree
            root.right.parent = root; // Set the new parent
        } else{
            return root; // If already exists return the initial root
        }

        root.height = Math.max(height(root.left), height(root.right)) + 1; // Update the height of the root

        // Balancing the tree to abide by the AVL property
        int balanceFactor = balanceFactor(root);
        if (balanceFactor > 1 && inserted.capacityConstraint < root.left.capacityConstraint) {
            return rightRotation(root);
        }
        if (balanceFactor > 1 && inserted.capacityConstraint > root.left.capacityConstraint) {
            return leftRightRotation(root);
        }
        if (balanceFactor < -1 && inserted.capacityConstraint > root.right.capacityConstraint) {
            return leftRotation(root);
        }
        if (balanceFactor < -1 && inserted.capacityConstraint < root.right.capacityConstraint) {
            return rightLeftRotation(root);
        }
        return root; // Return new root
    }

    /**
     * Method to delete a given key from the AVL tree, must be called similarly to insert; as a root assignment
     * @param root Root of the AVL tree
     * @param capacityConstraint Key of the parking lot
     * @return New root of the tree
     */
    public AVLNode delete(AVLNode root, int capacityConstraint) {
        // Binary search tree deletion
        if (root == null) {
            return null;
        }
        // If key is smaller, search left subtree; else search right subtree
        if (capacityConstraint < root.capacityConstraint) {
            root.left = delete(root.left, capacityConstraint); // Search left subtree and delete it
            if (root.left != null) {
                root.left.parent = root; // Update the new parent
            }
        } else if (capacityConstraint > root.capacityConstraint) {
            root.right = delete(root.right, capacityConstraint); // Search right subtree and delete it
            if (root.right != null) {
                root.right.parent = root; // Update the new parent
            }
        } else {
            if ((root.left == null) || (root.right == null)) { // If we want to delete the root, and it has one/no children
                AVLNode temp;
                if (root.left != null) {
                    temp = root.left;
                } else temp = root.right;
                if (temp == null) { // Root has no children
                    root = null;
                } else {
                    root = temp;
                    root.parent = temp.parent; // Update the parent accordingly
                }
            } else {
                AVLNode temp = smallest(root.right); // If the root has two children, get the smallest node in its right subtree

                // Set the root to that smallest node
                root.capacityConstraint = temp.capacityConstraint;
                root.parkingLot = temp.parkingLot;

                root.right = delete(root.right, temp.capacityConstraint); // Delete the found smallest node since it's the new root
                if (root.right != null) {
                    root.right.parent = root; // Update the parent accordingly
                }
            }
        }

        if (root == null) return null; // Check if the root has been deleted

        root.height = Math.max(height(root.left), height(root.right)) + 1; // Update the heights of the nodes

        // Balancing the tree to abide by the AVL property
        int balanceFactor = balanceFactor(root);
        if (balanceFactor > 1 && balanceFactor(root.left) >= 0) {
            return rightRotation(root);
        }
        if (balanceFactor > 1 && balanceFactor(root.left) < 0) {
            return leftRightRotation(root);
        }
        if (balanceFactor < -1 && balanceFactor(root.right) <= 0) {
            return leftRotation(root);
        }
        if (balanceFactor < -1 && balanceFactor(root.right) > 0) {
            return rightLeftRotation(root);
        }
        return root; // Return new root
    }

    /**
     * Method to recursively look if a key exists in the tree or not
     * @param root Root of the tree
     * @param capacityConstraint "Key" we're looking for
     * @return If found true else false
     */
    public boolean has(AVLNode root, int capacityConstraint){
        if (root == null){
            return false; // Tree is empty, return false
        }
        if (root.capacityConstraint == capacityConstraint){
            return true; // Root has the key we're searching for, return true
        }
        if (capacityConstraint < root.capacityConstraint){
            return has(root.left, capacityConstraint); // Key we're searching for is smaller than root, search left subtree
        }
        return has(root.right, capacityConstraint); // Key we're searching for is larger than root, search right subtree
    }
}
