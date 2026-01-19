package de.lukas.computerverleih;

/**
 * Klasse Schueler - Repräsentiert einen Schüler der Schule
 *
 * Diese Klasse erbt von Person und fügt das Attribut "geburtsdatum" hinzu.
 * Bei Schülern speichert meineKlassen alle Klassen, in denen der
 * Schüler im Laufe seiner Schulzeit war.
 *
 * Wichtig für Aufgabe 1.5:
 * - Schüler der Schulform "BBE" zahlen nur die Grundgebühr
 *
 * Laut Klassendiagramm (Material 1):
 * - geburtsdatum: Datum
 */
public class Schueler extends Person {

    // Geburtsdatum des Schülers
    private Datum geburtsdatum;

    /**
     * Konstruktor - erstellt einen neuen Schüler
     *
     * Ruft zuerst den Konstruktor der Oberklasse Person auf (mit super()),
     * dann wird das Schüler-spezifische Attribut gesetzt.
     *
     * @param nachn     Nachname des Schülers
     * @param vorn      Vorname des Schülers
     * @param kbez      Klassenbezeichnung der aktuellen Klasse
     * @param verw      Referenz auf die Verwaltung
     * @param gebDatum  Geburtsdatum des Schülers
     */
    public Schueler(String nachn, String vorn, String kbez, Verwaltung verw, Datum gebDatum) {
        // super() ruft den Konstruktor der Oberklasse (Person) auf
        // Dort werden nr, nachname, vorname, meineKlassen und verwaltung gesetzt
        super(nachn, vorn, kbez, verw);

        // Schüler-spezifisches Attribut setzen
        this.geburtsdatum = gebDatum;
    }

    /**
     * Gibt das Geburtsdatum zurück
     *
     * @return Das Geburtsdatum des Schülers
     */
    public Datum getGeburtsdatum() {
        return this.geburtsdatum;
    }

    /**
     * Prüft ob der Schüler zur Schulform BBE gehört
     *
     * Das ist wichtig für die Gebührenberechnung in Aufgabe 1.5:
     * Schüler der Schulform BBE zahlen nur die Grundgebühr.
     *
     * @return true wenn Schulform BBE, sonst false
     */
    public boolean istBBESchueler() {
        // Aktuelle Klasse holen
        Klasse aktuelleKlasse = this.getAktuelleKlasse();

        // Prüfung ob Klasse vorhanden ist
        if (aktuelleKlasse == null) {
            // Keine Klasse -> kein BBE Schüler
            return false;
        }

        // Schulform der aktuellen Klasse prüfen
        String schulform = aktuelleKlasse.getSchulform();

        // equals() vergleicht Strings auf Gleichheit
        // "BBE".equals(schulform) ist sicherer als schulform.equals("BBE")
        // weil es bei schulform == null nicht abstürzt
        return "BBE".equals(schulform);
    }

    /**
     * Gibt die Schulform der aktuellen Klasse zurück
     *
     * @return Die Schulform (z.B. "BG", "BBE") oder null
     */
    public String getSchulform() {
        Klasse aktuelleKlasse = this.getAktuelleKlasse();

        if (aktuelleKlasse == null) {
            return null;
        }

        return aktuelleKlasse.getSchulform();
    }

    /**
     * Gibt den Schüler als String zurück
     *
     * Format: "Vorname Nachname (Klasse)"
     *
     * @return String-Darstellung des Schülers
     */
    @Override
    public String toString() {
        // Name zusammenbauen (vorname und nachname sind von Person geerbt)
        String name = this.vorname + " " + this.nachname;

        // Aktuelle Klasse holen
        Klasse aktuelleKlasse = this.getAktuelleKlasse();

        // Prüfung ob Klasse vorhanden ist
        if (aktuelleKlasse == null) {
            return name + " (keine Klasse)";
        }

        // Mit Klassenbezeichnung zurückgeben
        return name + " (" + aktuelleKlasse.getBezeichnung() + ")";
    }
}
