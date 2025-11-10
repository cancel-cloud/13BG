package de.lukas.taxi.core;

/**
 * Represents a taxi vehicle with status tracking.
 */
public class Taxi {
    // Status constants
    public static final int FREI = 1;
    public static final int ANFAHRT_KUNDE = 2;
    public static final int ANFAHRT_ZIEL = 3;
    public static final int NICHT_IN_BETRIEB = 4;

    private final int wagenNr;
    private double kmStand;
    private int status;
    private Adresse aktuellerStandort;

    /**
     * Creates a new taxi.
     *
     * @param wagenNr vehicle number
     */
    public Taxi(int wagenNr) {
        this.wagenNr = wagenNr;
        this.kmStand = 0.0;
        this.status = NICHT_IN_BETRIEB;
        this.aktuellerStandort = null;
    }

    /**
     * Creates a new taxi with initial location and odometer reading.
     *
     * @param wagenNr          vehicle number
     * @param kmStand          initial odometer reading
     * @param aktuellerStandort initial location
     */
    public Taxi(int wagenNr, double kmStand, Adresse aktuellerStandort) {
        this.wagenNr = wagenNr;
        this.kmStand = kmStand;
        this.status = FREI;
        this.aktuellerStandort = aktuellerStandort;
    }

    /**
     * Returns the current location of the taxi.
     *
     * @return current address
     */
    public Adresse ermittleAktuellenStandort() {
        return aktuellerStandort;
    }

    /**
     * Sets the current location of the taxi.
     *
     * @param standort new location
     */
    public void setAktuellerStandort(Adresse standort) {
        this.aktuellerStandort = standort;
    }

    // Getters and setters
    public int getWagenNr() {
        return wagenNr;
    }

    public double getKmStand() {
        return kmStand;
    }

    public void setKmStand(double kmStand) {
        this.kmStand = kmStand;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * Returns a human-readable status description.
     *
     * @return status description
     */
    public String getStatusBeschreibung() {
        switch (status) {
            case FREI:
                return "Frei";
            case ANFAHRT_KUNDE:
                return "Anfahrt Kunde";
            case ANFAHRT_ZIEL:
                return "Anfahrt Ziel";
            case NICHT_IN_BETRIEB:
                return "Nicht in Betrieb";
            default:
                return "Unbekannt";
        }
    }

    @Override
    public String toString() {
        return "Taxi{" +
                "wagenNr=" + wagenNr +
                ", kmStand=" + kmStand +
                ", status=" + getStatusBeschreibung() +
                ", standort=" + (aktuellerStandort != null ? aktuellerStandort : "unbekannt") +
                '}';
    }
}
