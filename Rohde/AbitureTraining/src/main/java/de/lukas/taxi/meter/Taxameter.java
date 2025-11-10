package de.lukas.taxi.meter;

import de.lukas.taxi.core.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

/**
 * Taxameter for fare calculation and trip management.
 * Implements the state machine for taxi operations.
 */
public class Taxameter {
    // Tariff constants (Task 1.4.1)
    public static final double GRUNDPREIS = 3.5;
    public static final double TARIF1 = 2.0;
    public static final double TARIF2 = 1.75;

    private final Serial serial;
    private boolean taxiSign;
    private int driverNumber;
    private final Taxi taxi;
    private final EksAdapter eksAdapter;
    private final Display display;
    private final ThermalPrinter printer;

    // Trip data
    private double startKmStand;
    private Adresse startAdresse;
    private DateTime startZeit;
    private Adresse zielAdresse;

    // For testing: allows scripted button sequences
    private int[] scriptedButtons;
    private int scriptedButtonIndex;

    /**
     * Creates a new taxameter.
     *
     * @param taxi    the taxi this taxameter belongs to
     * @param comPort the COM port for serial communication
     */
    public Taxameter(Taxi taxi, String comPort) {
        this.taxi = taxi;
        this.taxiSign = true;
        this.driverNumber = -1;

        // Initialize serial communication
        this.serial = new Serial(comPort, 9600, 8, 1, 0);
        this.eksAdapter = new EksAdapter(serial);
        this.display = new Display();
        this.printer = new ThermalPrinter();

        this.scriptedButtons = null;
        this.scriptedButtonIndex = 0;
    }

    /**
     * Sets the taxi sign (roof light) state.
     *
     * @param on true to turn on, false to turn off
     */
    public void setTaxiSign(boolean on) {
        this.taxiSign = on;
        display.printLine("Dachzeichen: " + (on ? "AN" : "AUS"));
    }

