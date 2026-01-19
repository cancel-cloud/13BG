package de.lukas.socialMediaGPT;

/**
 * Bild (Image) class represents an image that can be used in posts.
 * Images are identified by a unique auto-generated ID and have a filename.
 * Images can be reused across multiple posts (Aggregation relationship).
 *
 * @author Social Media Platform Development Team
 * @version 1.0
 */
public class Bild {
    private static int autowert = 1; // Static counter for auto-generating IDs
    private int id;
    private String dateiname;

    /**
     * Creates a new Bild (image) with the specified filename.
     * The ID is automatically generated from a static counter.
     *
     * @param dateiname the filename of the image
     */
    public Bild(String dateiname) {
        this.id = autowert++;
        this.dateiname = dateiname;
    }

    /**
     * Gets the unique ID of this image.
     *
     * @return the image ID
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the filename of this image.
     *
     * @return the filename
     */
    public String getDateiname() {
        return dateiname;
    }

    /**
     * Gets the current value of the auto-increment counter.
     * Useful for testing or system information.
     *
     * @return the current autowert value
     */
    public static int getAutowert() {
        return autowert;
    }
}
