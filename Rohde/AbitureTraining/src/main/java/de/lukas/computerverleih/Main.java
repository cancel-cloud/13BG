package de.lukas.computerverleih;

/**
 * Klasse Main - Demonstrationsklasse für den Computerverleih
 *
 * Diese Klasse demonstriert die Funktionsweise aller implementierten
 * Klassen anhand eines Beispielszenarios aus der Aufgabe.
 *
 * Das Szenario basiert auf Aufgabe 1.3:
 * - Schülerin Kaja Njo
 * - PC Nr. 203 (Dell M4500, 2,4 GHz, Leistungsindex 7)
 * - Schuljahr 2024/25: Klasse 11BGP3 (Schulform BG)
 * - Schuljahr 2023/24: Klasse 12SFE1 (Schulform SFE)
 */
public class Main {

    /**
     * Hauptmethode - Einstiegspunkt des Programms
     *
     * Demonstriert alle Funktionen des Computerverleihs.
     */
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("    COMPUTERVERLEIH - DEMONSTRATION");
        System.out.println("    Konrad-Zuse-Schule Baku");
        System.out.println("========================================\n");

        // ===== VERWALTUNG ERSTELLEN =====
        System.out.println("1. Verwaltung erstellen...");
        Verwaltung verwaltung = new Verwaltung("Konrad-Zuse-Schule Baku");

        // ===== KLASSEN ERSTELLEN =====
        // Laut Aufgabe 1.3 gibt es diese Klassen:
        // - 11BGP3 (BG, 01.08.2024 - 31.07.2025)
        // - 12SFE1 (SFE, 01.08.2023 - 31.07.2024)
        // - 11BGP1 (BG, für Lehrkraft-Demo)
        // - 10BBE1 (BBE, für Gebührenberechnung-Demo)
        System.out.println("2. Klassen erstellen...");

        Klasse klasse11BGP3 = new Klasse("11BGP3", "BG",
            new Datum(1, 8, 2024), new Datum(31, 7, 2025));
        verwaltung.addKlasse(klasse11BGP3);

        Klasse klasse12SFE1 = new Klasse("12SFE1", "SFE",
            new Datum(1, 8, 2023), new Datum(31, 7, 2024));
        verwaltung.addKlasse(klasse12SFE1);

        Klasse klasse11BGP1 = new Klasse("11BGP1", "BG",
            new Datum(1, 8, 2024), new Datum(31, 7, 2025));
        verwaltung.addKlasse(klasse11BGP1);

        Klasse klasse10BBE1 = new Klasse("10BBE1", "BBE",
            new Datum(1, 8, 2024), new Datum(31, 7, 2025));
        verwaltung.addKlasse(klasse10BBE1);

        System.out.println("   - " + klasse11BGP3);
        System.out.println("   - " + klasse12SFE1);
        System.out.println("   - " + klasse11BGP1);
        System.out.println("   - " + klasse10BBE1);

        // ===== COMPUTER ERSTELLEN =====
        // Laut Aufgabe: PC Nr. 203, Dell M4500, 2,4 GHz, Li=7
        // Weitere Computer für Demo
        System.out.println("\n3. Computer erstellen...");

        Computer pc102 = new Computer(102, "Laptop Dell M4500", 7, "2x 2.8 GHz");
        verwaltung.addComputer(pc102);

        Computer pc107 = new Computer(107, "Laptop Acer 5630Z", 2, "2.2 GHz");
        verwaltung.addComputer(pc107);

        Computer pc108 = new Computer(108, "Laptop Lenovo X1", 7, "4x 1.9 GHz");
        verwaltung.addComputer(pc108);

        Computer pc203 = new Computer(203, "PC Dell M4500", 7, "2.4 GHz");
        verwaltung.addComputer(pc203);

        Computer pc204 = new Computer(204, "PC HP ProDesk", 5, "3.2 GHz");
        verwaltung.addComputer(pc204);

        System.out.println("   - " + pc102);
        System.out.println("   - " + pc107);
        System.out.println("   - " + pc108);
        System.out.println("   - " + pc203);
        System.out.println("   - " + pc204);

        // ===== PERSONEN ERSTELLEN =====
        System.out.println("\n4. Personen erstellen...");

        // Schülerin Kaja Njo (aus Aufgabe 1.3)
        Schueler kajaNjo = new Schueler("Njo", "Kaja", "11BGP3", verwaltung,
            new Datum(15, 5, 2006));
        verwaltung.addPerson(kajaNjo);
        // Frühere Klasse hinzufügen
        kajaNjo.hinzufuegenAktuelleKlasse("11BGP3"); // Aktuelle Klasse nochmal

        // Lehrkraft Bernd Beyer (aus Aufgabe 1.4)
        Lehrkraft berndBeyer = new Lehrkraft("Beyer", "Bernd", "11BGP1", verwaltung, 22.0);
        verwaltung.addPerson(berndBeyer);

        // BBE-Schüler für Gebühren-Demo
        Schueler maxMuster = new Schueler("Muster", "Max", "10BBE1", verwaltung,
            new Datum(10, 3, 2007));
        verwaltung.addPerson(maxMuster);

        System.out.println("   - " + kajaNjo);
        System.out.println("   - " + berndBeyer);
        System.out.println("   - " + maxMuster);

        // ===== DEMONSTRATION DER METHODEN =====
        System.out.println("\n========================================");
        System.out.println("         METHODEN-DEMONSTRATION");
        System.out.println("========================================\n");

