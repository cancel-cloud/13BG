package de.lukas.taxi.core;

import java.util.Objects;

/**
 * Represents an address with street, postal code, and city.
 * Provides distance calculation functionality.
 */
public class Adresse {
    private final String strasse;
    private final String plz;
    private final String ort;

    /**
     * Creates a new address.
     *
     * @param strasse street name and number
     * @param plz     postal code
     * @param ort     city name
     * @throws NullPointerException if any parameter is null
     */
    public Adresse(String strasse, String plz, String ort) {
        this.strasse = Objects.requireNonNull(strasse, "strasse must not be null");
        this.plz = Objects.requireNonNull(plz, "plz must not be null");
        this.ort = Objects.requireNonNull(ort, "ort must not be null");
    }

    /**
     * Calculates the distance to another address in meters.
     * This is a deterministic stub implementation for testing purposes.
     *
     * @param andere the target address
     * @return distance in meters
     */
    public int ermittleEntfernungNach(Adresse andere) {
        if (andere == null) {
            return Integer.MAX_VALUE;
        }

        // Deterministic calculation based on address components
        // This ensures consistent ordering for tests
        int distance = 0;

        // Factor in postal code difference (assume similar PLZ means closer)
        try {
            int plz1 = Integer.parseInt(this.plz);
            int plz2 = Integer.parseInt(andere.plz);
            distance += Math.abs(plz1 - plz2) * 100;
        } catch (NumberFormatException e) {
            distance += 5000;
        }

        // Factor in street name difference
        distance += Math.abs(this.strasse.hashCode() - andere.strasse.hashCode()) % 10000;

        // Factor in city difference
        if (!this.ort.equals(andere.ort)) {
            distance += 10000;
        }

        return Math.abs(distance);
    }

    /**
     * Returns the address in the format "[strasse],[plz],[ort]".
     *
     * @return formatted address string
     */
    @Override
    public String toString() {
        return strasse + "," + plz + "," + ort;
    }

    // Getters
    public String getStrasse() {
        return strasse;
    }

    public String getPlz() {
        return plz;
    }

    public String getOrt() {
        return ort;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Adresse adresse = (Adresse) o;
        return Objects.equals(strasse, adresse.strasse) &&
               Objects.equals(plz, adresse.plz) &&
               Objects.equals(ort, adresse.ort);
    }

    @Override
    public int hashCode() {
        return Objects.hash(strasse, plz, ort);
    }
}