    /**
     * Reads button input from user or scripted sequence.
     * Buttons: 1=frei, 2=Anfahrt Kunde, 3=Anfahrt Ziel, 4=Ziel erreicht, 5=Quittung
     *
     * @return the button number pressed (1-5), or 0 to exit
     */
    public int readButtons() {
        // If scripted buttons are available, use them
        if (scriptedButtons != null && scriptedButtonIndex < scriptedButtons.length) {
            int button = scriptedButtons[scriptedButtonIndex++];
            System.out.println("[INPUT] Taste " + button);
            return button;
        }

        // Otherwise, read from console
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Taste eingeben (1-5, 0=Ende): ");
            String line = reader.readLine();
            if (line == null || line.trim().isEmpty()) {
                return 0;
            }
            return Integer.parseInt(line.trim());
        } catch (IOException | NumberFormatException e) {
            return 0;
        }
    }

    /**
     * Task 1.4.1: Calculates the fare based on distance and tariff table.
     * <p>
     * Tariff:
     * - Base fare: 3.50 EUR
     * - First 15 km: 2.00 EUR per km
     * - From 16th km onwards: 1.75 EUR per km
     *
     * @param distanceKm distance in kilometers
     * @return calculated fare in EUR
     */
    public double calculatePrice(double distanceKm) {
        double preis = GRUNDPREIS;

        if (distanceKm <= 0) {
            return preis;
        }

        if (distanceKm <= 15.0) {
            // All distance at TARIF1
            preis += distanceKm * TARIF1;
        } else {
            // First 15 km at TARIF1
            preis += 15.0 * TARIF1;
            // Remaining km at TARIF2
            double remaining = distanceKm - 15.0;
            preis += remaining * TARIF2;
        }

        return preis;
    }

    /**
     * Task 1.4.3: Main state machine for taxameter operation.
     * Handles buttons 1-5:
     * - 1: Free status, taxi sign ON
     * - 2: Approaching customer, taxi sign OFF
     * - 3: Approaching destination, record start data
     * - 4: Destination reached, calculate fare, save to key
     * - 5: Print receipt
     */
    public void run() {
        display.printLine("Taxameter gestartet");

        // Open serial port
        if (!serial.open()) {
            display.printLine("Fehler: Serielle Schnittstelle konnte nicht geöffnet werden");
            return;
        }

        // Check for electronic key
        if (!eksAdapter.isKeyAvailable()) {
            display.printLine("Kein Key erkannt");
            taxi.setStatus(Taxi.NICHT_IN_BETRIEB);
            driverNumber = -1;
            return;
        }

        // Read driver number from key
        driverNumber = eksAdapter.readDriverNumber();
        if (driverNumber <= 0) {
            display.printLine("Fehler: FahrerNr konnte nicht gelesen werden");
            taxi.setStatus(Taxi.NICHT_IN_BETRIEB);
            driverNumber = -1;
            return;
        }

        display.printLine("Fahrer-Nr: " + driverNumber);

        // Main loop: process button presses
        boolean running = true;
        String currentAuftragData = null;

        while (running && eksAdapter.isKeyAvailable()) {
            display.printLine("\nStatus: " + taxi.getStatusBeschreibung());
            display.printLine("1=Frei | 2=Anfahrt Kunde | 3=Anfahrt Ziel | 4=Ziel | 5=Quittung | 0=Ende");

            int button = readButtons();

            switch (button) {
                case 0:
                    // Exit
                    running = false;
                    display.printLine("Taxameter beendet");
                    break;

                case 1:
                    // Free
                    taxi.setStatus(Taxi.FREI);
                    setTaxiSign(true);
                    display.printLine("Status: FREI");
                    break;

                case 2:
                    // Approaching customer
                    taxi.setStatus(Taxi.ANFAHRT_KUNDE);
                    setTaxiSign(false);
                    display.printLine("Status: ANFAHRT KUNDE");
                    break;

                case 3:
                    // Approaching destination - record start data
                    taxi.setStatus(Taxi.ANFAHRT_ZIEL);
                    setTaxiSign(false);

                    // Record trip start data
                    startKmStand = taxi.getKmStand();
                    startZeit = new DateTime();
                    // In real system, would get actual address from GPS
                    startAdresse = taxi.ermittleAktuellenStandort();
                    if (startAdresse == null) {
                        startAdresse = new Adresse("Start", "00000", "Unbekannt");
                    }

                    display.printLine("Status: ANFAHRT ZIEL");
                    display.printLine("Start-km: " + startKmStand);
                    display.printLine("Startzeit: " + startZeit);
                    display.printLine("Von: " + startAdresse);
                    break;

                case 4:
                    // Destination reached - calculate and save
                    if (taxi.getStatus() != Taxi.ANFAHRT_ZIEL) {
                        display.printLine("Fehler: Fahrt nicht gestartet!");
                        break;
                    }

                    // Record trip end data
                    double endKmStand = taxi.getKmStand() + 10.0; // Simulate 10km trip
                    taxi.setKmStand(endKmStand);

                    DateTime endZeit = new DateTime();
                    // In real system, would get actual address from GPS
                    zielAdresse = new Adresse("Ziel", "00000", "Unbekannt");

                    double fahrstrecke = endKmStand - startKmStand;
                    double fahrpreis = calculatePrice(fahrstrecke);

                    display.printLine("Status: ZIEL ERREICHT");
                    display.printLine("End-km: " + endKmStand);
                    display.printLine("Endzeit: " + endZeit);
                    display.printLine("Nach: " + zielAdresse);
                    display.printLine("Fahrstrecke: " + String.format("%.2f", fahrstrecke) + " km");
                    display.printLine("Fahrpreis: " + String.format("%.2f", fahrpreis) + " EUR");

                    // Format data for electronic key
                    // "[wagenNr];[fahrerNr];[von];[nach];[start];[ende];[fahrstrecke];[fahrpreis]"
                    currentAuftragData = taxi.getWagenNr() + ";" +
                            driverNumber + ";" +
                            startAdresse.toString() + ";" +
                            zielAdresse.toString() + ";" +
                            startZeit.toString() + ";" +
                            endZeit.toString() + ";" +
                            fahrstrecke + ";" +
                            fahrpreis;

                    // Write to electronic key
                    display.printLine("Speichere Auftragsdaten...");
                    int result = eksAdapter.writeData(currentAuftragData);

                    if (result == 0) {
                        display.printLine("Daten erfolgreich gespeichert");
                    } else if (result == 1) {
                        display.printLine("Fehler: Speicher voll!");
                    } else {
                        display.printLine("Fehler: Speichern fehlgeschlagen");
                    }

                    // Return to free status
                    taxi.setStatus(Taxi.FREI);
                    setTaxiSign(true);
                    break;

                case 5:
                    // Print receipt
                    if (currentAuftragData == null) {
                        display.printLine("Keine Auftragsdaten vorhanden!");
                        break;
                    }

                    display.printLine("Drucke Quittung...");
                    String[] parts = currentAuftragData.split(";");
                    if (parts.length >= 8) {
                        String receipt = "Taxi-Quittung\n" +
                                "=============\n" +
                                "Taxi-Nr: " + parts[0] + "\n" +
                                "Fahrer-Nr: " + parts[1] + "\n" +
                                "Von: " + parts[2] + "\n" +
                                "Nach: " + parts[3] + "\n" +
                                "Start: " + parts[4] + "\n" +
                                "Ende: " + parts[5] + "\n" +
                                "Strecke: " + parts[6] + " km\n" +
                                "Preis: " + String.format("%.2f", Double.parseDouble(parts[7])) + " EUR\n" +
                                "=============\n" +
                                "Vielen Dank!";
                        printer.printReceipt(receipt);
                    }
                    break;

                default:
                    display.printLine("Ungültige Taste: " + button);
                    break;
            }
        }

        // Key removed
        if (!eksAdapter.isKeyAvailable()) {
            display.printLine("Key entfernt");
            taxi.setStatus(Taxi.NICHT_IN_BETRIEB);
            driverNumber = -1;
        }

        serial.close();
        display.printLine("Taxameter gestoppt");
    }

    // Testing helper methods

    /**
     * Sets a scripted button sequence for testing.
     *
     * @param buttons array of button numbers to simulate
     */
    public void setScriptedButtons(int[] buttons) {
        this.scriptedButtons = buttons;
        this.scriptedButtonIndex = 0;
    }

    // Getters
    public boolean isTaxiSign() {
        return taxiSign;
    }

    public int getDriverNumber() {
        return driverNumber;
    }

    public Taxi getTaxi() {
        return taxi;
    }

    public Serial getSerial() {
        return serial;
    }

    public EksAdapter getEksAdapter() {
        return eksAdapter;
    }

    public Display getDisplay() {
        return display;
    }

    public ThermalPrinter getPrinter() {
        return printer;
    }
}
