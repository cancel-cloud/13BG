package de.lukas.computerverleih;

import java.util.ArrayList;

/**
 * Klasse List - Eine generische Liste basierend auf Material 2
 *
 * Diese Klasse ist ein Wrapper (Hülle) um ArrayList, der die
 * Methoden bereitstellt, die in der Abituraufgabe verwendet werden.
 *
 * Generisch bedeutet: Man kann den Typ der Elemente frei wählen.
 * z.B. List<Computer> für eine Liste von Computern
 *      List<String> für eine Liste von Strings
 *
 * @param <T> Der Typ der Elemente in der Liste
 */
public class List<T> {

    // Die eigentliche Liste wird intern als ArrayList gespeichert
    // ArrayList ist eine eingebaute Java-Klasse für dynamische Arrays
    private ArrayList<T> liste;

    /**
     * Konstruktor - erzeugt eine leere Liste
     *
     * Beispiel: List<Computer> meineComputer = new List<Computer>();
     */
    public List() {
        // Wir erstellen eine neue leere ArrayList
        this.liste = new ArrayList<T>();
    }

    /**
     * Fügt ein Element am Ende der Liste hinzu
     *
     * @param obj Das hinzuzufügende Objekt
     */
    public void add(T obj) {
        // add() von ArrayList fügt am Ende hinzu
        this.liste.add(obj);
    }

    /**
     * Fügt ein Element an einer bestimmten Position ein
     *
     * Alle Elemente ab dieser Position werden nach hinten verschoben.
     *
     * @param index Die Position (0-basiert)
     * @param obj   Das einzufügende Objekt
     */
    public void add(int index, T obj) {
        // add(index, obj) fügt an der Position ein
        this.liste.add(index, obj);
    }

    /**
     * Gibt das Element an einer bestimmten Position zurück
     *
     * @param index Die Position (0-basiert)
     * @return Das Element an der Position, oder null wenn ungültig
     */
    public T get(int index) {
        // Prüfung: Ist der Index gültig?
        // Er muss >= 0 sein und kleiner als die Anzahl der Elemente
        if (index < 0 || index >= this.liste.size()) {
            // Ungültiger Index -> null zurückgeben
            return null;
        }

        // Gültiger Index -> Element zurückgeben
        return this.liste.get(index);
    }

    /**
     * Ersetzt das Element an einer bestimmten Position
     *
     * @param index Die Position (0-basiert)
     * @param obj   Das neue Objekt
     */
    public void set(int index, T obj) {
        // Prüfung ob Index gültig ist
        if (index >= 0 && index < this.liste.size()) {
            // Element ersetzen
            this.liste.set(index, obj);
        }
        // Bei ungültigem Index passiert nichts
    }

    /**
     * Gibt die Anzahl der Elemente in der Liste zurück
     *
     * @return Anzahl der Elemente
     */
    public int size() {
        return this.liste.size();
    }

    /**
     * Entfernt das Element an einer bestimmten Position
     *
     * @param index Die Position (0-basiert)
     * @return Das entfernte Element, oder null wenn ungültig
     */
    public T remove(int index) {
        // Prüfung ob Index gültig ist
        if (index < 0 || index >= this.liste.size()) {
            return null;
        }

        // Element entfernen und zurückgeben
        return this.liste.remove(index);
    }

    /**
     * Prüft ob ein bestimmtes Element in der Liste enthalten ist
     *
     * @param obj Das zu suchende Objekt
     * @return true wenn gefunden, sonst false
     */
    public boolean contains(T obj) {
        return this.liste.contains(obj);
    }

    /**
     * Gibt die Liste als String zurück (für Debugging)
     *
     * @return String-Darstellung der Liste
     */
    @Override
    public String toString() {
        return this.liste.toString();
    }
}
