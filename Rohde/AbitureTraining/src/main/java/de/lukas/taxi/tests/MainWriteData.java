package de.lukas.taxi.tests;

import de.lukas.taxi.meter.EksAdapter;
import de.lukas.taxi.meter.Serial;

/**
 * Test: EksAdapter.writeData (Task 1.4.2)
 * Tests the handshake protocol for writing data to electronic key.
 */
public class MainWriteData {

    public static void main(String[] args) {
        System.out.println("=== Test: EKS writeData Protocol (Task 1.4.2) ===\n");

        System.out.println("Protocol description:");
        System.out.println("  1. Send STX (0x02)");
        System.out.println("  2. Wait for response:");
        System.out.println("     - DLE (0x10): proceed with transmission");
        System.out.println("     - NAK (0x15): retry");
        System.out.println("     - EM (0x19): memory full, abort");
        System.out.println("  3. Send data + DLE + ETX + checksum");
        System.out.println("  4. Wait for final DLE acknowledgement");
        System.out.println();

        // Test case 1: Successful transmission (DLE response)
        System.out.println("Test 1: Successful transmission");
        Serial serial1 = new Serial("COM1", 9600, 8, 1, 0);
        serial1.open();

        // Simulate successful handshake: respond with DLE to STX, then DLE to data
        serial1.addSimulatedResponse((byte) EksAdapter.DLE);
        serial1.addSimulatedResponse((byte) EksAdapter.DLE);

        EksAdapter adapter1 = new EksAdapter(serial1);
        String testData = "1;101;Start,00000,City;Ziel,00000,City;01.01.2023 10:00;01.01.2023 10:30;10.0;23.5";

        int result1 = adapter1.writeData(testData);
        System.out.println("  Result: " + result1 + " (expected: 0 = success)");
        System.out.println("  Status: " + (result1 == 0 ? "PASS" : "FAIL"));
        serial1.close();
        System.out.println();

        // Test case 2: Memory full (EM response)
        System.out.println("Test 2: Memory full");
        Serial serial2 = new Serial("COM2", 9600, 8, 1, 0);
        serial2.open();

        // Simulate memory full: respond with EM
        serial2.addSimulatedResponse((byte) EksAdapter.EM);

        EksAdapter adapter2 = new EksAdapter(serial2);
        int result2 = adapter2.writeData(testData);
        System.out.println("  Result: " + result2 + " (expected: 1 = memory full)");
        System.out.println("  Status: " + (result2 == 1 ? "PASS" : "FAIL"));
        serial2.close();
        System.out.println();

        // Test case 3: Retry with NAK, then success
        System.out.println("Test 3: Retry after NAK");
        Serial serial3 = new Serial("COM3", 9600, 8, 1, 0);
        serial3.open();

        // Simulate NAK then DLE for retry
        serial3.addSimulatedResponse((byte) EksAdapter.NAK);
        serial3.addSimulatedResponse((byte) EksAdapter.DLE);
        serial3.addSimulatedResponse((byte) EksAdapter.DLE);

        EksAdapter adapter3 = new EksAdapter(serial3);
        int result3 = adapter3.writeData(testData);
        System.out.println("  Result: " + result3 + " (expected: 0 = success after retry)");
        System.out.println("  Status: " + (result3 == 0 ? "PASS" : "FAIL"));
        serial3.close();
        System.out.println();

        // Test case 4: Serial not open
        System.out.println("Test 4: Serial port not open");
        Serial serial4 = new Serial("COM4", 9600, 8, 1, 0);
        // Don't open serial

        EksAdapter adapter4 = new EksAdapter(serial4);
        int result4 = adapter4.writeData(testData);
        System.out.println("  Result: " + result4 + " (expected: 2 = error)");
        System.out.println("  Status: " + (result4 == 2 ? "PASS" : "FAIL"));
        System.out.println();

        // Test case 5: Checksum verification
        System.out.println("Test 5: Checksum calculation");
        EksAdapter adapter5 = new EksAdapter(serial1);
        byte checksum1 = adapter5.calculateCheckDigit("test");
        byte checksum2 = adapter5.calculateCheckDigit("test");
        byte checksum3 = adapter5.calculateCheckDigit("different");

        System.out.println("  Same data produces same checksum: " + (checksum1 == checksum2 ? "PASS" : "FAIL"));
        System.out.println("  Different data produces different checksum: " + (checksum1 != checksum3 ? "PASS" : "FAIL"));

        System.out.println("\n=== Test Complete ===");
    }
}
