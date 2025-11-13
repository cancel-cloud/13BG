package de.lukas.socialMedia;

/**
 * ServerThread handles communication with a single client in its own thread.
 * Each connecting client gets its own ServerThread instance, allowing
 * the server to handle multiple clients simultaneously.
 *
 * The thread:
 * - Reads commands from the client
 * - Processes commands using the SocialMediaPlatform
 * - Sends responses back to the client
 * - Runs until the client disconnects
 *
 * @author Social Media Platform Development Team
 * @version 1.0
 */
public class ServerThread extends Thread {
    private Socket clientSocket;
    private SocialMediaPlatform smp;
    private Nutzer angemeldeterNutzer;

    /**
     * Creates a new ServerThread for handling a client connection.
     *
     * @param cs the Socket for communicating with the client
     * @param smp the SocialMediaPlatform for business logic access
     */
    public ServerThread(Socket cs, SocialMediaPlatform smp) {
        this.clientSocket = cs;
        this.smp = smp;
        this.angemeldeterNutzer = null;
    }

    /**
     * The main thread execution method.
     * Runs in a loop, reading commands from the client and processing them.
     * Continues until the client disconnects or sends a quit command.
     */
    @Override
    public void run() {
        System.out.println("ServerThread started for client: " + clientSocket.getRemoteHostIP());

        boolean verbindungAktiv = true;

        while (verbindungAktiv) {
            // Read command from client
            String kommando = clientSocket.readLine();

            if (kommando == null) {
                // Client disconnected
                verbindungAktiv = false;
                break;
            }

            System.out.println("Received: " + kommando);

            // Process command and get response
            String antwort = verarbeiteKommando(kommando);

            // Send response to client
            clientSocket.write(antwort + "\n");

            // Check if client wants to disconnect
            if (kommando.startsWith("quit") || kommando.startsWith("abmelden")) {
                verbindungAktiv = false;
            }
        }

        // Close connection
        clientSocket.close();
        System.out.println("ServerThread ended for client: " + clientSocket.getRemoteHostIP());
    }

    /**
     * Processes a command received from the client.
     * Parses the command string and calls appropriate methods on SocialMediaPlatform.
     *
     * Protocol format: <command;param1;param2;...>
     *
     * Supported commands:
     * - anmelden;username;password
     * - registrieren;username;password;email
     * - erstellen;titel;bildId;text (optional text)
     * - like;beitragIndex
     * - abonnieren;username
     *
     * @param kommando the command string from the client
     * @return the response string to send back to the client
     */
    private String verarbeiteKommando(String kommando) {
        // Remove leading < if present
        if (kommando.startsWith("<")) {
            kommando = kommando.substring(1);
        }

        // Split command by semicolon
        String[] teile = kommando.split(";");

        if (teile.length == 0) {
            return ">-ERR Invalid command";
        }

        String befehl = teile[0].toLowerCase();

        try {
            switch (befehl) {
                case "anmelden":
                    if (teile.length >= 3) {
                        String username = teile[1];
                        String passwort = teile[2];
                        angemeldeterNutzer = smp.anmelden(username, passwort);
                        if (angemeldeterNutzer != null) {
                            return ">+OK Willkommen";
                        } else {
                            return ">-ERR Anmeldung fehlgeschlagen";
                        }
                    }
                    return ">-ERR Invalid parameters";

                case "registrieren":
                    if (teile.length >= 4) {
                        String username = teile[1];
                        String passwort = teile[2];
                        String email = teile[3];
                        int ergebnis = smp.registrieren(username, passwort, email);
                        if (ergebnis == 0) {
                            return ">+OK Registrierung erfolgreich";
                        } else if (ergebnis == -1) {
                            return ">-ERR Benutzername bereits vergeben";
                        } else {
                            return ">-ERR E-Mail bereits registriert";
                        }
                    }
                    return ">-ERR Invalid parameters";

                case "abmelden":
                    angemeldeterNutzer = null;
                    return ">+OK Abgemeldet";

                case "quit":
                    return ">+OK Verbindung wird beendet";

                default:
                    return ">-ERR Unknown command: " + befehl;
            }
        } catch (Exception e) {
            return ">-ERR Error processing command: " + e.getMessage();
        }
    }

    /**
     * Gets the currently logged-in user.
     *
     * @return the logged-in Nutzer, or null if no user is logged in
     */
    public Nutzer getAngemeldeterNutzer() {
        return angemeldeterNutzer;
    }
}
