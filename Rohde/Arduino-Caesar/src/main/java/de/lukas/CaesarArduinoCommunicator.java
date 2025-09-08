package de.lukas;

import com.fazecast.jSerialComm.SerialPort;
import java.io.IOException;
import java.util.Scanner;

public class CaesarArduinoCommunicator {
    private SerialPort serialPort;
    private Scanner scanner;
    private final StringBuilder arduinoBuffer = new StringBuilder();

    public static void main(String[] args) {
        CaesarArduinoCommunicator communicator = new CaesarArduinoCommunicator();
        communicator.run();
    }

    public void run() {
        scanner = new Scanner(System.in);

        System.out.println("=== Java Caesar-Verschluesselung mit Arduino ===");

        // Arduino-Port finden und verbinden
        if (!connectToArduino()) {
            System.out.println("Fehler beim Verbinden mit Arduino!");
            return;
        }

        // Arduino bereinigen und initialisieren
        initializeArduino();

        // Hauptschleife
        mainLoop();

        // Aufraeumen
        cleanup();
    }

    private boolean connectToArduino() {
        // Alle verfuegbaren Ports anzeigen
        SerialPort[] ports = SerialPort.getCommPorts();

        if (ports.length == 0) {
            System.out.println("Keine seriellen Ports gefunden!");
            return false;
        }

        System.out.println("Verfuegbare serielle Ports:");
        for (int i = 0; i < ports.length; i++) {
            System.out.println((i + 1) + ": " + ports[i].getSystemPortName() +
                    " (" + ports[i].getDescriptivePortName() + ")");
        }

        // Port auswaehlen
        System.out.print("Waehle den Arduino-Port (1-" + ports.length + "): ");
        int choice;
        try {
            choice = Integer.parseInt(scanner.nextLine()) - 1;
            if (choice < 0 || choice >= ports.length) {
                System.out.println("Ungueltige Auswahl!");
                return false;
            }
        } catch (NumberFormatException e) {
            System.out.println("Ungueltige Eingabe!");
            return false;
        }

        // Port konfigurieren und öffnen
        serialPort = ports[choice];
        serialPort.setBaudRate(9600);
        serialPort.setNumDataBits(8);
        serialPort.setNumStopBits(1);
        serialPort.setParity(SerialPort.NO_PARITY);

        if (!serialPort.openPort()) {
            System.out.println("Fehler beim Öffnen des Ports!");
            return false;
        }

        System.out.println("Erfolgreich mit Arduino verbunden auf " + serialPort.getSystemPortName());

        return true;
    }

    private void initializeArduino() {
        System.out.print("Arduino wird initialisiert");

        // Arduino-Reader starten
        startArduinoReader();

        // Arduino reset - DTR Toggle
        serialPort.setDTR();
        try { Thread.sleep(100); } catch (InterruptedException e) {}
        serialPort.clearDTR();

        // Warten und alte Daten löschen
        for (int i = 0; i < 5; i++) {
            System.out.print(".");
            try { Thread.sleep(500); } catch (InterruptedException e) {}
        }

        // Buffer leeren
        byte[] dummyBuffer = new byte[1024];
        while (serialPort.bytesAvailable() > 0) {
            serialPort.readBytes(dummyBuffer, dummyBuffer.length);
            try { Thread.sleep(100); } catch (InterruptedException e) {}
        }

        arduinoBuffer.setLength(0);

        System.out.println(" Bereit!");
        System.out.println("Arduino befindet sich im Rainbow-Modus.\n");
    }

