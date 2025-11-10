package de.lukas.taxi.core;

/**
 * Represents a taxi driver.
 */
public class Fahrer {
    private final int fahrerNr;
    private final String vorname;
    private final String nachname;

    /**
     * Creates a new driver.
     *
     * @param fahrerNr driver number
     * @param vorname  first name
     * @param nachname last name
     */
    public Fahrer(int fahrerNr, String vorname, String nachname) {
        this.fahrerNr = fahrerNr;
        this.vorname = vorname;
        this.nachname = nachname;
    }

    /**
     * Asks the driver whether they will accept an order.
     * This is a stub implementation that accepts most orders.
     *
     * @param von  pickup address
     * @param nach destination address
     * @return true if the driver accepts the order
     */
    public boolean annehmenAuftrag(Adresse von, Adresse nach) {
        if (von == null || nach == null) {
            return false;
        }

        // Simple acceptance logic: accept if distance is reasonable
        int distance = von.ermittleEntfernungNach(nach);

        // Accept orders up to 50km (50000m)
        return distance <= 50000;
    }

    // Getters
    public int getFahrerNr() {
        return fahrerNr;
    }

    public String getVorname() {
        return vorname;
    }

    public String getNachname() {
        return nachname;
    }

    @Override
    public String toString() {
        return "Fahrer{" +
                "fahrerNr=" + fahrerNr +
                ", vorname='" + vorname + '\'' +
                ", nachname='" + nachname + '\'' +
                '}';
    }
}
