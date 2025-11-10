package de.lukas.taxi.meter;

/**
 * Thermal printer for printing receipts.
 */
public class ThermalPrinter {

    /**
     * Prints a receipt with the given data.
     *
     * @param data the receipt data to print
     */
    public void printReceipt(String data) {
        if (data != null) {
            System.out.println("========== QUITTUNG ==========");
            System.out.println(data);
            System.out.println("==============================");
        }
    }
}
