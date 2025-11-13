package de.lukas.socialMedia;

import java.util.ArrayList;

/**
 * Generic List class for storing elements of type T.
 * Provides methods for adding, removing, and accessing elements.
 *
 * @param <T> the type of elements in this list
 * @author Social Media Platform Development Team
 * @version 1.0
 */
public class List<T> {
    private ArrayList<T> elements;

    /**
     * Creates an empty generic list.
     */
    public List() {
        this.elements = new ArrayList<>();
    }

    /**
     * Appends the specified element to the end of this list.
     *
     * @param obj the element to be appended
     */
    public void add(T obj) {
        elements.add(obj);
    }

    /**
     * Inserts the specified element at the specified position in this list.
     *
     * @param index the index at which to insert the element
     * @param obj the element to be inserted
     */
    public void add(int index, T obj) {
        if (index >= 0 && index <= elements.size()) {
            elements.add(index, obj);
        }
    }

    /**
     * Checks if this list contains the specified element.
     *
     * @param obj the element to check for
     * @return true if this list contains the element, false otherwise
     */
    public boolean contains(T obj) {
        return elements.contains(obj);
    }

    /**
     * Returns the element at the specified position in this list.
     *
     * @param index the index of the element to return
     * @return the element at the specified position, or null if index is invalid
     */
    public T get(int index) {
        if (index < 0 || index >= elements.size()) {
            return null;
        }
        return elements.get(index);
    }

    /**
     * Removes the element at the specified position in this list.
     *
     * @param index the index of the element to remove
     * @return the removed element, or null if index is invalid
     */
    public T remove(int index) {
        if (index < 0 || index >= elements.size()) {
            return null;
        }
        return elements.remove(index);
    }

    /**
     * Removes the first occurrence of the specified element from this list.
     * If the element appears multiple times, only the first occurrence is removed.
     *
     * @param obj the element to be removed
     * @return true if the element was found and removed, false otherwise
     */
    public boolean remove(T obj) {
        return elements.remove(obj);
    }

    /**
     * Returns the number of elements in this list.
     *
     * @return the number of elements
     */
    public int size() {
        return elements.size();
    }
}
