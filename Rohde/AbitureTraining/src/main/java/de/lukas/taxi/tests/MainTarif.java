package de.lukas.taxi.tests;

import de.lukas.taxi.core.*;
import de.lukas.taxi.meter.Taxameter;

/**
 * Test: calculatePrice (Task 1.4.1)
 * Tests the fare calculation based on tariff table.
 */
public class MainTarif {

    public static void main(String[] args) {
        System.out.println("=== Test: Fare Calculation (Task 1.4.1) ===\n");

        Taxi taxi = new Taxi(1, 1000.0, new Adresse("Test", "00000", "Test"));
        Taxameter taxameter = new Taxameter(taxi, "COM1");

        System.out.println("Tariff structure:");
        System.out.println("  Base fare: " + Taxameter.GRUNDPREIS + " EUR");
        System.out.println("  First 15 km: " + Taxameter.TARIF1 + " EUR/km");
        System.out.println("  From 16th km: " + Taxameter.TARIF2 + " EUR/km");
        System.out.println();

        // Test various distances
        double[] testDistances = {0.0, 1.0, 5.0, 10.0, 15.0, 16.0, 20.0, 25.0, 50.0};

        System.out.println("Distance (km) | Fare (EUR) | Calculation");
        System.out.println("-------------|-----------|------------");

        for (double distance : testDistances) {
            double price = taxameter.calculatePrice(distance);
            String calculation = explainCalculation(distance);

            System.out.printf("%12.1f | %9.2f | %s%n", distance, price, calculation);
        }

        System.out.println();

        // Verify specific examples
        System.out.println("Verification:");
        System.out.println("  0 km = 3.50 EUR: " + verify(taxameter, 0.0, 3.5));
        System.out.println("  1 km = 5.50 EUR: " + verify(taxameter, 1.0, 5.5));
        System.out.println("  15 km = 33.50 EUR: " + verify(taxameter, 15.0, 33.5));
        System.out.println("  16 km = 35.25 EUR: " + verify(taxameter, 16.0, 35.25));
        System.out.println("  20 km = 42.25 EUR: " + verify(taxameter, 20.0, 42.25));

        System.out.println("\n=== Test Complete ===");
    }

    private static String explainCalculation(double distance) {
        if (distance <= 0) {
            return "Base fare only";
        } else if (distance <= 15.0) {
            return String.format("3.50 + %.1f * 2.00", distance);
        } else {
            double remaining = distance - 15.0;
            return String.format("3.50 + 15.0 * 2.00 + %.1f * 1.75", remaining);
        }
    }

    private static String verify(Taxameter taxameter, double distance, double expected) {
        double actual = taxameter.calculatePrice(distance);
        double diff = Math.abs(actual - expected);

        if (diff < 0.01) {
            return "PASS";
        } else {
            return String.format("FAIL (expected %.2f, got %.2f)", expected, actual);
        }
    }
}
