package de.lukas.socialMedia;

/**
 * Random number generator class.
 * Provides methods for generating random integers.
 *
 * @author Social Media Platform Development Team
 * @version 1.0
 */
public class Random {
    private java.util.Random random;

    /**
     * Creates a new Random object.
     */
    public Random() {
        this.random = new java.util.Random();
    }

    /**
     * Generates a random integer from the entire range of positive integers.
     *
     * @return a random positive integer
     */
    public int nextInt() {
        return random.nextInt(Integer.MAX_VALUE);
    }

    /**
     * Generates a random integer in the range [0, n).
     * The range is from 0 (inclusive) to n (exclusive).
     *
     * @param n the upper bound (exclusive)
     * @return a random integer between 0 (inclusive) and n (exclusive)
     */
    public int nextInt(int n) {
        return random.nextInt(n);
    }
}
