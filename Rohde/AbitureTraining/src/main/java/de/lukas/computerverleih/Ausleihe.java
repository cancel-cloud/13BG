package de.lukas.computerverleih;

/**
 * Klasse Ausleihe - Repräsentiert eine Ausleihe eines Computers
 *
 * AUFGABE 1.5: Implementierung der Methode berechneGebuehr()
 *
 * Eine Ausleihe verbindet einen Computer mit einem Leiher (Person).
 * Sie speichert das Ausleihdatum, voraussichtliches Ende und ob
 * der Computer zurückgegeben wurde.
 *
 * Laut Klassendiagramm (Material 1):
 * - ausleihdatum: Datum
 * - leihEnde: Datum (voraussichtliches Ende)
 * - rueckgabe: boolean (true wenn zurückgegeben)
 *
 * Gebührenregeln aus Aufgabe 1.5:
 * - Grundgebühr: 1-14 Tage = 1€, danach = 2€
 * - Leistungsindex: Li 1-2 = 0€, Li 3-6 = 1€, Li 7-10 = 2€
 * - Dauergebühr: 0,75€ pro angefangene 30 Tage (erste 30 Tage frei)
 * - BBE-Schüler zahlen nur Grundgebühr
 * - Lehrkräfte mit >20 Stunden zahlen 2€ extra
 */
public class Ausleihe {

    // Das Datum an dem der Computer ausgeliehen wurde
    private Datum ausleihdatum;

    // Das voraussichtliche Rückgabedatum
    private Datum leihEnde;

    // Flag ob der Computer zurückgegeben wurde
    // false = noch ausgeliehen, true = zurückgegeben
    private boolean rueckgabe;

    // Referenz auf den ausgeliehenen Computer
    private Computer computer;

    // Referenz auf den Leiher (Schüler oder Lehrkraft)
    private Person leiher;

    /**
     * Konstruktor - erstellt eine neue Ausleihe
     *
     * Bei einer neuen Ausleihe ist rueckgabe automatisch false.
     *
     * @param leiher Der Leiher (Schüler oder Lehrkraft)
     * @param cNr    Die Nummer des Computers (wird intern gesucht/gesetzt)
     */
    public Ausleihe(Person leiher, Computer computer) {
        // Attribute setzen
        this.leiher = leiher;
        this.computer = computer;

        // Aktuelles Datum als Ausleihdatum
        this.ausleihdatum = new Datum();

        // Voraussichtliches Ende wird später gesetzt
        this.leihEnde = null;

        // Noch nicht zurückgegeben
        this.rueckgabe = false;
    }

    /**
     * Alternativer Konstruktor mit allen Datumsangaben
     *
     * @param leiher       Der Leiher
     * @param computer     Der Computer
     * @param ausleihdatum Das Ausleihdatum
     * @param leihEnde     Das voraussichtliche Ende
     */
    public Ausleihe(Person leiher, Computer computer, Datum ausleihdatum, Datum leihEnde) {
        this.leiher = leiher;
        this.computer = computer;
        this.ausleihdatum = ausleihdatum;
        this.leihEnde = leihEnde;
        this.rueckgabe = false;
    }