        // ----- toString() der Lehrkraft (Aufgabe 1.4) -----
        System.out.println("5. toString() der Lehrkraft (Aufgabe 1.4):");
        System.out.println("   " + berndBeyer.toString());

        // ----- sucheFreieComputer() (Aufgabe 1.6) -----
        System.out.println("\n6. sucheFreieComputer() (Aufgabe 1.6):");
        List<Computer> freieComputer = verwaltung.sucheFreieComputer();
        System.out.println("   Freie Computer: " + freieComputer.size());
        for (int i = 0; i < freieComputer.size(); i++) {
            System.out.println("   - " + freieComputer.get(i));
        }

        // ----- Ausleihe erstellen -----
        System.out.println("\n7. Ausleihe erstellen...");

        // Kaja leiht PC 203 aus
        Ausleihe ausleihekaja = new Ausleihe(kajaNjo, pc203,
            new Datum(28, 9, 2024), new Datum(1, 7, 2025));
        verwaltung.addAusleihe(ausleihekaja);
        System.out.println("   " + ausleihekaja);

        // Max (BBE) leiht PC 102 aus
        Ausleihe ausleiheMax = new Ausleihe(maxMuster, pc102,
            new Datum(1, 10, 2024), new Datum(1, 12, 2024));
        verwaltung.addAusleihe(ausleiheMax);
        System.out.println("   " + ausleiheMax);

        // Bernd Beyer leiht PC 108 aus
        Ausleihe ausleiheBernd = new Ausleihe(berndBeyer, pc108,
            new Datum(15, 9, 2024), new Datum(15, 11, 2024));
        verwaltung.addAusleihe(ausleiheBernd);
        System.out.println("   " + ausleiheBernd);

        // ----- Erneut freie Computer suchen -----
        System.out.println("\n8. Freie Computer nach Ausleihen:");
        freieComputer = verwaltung.sucheFreieComputer();
        System.out.println("   Freie Computer: " + freieComputer.size());
        for (int i = 0; i < freieComputer.size(); i++) {
            System.out.println("   - " + freieComputer.get(i));
        }

        // ----- berechneGebuehr() (Aufgabe 1.5) -----
        System.out.println("\n9. berechneGebuehr() Demonstration (Aufgabe 1.5):");

        // Normale Schülerin (Kaja, BG)
        double gebuehrKaja = ausleihekaja.berechneGebuehr();
        System.out.println("   Gebühr für Kaja (BG-Schülerin, Li=7): " + gebuehrKaja + " EUR");

        // BBE-Schüler (nur Grundgebühr!)
        double gebuehrMax = ausleiheMax.berechneGebuehr();
        System.out.println("   Gebühr für Max (BBE-Schüler): " + gebuehrMax + " EUR (nur Grundgebühr!)");

        // Lehrkraft mit >20 Stunden
        double gebuehrBernd = ausleiheBernd.berechneGebuehr();
        System.out.println("   Gebühr für Bernd (Lehrkraft, >20 Std, Li=7): " + gebuehrBernd + " EUR");

        // ----- rueckgabeComputer() (Aufgabe 1.6) -----
        System.out.println("\n10. rueckgabeComputer() (Aufgabe 1.6):");
        double rueckgabeGebuehr = verwaltung.rueckgabeComputer(102);
        System.out.println("    Rückgabe PC 102: Gebühr = " + rueckgabeGebuehr + " EUR");
        System.out.println("    Ausleihe Status: " + ausleiheMax);

        // Computer der nie ausgeliehen war
        double rueckgabeGebuehr2 = verwaltung.rueckgabeComputer(204);
        System.out.println("    Rückgabe PC 204 (nie ausgeliehen): " + rueckgabeGebuehr2 + " (erwartet: -1)");

        // ----- berechneAusleihtage() (Aufgabe 1.8) -----
        System.out.println("\n11. berechneAusleihtage() (Aufgabe 1.8):");
        int ausleihtage102 = verwaltung.berechneAusleihtage(102);
        System.out.println("    Ausleihtage PC 102: " + ausleihtage102 + " Tage");

        // ----- sucheAusComSorNacEnd() (Aufgabe 1.7.2) -----
        System.out.println("\n12. sucheAusComSorNacEnd() (Aufgabe 1.7.2):");
        List<Computer> ausgeliehene = verwaltung.sucheAusComSorNacEnd();
        System.out.println("    Ausgeliehene Computer (sortiert nach Leihende):");
        for (int i = 0; i < ausgeliehene.size(); i++) {
            Computer comp = ausgeliehene.get(i);
            Datum ende = comp.getAktuelleAusleihe().getLeihEnde();
            System.out.println("    - " + comp + " (Ende: " + ende + ")");
        }

        // ===== ABSCHLUSS =====
        System.out.println("\n========================================");
        System.out.println("        DEMONSTRATION ABGESCHLOSSEN");
        System.out.println("========================================\n");

        System.out.println("HINWEIS: Um den Server zu starten, verwenden Sie:");
        System.out.println("  AusleihInfoServer server = new AusleihInfoServer(verwaltung);");
        System.out.println("  server.starteServer();");
        System.out.println("\nUnd in einem separaten Terminal den Client:");
        System.out.println("  java de.lukas.computerverleih.AusleihInfoClient");
    }
}
