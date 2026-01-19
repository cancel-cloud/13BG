package de.lukas.socialMediaGPT;

/**
 * Client class for connecting to the Social Media Platform server.
 * Provides methods for:
 * - Establishing a connection to the server
 * - Logging in
 * - Sending commands to the server
 * - Receiving responses from the server
 *
 * @author Social Media Platform Development Team
 * @version 1.0
 */
public class Client {
    private Socket clientSocket;

    /**
     * Creates a new Client instance.
     */
    public Client() {
        this.clientSocket = null;
    }

    /**
     * Establishes a connection to the specified server.
     *
     * @param server the server IP address or hostname
     * @param port the server port number
     * @return true if connection was successful, false otherwise
     */
    public boolean verbinden(String server, int port) {
        clientSocket = new Socket(server, port);
        return clientSocket.connect();
    }

    /**
     * Sends a login command to the server.
     * The user must be connected before calling this method.
     *
     * This method:
     * 1. Prompts for username and password (simulated here with parameters)
     * 2. Sends the login command to the server
     * 3. Waits for the server response
     * 4. Returns success or failure
     *
     * Note: In a real implementation, this would use a GUI or console input.
     * For now, parameters should be passed or hard-coded for testing.
     */
    public void anmelden() {
        if (clientSocket == null) {
            System.out.println("Nicht verbunden. Bitte zuerst verbinden()");
            return;
        }

        // In a real application, username and password would be obtained from user input
        // For testing purposes, this is a placeholder
        System.out.println("Bitte anmelden() mit Username und Passwort aufrufen");
    }

    /**
     * Sends a login command with username and password to the server.
     *
     * @param username the username
     * @param passwort the password
     * @return true if login was successful, false otherwise
     */
    public boolean anmelden(String username, String passwort) {
        if (clientSocket == null) {
            System.out.println("Nicht verbunden. Bitte zuerst verbinden()");
            return false;
        }

        // Send login command
        String kommando = "<anmelden;" + username + ";" + passwort + "\n";
        clientSocket.write(kommando);

        // Read response
        String antwort = clientSocket.readLine();

        if (antwort != null && antwort.contains("+OK")) {
            System.out.println("Anmeldung erfolgreich: " + antwort.substring(1));
            return true;
        } else {
            System.out.println("Anmeldung fehlgeschlagen: " + (antwort != null ? antwort : "Keine Antwort"));
            return false;
        }
    }

    /**
     * Sends a command to the server and returns the response.
     * The command format follows the protocol: <command;param1;param2;...>
     *
     * @param kommando the command string to send
     * @return the server's response string
     */
    public String verarbeiteKommando(String kommando) {
        if (clientSocket == null) {
            return "Error: Not connected";
        }

        // Ensure command starts with <
        if (!kommando.startsWith("<")) {
            kommando = "<" + kommando;
        }

        // Ensure command ends with newline
        if (!kommando.endsWith("\n")) {
            kommando = kommando + "\n";
        }

        // Send command
        clientSocket.write(kommando);

        // Read and return response
        String antwort = clientSocket.readLine();

        // Remove leading > if present
        if (antwort != null && antwort.startsWith(">")) {
            antwort = antwort.substring(1);
        }

        return antwort;
    }

    /**
     * Logs out from the server.
     */
    public void abmelden() {
        if (clientSocket != null) {
            clientSocket.write("<abmelden\n");
            String antwort = clientSocket.readLine();
            System.out.println(antwort);
        }
    }

    /**
     * Closes the connection to the server.
     */
    public void disconnect() {
        if (clientSocket != null) {
            clientSocket.close();
            clientSocket = null;
        }
    }

    /**
     * Gets the client socket.
     *
     * @return the Socket object, or null if not connected
     */
    public Socket getClientSocket() {
        return clientSocket;
    }
}
