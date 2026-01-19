package de.lukas.computerverleih;

/**
 * Klasse Klasse - Repräsentiert eine Schulklasse
 *
 * Eine Schulklasse hat eine Bezeichnung (z.B. "11BGP3"), eine Schulform
 * (z.B. "BG" für Berufliches Gymnasium) und einen Zeitraum (Schuljahr).
 *
 * Laut Klassendiagramm (Material 1):
 * - nr: int
 * - bezeichnung: String
 * - schulform: String
 * - beginn: Datum
 * - ende: Datum
 * - autowert: int = 10 (statischer Zähler für automatische IDs)
 */
public class Klasse {

    // Statischer Zähler für automatische Nummernvergabe
    // "static" bedeutet: alle Objekte teilen sich diese Variable
    // Startwert ist 10 (laut Klassendiagramm)
    private static int autowert = 10;

    // Eindeutige Nummer der Klasse
    private int nr;

    // Bezeichnung der Klasse (z.B. "11BGP3", "12SFE1")
    private String bezeichnung;

    // Schulform (z.B. "BG", "SFE", "BBE")
    // BG = Berufliches Gymnasium
    // SFE = Schulform E (evtl. Fachoberschule)
    // BBE = Berufsbildende Schule
    private String schulform;

    // Beginn des Schuljahres (z.B. 01.08.2024)
    private Datum beginn;

    // Ende des Schuljahres (z.B. 31.07.2025)
    private Datum ende;

    /**
     * Konstruktor - erstellt eine neue Schulklasse
     *
     * Die Nummer wird automatisch vergeben und hochgezählt.
     *
     * @param bezeichnung Die Klassenbezeichnung (z.B. "11BGP3")
     * @param schulform   Die Schulform (z.B. "BG")
     * @param beginn      Beginn des Schuljahres
     * @param ende        Ende des Schuljahres
     */
    public Klasse(String bezeichnung, String schulform, Datum beginn, Datum ende) {
        // Automatische Nummernvergabe: aktuellen Wert nehmen, dann erhöhen
        this.nr = autowert;
        autowert = autowert + 1;  // oder: autowert++

        // Parameter in Attribute übernehmen
        this.bezeichnung = bezeichnung;
        this.schulform = schulform;
        this.beginn = beginn;
        this.ende = ende;
    }

    /**
     * Gibt die Nummer der Klasse zurück
     *
     * @return Die eindeutige Nummer
     */
    public int getNr() {
        return this.nr;
    }

    /**
     * Gibt die Bezeichnung zurück
     *
     * @return Die Klassenbezeichnung (z.B. "11BGP3")
     */
    public String getBezeichnung() {
        return this.bezeichnung;
    }

    /**
     * Gibt die Schulform zurück
     *
     * @return Die Schulform (z.B. "BG", "BBE")
     */
    public String getSchulform() {
        return this.schulform;
    }

    /**
     * Gibt das Beginndatum zurück
     *
     * @return Das Datum des Schuljahresbeginns
     */
    public Datum getBeginn() {
        return this.beginn;
    }

    /**
     * Gibt das Enddatum zurück
     *
     * @return Das Datum des Schuljahresendes
     */
    public Datum getEnde() {
        return this.ende;
    }

    /**
     * Prüft ob ein gegebenes Datum innerhalb des Schuljahres liegt
     *
     * Diese Hilfsmethode ist nützlich um festzustellen, ob die Klasse
     * zu einem bestimmten Zeitpunkt aktiv war.
     *
     * @param datum Das zu prüfende Datum
     * @return true wenn das Datum im Schuljahr liegt, sonst false
     */
    public boolean istAktivAm(Datum datum) {
        // Datum muss >= beginn und <= ende sein
        // differenzInTagen gibt positiv zurück wenn datum1 > datum2

        int nachBeginn = Datum.differenzInTagen(datum, this.beginn);
        int vorEnde = Datum.differenzInTagen(this.ende, datum);

        // Beide müssen >= 0 sein (Datum liegt zwischen beginn und ende)
        return nachBeginn >= 0 && vorEnde >= 0;
    }

    /**
     * Gibt die Klasse als String zurück
     *
     * @return String im Format "Bezeichnung (Schulform)"
     */
    @Override
    public String toString() {
        return this.bezeichnung + " (" + this.schulform + ")";
    }
}
