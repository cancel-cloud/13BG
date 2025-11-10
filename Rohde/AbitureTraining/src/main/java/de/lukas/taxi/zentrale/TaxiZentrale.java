package de.lukas.taxi.zentrale;

import de.lukas.taxi.core.*;

import java.util.Comparator;

/**
 * Central taxi dispatch system.
 * Manages taxis, drivers, and orders.
 */
public class TaxiZentrale {
    private final List<Taxi> alleTaxis;
    private final List<Fahrer> alleFahrer;
    private final List<Auftrag> auftraege;

    /**
     * Creates a new taxi dispatch center with sample data for testing.
     */
    public TaxiZentrale() {
        this.alleTaxis = new List<>();
        this.alleFahrer = new List<>();
        this.auftraege = new List<>();

        // Initialize with sample data
        initializeSampleData();
    }

    /**
     * Initializes sample taxis and drivers for testing.
     */
    private void initializeSampleData() {
        // Sample drivers
        alleFahrer.add(new Fahrer(101, "Max", "Müller"));
        alleFahrer.add(new Fahrer(102, "Anna", "Schmidt"));
        alleFahrer.add(new Fahrer(103, "Karl-Heinz", "Großfuss"));
        alleFahrer.add(new Fahrer(104, "Tobias", "Tischendorf"));
        alleFahrer.add(new Fahrer(105, "Ina", "Ach"));

        // Sample taxis with different locations
        Taxi taxi1 = new Taxi(1, 1000.0, new Adresse("Hauptstraße 1", "60311", "Frankfurt a.M."));
        taxi1.setStatus(Taxi.FREI);
        alleTaxis.add(taxi1);

        Taxi taxi2 = new Taxi(2, 2500.0, new Adresse("Bahnhofstraße 10", "60329", "Frankfurt a.M."));
        taxi2.setStatus(Taxi.FREI);
        alleTaxis.add(taxi2);

        Taxi taxi3 = new Taxi(3, 5000.0, new Adresse("Sonnenweg 15", "60487", "Frankfurt a.M."));
        taxi3.setStatus(Taxi.FREI);
        alleTaxis.add(taxi3);

        Taxi taxi4 = new Taxi(4, 3500.0, new Adresse("Marktplatz 5", "60313", "Frankfurt a.M."));
        taxi4.setStatus(Taxi.ANFAHRT_KUNDE);
        alleTaxis.add(taxi4);

        Taxi taxi5 = new Taxi(5, 7800.0, new Adresse("Kirchweg 20", "60320", "Frankfurt a.M."));
        taxi5.setStatus(Taxi.FREI);
        alleTaxis.add(taxi5);

        Taxi taxi6 = new Taxi(6, 4200.0, new Adresse("Waldstraße 8", "60325", "Frankfurt a.M."));
        taxi6.setStatus(Taxi.FREI);
        alleTaxis.add(taxi6);
    }

    /**
     * Reads order data from string array and creates Auftrag objects.
     * Task 1.3.1: Parse semicolon-separated data.
     *
     * @param daten array of order data strings
     */
    public void einlesenAuftragsdaten(String[] daten) {
        if (daten == null || daten.length == 0) {
            return;
        }

        for (String auftragsdaten : daten) {
            if (auftragsdaten == null || auftragsdaten.trim().isEmpty()) {
                continue;
            }

            try {
                // For this method, we need taxi and driver
                // In a real system, these would be assigned
                // For now, use first available taxi and driver
                Taxi taxi = alleTaxis.size() > 0 ? alleTaxis.get(0) : null;
                Fahrer fahrer = alleFahrer.size() > 0 ? alleFahrer.get(0) : null;

                if (taxi != null && fahrer != null) {
                    Auftrag auftrag = new Auftrag(taxi, fahrer, auftragsdaten);
                    auftraege.add(auftrag);
                }
            } catch (IllegalArgumentException e) {
                System.err.println("Fehler beim Einlesen von Auftragsdaten: " + e.getMessage());
            }
        }
    }

