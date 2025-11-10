package de.lukas.taxi.core;

/**
 * Represents a taxi order with all relevant information.
 * Task 1.3.1: Constructor parses semicolon-separated data.
 */
public class Auftrag {
    private static int nextAuftragNr = 1;

    private final int auftragNr;
    private final DateTime start;
    private final DateTime ende;
    private final double fahrstrecke;
    private final double fahrpreis;
    private final int autowert;
    private final Taxi taxi;
    private final Fahrer fahrer;
    private final Adresse von;
    private final Adresse nach;

    /**
     * Creates a new order by parsing the auftragsdaten string.
     * Format: "von; nach; start; ende; km; preis"
     * Address format: "[strasse],[plz],[ort]"
     *
     * @param taxi          the assigned taxi
     * @param fahrer        the assigned driver
     * @param auftragsdaten semicolon-separated order data
     * @throws IllegalArgumentException if the format is invalid
     */
    public Auftrag(Taxi taxi, Fahrer fahrer, String auftragsdaten) {
        this.taxi = taxi;
        this.fahrer = fahrer;
        this.auftragNr = nextAuftragNr++;
        this.autowert = 0;

        if (auftragsdaten == null || auftragsdaten.trim().isEmpty()) {
            throw new IllegalArgumentException("Auftragsdaten dürfen nicht leer sein");
        }

        // Split by semicolon
        String[] parts = auftragsdaten.split(";");
        if (parts.length != 6) {
            throw new IllegalArgumentException(
                    "Ungültiges Format. Erwartet: 6 Teile getrennt durch ';', erhalten: " + parts.length);
        }

        try {
            // Parse von (pickup address)
            this.von = parseAdresse(parts[0].trim());

            // Parse nach (destination address)
            this.nach = parseAdresse(parts[1].trim());

            // Parse start time
            this.start = new DateTime(parts[2].trim());

            // Parse end time
            this.ende = new DateTime(parts[3].trim());

            // Parse distance
            this.fahrstrecke = Double.parseDouble(parts[4].trim());

            // Parse price
            this.fahrpreis = Double.parseDouble(parts[5].trim());

        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Fehler beim Parsen der Auftragsdaten: " + e.getMessage(), e);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Ungültige Zahl in Auftragsdaten: " + e.getMessage(), e);
        }
    }

    /**
     * Parses an address from the format "[strasse],[plz],[ort]".
     *
     * @param adresseStr address string
     * @return parsed Adresse object
     * @throws IllegalArgumentException if the format is invalid
     */
    private Adresse parseAdresse(String adresseStr) {
        String[] adresseParts = adresseStr.split(",");
        if (adresseParts.length != 3) {
            throw new IllegalArgumentException(
                    "Ungültiges Adressformat. Erwartet: '[strasse],[plz],[ort]', erhalten: " + adresseStr);
        }
        return new Adresse(
                adresseParts[0].trim(),
                adresseParts[1].trim(),
                adresseParts[2].trim()
        );
    }

    /**
     * Returns a string representation of the order.
     *
     * @return formatted order string
     */
    @Override
    public String toString() {
        return "Auftrag{" +
                "auftragNr=" + auftragNr +
                ", von=" + von +
                ", nach=" + nach +
                ", start=" + start +
                ", ende=" + ende +
                ", fahrstrecke=" + fahrstrecke + " km" +
                ", fahrpreis=" + String.format("%.2f", fahrpreis) + " EUR" +
                ", taxi=" + (taxi != null ? taxi.getWagenNr() : "null") +
                ", fahrer=" + (fahrer != null ? fahrer.getFahrerNr() : "null") +
                '}';
    }

    /**
     * Formats order data for export (e.g., to Electronic Key).
     * Format: "[wagenNr];[fahrerNr];[von];[nach];[start];[ende];[fahrstrecke];[fahrpreis]"
     *
     * @return formatted order data string
     */
    public String formatForExport() {
        return taxi.getWagenNr() + ";" +
                fahrer.getFahrerNr() + ";" +
                von.toString() + ";" +
                nach.toString() + ";" +
                start.toString() + ";" +
                ende.toString() + ";" +
                fahrstrecke + ";" +
                fahrpreis;
    }

    // Getters
    public int getAuftragNr() {
        return auftragNr;
    }

    public DateTime getStart() {
        return start;
    }

    public DateTime getEnde() {
        return ende;
    }

    public double getFahrstrecke() {
        return fahrstrecke;
    }

    public double getFahrpreis() {
        return fahrpreis;
    }

    public int getAutowert() {
        return autowert;
    }

    public Taxi getTaxi() {
        return taxi;
    }

    public Fahrer getFahrer() {
        return fahrer;
    }

    public Adresse getVon() {
        return von;
    }

    public Adresse getNach() {
        return nach;
    }
}
