package de.lukas.taxi.meter;

/**
 * Simple display for showing information to the driver.
 */
public class Display {

    /**
     * Prints a line of text to the display.
     *
     * @param line the text to display
     */
    public void printLine(String line) {
        if (line != null) {
            System.out.println("[DISPLAY] " + line);
        }
    }

    /**
     * Clears the display.
     */
    public void clear() {
        System.out.println("[DISPLAY] <cleared>");
    }
}
