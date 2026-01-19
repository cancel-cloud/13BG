package de.lukas.computerverleih;

/**
 * Abstrakte Klasse Person - Basisklasse für Schüler und Lehrkraft
 *
 * AUFGABE 1.4: Implementierung der Klasse Person
 *
 * Diese Klasse ist ABSTRAKT - das bedeutet:
 * - Man kann keine Person direkt erstellen (new Person() geht nicht)
 * - Sie dient als Vorlage für Unterklassen (Schüler, Lehrkraft)
 * - Unterklassen erben alle Attribute und Methoden
 *
 * Laut Klassendiagramm (Material 1):
 * - nr: int
 * - nachname: String
 * - vorname: String
 * - autowert: int = 1000 (statischer Zähler)
 * - meineKlassen: List<Klasse> (alle Klassen der Person)
 *
 * Hinweis aus der Aufgabe:
 * - Bei Schülern: meineKlassen enthält alle Klassen, in denen er war
 * - Bei Lehrkräften: meineKlassen enthält alle Klassen, wo er Klassenlehrer war
 */
public abstract class Person {

    // Statischer Zähler für automatische Nummernvergabe
    // Startwert ist 1000 (laut Klassendiagramm)
    private static int autowert = 1000;

    // Eindeutige Nummer der Person
    protected int nr;

    // Nachname der Person
    protected String nachname;

    // Vorname der Person
    protected String vorname;

    // Liste aller Klassen, zu denen die Person gehört(e)
    // "protected" bedeutet: Unterklassen können darauf zugreifen
    protected List<Klasse> meineKlassen;

    // Referenz auf die Verwaltung (für getKlasse-Methode)
    // Die Verwaltung kennt alle Klassen der Schule
    protected Verwaltung verwaltung;

    /**
     * Konstruktor - erstellt eine neue Person
     *
     * Die Nummer wird automatisch vergeben und hochgezählt.
     *
     * @param nachn Nachname der Person
     * @param vorn  Vorname der Person
     * @param kbez  Klassenbezeichnung der ersten/aktuellen Klasse
     * @param verw  Referenz auf die Verwaltung
     */
    public Person(String nachn, String vorn, String kbez, Verwaltung verw) {
        // Automatische Nummernvergabe
        this.nr = autowert;
        autowert = autowert + 1;

        // Parameter in Attribute übernehmen
        this.nachname = nachn;
        this.vorname = vorn;
        this.verwaltung = verw;

        // Leere Liste für Klassen erstellen
        this.meineKlassen = new List<Klasse>();

        // Die erste Klasse hinzufügen (wenn gefunden)
        // hinzufuegenAktuelleKlasse prüft ob die Klasse existiert
        this.hinzufuegenAktuelleKlasse(kbez);
    }

    /**
     * Gibt die aktuelle Klasse der Person zurück
     *
     * Die aktuelle Klasse ist immer die letzte in der Liste,
     * da neue Klassen immer am Ende hinzugefügt werden.
     *
     * @return Die aktuelle Klasse, oder null wenn keine vorhanden
     */
    public Klasse getAktuelleKlasse() {
        // Prüfung ob überhaupt Klassen vorhanden sind
        if (meineKlassen.size() == 0) {
            // Keine Klassen -> null zurückgeben
            return null;
        }

        // Letzte Klasse in der Liste ist die aktuelle
        // Index ist size() - 1 (da Listen bei 0 anfangen)
        int letzterIndex = meineKlassen.size() - 1;
        return meineKlassen.get(letzterIndex);
    }

    /**
     * Fügt eine Klasse zur Liste hinzu, wenn sie existiert
     *
     * Diese Methode sucht die Klasse in der Verwaltung und fügt
     * sie nur hinzu, wenn sie gefunden wurde.
     *
     * Hinweis: Jede neue Klasse wird am ENDE der Liste hinzugefügt!
     *
     * @param kbez Die Bezeichnung der Klasse (z.B. "11BGP3")
     * @return true wenn die Klasse gefunden und hinzugefügt wurde, sonst false
     */
    public boolean hinzufuegenAktuelleKlasse(String kbez) {
        // Prüfung ob Verwaltung vorhanden ist
        if (verwaltung == null) {
            // Ohne Verwaltung können wir keine Klasse suchen
            return false;
        }

        // Klasse in der Verwaltung suchen
        // getKlasse gibt die Klasse zurück oder null wenn nicht gefunden
        Klasse gefundeneKlasse = verwaltung.getKlasse(kbez);

        // Prüfung ob Klasse gefunden wurde
        if (gefundeneKlasse == null) {
            // Klasse existiert nicht -> false zurückgeben
            return false;
        }

        // Klasse gefunden -> am Ende der Liste hinzufügen
        meineKlassen.add(gefundeneKlasse);

        // Erfolgreich hinzugefügt -> true zurückgeben
        return true;
    }

    /**
     * Gibt die Nummer der Person zurück
     *
     * @return Die eindeutige Nummer
     */
    public int getNr() {
        return this.nr;
    }

    /**
     * Gibt den Nachnamen zurück
     *
     * @return Der Nachname
     */
    public String getNachname() {
        return this.nachname;
    }

    /**
     * Gibt den Vornamen zurück
     *
     * @return Der Vorname
     */
    public String getVorname() {
        return this.vorname;
    }

    /**
     * Gibt die Liste aller Klassen zurück
     *
     * @return Liste der Klassen
     */
    public List<Klasse> getMeineKlassen() {
        return this.meineKlassen;
    }

    /**
     * Gibt die Person als String zurück
     *
     * Format: "Vorname Nachname"
     *
     * @return String-Darstellung der Person
     */
    @Override
    public String toString() {
        return this.vorname + " " + this.nachname;
    }
}
