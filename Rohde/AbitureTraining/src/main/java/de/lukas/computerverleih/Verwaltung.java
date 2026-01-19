package de.lukas.computerverleih;

/**
 * Klasse Verwaltung - Zentrale Verwaltungsklasse für den Computerverleih
 *
 * AUFGABE 1.6: Implementierung von sucheFreieComputer() und rueckgabeComputer()
 *
 * Diese Klasse ist das "Herzstück" des Systems. Sie verwaltet:
 * - Alle Computer
 * - Alle Personen (Schüler und Lehrkräfte)
 * - Alle Klassen
 * - Alle Ausleihen (indirekt über die Computer)
 *
 * Laut Klassendiagramm (Material 1):
 * - bezeichnung: String (Name der Verwaltung/Schule)
 * - computer: List<Computer>
 * - klassen: List<Klasse>
 * - personen: List<Person>
 */
public class Verwaltung {

    // Name der Verwaltung (z.B. "Konrad-Zuse-Schule Baku")
    private String bezeichnung;

    // Liste aller Computer, die verliehen werden können
    private List<Computer> computer;

    // Liste aller Schulklassen
    private List<Klasse> klassen;

    // Liste aller Personen (Schüler und Lehrkräfte)
    private List<Person> personen;

    // Liste aller Ausleihen (für einfacheren Zugriff)
    private List<Ausleihe> ausleihen;

    /**
     * Konstruktor - erstellt eine neue Verwaltung
     *
     * Alle Listen werden leer initialisiert.
     */
    public Verwaltung() {
        // Leere Listen erstellen
        this.computer = new List<Computer>();
        this.klassen = new List<Klasse>();
        this.personen = new List<Person>();
        this.ausleihen = new List<Ausleihe>();
        this.bezeichnung = "Verwaltung";
    }

    /**
     * Konstruktor mit Bezeichnung
     *
     * @param bezeichnung Name der Verwaltung/Schule
     */
    public Verwaltung(String bezeichnung) {
        this();  // Ruft den anderen Konstruktor auf
        this.bezeichnung = bezeichnung;
    }

    /**
     * Sucht alle freien (ausleihbaren) Computer
     *
     * AUFGABE 1.6: Diese Methode gibt eine Liste aller Computer zurück,
     * die momentan nicht ausgeliehen sind.
     *
     * Ein Computer ist frei, wenn er keine aktive Ausleihe hat
     * (d.h. alle seine Ausleihen haben rueckgabe == true).
     *
     * @return Liste aller freien Computer
     */
    public List<Computer> sucheFreieComputer() {
        // Neue Liste für die freien Computer erstellen
        List<Computer> freieComputer = new List<Computer>();

        // Alle Computer durchgehen
        for (int i = 0; i < computer.size(); i++) {
            // Aktuellen Computer holen
            Computer comp = computer.get(i);

            // Prüfung ob der Computer NICHT ausgeliehen ist
            // istAusgeliehen() gibt true zurück wenn ausgeliehen
            if (!comp.istAusgeliehen()) {
                // Computer ist frei -> zur Liste hinzufügen
                freieComputer.add(comp);
            }
        }

        // Liste der freien Computer zurückgeben
        return freieComputer;
    }

