package de.lukas.searchtree;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BinarySearchTreeTest {

    @Test
    void insertsAndFindsValues() {
        BinarySearchTree tree = new BinarySearchTree();

        tree.insert(10);
        tree.insert(5);
        tree.insert(15);
        tree.insert(10); // duplicate should be ignored

        assertTrue(tree.contains(10));
        assertTrue(tree.contains(5));
        assertTrue(tree.contains(15));
        assertFalse(tree.contains(99));
        assertEquals(3, tree.size());
        assertEquals(List.of(5, 10, 15), tree.inOrder());
    }

    @Test
    void staysBalancedEvenWithSortedInsertions() {
        BinarySearchTree tree = new BinarySearchTree();

        for (int value = 1; value <= 10; value++) {
            tree.insert(value);
        }

        assertTrue(tree.isBalanced());
        assertEquals(10, tree.size());
        assertEquals(List.of(1,2,3,4,5,6,7,8,9,10), tree.inOrder());
        assertTrue(tree.height() <= 4, "AVL tree with 10 nodes should stay low");
    }

    @Test
    void createsTerminalTreeView() {
        BinarySearchTree tree = new BinarySearchTree(10, 5, 15, 3, 7);

        String expected = """
                10
                |-- 5
                |   |-- 3
                |   \\-- 7
                \\-- 15
                """;

        assertEquals(expected, tree.toTreeString());
    }
}
