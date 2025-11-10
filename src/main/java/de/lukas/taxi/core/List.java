package de.lukas.taxi.core;

import java.util.ArrayList;

/**
 * Generic list wrapper around ArrayList with required methods.
 *
 * @param <T> the type of elements in this list
 */
public class List<T> {
    private final ArrayList<T> internalList;

    /**
     * Creates an empty list.
     */
    public List() {
        this.internalList = new ArrayList<>();
    }

    /**
     * Appends the specified element to the end of this list.
     *
     * @param obj element to be appended
     */
    public void add(T obj) {
        internalList.add(obj);
    }

    /**
     * Inserts the specified element at the specified position.
     *
     * @param index index at which to insert
     * @param obj   element to be inserted
     */
    public void add(int index, T obj) {
        internalList.add(index, obj);
    }

    /**
     * Returns the element at the specified position.
     *
     * @param index index of the element
     * @return the element at the specified position, or null if index is out of bounds
     */
    public T get(int index) {
        if (index < 0 || index >= internalList.size()) {
            return null;
        }
        return internalList.get(index);
    }

    /**
     * Removes the element at the specified position.
     *
     * @param index the index of the element to be removed
     * @return the element that was removed
     */
    public T remove(int index) {
        return internalList.remove(index);
    }

    /**
     * Returns the number of elements in this list.
     *
     * @return the number of elements
     */
    public int size() {
        return internalList.size();
    }

    /**
     * Returns true if this list contains no elements.
     *
     * @return true if empty
     */
    public boolean isEmpty() {
        return internalList.isEmpty();
    }

    /**
     * Gets the internal ArrayList for advanced operations.
     *
     * @return the internal ArrayList
     */
    public ArrayList<T> getInternalList() {
        return internalList;
    }
}