    /**
     * Verarbeitet die Rückgabe eines Computers
     *
     * AUFGABE 1.6: Diese Methode aktualisiert die Ausleihe und gibt
     * die entsprechende Leihgebühr zurück.
     *
     * Ablauf:
     * 1. Computer nach Nummer suchen
     * 2. Aktive Ausleihe des Computers finden
     * 3. Gebühr berechnen
     * 4. Ausleihe als zurückgegeben markieren
     * 5. Datum aktualisieren
     *
     * @param comNr Die Nummer des zurückgegebenen Computers
     * @return Die Leihgebühr, oder -1 wenn kein Computer ausgeliehen war
     */
    public double rueckgabeComputer(int comNr) {
        // Computer nach Nummer suchen
        Computer comp = sucheComputerNachNr(comNr);

        // Prüfung ob Computer gefunden wurde
        if (comp == null) {
            // Computer existiert nicht -> -1 zurückgeben
            return -1;
        }

        // Aktive Ausleihe des Computers suchen
        Ausleihe aktiveAusleihe = comp.getAktuelleAusleihe();

        // Prüfung ob Computer überhaupt ausgeliehen war
        if (aktiveAusleihe == null) {
            // Computer war nicht ausgeliehen -> -1 zurückgeben
            return -1;
        }

        // Gebühr berechnen (VOR dem Markieren als zurückgegeben!)
        // berechneGebuehr() verwendet das aktuelle Datum
        double gebuehr = aktiveAusleihe.berechneGebuehr();

        // Ausleihe als zurückgegeben markieren
        aktiveAusleihe.setRueckgabe(true);

        // Leihende auf aktuelles Datum setzen (das tatsächliche Rückgabedatum)
        Datum heute = new Datum();
        aktiveAusleihe.setLeihEnde(heute);

        // Gebühr zurückgeben
        return gebuehr;
    }

    /**
     * Sucht einen Computer nach seiner Nummer
     *
     * @param nr Die Nummer des Computers
     * @return Der Computer oder null wenn nicht gefunden
     */
    public Computer sucheComputerNachNr(int nr) {
        // Alle Computer durchgehen
        for (int i = 0; i < computer.size(); i++) {
            Computer comp = computer.get(i);

            // Prüfung ob die Nummer übereinstimmt
            if (comp.getNr() == nr) {
                // Gefunden!
                return comp;
            }
        }

        // Nicht gefunden
        return null;
    }

    /**
     * Gibt eine Liste der ausgeliehenen Computer zurück, sortiert nach Leihende
     *
     * AUFGABE 1.7.2: Diese Methode gibt die momentan ausgeliehenen Computer
     * zurück, aufsteigend sortiert nach dem voraussichtlichen Leihende.
     *
     * Der Computer, dessen Leihende zuerst kommt, steht am Anfang.
     *
     * Algorithmus (Bubble Sort - einfach zu verstehen):
     * 1. Alle ausgeliehenen Computer in eine neue Liste kopieren
     * 2. Liste sortieren durch paarweises Vertauschen
     *
     * @return Sortierte Liste der ausgeliehenen Computer
     */
    public List<Computer> sucheAusComSorNacEnd() {
        // Neue Liste für ausgeliehene Computer
        List<Computer> ausgelieheneComputer = new List<Computer>();

        // Alle Computer durchgehen und ausgeliehene sammeln
        for (int i = 0; i < computer.size(); i++) {
            Computer comp = computer.get(i);

            // Nur ausgeliehene Computer hinzufügen
            if (comp.istAusgeliehen()) {
                ausgelieheneComputer.add(comp);
            }
        }

        // Sortierung mit Bubble Sort (aufsteigend nach Leihende)
        // Bubble Sort vergleicht immer zwei benachbarte Elemente
        // und tauscht sie wenn sie in falscher Reihenfolge sind
        for (int i = 0; i < ausgelieheneComputer.size() - 1; i++) {
            for (int j = 0; j < ausgelieheneComputer.size() - 1 - i; j++) {

                // Die zwei zu vergleichenden Computer holen
                Computer comp1 = ausgelieheneComputer.get(j);
                Computer comp2 = ausgelieheneComputer.get(j + 1);

                // Die Leihenddaten holen
                Datum ende1 = comp1.getAktuelleAusleihe().getLeihEnde();
                Datum ende2 = comp2.getAktuelleAusleihe().getLeihEnde();

                // Vergleichen: Wenn ende1 NACH ende2 liegt, tauschen
                // differenzInTagen(ende1, ende2) > 0 bedeutet ende1 ist später
                if (ende1 != null && ende2 != null) {
                    if (Datum.differenzInTagen(ende1, ende2) > 0) {
                        // Tauschen: comp1 und comp2 vertauschen
                        ausgelieheneComputer.set(j, comp2);
                        ausgelieheneComputer.set(j + 1, comp1);
                    }
                }
            }
        }

        return ausgelieheneComputer;
    }

