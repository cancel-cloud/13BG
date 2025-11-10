package de.lukas.taxi.tests;

import de.lukas.taxi.core.*;
import de.lukas.taxi.zentrale.TaxiZentrale;

/**
 * Test: getTaxisSortiertNachStandort (Task 1.3.3)
 * Tests the sorting and filtering of taxis by distance.
 */
public class MainNearestTaxis {

    public static void main(String[] args) {
        System.out.println("=== Test: Nearest Taxis (Task 1.3.3) ===\n");

        TaxiZentrale zentrale = new TaxiZentrale();

        // Display all taxis
        System.out.println("All taxis in system:");
        List<Taxi> alleTaxis = zentrale.getAlleTaxis();
        for (int i = 0; i < alleTaxis.size(); i++) {
            Taxi taxi = alleTaxis.get(i);
            System.out.println("  " + taxi);
        }
        System.out.println();

        // Test case 1: Find taxis near a specific location
        Adresse pickupLocation = new Adresse("Bahnhofsplatz 1", "60314", "Frankfurt a.M.");
        System.out.println("Looking for taxis near: " + pickupLocation);
        System.out.println();

        List<Taxi> nearestTaxis = zentrale.getTaxisSortiertNachStandort(pickupLocation);

        System.out.println("Found " + nearestTaxis.size() + " free taxi(s):");
        for (int i = 0; i < nearestTaxis.size(); i++) {
            Taxi taxi = nearestTaxis.get(i);
            Adresse standort = taxi.ermittleAktuellenStandort();
            int distance = pickupLocation.ermittleEntfernungNach(standort);

            System.out.println("  " + (i + 1) + ". Taxi " + taxi.getWagenNr() +
                    " - Distance: " + distance + "m" +
                    " - Location: " + standort);
        }
        System.out.println();

        // Verify properties
        System.out.println("Verification:");
        System.out.println("  - Max 5 taxis: " + (nearestTaxis.size() <= 5 ? "PASS" : "FAIL"));
        System.out.println("  - Only FREE taxis: " + verifyOnlyFreeTaxis(nearestTaxis));
        System.out.println("  - Sorted by distance: " + verifySortedByDistance(nearestTaxis, pickupLocation));

        System.out.println("\n=== Test Complete ===");
    }

    private static String verifyOnlyFreeTaxis(List<Taxi> taxis) {
        for (int i = 0; i < taxis.size(); i++) {
            if (taxis.get(i).getStatus() != Taxi.FREI) {
                return "FAIL (found non-free taxi)";
            }
        }
        return "PASS";
    }

    private static String verifySortedByDistance(List<Taxi> taxis, Adresse from) {
        if (taxis.size() <= 1) {
            return "PASS";
        }

        for (int i = 0; i < taxis.size() - 1; i++) {
            Taxi taxi1 = taxis.get(i);
            Taxi taxi2 = taxis.get(i + 1);

            int dist1 = from.ermittleEntfernungNach(taxi1.ermittleAktuellenStandort());
            int dist2 = from.ermittleEntfernungNach(taxi2.ermittleAktuellenStandort());

            if (dist1 > dist2) {
                return "FAIL (not sorted)";
            }
        }
        return "PASS";
    }
}
