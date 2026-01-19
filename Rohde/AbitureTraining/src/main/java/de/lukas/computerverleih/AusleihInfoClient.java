package de.lukas.computerverleih;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.IOException;
import java.net.Socket;

/**
 * Klasse AusleihInfoClient - Client für den Ausleihinformationsserver
 *
 * Dieser Client verbindet sich mit dem AusleihInfoServer und ermöglicht
 * es dem Benutzer, Informationen über die Computer abzurufen.
 *
 * HINWEIS: Diese Klasse ist NICHT Teil der Aufgabe, sondern dient nur
 * zum Testen des Servers.
 */
public class AusleihInfoClient {

    // Der Hostname oder IP-Adresse des Servers
    private String host;

    // Der Port des Servers
    private int port;

    /**
     * Konstruktor - erstellt einen Client für die angegebene Adresse
     *
     * @param host Der Hostname (z.B. "localhost")
     * @param port Der Port (laut Aufgabe 4712)
     */
    public AusleihInfoClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    /**
     * Startet eine interaktive Sitzung mit dem Server
     *
     * Der Benutzer kann Befehle eingeben, die an den Server gesendet werden.
     */
    public void starteSitzung() {
        try {
            // Verbindung zum Server herstellen
            Socket socket = new Socket(host, port);
            System.out.println("Verbunden mit " + host + ":" + port);

            // Ein- und Ausgabe einrichten
            BufferedReader serverEingabe = new BufferedReader(
                new InputStreamReader(socket.getInputStream())
            );
            PrintWriter serverAusgabe = new PrintWriter(
                socket.getOutputStream(), true
            );

            // BufferedReader für Benutzereingabe (Tastatur)
            BufferedReader tastatur = new BufferedReader(
                new InputStreamReader(System.in)
            );

            // Sitzungsschleife
            boolean laeuft = true;
            while (laeuft) {
                // Nachricht vom Server lesen und anzeigen
                String serverNachricht = serverEingabe.readLine();
                if (serverNachricht == null) {
                    System.out.println("Verbindung getrennt");
                    break;
                }
                System.out.println("Server: " + serverNachricht);

                // Prüfung ob Sitzung beendet wurde
                if (serverNachricht.contains("tschüss")) {
                    laeuft = false;
                    continue;
                }

                // Benutzereingabe lesen
                System.out.print("Eingabe: ");
                String eingabe = tastatur.readLine();

                // Eingabe an Server senden
                serverAusgabe.println(eingabe);
            }

            // Verbindung schließen
            socket.close();
            System.out.println("Verbindung geschlossen");

        } catch (IOException e) {
            System.out.println("Fehler: " + e.getMessage());
        }
    }

    /**
     * Main-Methode zum Starten des Clients
     *
     * @param args Kommandozeilenargumente (werden ignoriert)
     */
    public static void main(String[] args) {
        // Client erstellen und starten
        AusleihInfoClient client = new AusleihInfoClient("localhost", 4712);
        client.starteSitzung();
    }
}