    /**
     * Task 1.3.3: Returns up to 5 nearest FREE taxis sorted by distance.
     * Only considers taxis with status FREI.
     *
     * @param von pickup address
     * @return list of up to 5 nearest free taxis
     */
    public List<Taxi> getTaxisSortiertNachStandort(Adresse von) {
        if (von == null) {
            return new List<>();
        }

        // Collect free taxis with their distances
        java.util.List<TaxiDistance> taxisWithDistance = new java.util.ArrayList<>();

        for (int i = 0; i < alleTaxis.size(); i++) {
            Taxi taxi = alleTaxis.get(i);
            if (taxi.getStatus() == Taxi.FREI) {
                Adresse standort = taxi.ermittleAktuellenStandort();
                if (standort != null) {
                    int distance = von.ermittleEntfernungNach(standort);
                    taxisWithDistance.add(new TaxiDistance(taxi, distance));
                }
            }
        }

        // Sort by distance (ascending)
        taxisWithDistance.sort(Comparator.comparingInt(td -> td.distance));

        // Return up to 5 taxis
        List<Taxi> result = new List<>();
        int limit = Math.min(5, taxisWithDistance.size());
        for (int i = 0; i < limit; i++) {
            result.add(taxisWithDistance.get(i).taxi);
        }

        return result;
    }

    /**
     * Asks a driver if they are willing to accept an order.
     *
     * @param fahrer the driver to ask
     * @param von    pickup address
     * @param nach   destination address
     * @return true if the driver accepts
     */
    public boolean anfragenBereitschaft(Fahrer fahrer, Adresse von, Adresse nach) {
        if (fahrer == null) {
            return false;
        }
        return fahrer.annehmenAuftrag(von, nach);
    }

    /**
     * Assigns an order to a specific taxi with a driver and updates taxi status.
     *
     * @param taxi   the specific taxi to assign
     * @param fahrer the assigned driver
     * @param von    pickup address
     * @param nach   destination address
     */
    public void vergebeAuftrag(Taxi taxi, Fahrer fahrer, Adresse von, Adresse nach) {
        if (taxi == null || fahrer == null) {
            return;
        }

        // Update the specific taxi's status
        taxi.setStatus(Taxi.ANFAHRT_KUNDE);
    }

    /**
     * Task 1.3.2: Processes an order request.
     * Finds nearest taxis, asks drivers for willingness, assigns to first accepting driver.
     *
     * @param von  pickup address
     * @param nach destination address
     * @return true if order was successfully assigned
     */
    public boolean bearbeiteAuftrag(Adresse von, Adresse nach) {
        if (von == null || nach == null) {
            return false;
        }

        // Step 1: Get sorted list of free taxis
        List<Taxi> verfuegbareTaxis = getTaxisSortiertNachStandort(von);

        if (verfuegbareTaxis.isEmpty()) {
            return false;
        }

        // Step 2: Iterate through taxis and ask their drivers
        for (int i = 0; i < verfuegbareTaxis.size(); i++) {
            Taxi taxi = verfuegbareTaxis.get(i);

            // Find a driver (in real system, each taxi would have assigned driver)
            // For simplicity, try drivers in order
            for (int j = 0; j < alleFahrer.size(); j++) {
                Fahrer fahrer = alleFahrer.get(j);

                // Step 3: Ask for willingness
                if (anfragenBereitschaft(fahrer, von, nach)) {
                    // Step 4: Assign order with the specific taxi
                    vergebeAuftrag(taxi, fahrer, von, nach);
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Searches for a taxi by vehicle number.
     *
     * @param wagenNr vehicle number
     * @return the taxi, or null if not found
     */
    public Taxi sucheTaxi(int wagenNr) {
        for (int i = 0; i < alleTaxis.size(); i++) {
            Taxi taxi = alleTaxis.get(i);
            if (taxi.getWagenNr() == wagenNr) {
                return taxi;
            }
        }
        return null;
    }

    /**
     * Searches for a driver by driver number.
     *
     * @param fahrerNr driver number
     * @return the driver, or null if not found
     */
    public Fahrer sucheFahrer(int fahrerNr) {
        for (int i = 0; i < alleFahrer.size(); i++) {
            Fahrer fahrer = alleFahrer.get(i);
            if (fahrer.getFahrerNr() == fahrerNr) {
                return fahrer;
            }
        }
        return null;
    }

    // Getters (return defensive copies to protect internal state)
    public List<Taxi> getAlleTaxis() {
        return new List<>(alleTaxis);
    }

    public List<Fahrer> getAlleFahrer() {
        return new List<>(alleFahrer);
    }

    public List<Auftrag> getAuftraege() {
        return new List<>(auftraege);
    }

    /**
     * Helper class for sorting taxis by distance.
     */
    private static class TaxiDistance {
        final Taxi taxi;
        final int distance;

        TaxiDistance(Taxi taxi, int distance) {
            this.taxi = taxi;
            this.distance = distance;
        }
    }
}
