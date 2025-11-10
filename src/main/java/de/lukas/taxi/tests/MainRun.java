package de.lukas.taxi.tests;

import de.lukas.taxi.core.*;
import de.lukas.taxi.meter.Taxameter;

/**
 * Test: Taxameter.run() state machine (Task 1.4.3)
 * Tests the main taxameter operation with simulated button presses.
 */
public class MainRun {

    public static void main(String[] args) {
        System.out.println("=== Test: Taxameter State Machine (Task 1.4.3) ===\n");

        System.out.println("State machine buttons:");
        System.out.println("  1 = Free (Frei) - taxi sign ON");
        System.out.println("  2 = Approaching customer (Anfahrt Kunde) - taxi sign OFF");
        System.out.println("  3 = Approaching destination (Anfahrt Ziel) - record start data");
        System.out.println("  4 = Destination reached (Ziel erreicht) - calculate fare, save data");
        System.out.println("  5 = Print receipt (Quittung)");
        System.out.println();

        // Create taxi and taxameter
        Adresse startLocation = new Adresse("Hauptstraße 1", "60311", "Frankfurt a.M.");
        Taxi taxi = new Taxi(1, 1000.0, startLocation);
        Taxameter taxameter = new Taxameter(taxi, "COM1");

        // Simulate a complete trip sequence
        int[] buttonSequence = {
            1,  // Set to free
            2,  // Customer approaching
            3,  // Start trip (approaching destination)
            4,  // Destination reached (calculate fare)
            5,  // Print receipt
            1,  // Back to free
            0   // Exit
        };

        System.out.println("Simulating button sequence: ");
        for (int i = 0; i < buttonSequence.length; i++) {
            if (i > 0) System.out.print(" -> ");
            System.out.print(buttonSequence[i]);
        }
        System.out.println("\n");

        // Set scripted buttons
        taxameter.setScriptedButtons(buttonSequence);

        // Open serial port to simulate key present
        taxameter.getSerial().open();

        // Run the state machine
        System.out.println("--- Running Taxameter ---\n");
        taxameter.run();

        System.out.println("\n--- Taxameter Stopped ---\n");

        // Verification
        System.out.println("Final state:");
        System.out.println("  Taxi status: " + taxi.getStatusBeschreibung());
        System.out.println("  Taxi sign: " + (taxameter.isTaxiSign() ? "ON" : "OFF"));
        System.out.println("  Odometer: " + taxi.getKmStand() + " km");

        System.out.println("\n=== Test: Alternative Sequence ===\n");

        // Test another sequence: just check status changes
        Taxi taxi2 = new Taxi(2, 2000.0, startLocation);
        Taxameter taxameter2 = new Taxameter(taxi2, "COM2");

        int[] simpleSequence = {1, 2, 3, 0};
        taxameter2.setScriptedButtons(simpleSequence);
        taxameter2.getSerial().open();

        System.out.println("Simulating sequence: 1 -> 2 -> 3 -> 0");
        System.out.println();

        taxameter2.run();

        System.out.println("\nFinal status: " + taxi2.getStatusBeschreibung());
        System.out.println("(Should be ANFAHRT_ZIEL)");

        System.out.println("\n=== Test Complete ===");
    }
}