    /**
     * Berechnet die Gesamtzahl der Ausleihtage eines Computers
     *
     * AUFGABE 1.8: Diese Methode summiert alle Tage, die ein Computer
     * insgesamt ausgeliehen wurde.
     *
     * @param comNr Die Nummer des Computers
     * @return Die Summe der Ausleihtage, oder 0 wenn nicht gefunden
     */
    public int berechneAusleihtage(int comNr) {
        // Computer suchen
        Computer comp = sucheComputerNachNr(comNr);

        // Prüfung ob gefunden
        if (comp == null) {
            return 0;
        }

        // Summe der Tage
        int gesamtTage = 0;

        // Alle Ausleihen des Computers durchgehen
        List<Ausleihe> compAusleihen = comp.getAusleihen();

        for (int i = 0; i < compAusleihen.size(); i++) {
            Ausleihe ausleihe = compAusleihen.get(i);

            // Start- und Enddatum holen
            Datum start = ausleihe.getAusleihdatum();
            Datum ende;

            // Bei zurückgegebenen Ausleihen das Leihende verwenden
            // Bei aktiven Ausleihen das aktuelle Datum
            if (ausleihe.isRueckgabe()) {
                ende = ausleihe.getLeihEnde();
            } else {
                ende = new Datum();  // Aktuelles Datum
            }

            // Differenz berechnen und zur Summe addieren
            if (start != null && ende != null) {
                int tage = Datum.differenzInTagen(ende, start);
                if (tage > 0) {
                    gesamtTage = gesamtTage + tage;
                }
            }
        }

        return gesamtTage;
    }

    /**
     * Sucht eine Klasse nach ihrer Bezeichnung
     *
     * @param bez Die Klassenbezeichnung (z.B. "11BGP3")
     * @return Die Klasse oder null wenn nicht gefunden
     */
    public Klasse getKlasse(String bez) {
        // Alle Klassen durchgehen
        for (int i = 0; i < klassen.size(); i++) {
            Klasse kl = klassen.get(i);

            // Bezeichnung vergleichen (mit equals für Strings!)
            if (kl.getBezeichnung().equals(bez)) {
                return kl;
            }
        }

        // Nicht gefunden
        return null;
    }

    // ===== METHODEN ZUM HINZUFÜGEN =====

    /**
     * Fügt einen Computer hinzu
     */
    public void addComputer(Computer comp) {
        this.computer.add(comp);
    }

    /**
     * Fügt eine Klasse hinzu
     */
    public void addKlasse(Klasse kl) {
        this.klassen.add(kl);
    }

    /**
     * Fügt eine Person hinzu
     */
    public void addPerson(Person p) {
        this.personen.add(p);
    }

    /**
     * Fügt eine Ausleihe hinzu
     *
     * Registriert die Ausleihe sowohl in der Verwaltung als auch beim Computer.
     */
    public void addAusleihe(Ausleihe ausleihe) {
        this.ausleihen.add(ausleihe);
        // Auch beim Computer registrieren
        ausleihe.getComputer().addAusleihe(ausleihe);
    }

    // ===== GETTER =====

    public String getBezeichnung() {
        return this.bezeichnung;
    }

    public List<Computer> getComputer() {
        return this.computer;
    }

    public List<Klasse> getKlassen() {
        return this.klassen;
    }

    public List<Person> getPersonen() {
        return this.personen;
    }

    public List<Ausleihe> getAusleihen() {
        return this.ausleihen;
    }
}
