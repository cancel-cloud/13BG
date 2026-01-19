package de.lukas.computerverleih;

/**
 * Klasse AusleihInfoServer - Server für Ausleihinformationen
 *
 * AUFGABE 1.7.1: Implementierung des Servers
 *
 * Dieser Server ermöglicht es Schülern, von jedem Computer-Arbeitsplatz
 * aus Informationen über die Ausleihe abzurufen.
 *
 * Protokoll (aus der Aufgabe):
 * - Server sendet Menü
 * - Client sendet Auswahl:
 *   1 - freie Geräte anzeigen
 *   2 - freie Geräte sortiert nach Leistungsindex
 *   3#Nr - Informationen zu Gerät mit Nummer Nr
 *   4 - Beenden
 *
 * Hinweise aus der Aufgabe:
 * - Port: 4712
 * - Menütext kann auf "Menü" verkürzt werden
 * - Erst wenn ein Client fertig ist, wird der nächste akzeptiert
 *
 * Laut Klassendiagramm (Material 1):
 * - port: int
 * - Verwaltung: Verwaltung
 */
public class AusleihInfoServer {

    // Der Port, auf dem der Server lauscht
    private int port;

    // Referenz auf die Verwaltung (für Computersuche etc.)
    private Verwaltung verwaltung;

    // Der ServerSocket für eingehende Verbindungen
    private ServerSocket serverSocket;

    /**
     * Konstruktor - erstellt einen neuen AusleihInfoServer
     *
     * @param verwaltung Referenz auf die Verwaltung
     */
    public AusleihInfoServer(Verwaltung verwaltung) {
        // Port ist laut Aufgabe 4712
        this.port = 4712;

        // Verwaltung speichern
        this.verwaltung = verwaltung;

        // ServerSocket wird erst beim Starten erstellt
        this.serverSocket = null;
    }

    /**
     * Startet den Server
     *
     * Diese Methode startet den Server und wartet auf Clients.
     * Für jeden Client wird eine Sitzung durchgeführt.
     * Der Server läuft in einer Endlosschleife.
     */
    public void starteServer() {
        // ServerSocket erstellen und an Port binden
        serverSocket = new ServerSocket(port);

        // Endlosschleife: Immer auf neue Clients warten
        // In echtem Code würde man hier eine Abbruchbedingung haben
        while (true) {
            // Auf Client-Verbindung warten (blockiert!)
            Socket clientSocket = serverSocket.accept();

            // Wenn Verbindung erfolgreich, Sitzung durchführen
            if (clientSocket != null) {
                // Sitzung mit diesem Client durchführen
                behandleClient(clientSocket);

                // Socket schließen nach der Sitzung
                clientSocket.close();
            }
        }
    }

    /**
     * Behandelt eine Client-Sitzung
     *
     * Diese Methode führt den Dialog mit einem Client durch.
     * Sie wird aufgerufen wenn sich ein Client verbunden hat.
     *
     * Der Ablauf ist:
     * 1. Menü senden
     * 2. Eingabe vom Client lesen
     * 3. Entsprechend reagieren
     * 4. Zurück zu 1 (oder Beenden bei "4")
     *
     * @param clientSocket Der Socket für die Kommunikation mit dem Client
     */
    private void behandleClient(Socket clientSocket) {
        // Flag ob die Sitzung weiterlaufen soll
        boolean sitzungLaeuft = true;

        // Sitzungsschleife
        while (sitzungLaeuft) {
            // Menü an den Client senden
            // Laut Aufgabe kann der Text auf "Menü" verkürzt werden
            clientSocket.write("Menü: 1-freie Geräte, 2-sortiert nach Li, 3#Nr-Info, 4-Beenden");

            // Eingabe vom Client lesen
            // readLine() blockiert bis eine Zeile empfangen wurde
            String eingabe = clientSocket.readLine();

            // Prüfung ob Eingabe erfolgreich war
            if (eingabe == null) {
                // Verbindung abgebrochen -> Sitzung beenden
                sitzungLaeuft = false;
                continue;
            }

            // Eingabe auswerten
            // Wir prüfen welche Option der Client gewählt hat
            if (eingabe.equals("1")) {
                // Option 1: Freie Geräte anzeigen
                String antwort = erzeugeListeFreieGeraete();
                clientSocket.write(antwort);

            } else if (eingabe.equals("2")) {
                // Option 2: Freie Geräte sortiert nach Leistungsindex
                String antwort = erzeugeListeFreieGeraeteSortiert();
                clientSocket.write(antwort);

            } else if (eingabe.startsWith("3#")) {
                // Option 3: Info zu einem bestimmten Gerät
                // Format: "3#Nr" wobei Nr die Gerätenummer ist
                String antwort = erzeugeGeraeteInfo(eingabe);
                clientSocket.write(antwort);

            } else if (eingabe.equals("4")) {
                // Option 4: Beenden
                clientSocket.write("Server sagt tschüss");
                sitzungLaeuft = false;

            } else {
                // Ungültige Eingabe
                clientSocket.write("Falsche Eingabe");
            }
        }
    }

