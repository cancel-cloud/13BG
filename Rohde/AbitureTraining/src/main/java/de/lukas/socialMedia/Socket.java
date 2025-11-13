package de.lukas.socialMedia;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;

/**
 * Socket class for network communication.
 * Provides methods for connecting to a remote host and sending/receiving data.
 *
 * @author Social Media Platform Development Team
 * @version 1.0
 */
public class Socket {
    private String remoteHostIP;
    private int remotePort;
    private java.net.Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;

    /**
     * Creates a Socket object with the specified remote host and port.
     *
     * @param remoteHostIP the IP address of the remote host
     * @param remotePort the port number of the remote host
     */
    public Socket(String remoteHostIP, int remotePort) {
        this.remoteHostIP = remoteHostIP;
        this.remotePort = remotePort;
    }

    /**
     * Internal constructor for wrapping an existing Java socket.
     * Used by ServerSocket when accepting connections.
     *
     * @param socket the Java socket to wrap
     */
    protected Socket(java.net.Socket socket) {
        try {
            this.socket = socket;
            this.remoteHostIP = socket.getInetAddress().getHostAddress();
            this.remotePort = socket.getPort();
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.writer = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Establishes a connection to the remote host.
     *
     * @return true if connection was successful, false otherwise
     */
    public boolean connect() {
        try {
            socket = new java.net.Socket();
            socket.connect(new InetSocketAddress(remoteHostIP, remotePort), 5000);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Reads one line from the socket.
     * Blocks until a complete line is read or returns null if socket is not open.
     * The line-end character is not included in the returned string.
     *
     * @return the line read from the socket, or null if socket is not open
     */
    public String readLine() {
        if (socket == null || !socket.isConnected() || reader == null) {
            return null;
        }
        try {
            return reader.readLine();
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Writes a string to the socket.
     * Does nothing if the socket is not open.
     *
     * @param s the string to write
     */
    public void write(String s) {
        if (socket == null || !socket.isConnected() || writer == null) {
            return;
        }
        writer.print(s);
        writer.flush();
    }

    /**
     * Closes the connection.
     */
    public void close() {
        try {
            if (reader != null) reader.close();
            if (writer != null) writer.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the remote host IP address.
     *
     * @return the remote host IP address
     */
    public String getRemoteHostIP() {
        return remoteHostIP;
    }

    /**
     * Gets the remote port number.
     *
     * @return the remote port number
     */
    public int getRemotePort() {
        return remotePort;
    }
}
