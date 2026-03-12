package de.lukas.searchtree;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple self-balancing binary search tree (AVL tree) for {@code int} values.
 * <p>
 * It keeps the height difference between the left and right subtree of every node
 * at most 1 by applying rotations after insertions. This keeps lookups and inserts
 * fast even when values are added in sorted order.
 * </p>
 * <p>
 * Duplicate values are ignored.
 * </p>
 */
public class BinarySearchTree {
    private Node root;
    private int size;

    /**
     * Creates an empty balanced binary search tree.
     */
    public BinarySearchTree() {
    }

    /**
     * Creates a balanced binary search tree and inserts all given values.
     *
     * @param values values to insert (duplicates are ignored)
     */
    public BinarySearchTree(int... values) {
        for (int value : values) {
            insert(value);
        }
    }

    /**
     * Inserts a value into the tree.
     *
     * @param value the value to insert
     */
    public void insert(int value) {
        if (contains(value)) {
            return;
        }
        root = insert(root, value);
        size++;
    }

    /**
     * Checks if the tree contains a value.
     *
     * @param value the value to search for
     * @return {@code true} if the value exists in the tree
     */
    public boolean contains(int value) {
        Node current = root;
        while (current != null) {
            if (value < current.value) {
                current = current.left;
            } else if (value > current.value) {
                current = current.right;
            } else {
                return true;
            }
        }
        return false;
    }

    /**
     * @return number of values currently stored in the tree
     */
    public int size() {
        return size;
    }

    /**
     * @return {@code true} when the tree contains no values
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Returns the height of the whole tree.
     * Empty tree = 0, single node = 1.
     *
     * @return tree height
     */
    public int height() {
        return nodeHeight(root);
    }

    /**
     * Returns the values in sorted order (in-order traversal).
     *
     * @return sorted list of all values in the tree
     */
    public List<Integer> inOrder() {
        List<Integer> values = new ArrayList<>();
        inOrder(root, values);
        return values;
    }

    /**
     * Helper method for learning/testing: verifies that every node in the tree
     * currently satisfies the AVL balance rule.
     *
     * @return {@code true} if the tree is balanced
     */
    public boolean isBalanced() {
        return isBalanced(root);
    }

    /**
     * Returns an ASCII representation of the tree that can be printed in a terminal.
     *
     * @return multi-line tree view (or {@code "(empty)\n"} for an empty tree)
     */
    public String toTreeString() {
        if (root == null) {
            return "(empty)\n";
        }

        StringBuilder builder = new StringBuilder();
        builder.append(root.value).append('\n');
        appendChildren(builder, root, "");
        return builder.toString();
    }

    /**
     * Prints the tree structure to the terminal.
     */
    public void printTree() {
        System.out.print(toTreeString());
    }

    public static void main(String[] args) {
        BinarySearchTree tree = new BinarySearchTree();

        // Even sorted insertions stay balanced because this class performs AVL rotations.
        for (int value = 1; value <= 10; value++) {
            tree.insert(value);
        }

        System.out.println("In-order (sorted): " + tree.inOrder());
        System.out.println("Size: " + tree.size());
        System.out.println("Height: " + tree.height());
        System.out.println("Balanced: " + tree.isBalanced());
        System.out.println("Contains 7: " + tree.contains(7));
        System.out.println("Contains 42: " + tree.contains(42));
        System.out.println();
        System.out.println("Tree view:");
        tree.printTree();
    }

    private Node insert(Node node, int value) {
        if (node == null) {
            return new Node(value);
        }

        if (value < node.value) {
            node.left = insert(node.left, value);
        } else if (value > node.value) {
            node.right = insert(node.right, value);
        } else {
            return node;
        }

        updateHeight(node);
        return rebalance(node);
    }

    private void inOrder(Node node, List<Integer> values) {
        if (node == null) {
            return;
        }
        inOrder(node.left, values);
        values.add(node.value);
        inOrder(node.right, values);
    }

    private boolean isBalanced(Node node) {
        if (node == null) {
            return true;
        }
        int balance = Math.abs(balanceFactor(node));
        return balance <= 1 && isBalanced(node.left) && isBalanced(node.right);
    }

    private void appendChildren(StringBuilder builder, Node node, String prefix) {
        Node[] children = new Node[2];
        int count = 0;

        if (node.left != null) {
            children[count++] = node.left;
        }
        if (node.right != null) {
            children[count++] = node.right;
        }

        for (int i = 0; i < count; i++) {
            Node child = children[i];
            boolean isLast = i == count - 1;

            builder.append(prefix);
            builder.append(isLast ? "\\-- " : "|-- ");
            builder.append(child.value).append('\n');

            appendChildren(builder, child, prefix + (isLast ? "    " : "|   "));
        }
    }

    private Node rebalance(Node node) {
        int balance = balanceFactor(node);

        // Left-heavy subtree
        if (balance > 1) {
            if (balanceFactor(node.left) < 0) {
                node.left = rotateLeft(node.left);
            }
            return rotateRight(node);
        }

        // Right-heavy subtree
        if (balance < -1) {
            if (balanceFactor(node.right) > 0) {
                node.right = rotateRight(node.right);
            }
            return rotateLeft(node);
        }

        return node;
    }

    private Node rotateLeft(Node node) {
        Node newRoot = node.right;
        Node movedSubtree = newRoot.left;

        newRoot.left = node;
        node.right = movedSubtree;

        updateHeight(node);
        updateHeight(newRoot);

        return newRoot;
    }

    private Node rotateRight(Node node) {
        Node newRoot = node.left;
        Node movedSubtree = newRoot.right;

        newRoot.right = node;
        node.left = movedSubtree;

        updateHeight(node);
        updateHeight(newRoot);

        return newRoot;
    }

    private int balanceFactor(Node node) {
        return nodeHeight(node.left) - nodeHeight(node.right);
    }

    private void updateHeight(Node node) {
        node.height = 1 + Math.max(nodeHeight(node.left), nodeHeight(node.right));
    }

    private int nodeHeight(Node node) {
        return node == null ? 0 : node.height;
    }

    private static final class Node {
        private final int value;
        private int height = 1;
        private Node left;
        private Node right;

        private Node(int value) {
            this.value = value;
        }
    }
}
