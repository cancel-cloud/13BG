package de.lukas.computerverleih;

import java.io.IOException;

/**
 * Klasse ServerSocket - Server-seitiger Socket für eingehende Verbindungen
 *
 * Ein ServerSocket "lauscht" auf einem Port und wartet darauf, dass
 * Clients sich verbinden. Wenn ein Client sich verbindet, wird ein
 * normaler Socket für die Kommunikation erstellt.
 *
 * Beispiel: Ein Webserver lauscht auf Port 80 und wartet auf Browser.
 *
 * Basiert auf Material 2 der Abituraufgabe.
 */
public class ServerSocket {

    // Der Port auf dem der Server lauscht
    // Ein Port ist wie eine "Türnummer" - jeder Dienst hat seinen eigenen
    private int localPort;

    // Der eigentliche Java ServerSocket
    private java.net.ServerSocket serverSocket;

    /**
     * Konstruktor - erstellt einen ServerSocket auf einem bestimmten Port
     *
     * Der Server beginnt sofort auf dem Port zu lauschen.
     *
     * @param localPort Der Port (z.B. 4712 wie in der Aufgabe)
     */
    public ServerSocket(int localPort) {
        // Port speichern
        this.localPort = localPort;

        try {
            // Neuen Java ServerSocket erstellen und an den Port binden
            // Ab jetzt "lauscht" der Server auf diesem Port
            this.serverSocket = new java.net.ServerSocket(localPort);
            System.out.println("Server gestartet auf Port " + localPort);

        } catch (IOException e) {
            // IOException kann auftreten wenn:
            // - Der Port schon belegt ist
            // - Keine Berechtigung für den Port besteht
            System.out.println("Fehler beim Starten des Servers: " + e.getMessage());
            this.serverSocket = null;
        }
    }

    /**
     * Wartet auf eine eingehende Client-Verbindung
     *
     * Diese Methode BLOCKIERT - das heißt, sie wartet solange bis
     * ein Client sich verbindet. Das Programm macht in dieser Zeit
     * nichts anderes.
     *
     * Wenn ein Client sich verbindet, wird ein Socket-Objekt zurückgegeben,
     * über das man mit dem Client kommunizieren kann.
     *
     * @return Ein Socket-Objekt für die Kommunikation mit dem Client
     */
    public Socket accept() {
        // Prüfung ob Server erfolgreich gestartet wurde
        if (serverSocket == null) {
            return null;
        }

        try {
            // accept() wartet auf eine Verbindung und gibt einen Java-Socket zurück
            java.net.Socket clientSocket = serverSocket.accept();

            // Wir erstellen unseren eigenen Socket-Wrapper aus dem Java-Socket
            return new Socket(clientSocket);

        } catch (IOException e) {
            // Fehler bei der Verbindung
            System.out.println("Fehler bei accept(): " + e.getMessage());
            return null;
        }
    }

    /**
     * Schließt den ServerSocket
     *
     * Nach dem Schließen werden keine neuen Verbindungen mehr akzeptiert.
     */
    public void close() {
        try {
            if (serverSocket != null) {
                serverSocket.close();
                System.out.println("Server auf Port " + localPort + " geschlossen");
            }
        } catch (IOException e) {
            System.out.println("Fehler beim Schließen des Servers: " + e.getMessage());
        }
    }

    /**
     * Gibt den Port zurück auf dem der Server lauscht
     *
     * @return Der Port
     */
    public int getLocalPort() {
        return this.localPort;
    }
}
