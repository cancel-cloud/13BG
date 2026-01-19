package de.lukas.computerverleih;

/**
 * Klasse Lehrkraft - Repräsentiert eine Lehrkraft der Schule
 *
 * AUFGABE 1.4: Implementierung der Klasse Lehrkraft
 *
 * Diese Klasse erbt von Person und fügt das Attribut "sollstunden" hinzu.
 * Bei Lehrkräften speichert meineKlassen alle Klassen, in denen die
 * Lehrkraft Klassenleitung hatte.
 *
 * Laut Klassendiagramm (Material 1):
 * - sollstunden: double (Anzahl der Unterrichtsstunden pro Woche)
 *
 * Laut Aufgabenstellung:
 * - toString() gibt zurück: "Bernd Beyer – Klassenlehrer/in der 11BGP1"
 *   (Werte für das aktuelle Schuljahr)
 */
public class Lehrkraft extends Person {

    // Anzahl der Soll-Stunden pro Woche
    // Wichtig für Aufgabe 1.5: Lehrkräfte mit mehr als 20 Stunden zahlen 2€ mehr
    private double sollstunden;

    /**
     * Konstruktor - erstellt eine neue Lehrkraft
     *
     * Ruft zuerst den Konstruktor der Oberklasse Person auf (mit super()),
     * dann werden die Lehrkraft-spezifischen Attribute gesetzt.
     *
     * @param nachn    Nachname der Lehrkraft
     * @param vorn     Vorname der Lehrkraft
     * @param kbez     Klassenbezeichnung der Klasse, wo Klassenleitung
     * @param verw     Referenz auf die Verwaltung
     * @param sollstd  Anzahl der Sollstunden pro Woche
     */
    public Lehrkraft(String nachn, String vorn, String kbez, Verwaltung verw, double sollstd) {
        // super() ruft den Konstruktor der Oberklasse (Person) auf
        // Dort werden nr, nachname, vorname, meineKlassen und verwaltung gesetzt
        super(nachn, vorn, kbez, verw);

        // Lehrkraft-spezifisches Attribut setzen
        this.sollstunden = sollstd;
    }

    /**
     * Gibt die Sollstunden zurück
     *
     * @return Anzahl der Sollstunden pro Woche
     */
    public double getSollstunden() {
        return this.sollstunden;
    }

    /**
     * Setzt die Sollstunden
     *
     * @param sollstd Neue Anzahl der Sollstunden
     */
    public void setSollstunden(double sollstd) {
        this.sollstunden = sollstd;
    }

    /**
     * Gibt die Lehrkraft als String zurück
     *
     * WICHTIG: Laut Aufgabe soll das Format sein:
     * "Bernd Beyer – Klassenlehrer/in der 11BGP1"
     *
     * Die Werte beziehen sich auf das aktuelle Schuljahr,
     * also die aktuelle Klasse (letzte in der Liste).
     *
     * @return String-Darstellung der Lehrkraft
     */
    @Override
    public String toString() {
        // Erst den Namen zusammenbauen
        // vorname und nachname sind von der Oberklasse geerbt
        String name = this.vorname + " " + this.nachname;

        // Die aktuelle Klasse holen (wo die Lehrkraft Klassenleitung hat)
        Klasse aktuelleKlasse = this.getAktuelleKlasse();

        // Prüfung ob eine Klasse vorhanden ist
        if (aktuelleKlasse == null) {
            // Keine Klasse -> nur den Namen zurückgeben
            return name + " – Klassenlehrer/in (keine Klasse)";
        }

        // Klassenbezeichnung holen (z.B. "11BGP1")
        String klassenBez = aktuelleKlasse.getBezeichnung();

        // Ergebnis zusammenbauen und zurückgeben
        // Hinweis: Das Zeichen "–" ist ein Halbgeviertstrich (En-Dash)
        return name + " – Klassenlehrer/in der " + klassenBez;
    }
}
