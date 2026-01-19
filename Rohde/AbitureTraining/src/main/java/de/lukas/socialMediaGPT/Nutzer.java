package de.lukas.socialMediaGPT;

/**
 * Nutzer (User) class represents a user on the social media platform.
 * Users can create posts, like posts, subscribe to other users, and manage images.
 *
 * Key features:
 * - All user actions automatically update the last activity timestamp
 * - Users cannot like their own posts
 * - Users cannot subscribe to themselves
 * - Subscriptions are bidirectional and stored only once
 * - Users maintain collections of their posts, subscribers, subscribed users, and images
 *
 * @author Social Media Platform Development Team
 * @version 1.0
 */
public class Nutzer {
    private String benutzerName;
    private String passwort;
    private String emailAdresse;
    private DateTime zuletztAktiv;
    private List<Beitrag> beitraege;
    private List<Nutzer> abonnenten;
    private List<Nutzer> abonnierteNutzer;
    private List<Bild> bilder;

    /**
     * Creates a new Nutzer (user) with the specified credentials.
     * Initializes all collections and sets the last activity to the current time.
     *
     * @param name the username
     * @param passwort the password
     * @param email the email address
     */
    public Nutzer(String name, String passwort, String email) {
        this.benutzerName = name;
        this.passwort = passwort;
        this.emailAdresse = email;
        this.zuletztAktiv = new DateTime();
        this.beitraege = new List<>();
        this.abonnenten = new List<>();
        this.abonnierteNutzer = new List<>();
        this.bilder = new List<>();
    }

    /**
     * Creates a new post with a title and an image.
     * Updates the user's last activity timestamp.
     *
     * @param titel the title of the post
     * @param bild the image for the post
     * @return the created Beitrag object
     */
    public Beitrag erstelleBeitrag(String titel, Bild bild) {
        aktualisiereAktivitaet();
        Beitrag neuerBeitrag = new Beitrag(this, titel, bild);
        beitraege.add(neuerBeitrag);
        return neuerBeitrag;
    }

    /**
     * Creates a new post with a title, an image, and text content.
     * Updates the user's last activity timestamp.
     *
     * @param titel the title of the post
     * @param bild the image for the post
     * @param text the text content for the post
     * @return the created Beitrag object
     */
    public Beitrag erstelleBeitrag(String titel, Bild bild, String text) {
        aktualisiereAktivitaet();
        Beitrag neuerBeitrag = new Beitrag(this, titel, bild);
        neuerBeitrag.erstelleText(text);
        beitraege.add(neuerBeitrag);
        return neuerBeitrag;
    }

    /**
     * Likes a post if it's not the user's own post.
     * Updates the user's last activity timestamp.
     *
     * @param beitrag the post to like
     */
    public void like(Beitrag beitrag) {
        aktualisiereAktivitaet();
        // A user cannot like their own posts
        if (beitrag != null && beitrag.getAutor() != this) {
            beitrag.like();
        }
    }

    /**
     * Subscribes to another user.
     * Implements a bidirectional subscription relationship:
     * - Adds the other user to this user's "subscribed users" list
     * - Adds this user to the other user's "subscribers" list
     * Ensures that:
     * - A user cannot subscribe to themselves
     * - Each subscription is stored only once
     * Updates the user's last activity timestamp.
     *
     * @param n the user to subscribe to
     */
    public void abonnieren(Nutzer n) {
        aktualisiereAktivitaet();
        // Cannot subscribe to oneself
        if (n == null || n == this) {
            return;
        }
        // Only subscribe if not already subscribed
        if (!abonnierteNutzer.contains(n)) {
            abonnierteNutzer.add(n);
            // Bidirectional relationship: add this user as subscriber to the other user
            if (!n.abonnenten.contains(this)) {
                n.abonnenten.add(this);
            }
        }
    }

    /**
     * Adds an image to this user's image collection.
     * Updates the user's last activity timestamp.
     *
     * @param bild the image to add
     */
    public void hinzufuegenBild(Bild bild) {
        aktualisiereAktivitaet();
        if (bild != null && !bilder.contains(bild)) {
            bilder.add(bild);
        }
    }

    /**
     * Updates the last activity timestamp to the current time.
     * This method is called automatically by all user actions.
     */
    private void aktualisiereAktivitaet() {
        this.zuletztAktiv = new DateTime();
    }

    /**
     * Gets the username.
     *
     * @return the username
     */
    public String getBenutzerName() {
        return benutzerName;
    }

    /**
     * Gets the password.
     *
     * @return the password
     */
    public String getPasswort() {
        return passwort;
    }

    /**
     * Sets the password.
     *
     * @param passwort the new password
     */
    public void setPasswort(String passwort) {
        this.passwort = passwort;
    }

    /**
     * Gets the email address.
     *
     * @return the email address
     */
    public String getEmailAdresse() {
        return emailAdresse;
    }

    /**
     * Gets the timestamp of the user's last activity.
     *
     * @return the last activity timestamp
     */
    public DateTime getZuletztAktiv() {
        return zuletztAktiv;
    }

    /**
     * Gets the list of posts created by this user.
     *
     * @return the list of posts
     */
    public List<Beitrag> getBeitraege() {
        return beitraege;
    }

    /**
     * Gets the list of users who subscribe to this user.
     *
     * @return the list of subscribers
     */
    public List<Nutzer> getAbonnenten() {
        return abonnenten;
    }

    /**
     * Gets the list of users this user has subscribed to.
     *
     * @return the list of subscribed users
     */
    public List<Nutzer> getAbonnierteNutzer() {
        return abonnierteNutzer;
    }

    /**
     * Gets the list of images owned by this user.
     *
     * @return the list of images
     */
    public List<Bild> getBilder() {
        return bilder;
    }
}
