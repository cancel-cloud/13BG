package de.lukas.socialMedia;

/**
 * Text class represents the text content of a post.
 * A text object is existentially dependent on its post (Composition relationship).
 * When a post is deleted, its text is automatically deleted as well.
 *
 * @author Social Media Platform Development Team
 * @version 1.0
 */
public class Text {
    private String text;

    /**
     * Creates a new Text object with the specified content.
     *
     * @param text the text content
     */
    public Text(String text) {
        this.text = text;
    }

    /**
     * Gets the text content.
     *
     * @return the text content
     */
    public String getText() {
        return text;
    }
}