    /**
     * Erzeugt eine Liste aller freien Geräte
     *
     * Format: "Nr: Bezeichnung, CPU, Li=Leistungsindex"
     * Mehrere Geräte werden durch Zeilenumbrüche getrennt.
     *
     * @return String mit der Liste der freien Geräte
     */
    private String erzeugeListeFreieGeraete() {
        // Freie Computer von der Verwaltung holen
        List<Computer> freieComputer = verwaltung.sucheFreieComputer();

        // Prüfung ob Computer vorhanden sind
        if (freieComputer.size() == 0) {
            return "Keine freien Geräte vorhanden";
        }

        // StringBuilder zum effizienten Zusammenbauen des Strings
        StringBuilder sb = new StringBuilder();

        // Alle freien Computer durchgehen
        for (int i = 0; i < freieComputer.size(); i++) {
            Computer comp = freieComputer.get(i);

            // Computer-Info hinzufügen (toString() liefert das richtige Format)
            sb.append(comp.toString());

            // Zeilenumbruch hinzufügen (außer beim letzten)
            if (i < freieComputer.size() - 1) {
                sb.append("\n");
            }
        }

        return sb.toString();
    }

    /**
     * Erzeugt eine Liste freier Geräte sortiert nach Leistungsindex
     *
     * Die Liste ist absteigend sortiert (höchster Li zuerst).
     *
     * @return String mit der sortierten Liste
     */
    private String erzeugeListeFreieGeraeteSortiert() {
        // Freie Computer holen
        List<Computer> freieComputer = verwaltung.sucheFreieComputer();

        // Prüfung
        if (freieComputer.size() == 0) {
            return "Keine freien Geräte vorhanden";
        }

        // Sortierung mit Bubble Sort (absteigend nach Leistungsindex)
        // Absteigend bedeutet: Höchster Leistungsindex zuerst
        for (int i = 0; i < freieComputer.size() - 1; i++) {
            for (int j = 0; j < freieComputer.size() - 1 - i; j++) {

                Computer comp1 = freieComputer.get(j);
                Computer comp2 = freieComputer.get(j + 1);

                // Vergleichen: Wenn Li von comp1 KLEINER als von comp2, tauschen
                // (weil wir absteigend sortieren wollen)
                if (comp1.getLeistungsindex() < comp2.getLeistungsindex()) {
                    // Tauschen
                    freieComputer.set(j, comp2);
                    freieComputer.set(j + 1, comp1);
                }
            }
        }

        // Liste in String umwandeln
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < freieComputer.size(); i++) {
            Computer comp = freieComputer.get(i);
            sb.append(comp.toString());

            if (i < freieComputer.size() - 1) {
                sb.append("\n");
            }
        }

        return sb.toString();
    }

    /**
     * Erzeugt Info zu einem bestimmten Gerät
     *
     * Die Eingabe hat das Format "3#Nr", wobei Nr die Gerätenummer ist.
     *
     * @param eingabe Die Client-Eingabe im Format "3#Nr"
     * @return String mit Geräteinfo oder Fehlermeldung
     */
    private String erzeugeGeraeteInfo(String eingabe) {
        // Die Nummer aus der Eingabe extrahieren
        // Format ist "3#Nr", wir müssen nach dem "#" aufteilen
        String[] teile = eingabe.split("#");

        // Prüfung ob das Format korrekt ist
        if (teile.length != 2) {
            return "Falsche Eingabe";
        }

        // Die Nummer ist der zweite Teil
        String nrString = teile[1];

        // String in Integer umwandeln
        // Integer.parseInt() kann eine NumberFormatException werfen
        int nr;
        try {
            nr = Integer.parseInt(nrString);
        } catch (NumberFormatException e) {
            // Keine gültige Zahl
            return "Falsche Eingabe";
        }

        // Computer mit dieser Nummer suchen
        Computer comp = verwaltung.sucheComputerNachNr(nr);

        // Prüfung ob gefunden
        if (comp == null) {
            return "Gerät Nr. " + nr + " nicht vorhanden";
        }

        // Geräteinfo zurückgeben
        return comp.toString();
    }

    /**
     * Gibt den Port zurück
     *
     * @return Der Port
     */
    public int getPort() {
        return this.port;
    }
}
