package de.lukas.socialMedia;

/**
 * Beitrag (Post) class represents a post on the social media platform.
 * A post has a title, creation timestamp, author, images, optional text, and can be liked.
 *
 * Key features:
 * - Every post must have at least one image
 * - A post can have zero or one text component
 * - Posts can be liked by users (but not by their own author)
 * - Posts track when they were created
 *
 * @author Social Media Platform Development Team
 * @version 1.0
 */
public class Beitrag {
    private String titel;
    private DateTime gepostet;
    private int anzahlLikes;
    private List<Bild> bilder;
    private Text text;
    private Nutzer autor;

    /**
     * Creates a new Beitrag (post) with a title, initial image, and author.
     * The post is timestamped with the current date and time.
     *
     * @param autor the user creating this post
     * @param titel the title of the post
     * @param bild the first image to include in the post
     */
    public Beitrag(Nutzer autor, String titel, Bild bild) {
        this.autor = autor;
        this.titel = titel;
        this.bilder = new List<>();
        this.bilder.add(bild);
        this.gepostet = new DateTime();
        this.anzahlLikes = 0;
        this.text = null;
    }

    /**
     * Adds an additional image to this post.
     * A post can contain multiple images.
     *
     * @param bild the image to add to the post
     */
    public void hinzufuegen(Bild bild) {
        if (bild != null && !bilder.contains(bild)) {
            bilder.add(bild);
        }
    }

    /**
     * Creates and adds text content to this post.
     * If the post already has text, it will be replaced.
     * This implements the composition relationship: the text is existentially
     * dependent on the post.
     *
     * @param text the text content for the post
     */
    public void erstelleText(String text) {
        this.text = new Text(text);
    }

    /**
     * Increments the like counter for this post.
     * Note: The calling code should ensure that users don't like their own posts.
     * This is typically enforced by the Nutzer.like() method.
     */
    public void like() {
        anzahlLikes++;
    }

    /**
     * Gets the title of this post.
     *
     * @return the post title
     */
    public String getTitel() {
        return titel;
    }

    /**
     * Gets the timestamp when this post was created.
     *
     * @return the creation timestamp
     */
    public DateTime getGepostet() {
        return gepostet;
    }

    /**
     * Gets the number of likes this post has received.
     *
     * @return the number of likes
     */
    public int getAnzahlLikes() {
        return anzahlLikes;
    }

    /**
     * Gets the list of images in this post.
     *
     * @return the list of images
     */
    public List<Bild> getBilder() {
        return bilder;
    }

    /**
     * Gets the text content of this post.
     *
     * @return the text object, or null if the post has no text
     */
    public Text getText() {
        return text;
    }

    /**
     * Gets the author of this post.
     *
     * @return the author (Nutzer object)
     */
    public Nutzer getAutor() {
        return autor;
    }
}
