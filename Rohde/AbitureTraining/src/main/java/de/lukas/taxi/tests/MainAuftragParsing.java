package de.lukas.taxi.tests;

import de.lukas.taxi.core.*;

/**
 * Test: Auftrag parsing (Task 1.3.1)
 * Tests the parsing of semicolon-separated order data.
 */
public class MainAuftragParsing {

    public static void main(String[] args) {
        System.out.println("=== Test: Auftrag Parsing ===\n");

        // Create sample taxi and driver
        Taxi taxi = new Taxi(1, 1000.0, new Adresse("Hauptstraße 1", "60311", "Frankfurt a.M."));
        Fahrer fahrer = new Fahrer(101, "Max", "Müller");

        // Example order data from exam spec
        String auftragsdaten = "Sonnenweg 15,60487,Frankfurt a.M.; " +
                               "Markt 17,60311,Frankfurt a.M.; " +
                               "14.04.2022 16:28; " +
                               "14.04.2022 17:10; " +
                               "20.0; " +
                               "42.25";

        System.out.println("Input data:");
        System.out.println(auftragsdaten);
        System.out.println();

        try {
            Auftrag auftrag = new Auftrag(taxi, fahrer, auftragsdaten);

            System.out.println("Parsed successfully:");
            System.out.println(auftrag);
            System.out.println();

            System.out.println("Individual fields:");
            System.out.println("  Auftrag-Nr: " + auftrag.getAuftragNr());
            System.out.println("  Von: " + auftrag.getVon());
            System.out.println("  Nach: " + auftrag.getNach());
            System.out.println("  Start: " + auftrag.getStart());
            System.out.println("  Ende: " + auftrag.getEnde());
            System.out.println("  Fahrstrecke: " + auftrag.getFahrstrecke() + " km");
            System.out.println("  Fahrpreis: " + String.format("%.2f", auftrag.getFahrpreis()) + " EUR");
            System.out.println("  Taxi: " + auftrag.getTaxi().getWagenNr());
            System.out.println("  Fahrer: " + auftrag.getFahrer().getFahrerNr());
            System.out.println();

            System.out.println("Export format:");
            System.out.println(auftrag.formatForExport());

        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("\n=== Test: Invalid Format ===\n");

        String invalidData = "Invalid;Data;Format";
        try {
            new Auftrag(taxi, fahrer, invalidData);
            System.out.println("ERROR: Should have thrown exception!");
        } catch (IllegalArgumentException e) {
            System.out.println("Correctly rejected invalid data:");
            System.out.println("  " + e.getMessage());
        }

        System.out.println("\n=== Test Complete ===");
    }
}
