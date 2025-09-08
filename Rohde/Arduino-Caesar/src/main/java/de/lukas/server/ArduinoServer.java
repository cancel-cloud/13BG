package de.lukas.server;

import com.fazecast.jSerialComm.SerialPort;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class ArduinoServer {
    private ServerSocket serverSocket;
    private SerialPort serialPort;
    private final int SERVER_PORT = 8080;
    private final Set<ClientHandler> clients = ConcurrentHashMap.newKeySet();
    private final StringBuilder arduinoBuffer = new StringBuilder();
    private volatile boolean running = false;
    private ExecutorService clientExecutor;

    public static void main(String[] args) {
        ArduinoServer server = new ArduinoServer();
        server.start();
    }

    public void start() {
        System.out.println("=== Arduino Server gestartet ===");

        // Arduino verbinden
        if (!connectToArduino()) {
            System.out.println("Fehler beim Verbinden mit Arduino!");
            return;
        }

        // Arduino initialisieren
        initializeArduino();

        // Server starten
        startServer();

        // Cleanup bei Beendigung
        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));
    }

    private boolean connectToArduino() {
        Scanner scanner = new Scanner(System.in);
        SerialPort[] ports = SerialPort.getCommPorts();

        if (ports.length == 0) {
            System.out.println("Keine seriellen Ports gefunden!");
            return false;
        }

        System.out.println("Verfügbare serielle Ports:");
        for (int i = 0; i < ports.length; i++) {
            System.out.println((i + 1) + ": " + ports[i].getSystemPortName() +
                    " (" + ports[i].getDescriptivePortName() + ")");
        }

        System.out.print("Wähle den Arduino-Port (1-" + ports.length + "): ");
        int choice;
        try {
            choice = Integer.parseInt(scanner.nextLine()) - 1;
            if (choice < 0 || choice >= ports.length) {
                System.out.println("Ungültige Auswahl!");
                return false;
            }
        } catch (NumberFormatException e) {
            System.out.println("Ungültige Eingabe!");
            return false;
        }

        serialPort = ports[choice];
        serialPort.setBaudRate(9600);
        serialPort.setNumDataBits(8);
        serialPort.setNumStopBits(1);
        serialPort.setParity(SerialPort.NO_PARITY);

        if (!serialPort.openPort()) {
            System.out.println("Fehler beim Öffnen des Ports!");
            return false;
        }

        System.out.println("Arduino verbunden auf " + serialPort.getSystemPortName());
        return true;
    }

    private void initializeArduino() {
        System.out.print("Arduino wird initialisiert");

        startArduinoReader();

        // Arduino reset
        serialPort.setDTR();
        try { Thread.sleep(100); } catch (InterruptedException e) {}
        serialPort.clearDTR();

        // Warten
        for (int i = 0; i < 3; i++) {
            System.out.print(".");
            try { Thread.sleep(500); } catch (InterruptedException e) {}
        }

        // Buffer leeren
        byte[] dummyBuffer = new byte[1024];
        while (serialPort.bytesAvailable() > 0) {
            serialPort.readBytes(dummyBuffer, dummyBuffer.length);
            try { Thread.sleep(50); } catch (InterruptedException e) {}
        }

        arduinoBuffer.setLength(0);
        System.out.println(" Bereit!");
    }

    private void startArduinoReader() {
        Thread readerThread = new Thread(() -> {
            byte[] buffer = new byte[1024];
            while (running && serialPort.isOpen()) {
                int bytesRead = serialPort.readBytes(buffer, buffer.length);
                if (bytesRead > 0) {
                    String data = new String(buffer, 0, bytesRead);
                    synchronized (arduinoBuffer) {
                        arduinoBuffer.append(data);
                        processArduinoBuffer();
                    }
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

        int newlineIndex;
        while ((newlineIndex = bufferContent.indexOf('\n')) != -1) {
            String line = bufferContent.substring(0, newlineIndex).trim();
            arduinoBuffer.delete(0, newlineIndex + 1);
            bufferContent = arduinoBuffer.toString();

            if (!line.isEmpty()) {
                System.out.println("🤖 Arduino: " + line);
                // Broadcast an alle Clients
                broadcastToClients("ARDUINO_RESPONSE:" + line);
            }
        }
    }

    private void startServer() {
        try {
            serverSocket = new ServerSocket(SERVER_PORT);
            clientExecutor = Executors.newCachedThreadPool();
            running = true;

            System.out.println("🌐 Server läuft auf Port " + SERVER_PORT);
            System.out.println("Warte auf Client-Verbindungen...\n");

            while (running) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    ClientHandler clientHandler = new ClientHandler(clientSocket, this);
                    clients.add(clientHandler);
                    clientExecutor.submit(clientHandler);

                    System.out.println("✅ Neuer Client verbunden: " +
                            clientSocket.getRemoteSocketAddress());
                } catch (IOException e) {
                    if (running) {
                        System.out.println("Fehler beim Akzeptieren der Client-Verbindung: " + e.getMessage());
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Server-Fehler: " + e.getMessage());
        }
    }

    public String sendToArduino(String command) {
        if (serialPort != null && serialPort.isOpen()) {
            String message = command + "\n";
            byte[] bytes = message.getBytes();
            int bytesWritten = serialPort.writeBytes(bytes, bytes.length);

            if (bytesWritten == bytes.length) {
                System.out.println("📡 An Arduino gesendet: " + command);
                return "SENT";
            } else {
                return "ERROR: Nicht alle Bytes gesendet";
            }
        } else {
            return "ERROR: Arduino nicht verbunden";
        }
    }

    public void removeClient(ClientHandler client) {
        clients.remove(client);
        System.out.println("❌ Client getrennt. Aktive Clients: " + clients.size());
    }

    private void broadcastToClients(String message) {
        clients.removeIf(client -> !client.sendMessage(message));
    }

    public void shutdown() {
        System.out.println("\n🔴 Server wird heruntergefahren...");
        running = false;

        // Alle Clients schließen
        for (ClientHandler client : clients) {
            client.close();
        }
        clients.clear();

        // Server Socket schließen
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (IOException e) {
            System.out.println("Fehler beim Schließen des Server-Sockets: " + e.getMessage());
        }

        // Executor herunterfahren
        if (clientExecutor != null) {
            clientExecutor.shutdown();
            try {
                if (!clientExecutor.awaitTermination(5, TimeUnit.SECONDS)) {
                    clientExecutor.shutdownNow();
                }
            } catch (InterruptedException e) {
                clientExecutor.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }

        // Arduino-Verbindung schließen
        if (serialPort != null && serialPort.isOpen()) {
            serialPort.closePort();
            System.out.println("Arduino-Verbindung geschlossen.");
        }

        System.out.println("Server heruntergefahren.");
    }
}

class ClientHandler implements Runnable {
    private Socket clientSocket;
    private BufferedReader reader;
    private PrintWriter writer;
    private ArduinoServer server;
    private String clientId;

    public ClientHandler(Socket socket, ArduinoServer server) {
        this.clientSocket = socket;
        this.server = server;
        this.clientId = socket.getRemoteSocketAddress().toString();

        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            System.out.println("Fehler beim Initialisieren der Client-Streams: " + e.getMessage());
        }
    }

    @Override
    public void run() {
        try {
            // Willkommensnachricht
            sendMessage("CONNECTED:Willkommen! Du bist mit dem Arduino-Server verbunden.");
            sendMessage("HELP:Verfügbare Befehle: ENCRYPT:<shift>:<text>, DECRYPT:<shift>:<text>, PING, QUIT");

            String inputLine;
            while ((inputLine = reader.readLine()) != null) {
                processClientCommand(inputLine.trim());

                if (inputLine.trim().equalsIgnoreCase("QUIT")) {
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println("Client-Verbindungsfehler: " + e.getMessage());
        } finally {
            close();
        }
    }

    private void processClientCommand(String command) {
        System.out.println("👤 Client " + clientId + ": " + command);

        if (command.equalsIgnoreCase("PING")) {
            sendMessage("PONG:Server ist erreichbar");
        }
        else if (command.equalsIgnoreCase("STATUS")) {
            sendMessage("STATUS:Arduino-Server läuft");
        }
        else if (command.toUpperCase().startsWith("ENCRYPT:")) {
            handleCaesarCommand(command, "E");
        }
        else if (command.toUpperCase().startsWith("DECRYPT:")) {
            handleCaesarCommand(command, "D");
        }
        else if (command.toUpperCase().startsWith("SEND:")) {
            // Direkter Arduino-Befehl
            String arduinoCommand = command.substring(5);
            String result = server.sendToArduino(arduinoCommand);
            sendMessage("SEND_RESULT:" + result);
        }
        else if (command.equalsIgnoreCase("QUIT")) {
            sendMessage("BYE:Auf Wiedersehen!");
        }
        else {
            sendMessage("ERROR:Unbekannter Befehl. Verwende HELP für Hilfe.");
        }
    }

    private void handleCaesarCommand(String command, String operation) {
        // Format: ENCRYPT:3:hallo oder DECRYPT:3:kdoor
        String[] parts = command.split(":", 3);

        if (parts.length != 3) {
            sendMessage("ERROR:Falsches Format. Verwende " + operation.toUpperCase() + ":<shift>:<text>");
            return;
        }

        try {
            int shift = Integer.parseInt(parts[1]);
            String text = parts[2];

            // Text für Arduino vorbereiten (ohne Leerzeichen)
            String cleanText = text.replaceAll("\\s", "");

            // Arduino-Format: "E:nachricht:shift"
            String arduinoCommand = operation + ":" + cleanText + ":" + shift;

            // An Arduino senden
            String result = server.sendToArduino(arduinoCommand);
            sendMessage("COMMAND_SENT:" + result);

            // Lokale Verarbeitung für sofortige Antwort
            String localResult;
            if (operation.equals("E")) {
                localResult = caesarEncrypt(text, shift);
                sendMessage("LOCAL_ENCRYPT:" + localResult);
            } else {
                localResult = caesarDecrypt(text, shift);
                sendMessage("LOCAL_DECRYPT:" + localResult);
            }

        } catch (NumberFormatException e) {
            sendMessage("ERROR:Ungültige Verschiebung: " + parts[1]);
        }
    }

    private String caesarEncrypt(String text, int shift) {
        StringBuilder result = new StringBuilder();
        shift = shift % 26;

        for (char c : text.toCharArray()) {
            if (c >= 'a' && c <= 'z') {
                c = (char) (((c - 'a' + shift) % 26) + 'a');
            } else if (c >= 'A' && c <= 'Z') {
                c = (char) (((c - 'A' + shift) % 26) + 'A');
            }
            result.append(c);
        }

        return result.toString();
    }

    private String caesarDecrypt(String text, int shift) {
        return caesarEncrypt(text, 26 - (shift % 26));
    }

    public boolean sendMessage(String message) {
        if (writer != null && !clientSocket.isClosed()) {
            writer.println(message);
            return !writer.checkError();
        }
        return false;
    }

    public void close() {
        try {
            if (reader != null) reader.close();
            if (writer != null) writer.close();
            if (clientSocket != null && !clientSocket.isClosed()) {
                clientSocket.close();
            }
        } catch (IOException e) {
            System.out.println("Fehler beim Schließen der Client-Verbindung: " + e.getMessage());
        }

        server.removeClient(this);
    }
}