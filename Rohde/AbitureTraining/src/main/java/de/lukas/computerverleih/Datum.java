package de.lukas.computerverleih;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Klasse Datum - Hilfsklasse für Datumsoperationen
 *
 * Diese Klasse speichert ein Datum und bietet Methoden zur Berechnung
 * von Differenzen zwischen zwei Datumswerten.
 *
 * Basiert auf Material 2 der Abituraufgabe.
 */
public class Datum {

    // Das eigentliche Datum wird intern als LocalDate gespeichert
    // LocalDate ist eine Java-Klasse für Datumsangaben ohne Uhrzeit
    private LocalDate datum;

    /**
     * Standardkonstruktor - erzeugt ein Datum mit dem aktuellen Systemdatum
     *
     * Dieser Konstruktor wird aufgerufen, wenn man new Datum() schreibt.
     * Das Datum wird automatisch auf "heute" gesetzt.
     */
    public Datum() {
        // LocalDate.now() holt sich das aktuelle Datum vom System
        this.datum = LocalDate.now();
    }

    /**
     * Konstruktor mit Tag, Monat und Jahr
     *
     * Damit kann man ein bestimmtes Datum erstellen, z.B.:
     * new Datum(28, 9, 2024) für den 28. September 2024
     *
     * @param tag   Der Tag des Monats (1-31)
     * @param monat Der Monat (1-12)
     * @param jahr  Das Jahr (z.B. 2024)
     */
    public Datum(int tag, int monat, int jahr) {
        // LocalDate.of erstellt ein Datum aus Jahr, Monat und Tag
        // Achtung: Die Reihenfolge ist Jahr, Monat, Tag!
        this.datum = LocalDate.of(jahr, monat, tag);
    }

    /**
     * Berechnet die Differenz in Tagen zwischen zwei Datumswerten
     *
     * Diese Methode ist statisch, d.h. sie gehört zur Klasse und nicht
     * zu einem einzelnen Objekt. Man ruft sie auf mit:
     * Datum.differenzInTagen(datum1, datum2)
     *
     * @param d1 Das erste Datum
     * @param d2 Das zweite Datum
     * @return   Anzahl der Tage zwischen d1 und d2
     *           - Positiv, wenn d1 nach d2 liegt
     *           - Negativ, wenn d1 vor d2 liegt
     *           - 0, wenn beide gleich sind
     */
    public static int differenzInTagen(Datum d1, Datum d2) {
        // ChronoUnit.DAYS.between berechnet die Tage zwischen zwei Datumswerten
        // Wir müssen das Ergebnis umdrehen, weil between(d2, d1) = d1 - d2
        long tage = ChronoUnit.DAYS.between(d2.datum, d1.datum);

        // Wir casten von long auf int, weil die Methode int zurückgeben soll
        return (int) tage;
    }

    /**
     * Gibt das interne LocalDate-Objekt zurück
     *
     * Diese Methode ist nötig, um intern mit dem Datum arbeiten zu können.
     *
     * @return Das LocalDate-Objekt
     */
    public LocalDate getLocalDate() {
        return this.datum;
    }

    /**
     * Gibt das Datum als String zurück
     *
     * Format: TT.MM.JJJJ (z.B. "28.09.2024")
     *
     * @return Das Datum als formatierter String
     */
    @Override
    public String toString() {
        // String.format erstellt einen formatierten String
        // %02d bedeutet: 2 Stellen, mit führender 0 falls nötig
        return String.format("%02d.%02d.%04d",
            datum.getDayOfMonth(),  // Tag
            datum.getMonthValue(),  // Monat
            datum.getYear());       // Jahr
    }
}
