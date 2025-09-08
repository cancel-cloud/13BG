package de.lukas.client;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ArduinoClient {
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;
    private Scanner scanner;
    private final String SERVER_HOST = "localhost";
    private final int SERVER_PORT = 8080;
    private volatile boolean connected = false;

    public static void main(String[] args) {
        ArduinoClient client = new ArduinoClient();
        client.start();
    }

    public void start() {
        scanner = new Scanner(System.in);

        System.out.println("=== Arduino Client ===");

        // Mit Server verbinden
        if (!connectToServer()) {
            System.out.println("❌ Verbindung zum Server fehlgeschlagen!");
            return;
        }

        // Server-Listener starten
        startServerListener();

        // Hauptschleife für Benutzereingaben
        mainLoop();

        // Aufräumen
        cleanup();
    }

    private boolean connectToServer() {
        try {
            System.out.println("🔄 Verbinde mit Server " + SERVER_HOST + ":" + SERVER_PORT + "...");

            socket = new Socket(SERVER_HOST, SERVER_PORT);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);

            connected = true;
            System.out.println("✅ Erfolgreich mit Server verbunden!");

            return true;
        } catch (IOException e) {
            System.out.println("❌ Verbindungsfehler: " + e.getMessage());
            return false;
        }
    }

    private void startServerListener() {
        Thread listenerThread = new Thread(() -> {
            try {
                String response;
                while (connected && (response = reader.readLine()) != null) {
                    processServerResponse(response);
                }
            } catch (IOException e) {
                if (connected) {
                    System.out.println("\n❌ Verbindung zum Server verloren: " + e.getMessage());
                    connected = false;
                }
            }
        });

        listenerThread.setDaemon(true);
        listenerThread.start();
    }

    private void processServerResponse(String response) {
        if (response.startsWith("CONNECTED:")) {
            System.out.println("🟢 " + response.substring(10));
        }
        else if (response.startsWith("HELP:")) {
            System.out.println("📋 " + response.substring(5));
        }
        else if (response.startsWith("PONG:")) {
            System.out.println("🏓 " + response.substring(5));
        }
        else if (response.startsWith("STATUS:")) {
            System.out.println("📊 " + response.substring(7));
        }
        else if (response.startsWith("LOCAL_ENCRYPT:")) {
            System.out.println("🔒 Java-Verschlüsselung: " + response.substring(14));
        }
        else if (response.startsWith("LOCAL_DECRYPT:")) {
            System.out.println("🔓 Java-Entschlüsselung: " + response.substring(14));
        }
        else if (response.startsWith("ARDUINO_RESPONSE:")) {
            System.out.println("🤖 Arduino-Antwort: " + response.substring(17));
        }
        else if (response.startsWith("COMMAND_SENT:")) {
            System.out.println("📡 Befehl-Status: " + response.substring(13));
        }
        else if (response.startsWith("SEND_RESULT:")) {
            System.out.println("📤 Send-Ergebnis: " + response.substring(12));
        }
        else if (response.startsWith("ERROR:")) {
            System.out.println("❌ Fehler: " + response.substring(6));
        }
        else if (response.startsWith("BYE:")) {
            System.out.println("👋 " + response.substring(4));
            connected = false;
        }
        else {
            System.out.println("📨 Server: " + response);
        }
    }

    private void mainLoop() {
        System.out.println("\n=== Client-Befehle ===");
        System.out.println("encrypt <shift> <text>  - Text verschlüsseln (z.B. encrypt 3 hallo welt)");
        System.out.println("decrypt <shift> <text>  - Text entschlüsseln (z.B. decrypt 3 kdoor zhow)");
        System.out.println("send <arduino_command> - Direkten Arduino-Befehl senden");
        System.out.println("ping                   - Server-Ping");
        System.out.println("status                 - Server-Status");
        System.out.println("clear                  - Bildschirm löschen");
        System.out.println("quit                   - Verbindung beenden");
        System.out.println("======================\n");

        while (connected) {
            System.out.print("👤 Eingabe: ");
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                continue;
            }

            if (processLocalCommand(input)) {
                continue;
            }

            // Befehl an Server senden
            sendToServer(input);

            if (input.equalsIgnoreCase("quit")) {
                break;
            }
        }
    }

    private boolean processLocalCommand(String input) {
        if (input.equalsIgnoreCase("clear")) {
            clearScreen();
            return true;
        }

        if (input.equalsIgnoreCase("help")) {
            showHelp();
            return true;
        }

        return false;
    }

    private void sendToServer(String input) {
        if (!connected) {
            System.out.println("❌ Nicht mit Server verbunden!");
            return;
        }

        // Lokale Befehl-Transformation
        String serverCommand = transformCommand(input);

        if (writer != null) {
            writer.println(serverCommand);

            if (writer.checkError()) {
                System.out.println("❌ Fehler beim Senden des Befehls!");
                connected = false;
            }
        }
    }

    private String transformCommand(String input) {
        String[] parts = input.split("\\s+");

        if (parts.length >= 3 && parts[0].equalsIgnoreCase("encrypt")) {
            // "encrypt 3 hallo welt" -> "ENCRYPT:3:hallo welt"
            String shift = parts[1];
            String text = String.join(" ", java.util.Arrays.copyOfRange(parts, 2, parts.length));
            return "ENCRYPT:" + shift + ":" + text;
        }
        else if (parts.length >= 3 && parts[0].equalsIgnoreCase("decrypt")) {
            // "decrypt 3 kdoor zhow" -> "DECRYPT:3:kdoor zhow"
            String shift = parts[1];
            String text = String.join(" ", java.util.Arrays.copyOfRange(parts, 2, parts.length));
            return "DECRYPT:" + shift + ":" + text;
        }
        else if (parts.length >= 2 && parts[0].equalsIgnoreCase("send")) {
            // "send E:hallo:3" -> "SEND:E:hallo:3"
            String command = String.join(" ", java.util.Arrays.copyOfRange(parts, 1, parts.length));
            return "SEND:" + command;
        }
        else if (input.equalsIgnoreCase("ping")) {
            return "PING";
        }
        else if (input.equalsIgnoreCase("status")) {
            return "STATUS";
        }
        else if (input.equalsIgnoreCase("quit")) {
            return "QUIT";
        }
        else {
            // Unbekannter Befehl - direkt weiterleiten
            return input;
        }
    }

    private void clearScreen() {
        System.out.print("\033[2J\033[H");
        System.out.flush();
        System.out.println("Client-Bildschirm gelöscht.\n");
    }

    private void showHelp() {
        System.out.println("\n=== Verfügbare Befehle ===");
        System.out.println("encrypt <shift> <text>  - Text mit Caesar-Cipher verschlüsseln");
        System.out.println("                          Beispiel: encrypt 3 hallo welt");
        System.out.println("decrypt <shift> <text>  - Text mit Caesar-Cipher entschlüsseln");
        System.out.println("                          Beispiel: decrypt 3 kdoor zhow");
        System.out.println("send <command>          - Direkten Befehl an Arduino senden");
        System.out.println("                          Beispiel: send E:test:5");
        System.out.println("ping                    - Server-Erreichbarkeit prüfen");
        System.out.println("status                  - Server-Status abfragen");
        System.out.println("clear                   - Bildschirm löschen");
        System.out.println("help                    - Diese Hilfe anzeigen");
        System.out.println("quit                    - Verbindung beenden");
        System.out.println("===========================\n");
    }

    private void cleanup() {
        System.out.println("🔄 Client wird beendet...");

        connected = false;

        try {
            if (reader != null) reader.close();
            if (writer != null) writer.close();
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            System.out.println("Fehler beim Schließen der Verbindung: " + e.getMessage());
        }

        if (scanner != null) {
            scanner.close();
        }

        System.out.println("✅ Client beendet.");
    }
}