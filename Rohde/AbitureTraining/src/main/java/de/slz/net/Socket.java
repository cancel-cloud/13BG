package de.slz.net;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;

public class Socket implements AutoCloseable {

    private final String remoteHostIP;
    private final int remotePort;
    private final java.net.Socket socket;
    private BufferedReader reader;
    private BufferedWriter writer;

    public Socket(String remoteHostIP, int remotePort) {
        this.remoteHostIP = remoteHostIP;
        this.remotePort = remotePort;
        this.socket = new java.net.Socket();
    }

    public boolean connect() {
        try {
            socket.connect(new InetSocketAddress(remoteHostIP, remotePort));
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public String readLine() throws IOException {
        return reader.readLine();
    }

    public void write(String s) throws IOException {
        writer.write(s);
        writer.flush();
    }

    @Override
    public void close() throws IOException {
        try {
            if (writer != null) {
                writer.close();
            }
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } finally {
                socket.close();
            }
        }
    }
}
