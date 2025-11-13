package de.lukas.socialMedia;

/**
 * SocialMediaPlatform is the central class managing all users and business logic.
 * It provides methods for:
 * - User registration and authentication
 * - User search
 * - Password generation
 * - Finding subscribed users with new posts
 *
 * This class is shared among all ServerThreads and manages the platform's data.
 *
 * @author Social Media Platform Development Team
 * @version 1.0
 */
public class SocialMediaPlatform {
    private List<Nutzer> nutzer;
    private List<Bild> bilder;

    /**
     * Creates a new SocialMediaPlatform instance.
     * Initializes the collections for users and images.
     */
    public SocialMediaPlatform() {
        this.nutzer = new List<>();
        this.bilder = new List<>();
    }

    /**
     * Registers a new user on the platform.
     * Checks that the username is unique and the email is not already registered.
     *
     * Task 1.6 Implementation
     *
     * @param name the desired username
     * @param passwort the password
     * @param email the email address
     * @return 0 if registration was successful,
     *         -1 if the username is already taken,
     *         -2 if the email is already registered to another user
     */
    public int registrieren(String name, String passwort, String email) {
        // Check if username already exists
        for (int i = 0; i < nutzer.size(); i++) {
            Nutzer bestehendeNutzer = nutzer.get(i);
            if (bestehendeNutzer.getBenutzerName().equals(name)) {
                return -1; // Username already taken
            }
        }

        // Check if email is already registered
        for (int i = 0; i < nutzer.size(); i++) {
            Nutzer bestehendeNutzer = nutzer.get(i);
            if (bestehendeNutzer.getEmailAdresse().equals(email)) {
                return -2; // Email already registered
            }
        }

        // Create new user and add to list
        Nutzer neuerNutzer = new Nutzer(name, passwort, email);
        nutzer.add(neuerNutzer);
        return 0; // Success
    }

    /**
     * Authenticates a user with username and password.
     * Returns the Nutzer object if credentials are valid.
     *
     * @param name the username
     * @param passwort the password
     * @return the Nutzer object if authentication succeeds, null otherwise
     */
    public Nutzer anmelden(String name, String passwort) {
        Nutzer gefundenerNutzer = sucheNutzer(name);

        if (gefundenerNutzer != null && gefundenerNutzer.getPasswort().equals(passwort)) {
            return gefundenerNutzer;
        }

        return null; // Authentication failed
    }

    /**
     * Searches for a user by username.
     *
     * @param name the username to search for
     * @return the Nutzer object if found, null otherwise
     */
    public Nutzer sucheNutzer(String name) {
        for (int i = 0; i < nutzer.size(); i++) {
            Nutzer aktuellerNutzer = nutzer.get(i);
            if (aktuellerNutzer.getBenutzerName().equals(name)) {
                return aktuellerNutzer;
            }
        }
        return null; // User not found
    }

    /**
     * Generates a secure password with the following requirements:
     * - Length: 12 characters
     * - Contains at least one uppercase letter [A-Z]
     * - Contains at least one digit [0-9]
     * - Contains at least one special character [#, $, %, &]
     * - Remaining positions filled with lowercase letters [a-z]
     * - All required elements are placed at random positions
     *
     * Task 1.5.2 Implementation (based on algorithm from Task 1.5.1)
     *
     * Examples: %fivqwj2iKez, i4koarmbN$ae, Sbazoygvc4#s
     *
     * @return a character array containing the generated password
     */
    public char[] generierePasswort() {
        // 1. Initialize password array with 12 positions
        char[] passwort = new char[12];
        Random random = new Random();

        // 2. Fill all 12 positions with random lowercase letters [a-z]
        // ASCII: 97 ('a') to 122 ('z') = 26 letters
        for (int i = 0; i < 12; i++) {
            int zufallsASCII = random.nextInt(26) + 97; // 97 = 'a'
            passwort[i] = (char) zufallsASCII;
        }

        // 3. Insert uppercase letter at random position
        // ASCII: 65 ('A') to 90 ('Z') = 26 letters
        int positionGross = random.nextInt(12); // Position 0-11
        int grossbuchstabeASCII = random.nextInt(26) + 65; // 65 = 'A'
        passwort[positionGross] = (char) grossbuchstabeASCII;

        // 4. Insert digit at random position
        // ASCII: 48 ('0') to 57 ('9') = 10 digits
        int positionZiffer = random.nextInt(12); // Position 0-11
        int zifferASCII = random.nextInt(10) + 48; // 48 = '0'
        passwort[positionZiffer] = (char) zifferASCII;

        // 5. Insert special character at random position
        // Special characters: # (35), $ (36), % (37), & (38)
        int positionSonder = random.nextInt(12); // Position 0-11
        int sonderzeichenWahl = random.nextInt(4); // 0-3 for the 4 special chars

        int sonderzeichenASCII;
        if (sonderzeichenWahl == 0) {
            sonderzeichenASCII = 35; // #
        } else if (sonderzeichenWahl == 1) {
            sonderzeichenASCII = 36; // $
        } else if (sonderzeichenWahl == 2) {
            sonderzeichenASCII = 37; // %
        } else {
            sonderzeichenASCII = 38; // &
        }

        passwort[positionSonder] = (char) sonderzeichenASCII;

        // 6. Return the generated password
        return passwort;
    }

    /**
     * Finds all users that user n has subscribed to and who have created
     * new posts since user n was last active.
     *
     * Task 1.3 Implementation (based on struktogramm from task 1.3)
     *
     * Important: Each subscribed user appears only once in the result list,
     * even if they created multiple posts in the relevant time period.
     *
     * @param n the user to check
     * @return a list of subscribed users with new posts
     */
    public List<Nutzer> ermittleAbonnierteNutzerMitNeuenBeitraegen(Nutzer n) {
        // 1. Initialize result list
        List<Nutzer> ergebnisListe = new List<>();

        // 2. Get list of subscribed users
        List<Nutzer> abonnierteListe = n.getAbonnierteNutzer();

        // 3. Iterate through all subscribed users
        for (int i = 0; i < abonnierteListe.size(); i++) {
            Nutzer abonnierterNutzer = abonnierteListe.get(i);
            boolean hatNeuenBeitrag = false;

            // 4. Get posts of the subscribed user
            List<Beitrag> beitragsListe = abonnierterNutzer.getBeitraege();

            // 5. Check each post if it was created after n's last activity
            for (int j = 0; j < beitragsListe.size(); j++) {
                Beitrag beitrag = beitragsListe.get(j);

                // Check if post was created after user n was last active
                if (beitrag.getGepostet().isAfter(n.getZuletztAktiv())) {
                    hatNeuenBeitrag = true;
                    // We can break here as we only need to know if there's at least one new post
                    // But we'll continue checking for completeness
                }
            }

            // 6. If subscribed user has new posts, add to result (avoid duplicates)
            if (hatNeuenBeitrag) {
                if (!ergebnisListe.contains(abonnierterNutzer)) {
                    ergebnisListe.add(abonnierterNutzer);
                }
            }
        }

        // 7. Return result list
        return ergebnisListe;
    }

    /**
     * Gets the list of all users on the platform.
     *
     * @return the list of users
     */
    public List<Nutzer> getNutzer() {
        return nutzer;
    }

    /**
     * Gets the list of all images on the platform.
     *
     * @return the list of images
     */
    public List<Bild> getBilder() {
        return bilder;
    }

    /**
     * Adds an image to the platform's image collection.
     *
     * @param bild the image to add
     */
    public void hinzufuegenBild(Bild bild) {
        if (bild != null && !bilder.contains(bild)) {
            bilder.add(bild);
        }
    }
}
