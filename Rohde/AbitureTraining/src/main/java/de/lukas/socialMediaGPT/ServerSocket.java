package de.lukas.socialMediaGPT;

import java.io.IOException;

/**
 * ServerSocket class for accepting client connections.
 * Provides methods for binding to a port and accepting incoming connections.
 *
 * @author Social Media Platform Development Team
 * @version 1.0
 */
public class ServerSocket {
    private int localPort;
    private java.net.ServerSocket serverSocket;

    /**
     * Creates a server socket and binds it to the specified port.
     *
     * @param localPort the port number to bind to
     */
    public ServerSocket(int localPort) {
        this.localPort = localPort;
        try {
            this.serverSocket = new java.net.ServerSocket(localPort);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Waits for a client to establish a connection.
     * Blocks until a connection is established.
     *
     * @return a Socket object representing the connection, or null if an error occurred
     */
    public Socket accept() {
        try {
            java.net.Socket clientSocket = serverSocket.accept();
            return new Socket(clientSocket);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Closes the server socket.
     */
    public void close() {
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the local port number.
     *
     * @return the local port number
     */
    public int getLocalPort() {
        return localPort;
    }
}
