package de.lukas.taxi.meter;

/**
 * Adapter for Electronic Key System (EKS).
 * Handles communication with the electronic key for driver identification and data storage.
 */
public class EksAdapter {
    // Control characters
    public static final char DLE = 0x10;  // Data Link Escape
    public static final char STX = 0x02;  // Start of Text
    public static final char ETX = 0x03;  // End of Text
    public static final char NAK = 0x15;  // Negative Acknowledgement
    public static final char EM = 0x19;   // End of Medium

    private final Serial serial;
    private static final int MAX_RETRIES = 3;

    /**
     * Creates a new EKS adapter.
     *
     * @param serial the serial port for communication
     */
    public EksAdapter(Serial serial) {
        this.serial = serial;
    }

    /**
     * Checks if an electronic key is available.
     *
     * @return true if a key is present
     */
    public boolean isKeyAvailable() {
        // Stub implementation: assume key is available if serial is open
        return serial != null && serial.isOpen();
    }

    /**
     * Reads the driver number from the electronic key.
     *
     * @return the driver number, or -1 if no key present
     */
    public int readDriverNumber() {
        if (!isKeyAvailable()) {
            return -1;
        }

        // Stub implementation: return a sample driver number
        // In real system, this would read from the key
        return 101;
    }

    /**
     * Writes a single character to the serial port.
     *
     * @param c the character to write
     */
    public void writeChar(char c) {
        if (serial != null && serial.isOpen()) {
            serial.write((int) c);
        }
    }

    /**
     * Reads a single character from the serial port.
     *
     * @return the character read, or -1 if no data
     */
    public char readChar() {
        if (serial == null || !serial.isOpen()) {
            return (char) -1;
        }

        byte b = serial.read();
        if (b == -1) {
            return (char) -1;
        }
        return (char) (b & 0xFF);
    }

    /**
     * Reads data from the electronic key.
     *
     * @return array of data strings, or empty array if no data
     */
    public String[] readData() {
        if (!isKeyAvailable()) {
            return new String[0];
        }

        // Stub implementation
        String line = serial.readLine();
        if (line != null && !line.isEmpty()) {
            return line.split(";");
        }
        return new String[0];
    }

    /**
     * Calculates a checksum digit for data integrity.
     *
     * @param data the data to calculate checksum for
     * @return the checksum byte
     */
    public byte calculateCheckDigit(String data) {
        if (data == null || data.isEmpty()) {
            return 0;
        }

        // Simple XOR checksum
        byte checksum = 0;
        byte[] bytes = data.getBytes();
        for (byte b : bytes) {
            checksum ^= b;
        }
        return checksum;
    }

    /**
     * Task 1.4.2: Writes data to the electronic key using handshake protocol.
     * <p>
     * Protocol:
     * 1. Send STX
     * 2. Wait for response:
     * - DLE: proceed with data transmission
     * - NAK: retry from step 1
     * - EM: no memory available, abort
     * 3. Send data bytes followed by DLE, ETX, checksum
     * 4. Wait for final acknowledgement
     *
     * @param data the data to write
     * @return 0 = success, 1 = memory full (EM received), 2 = serial not open or key missing
     */
    public int writeData(String data) {
        // Check preconditions
        if (serial == null || !serial.isOpen()) {
            return 2;  // Serial not open
        }

        if (!isKeyAvailable()) {
            return 2;  // Key missing
        }

        if (data == null || data.isEmpty()) {
            return 2;  // Invalid data
        }

        int retries = 0;

        while (retries < MAX_RETRIES) {
            // Step 1: Send STX to initiate transmission
            writeChar(STX);

            // Small delay to simulate communication
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            // Step 2: Wait for response
            char response = readChar();

            if (response == (char) -1) {
                // No response, retry
                retries++;
                continue;
            }

            if (response == EM) {
                // End of Medium - no memory available
                return 1;
            }

            if (response == NAK) {
                // Negative acknowledgement - retry
                retries++;
                continue;
            }

            if (response == DLE) {
                // Data Link Escape - proceed with transmission

                // Step 3: Send data block
                byte[] dataBytes = data.getBytes();
                for (byte b : dataBytes) {
                    serial.write(b & 0xFF);
                }

                // Send DLE to mark end of data
                writeChar(DLE);

                // Send ETX to mark end of text
                writeChar(ETX);

                // Calculate and send checksum
                byte checksum = calculateCheckDigit(data);
                serial.write(checksum & 0xFF);

                // Step 4: Wait for final acknowledgement
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

                char finalResponse = readChar();
                if (finalResponse == DLE) {
                    // Success!
                    return 0;
                } else if (finalResponse == NAK) {
                    // Retry
                    retries++;
                    continue;
                } else if (finalResponse == EM) {
                    return 1;
                }
            }

            // Unknown response, retry
            retries++;
        }

        // Max retries exceeded
        return 2;
    }
}