    private void startArduinoReader() {
        Thread readerThread = new Thread(() -> {
            byte[] buffer = new byte[1024];
            while (serialPort.isOpen()) {
                int bytesRead = serialPort.readBytes(buffer, buffer.length);
                if (bytesRead > 0) {
                    String data = new String(buffer, 0, bytesRead);

                    // Daten zum Buffer hinzufuegen
                    arduinoBuffer.append(data);

                    // Vollstaendige Zeilen verarbeiten
                    processArduinoBuffer();
                }
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
        readerThread.setDaemon(true);
        readerThread.start();
    }

    private void processArduinoBuffer() {
        String bufferContent = arduinoBuffer.toString();

        // Nach Zeilenendes suchen
        int newlineIndex;
        while ((newlineIndex = bufferContent.indexOf('\n')) != -1) {
            String line = bufferContent.substring(0, newlineIndex).trim();

            // Verarbeitete Zeile aus Buffer entfernen
            arduinoBuffer.delete(0, newlineIndex + 1);
            bufferContent = arduinoBuffer.toString();

            // Nur relevante Arduino-Nachrichten anzeigen
            if (!line.isEmpty() && isRelevantArduinoMessage(line)) {
                System.out.println("🤖 Arduino: " + line);
            }
        }
    }

    private boolean isRelevantArduinoMessage(String message) {
        // Unwichtige Systemnachrichten filtern
        String msg = message.toLowerCase();

        // Ignoriere diese Nachrichten
        if (msg.contains("rainbow") && msg.contains("modus") ||
                msg.contains("verarbeitung gestartet") ||
                msg.contains("zurueck zum")) {
            return false;
        }

        // Zeige nur wichtige Nachrichten
        return msg.contains("verschluesselt:") ||
                msg.contains("entschluesselt:") ||
                msg.contains("fehler:") ||
                msg.contains("===") ||
                msg.contains("format:") ||
                msg.contains("beispiel:");
    }

    private void mainLoop() {
        System.out.println("=== Neue Befehls-Syntax ===");
        System.out.println("E:<verschiebung> <text> - Verschluesseln (z.B. E:3 hallo welt)");
        System.out.println("D:<verschiebung> <text> - Entschluesseln (z.B. D:3 kdoor zhow)");
        System.out.println("send <command> - Direkten Arduino-Befehl");
        System.out.println("clear - Arduino-Output löschen");
        System.out.println("quit - Programm beenden");
        System.out.println("============================\n");

        while (true) {
            System.out.print("💻 Eingabe: ");
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("quit") || input.equalsIgnoreCase("q")) {
                break;
            }

            processCommand(input);
        }
    }

    private void processCommand(String input) {
        if (input.isEmpty()) {
            return;
        }

        if (input.equalsIgnoreCase("clear")) {
            clearScreen();
            return;
        }

        if (input.toLowerCase().startsWith("send ")) {
            sendToArduino(input.substring(5));
            return;
        }

        // Neue Syntax: E:4 text oder D:4 text
        if (input.matches("^[ED]:\\d+\\s+.+")) {
            handleNewCaesarCommand(input);
        } else {
            System.out.println("❌ Ungueltiges Format!");
            System.out.println("   Verwende: E:<shift> <text> oder D:<shift> <text>");
            System.out.println("   Beispiel: E:3 hallo welt");
        }
    }

    private void handleNewCaesarCommand(String input) {
        // Parse: "E:4 hello world" -> operation=E, shift=4, text="hello world"
        String[] parts = input.split("\\s+", 2);
        if (parts.length < 2) {
            System.out.println("❌ Text fehlt!");
            return;
        }

        String operationPart = parts[0]; // "E:4"
        String text = parts[1]; // "hello world"

        String[] opSplit = operationPart.split(":");
        String operation = opSplit[0]; // "E"
        int shift;

        try {
            shift = Integer.parseInt(opSplit[1]); // 4
        } catch (NumberFormatException e) {
            System.out.println("❌ Ungueltige Verschiebung: " + opSplit[1]);
            return;
        }

        // Text fuer Arduino vorbereiten (Leerzeichen entfernen)
        String cleanText = text.replaceAll("\\s", "");

        // Arduino-Format: "E:nachricht:shift" oder "D:nachricht:shift"
        String arduinoCommand = operation + ":" + cleanText + ":" + shift;

        System.out.println("📡 Sende an Arduino: " + arduinoCommand);
        sendToArduino(arduinoCommand);

        // Lokale Verarbeitung (mit Original-Text inklusive Leerzeichen)
        String result;
        if (operation.equalsIgnoreCase("E")) {
            result = caesarEncrypt(text, shift);
            System.out.println("✅ Java-Ergebnis (Verschluesselt): " + result);
        } else {
            result = caesarDecrypt(text, shift);
            System.out.println("✅ Java-Ergebnis (Entschluesselt): " + result);
        }

        System.out.println(); // Leerzeile fuer bessere uebersicht
    }

    private void sendToArduino(String message) {
        if (serialPort != null && serialPort.isOpen()) {
            String command = message + "\n";
            byte[] bytes = command.getBytes();
            int bytesWritten = serialPort.writeBytes(bytes, bytes.length);

            if (bytesWritten != bytes.length) {
                System.out.println("⚠️  Warnung: Nicht alle Bytes gesendet!");
            }
        } else {
            System.out.println("❌ Arduino nicht verbunden!");
        }
    }

    private void clearScreen() {
        // ANSI Escape-Codes fuer Terminal-Clearing
        System.out.print("\033[2J\033[H");
        System.out.flush();
        System.out.println("Arduino-Output gelöscht.\n");
    }

    // Caesar-Verschluesselung (identisch mit Arduino-Implementierung)
    private String caesarEncrypt(String text, int shift) {
        StringBuilder result = new StringBuilder();
        shift = shift % 26;

        for (char c : text.toCharArray()) {
            if (c >= 'a' && c <= 'z') {
                c = (char) (((c - 'a' + shift) % 26) + 'a');
            } else if (c >= 'A' && c <= 'Z') {
                c = (char) (((c - 'A' + shift) % 26) + 'A');
            }
            // Andere Zeichen (Leerzeichen, Zahlen, etc.) bleiben unveraendert
            result.append(c);
        }

        return result.toString();
    }

    private String caesarDecrypt(String text, int shift) {
        return caesarEncrypt(text, 26 - (shift % 26));
    }

    private void cleanup() {
        if (serialPort != null && serialPort.isOpen()) {
            serialPort.closePort();
            System.out.println("Arduino-Verbindung geschlossen.");
        }

        if (scanner != null) {
            scanner.close();
        }
    }
}