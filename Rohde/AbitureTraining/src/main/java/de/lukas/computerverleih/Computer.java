package de.lukas.computerverleih;

/**
 * Klasse Computer - Repräsentiert einen ausleihbaren Computer
 *
 * Ein Computer hat eine Nummer, Bezeichnung, Leistungsindex und CPU-Beschreibung.
 * Computer können ausgeliehen werden, daher wird eine Liste der Ausleihen geführt.
 *
 * Laut Klassendiagramm (Material 1):
 * - nr: int
 * - bezeichnung: String
 * - leistungsindex: int (1-10, wichtig für Gebührenberechnung)
 * - cpu: String
 * - autowert: int = 100 (statischer Zähler)
 *
 * Hinweis aus Material 1:
 * toString() ergibt: "bezeichnung, cpu, leistungsindex"
 * z.B. "Dell M4500, 2,4 GHz, Li=7"
 */
public class Computer {

    // Statischer Zähler für automatische Nummernvergabe
    // Startwert ist 100 (laut Klassendiagramm)
    private static int autowert = 100;

    // Eindeutige Nummer des Computers (z.B. 203)
    private int nr;

    // Bezeichnung/Name des Computers (z.B. "Laptop Dell M4500")
    private String bezeichnung;

    // Leistungsindex von 1-10
    // Wichtig für Gebührenberechnung in Aufgabe 1.5:
    // Li 1-2: 0€, Li 3-6: 1€, Li 7-10: 2€
    private int leistungsindex;

    // CPU-Beschreibung (z.B. "2x 2.8 GHz")
    private String cpu;

    // Liste aller Ausleihen dieses Computers
    // Ein Computer kann mehrfach ausgeliehen werden (nacheinander)
    private List<Ausleihe> ausleihen;

    /**
     * Konstruktor - erstellt einen neuen Computer
     *
     * Die Nummer wird automatisch vergeben und hochgezählt.
     *
     * @param bezeichnung    Name/Typ des Computers
     * @param leistungsindex Leistungsindex (1-10)
     * @param cpu            CPU-Beschreibung
     */
    public Computer(String bezeichnung, int leistungsindex, String cpu) {
        // Automatische Nummernvergabe
        this.nr = autowert;
        autowert = autowert + 1;

        // Parameter in Attribute übernehmen
        this.bezeichnung = bezeichnung;
        this.leistungsindex = leistungsindex;
        this.cpu = cpu;

        // Leere Liste für Ausleihen erstellen
        this.ausleihen = new List<Ausleihe>();
    }

    /**
     * Alternativer Konstruktor mit expliziter Nummer
     *
     * Wird verwendet wenn die Nummer schon feststeht (z.B. aus Datenbank).
     *
     * @param nr             Die feste Nummer
     * @param bezeichnung    Name/Typ des Computers
     * @param leistungsindex Leistungsindex (1-10)
     * @param cpu            CPU-Beschreibung
     */
    public Computer(int nr, String bezeichnung, int leistungsindex, String cpu) {
        this.nr = nr;
        this.bezeichnung = bezeichnung;
        this.leistungsindex = leistungsindex;
        this.cpu = cpu;
        this.ausleihen = new List<Ausleihe>();
    }

    /**
     * Prüft ob der Computer momentan ausgeliehen ist
     *
     * Ein Computer ist ausgeliehen, wenn es eine aktive Ausleihe gibt,
     * bei der rueckgabe == false ist.
     *
     * @return true wenn ausgeliehen, sonst false
     */
    public boolean istAusgeliehen() {
        // Alle Ausleihen durchgehen
        for (int i = 0; i < ausleihen.size(); i++) {
            Ausleihe ausleihe = ausleihen.get(i);

            // Prüfung ob diese Ausleihe noch aktiv ist (nicht zurückgegeben)
            if (!ausleihe.isRueckgabe()) {
                // Aktive Ausleihe gefunden -> Computer ist ausgeliehen
                return true;
            }
        }

        // Keine aktive Ausleihe gefunden -> Computer ist frei
        return false;
    }

    /**
     * Gibt die aktuelle (aktive) Ausleihe zurück
     *
     * @return Die aktive Ausleihe oder null wenn nicht ausgeliehen
     */
    public Ausleihe getAktuelleAusleihe() {
        // Alle Ausleihen durchgehen
        for (int i = 0; i < ausleihen.size(); i++) {
            Ausleihe ausleihe = ausleihen.get(i);

            // Prüfung ob diese Ausleihe noch aktiv ist
            if (!ausleihe.isRueckgabe()) {
                return ausleihe;
            }
        }

        // Keine aktive Ausleihe
        return null;
    }

    /**
     * Fügt eine neue Ausleihe hinzu
     *
     * @param ausleihe Die neue Ausleihe
     */
    public void addAusleihe(Ausleihe ausleihe) {
        this.ausleihen.add(ausleihe);
    }

    /**
     * Gibt die Nummer des Computers zurück
     *
     * @return Die eindeutige Nummer
     */
    public int getNr() {
        return this.nr;
    }

    /**
     * Gibt die Bezeichnung zurück
     *
     * @return Der Name/Typ des Computers
     */
    public String getBezeichnung() {
        return this.bezeichnung;
    }

    /**
     * Gibt den Leistungsindex zurück
     *
     * @return Der Leistungsindex (1-10)
     */
    public int getLeistungsindex() {
        return this.leistungsindex;
    }

    /**
     * Gibt die CPU-Beschreibung zurück
     *
     * @return Die CPU-Info
     */
    public String getCpu() {
        return this.cpu;
    }

    /**
     * Gibt die Liste aller Ausleihen zurück
     *
     * @return Liste der Ausleihen
     */
    public List<Ausleihe> getAusleihen() {
        return this.ausleihen;
    }

    /**
     * Gibt den Computer als String zurück
     *
     * Laut Material 1: "bezeichnung, cpu, leistungsindex"
     * Beispiel aus Aufgabe 1.7.1: "102: Laptop Dell M4500, 2x 2.8 Ghz, Li=7"
     *
     * @return String-Darstellung des Computers
     */
    @Override
    public String toString() {
        // Format: "Nr: Bezeichnung, CPU, Li=Leistungsindex"
        return this.nr + ": " + this.bezeichnung + ", " + this.cpu + ", Li=" + this.leistungsindex;
    }
}
