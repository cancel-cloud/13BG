package de.lukas.socialMediaGPT;

/**
 * Server class manages the ServerSocket and creates a ServerThread for each connecting client.
 * The server runs in an endless loop, accepting client connections and spawning threads
 * to handle each client independently.
 *
 * Key features:
 * - Listens on a specified port for incoming connections
 * - Creates a new ServerThread for each connecting client
 * - Provides each ServerThread with access to the SocialMediaPlatform
 * - Allows multiple clients to connect simultaneously
 *
 * @author Social Media Platform Development Team
 * @version 1.0
 */
public class Server {
    private ServerSocket serverSocket;
    private SocialMediaPlatform smp;

    /**
     * Creates a new Server that listens on the specified port.
     * Initializes the ServerSocket and stores the reference to the SocialMediaPlatform.
     *
     * @param port the port number to listen on
     * @param smp the SocialMediaPlatform instance for business logic access
     */
    public Server(int port, SocialMediaPlatform smp) {
        this.serverSocket = new ServerSocket(port);
        this.smp = smp;
    }

    /**
     * Runs the server in an endless loop.
     * The server:
     * 1. Waits for a client connection via accept()
     * 2. Creates a new ServerThread for the connected client
     * 3. Starts the ServerThread
     * 4. Returns to accept() to wait for the next client
     *
     * This allows the server to handle multiple clients concurrently,
     * as each client is handled by its own thread.
     */
    public void runServer() {
        System.out.println("Server started and waiting for clients...");
        while (true) {
            // Wait for a client to connect (blocking call)
            Socket clientSocket = serverSocket.accept();

            if (clientSocket != null) {
                System.out.println("Client connected: " + clientSocket.getRemoteHostIP());

                // Create a new thread for this client
                ServerThread clientThread = new ServerThread(clientSocket, smp);

                // Start the thread (calls run() method)
                clientThread.start();
            }
        }
    }

    /**
     * Gets the ServerSocket instance.
     *
     * @return the ServerSocket
     */
    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    /**
     * Gets the SocialMediaPlatform instance.
     *
     * @return the SocialMediaPlatform
     */
    public SocialMediaPlatform getSmp() {
        return smp;
    }
}
