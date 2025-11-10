package de.lukas.taxi.meter;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Mock implementation of a serial port interface.
 * Simulates serial communication for testing purposes.
 */
public class Serial {
    private final String portName;
    private final int baudrate;
    private final int dataBits;
    private final int stopBits;
    private final int parity;

    private boolean isOpen;
    private final Queue<Byte> readBuffer;
    private final Queue<Byte> writeBuffer;

    // For testing: allows injection of responses
    private Queue<Byte> simulatedResponses;

    /**
     * Creates a new serial port connection.
     *
     * @param portName COM port name (e.g., "COM1")
     * @param baudrate baud rate (e.g., 9600)
     * @param dataBits number of data bits (e.g., 8)
     * @param stopBits number of stop bits (e.g., 1)
     * @param parity   parity setting (e.g., 0 for none)
     */
    public Serial(String portName, int baudrate, int dataBits, int stopBits, int parity) {
        this.portName = portName;
        this.baudrate = baudrate;
        this.dataBits = dataBits;
        this.stopBits = stopBits;
        this.parity = parity;
        this.isOpen = false;
        this.readBuffer = new LinkedList<>();
        this.writeBuffer = new LinkedList<>();
        this.simulatedResponses = new LinkedList<>();
    }

    /**
     * Opens the serial port.
     *
     * @return true if successful
     */
    public boolean open() {
        this.isOpen = true;
        return true;
    }

    /**
     * Closes the serial port.
     */
    public void close() {
        this.isOpen = false;
        readBuffer.clear();
        writeBuffer.clear();
    }

    /**
     * Checks if the port is open.
     *
     * @return true if open
     */
    public boolean isOpen() {
        return isOpen;
    }

    /**
     * Returns the number of bytes available to read.
     *
     * @return number of available bytes
     */
    public int dataAvailable() {
        return readBuffer.size();
    }

    /**
     * Reads a single byte (blocking).
     *
     * @return the byte read, or -1 if no data
     */
    public byte read() {
        if (!isOpen) {
            return -1;
        }

        // First check simulated responses
        if (!simulatedResponses.isEmpty()) {
            return simulatedResponses.poll();
        }

        // Then check read buffer
        if (readBuffer.isEmpty()) {
            return -1;
        }
        return readBuffer.poll();
    }

    /**
     * Reads multiple bytes into a buffer (blocking).
     *
     * @param buffer buffer to read into
     * @param len    maximum number of bytes to read
     * @return number of bytes actually read
     */
    public int read(byte[] buffer, int len) {
        if (!isOpen || buffer == null) {
            return -1;
        }

        int count = 0;
        while (count < len && (dataAvailable() > 0 || !simulatedResponses.isEmpty())) {
            byte b = read();
            if (b != -1) {
                buffer[count++] = b;
            } else {
                break;
            }
        }
        return count;
    }

    /**
     * Reads a complete line (blocking).
     *
     * @return the line read, or null if no complete line available
     */
    public String readLine() {
        if (!isOpen) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        while (true) {
            byte b = read();
            if (b == -1) {
                break;
            }
            if (b == '\n') {
                break;
            }
            if (b != '\r') {
                sb.append((char) b);
            }
        }

        return sb.length() > 0 ? sb.toString() : null;
    }

    /**
     * Writes a single byte value.
     *
     * @param value the byte to write (0-255)
     */
    public void write(int value) {
        if (isOpen) {
            writeBuffer.add((byte) (value & 0xFF));
        }
    }

    /**
     * Writes multiple bytes.
     *
     * @param buffer the bytes to write
     * @param len    number of bytes to write
     */
    public void write(byte[] buffer, int len) {
        if (!isOpen || buffer == null) {
            return;
        }

        for (int i = 0; i < len && i < buffer.length; i++) {
            writeBuffer.add(buffer[i]);
        }
    }

    /**
     * Writes a string.
     *
     * @param str the string to write
     */
    public void write(String str) {
        if (!isOpen || str == null) {
            return;
        }

        byte[] bytes = str.getBytes();
        write(bytes, bytes.length);
    }

    // Testing helpers

    /**
     * Sets simulated responses for testing.
     *
     * @param responses queue of bytes to return on read operations
     */
    public void setSimulatedResponses(Queue<Byte> responses) {
        this.simulatedResponses = responses;
    }

    /**
     * Adds a simulated response byte.
     *
     * @param b the byte to add
     */
    public void addSimulatedResponse(byte b) {
        this.simulatedResponses.add(b);
    }

    /**
     * Gets the write buffer for testing.
     *
     * @return the write buffer
     */
    public Queue<Byte> getWriteBuffer() {
        return writeBuffer;
    }

    /**
     * Clears all buffers.
     */
    public void clearBuffers() {
        readBuffer.clear();
        writeBuffer.clear();
        simulatedResponses.clear();
    }

    // Getters
    public String getPortName() {
        return portName;
    }

    public int getBaudrate() {
        return baudrate;
    }

    public int getDataBits() {
        return dataBits;
    }

    public int getStopBits() {
        return stopBits;
    }

    public int getParity() {
        return parity;
    }
}
