package de.lukas.computerverleih;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.IOException;

/**
 * Klasse Socket - Wrapper für Netzwerkkommunikation
 *
 * Ein Socket ist wie ein "Kommunikationskanal" zwischen zwei Programmen.
 * Man kann Nachrichten senden (write) und empfangen (readLine).
 *
 * Diese Klasse vereinfacht die Nutzung der echten Java Socket-Klasse.
 * Basiert auf Material 2 der Abituraufgabe.
 */
public class Socket {

    // Der eigentliche Java-Socket für die Netzwerkverbindung
    private java.net.Socket socket;

    // BufferedReader zum Lesen von Daten (Eingabe vom Client/Server)
    private BufferedReader eingabe;

    // PrintWriter zum Schreiben von Daten (Ausgabe zum Client/Server)
    private PrintWriter ausgabe;

    // Flag ob der Socket geöffnet ist
    private boolean istOffen;

    /**
     * Standardkonstruktor - erstellt einen leeren Socket
     *
     * Dieser Konstruktor erstellt nur das Objekt, aber keine Verbindung.
     * Normalerweise wird der Socket vom ServerSocket erstellt.
     */
    public Socket() {
        this.socket = null;
        this.eingabe = null;
        this.ausgabe = null;
        this.istOffen = false;
    }

    /**
     * Interner Konstruktor - erstellt einen Socket aus einem Java-Socket
     *
     * Dieser Konstruktor wird vom ServerSocket verwendet, wenn ein
     * Client sich verbindet.
     *
     * @param javaSocket Der Java-Socket von der accept()-Methode
     */
    public Socket(java.net.Socket javaSocket) {
        this.socket = javaSocket;

        try {
            // InputStreamReader liest Bytes und wandelt sie in Zeichen um
            // BufferedReader puffert die Eingabe und erlaubt zeilenweises Lesen
            this.eingabe = new BufferedReader(
                new InputStreamReader(socket.getInputStream())
            );

            // PrintWriter schreibt Text zum Socket
            // Das 'true' am Ende aktiviert "auto-flush" (sofortiges Senden)
            this.ausgabe = new PrintWriter(socket.getOutputStream(), true);

            this.istOffen = true;

        } catch (IOException e) {
            // IOException tritt auf bei Netzwerkproblemen
            System.out.println("Fehler beim Erstellen des Sockets: " + e.getMessage());
            this.istOffen = false;
        }
    }

    /**
     * Liest eine Zeile vom Socket
     *
     * Diese Methode blockiert (wartet), bis eine komplette Zeile
     * empfangen wurde. Eine Zeile endet mit einem Zeilenumbruch.
     *
     * @return Die gelesene Zeile (ohne Zeilenumbruch), oder null bei Fehler
     */
    public String readLine() {
        // Prüfung ob Socket offen ist
        if (!istOffen || eingabe == null) {
            return null;
        }

        try {
            // readLine() liest bis zum Zeilenumbruch und wartet falls nötig
            return eingabe.readLine();

        } catch (IOException e) {
            // Bei Fehler null zurückgeben
            return null;
        }
    }

    /**
     * Schreibt einen String zum Socket
     *
     * Der String wird sofort gesendet (wegen auto-flush).
     * Ein Zeilenumbruch wird automatisch angehängt.
     *
     * @param s Der zu sendende String
     */
    public void write(String s) {
        // Prüfung ob Socket offen ist
        if (!istOffen || ausgabe == null) {
            // Nichts tun wenn nicht offen
            return;
        }

        // println sendet den String mit Zeilenumbruch
        ausgabe.println(s);
    }

    /**
     * Schließt den Socket und gibt Ressourcen frei
     *
     * Nach dem Schließen kann der Socket nicht mehr verwendet werden.
     */
    public void close() {
        try {
            // Alle Ressourcen schließen
            if (eingabe != null) {
                eingabe.close();
            }
            if (ausgabe != null) {
                ausgabe.close();
            }
            if (socket != null) {
                socket.close();
            }

            istOffen = false;

        } catch (IOException e) {
            // Fehler beim Schließen ignorieren
            System.out.println("Fehler beim Schließen: " + e.getMessage());
        }
    }

    /**
     * Prüft ob der Socket noch geöffnet ist
     *
     * @return true wenn offen, sonst false
     */
    public boolean isOpen() {
        return istOffen;
    }
}