    /**
     * Berechnet die Leihgebühr
     *
     * AUFGABE 1.5: Diese Methode wird bei der Rückgabe aufgerufen.
     *
     * Gebührenregeln:
     * 1. Grundgebühr: 1-14 Tage = 1€, ab 15 Tage = 2€
     * 2. Leistungsindex-Gebühr:
     *    - Li 1-2: 0€
     *    - Li 3-6: 1€
     *    - Li 7-10: 2€
     * 3. Dauer-Gebühr: 0,75€ pro angefangene 30 Tage
     *    - Die ersten 30 Tage sind kostenfrei
     * 4. BBE-Schüler zahlen NUR die Grundgebühr
     * 5. Lehrkräfte mit >20 Stunden zahlen 2€ extra
     *
     * @return Die berechnete Gebühr in Euro
     */
    public double berechneGebuehr() {
        // Zuerst die tatsächliche Ausleihdauer berechnen
        // Wir verwenden das aktuelle Datum als Rückgabedatum
        Datum heute = new Datum();

        // Differenz in Tagen berechnen (heute - ausleihdatum)
        int tage = Datum.differenzInTagen(heute, this.ausleihdatum);

        // Falls negativ (sollte nicht vorkommen), auf 0 setzen
        if (tage < 0) {
            tage = 0;
        }

        // ===== 1. GRUNDGEBÜHR =====
        // 1-14 Tage = 1€, ab 15 Tage = 2€
        double grundgebuehr;
        if (tage <= 14) {
            grundgebuehr = 1.0;
        } else {
            grundgebuehr = 2.0;
        }

        // ===== SONDERFALL: BBE-SCHÜLER =====
        // BBE-Schüler zahlen NUR die Grundgebühr
        // Wir müssen prüfen ob der Leiher ein Schüler der Schulform BBE ist
        if (leiher instanceof Schueler) {
            // Cast zu Schueler um auf die Methode zugreifen zu können
            Schueler schueler = (Schueler) leiher;

            // Prüfung ob BBE Schüler
            if (schueler.istBBESchueler()) {
                // BBE Schüler -> nur Grundgebühr zurückgeben
                return grundgebuehr;
            }
        }

        // ===== 2. LEISTUNGSINDEX-GEBÜHR =====
        // Li 1-2: 0€, Li 3-6: 1€, Li 7-10: 2€
        double liGebuehr;
        int leistungsindex = computer.getLeistungsindex();

        if (leistungsindex <= 2) {
            // Li 1 oder 2 -> kostenlos
            liGebuehr = 0.0;
        } else if (leistungsindex <= 6) {
            // Li 3, 4, 5 oder 6 -> 1€
            liGebuehr = 1.0;
        } else {
            // Li 7, 8, 9 oder 10 -> 2€
            liGebuehr = 2.0;
        }

        // ===== 3. DAUER-GEBÜHR =====
        // 0,75€ pro angefangene 30 Tage
        // Die ersten 30 Tage sind kostenfrei
        double dauerGebuehr = 0.0;

        if (tage > 30) {
            // Tage nach den ersten 30 berechnen
            int tageNach30 = tage - 30;

            // Anzahl der angefangenen 30-Tage-Perioden berechnen
            // Beispiel: 35 Tage -> 5 Tage nach 30 -> 1 Periode (angefangen)
            // Beispiel: 61 Tage -> 31 Tage nach 30 -> 2 Perioden (angefangen)
            int anzahlPerioden = (tageNach30 + 29) / 30;  // Aufrunden durch (n+29)/30

            // Alternativ mit Math.ceil:
            // int anzahlPerioden = (int) Math.ceil((double) tageNach30 / 30.0);

            dauerGebuehr = anzahlPerioden * 0.75;
        }

        // ===== 4. LEHRKRAFT-ZUSCHLAG =====
        // Lehrkräfte mit mehr als 20 Stunden zahlen 2€ mehr
        double lehrkraftZuschlag = 0.0;

        if (leiher instanceof Lehrkraft) {
            // Cast zu Lehrkraft um auf Sollstunden zugreifen zu können
            Lehrkraft lehrkraft = (Lehrkraft) leiher;

            // Prüfung ob mehr als 20 Stunden
            if (lehrkraft.getSollstunden() > 20) {
                lehrkraftZuschlag = 2.0;
            }
        }

        // ===== GESAMTGEBÜHR BERECHNEN =====
        double gesamtGebuehr = grundgebuehr + liGebuehr + dauerGebuehr + lehrkraftZuschlag;

        return gesamtGebuehr;
    }

    // ===== GETTER UND SETTER =====

    /**
     * Gibt das Ausleihdatum zurück
     */
    public Datum getAusleihdatum() {
        return this.ausleihdatum;
    }

    /**
     * Gibt das voraussichtliche Leihende zurück
     */
    public Datum getLeihEnde() {
        return this.leihEnde;
    }

    /**
     * Setzt das voraussichtliche Leihende
     */
    public void setLeihEnde(Datum leihEnde) {
        this.leihEnde = leihEnde;
    }

    /**
     * Prüft ob der Computer zurückgegeben wurde
     */
    public boolean isRueckgabe() {
        return this.rueckgabe;
    }

    /**
     * Setzt den Rückgabe-Status
     */
    public void setRueckgabe(boolean rueckgabe) {
        this.rueckgabe = rueckgabe;
    }

    /**
     * Gibt den Computer zurück
     */
    public Computer getComputer() {
        return this.computer;
    }

    /**
     * Gibt den Leiher zurück
     */
    public Person getLeiher() {
        return this.leiher;
    }

    /**
     * Gibt die Ausleihe als String zurück
     */
    @Override
    public String toString() {
        String status = rueckgabe ? "zurückgegeben" : "ausgeliehen";
        return "Ausleihe: " + computer.getBezeichnung() +
               " von " + leiher.getVorname() + " " + leiher.getNachname() +
               " (" + status + ")";
    }
}
