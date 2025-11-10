package de.lukas.schuelerGeraetVerwaltung.client;

import de.lukas.schuelerGeraetVerwaltung.net.Socket;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class SLZClient {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    private Socket socket;

    public SLZClient() {
    }

    public boolean verbinden(String ip, int port) {
        Objects.requireNonNull(ip, "ip");
        socket = new Socket(ip, port);
        return socket.connect();
    }

    public boolean authentisieren(int ausweisNr) {
        ensureConnected();
        try {
            socket.write("authentisieren;" + ausweisNr + "\n");
            String antwort = socket.readLine();
            return antwort != null && antwort.startsWith("+OK");
        } catch (IOException e) {
            return false;
        }
    }

    public String ausleihen(String signatur) {
        return sendeBefehl("ausleihen;" + signatur);
    }

    public String zurueckgeben(String signatur) {
        return sendeBefehl("zurueckgeben;" + signatur);
    }

    public String reservieren(int typNr, LocalDate von, LocalDate bis) {
        Objects.requireNonNull(von, "von");
        Objects.requireNonNull(bis, "bis");
        return sendeBefehl("reservieren;" + typNr + ";" + DATE_FORMATTER.format(von) + ";" + DATE_FORMATTER.format(bis));
    }

    public String beenden() {
        String antwort = sendeBefehl("Ende");
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            // ignorieren, Verbindung wird ohnehin verworfen
        } finally {
            socket = null;
        }
        return antwort;
    }

    public void druckeBestaetigung(String text) {
        // Platzhalter für Druckfunktionalität
        System.out.println(text);
    }

    private String sendeBefehl(String befehl) {
        ensureConnected();
        try {
            socket.write(befehl + "\n");
            return socket.readLine();
        } catch (IOException e) {
            return "-ERR Verbindung unterbrochen";
        }
    }

    private void ensureConnected() {
        if (socket == null) {
            throw new IllegalStateException("Keine offene Verbindung");
        }
    }
}
