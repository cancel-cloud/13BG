package de.lukas.socialMedia;

/**
 * Main demonstration class for the Social Media Platform.
 *
 * This program demonstrates all key features of the social media platform:
 *
 * 1. USER REGISTRATION AND AUTHENTICATION
 *    - Register new users with username, password, and email
 *    - Validate that usernames and emails are unique
 *    - Authenticate users with login credentials
 *
 * 2. PASSWORD GENERATION
 *    - Generate secure passwords with specific requirements:
 *      * 12 characters long
 *      * At least one uppercase letter
 *      * At least one digit
 *      * At least one special character (#, $, %, &)
 *      * Remaining positions filled with lowercase letters
 *
 * 3. CONTENT CREATION
 *    - Create posts with images
 *    - Add text content to posts
 *    - Add multiple images to a post
 *
 * 4. SOCIAL INTERACTIONS
 *    - Like posts (users cannot like their own posts)
 *    - Subscribe to other users (bidirectional relationship)
 *    - Track last activity timestamps
 *
 * 5. FEED FUNCTIONALITY
 *    - Find subscribed users who have created new posts
 *    - Filter posts created after user's last activity
 *
 * @author Social Media Platform Development Team
 * @version 1.0
 */
public class Main {

    /**
     * Main entry point of the application.
     * Runs a series of demonstrations showing all platform features.
     *
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        System.out.println("=".repeat(80));
        System.out.println("SOCIAL MEDIA PLATFORM - DEMONSTRATION");
        System.out.println("=".repeat(80));
        System.out.println();

        // Create the platform instance
        SocialMediaPlatform platform = new SocialMediaPlatform();

        // Run demonstrations
        demonstrateUserRegistration(platform);
        System.out.println();

        demonstrateAuthentication(platform);
        System.out.println();

        demonstratePasswordGeneration(platform);
        System.out.println();

        demonstrateContentCreation(platform);
        System.out.println();

        demonstrateSocialInteractions(platform);
        System.out.println();

        demonstrateFeedFunctionality(platform);
        System.out.println();

        System.out.println("=".repeat(80));
        System.out.println("DEMONSTRATION COMPLETED");
        System.out.println("=".repeat(80));
    }

    /**
     * Demonstrates user registration functionality.
     * Shows successful registration and validation of duplicate usernames/emails.
     *
     * @param platform the social media platform instance
     */
    private static void demonstrateUserRegistration(SocialMediaPlatform platform) {
        System.out.println("-".repeat(80));
        System.out.println("1. USER REGISTRATION");
        System.out.println("-".repeat(80));

        // Register first user - should succeed
        System.out.println("\nRegistering user 'alice' with email 'alice@example.com'...");
        int result1 = platform.registrieren("alice", "SecurePass123!", "alice@example.com");
        printRegistrationResult(result1);

        // Register second user - should succeed
        System.out.println("\nRegistering user 'bob' with email 'bob@example.com'...");
        int result2 = platform.registrieren("bob", "BobsPassword456", "bob@example.com");
        printRegistrationResult(result2);

        // Try to register with duplicate username - should fail
        System.out.println("\nAttempting to register duplicate username 'alice'...");
        int result3 = platform.registrieren("alice", "DifferentPass", "newemail@example.com");
        printRegistrationResult(result3);

        // Try to register with duplicate email - should fail
        System.out.println("\nAttempting to register with duplicate email 'bob@example.com'...");
        int result4 = platform.registrieren("charlie", "CharliePass789", "bob@example.com");
        printRegistrationResult(result4);

        // Register third user - should succeed
        System.out.println("\nRegistering user 'charlie' with email 'charlie@example.com'...");
        int result5 = platform.registrieren("charlie", "CharliePass789", "charlie@example.com");
        printRegistrationResult(result5);

        // Register fourth user for later demonstrations
        System.out.println("\nRegistering user 'diana' with email 'diana@example.com'...");
        int result6 = platform.registrieren("diana", "DianaPass000", "diana@example.com");
        printRegistrationResult(result6);

        System.out.println("\nTotal registered users: " + platform.getNutzer().size());
    }

    /**
     * Prints the result of a registration attempt.
     *
     * @param result the return code from registrieren() method
     */
    private static void printRegistrationResult(int result) {
        if (result == 0) {
            System.out.println("  ✓ Registration successful!");
        } else if (result == -1) {
            System.out.println("  ✗ Registration failed: Username already taken");
        } else if (result == -2) {
            System.out.println("  ✗ Registration failed: Email already registered");
        }
    }

    /**
     * Demonstrates user authentication (login) functionality.
     * Shows successful and failed login attempts.
     *
     * @param platform the social media platform instance
     */
    private static void demonstrateAuthentication(SocialMediaPlatform platform) {
        System.out.println("-".repeat(80));
        System.out.println("2. USER AUTHENTICATION");
        System.out.println("-".repeat(80));

        // Successful login
        System.out.println("\nAttempting to login as 'alice' with correct password...");
        Nutzer alice = platform.anmelden("alice", "SecurePass123!");
        if (alice != null) {
            System.out.println("  ✓ Login successful! Welcome, " + alice.getBenutzerName());
        } else {
            System.out.println("  ✗ Login failed: Invalid credentials");
        }

        // Failed login - wrong password
        System.out.println("\nAttempting to login as 'bob' with incorrect password...");
        Nutzer bobFail = platform.anmelden("bob", "WrongPassword");
        if (bobFail != null) {
            System.out.println("  ✓ Login successful! Welcome, " + bobFail.getBenutzerName());
        } else {
            System.out.println("  ✗ Login failed: Invalid credentials");
        }

        // Failed login - non-existent user
        System.out.println("\nAttempting to login as non-existent user 'eve'...");
        Nutzer eveFail = platform.anmelden("eve", "SomePassword");
        if (eveFail != null) {
            System.out.println("  ✓ Login successful! Welcome, " + eveFail.getBenutzerName());
        } else {
            System.out.println("  ✗ Login failed: Invalid credentials");
        }
    }

    /**
     * Demonstrates password generation functionality.
     * Generates and validates several passwords according to requirements.
     *
     * @param platform the social media platform instance
     */
    private static void demonstratePasswordGeneration(SocialMediaPlatform platform) {
        System.out.println("-".repeat(80));
        System.out.println("3. SECURE PASSWORD GENERATION");
        System.out.println("-".repeat(80));

        System.out.println("\nPassword requirements:");
        System.out.println("  - Length: 12 characters");
        System.out.println("  - At least one uppercase letter [A-Z]");
        System.out.println("  - At least one digit [0-9]");
        System.out.println("  - At least one special character [#, $, %, &]");
        System.out.println("  - Remaining positions: lowercase letters [a-z]");
        System.out.println();

        System.out.println("Generating 5 sample passwords:");
        for (int i = 1; i <= 5; i++) {
            char[] password = platform.generierePasswort();
            String passwordStr = new String(password);

            // Validate the password
            boolean hasUppercase = hasUppercase(password);
            boolean hasDigit = hasDigit(password);
            boolean hasSpecial = hasSpecialChar(password);
            boolean correctLength = password.length == 12;

            System.out.println("  " + i + ". " + passwordStr +
                             " [Length: " + password.length +
                             ", Upper: " + (hasUppercase ? "✓" : "✗") +
                             ", Digit: " + (hasDigit ? "✓" : "✗") +
                             ", Special: " + (hasSpecial ? "✓" : "✗") + "]");
        }
    }

    /**
     * Checks if a password contains at least one uppercase letter.
     */
    private static boolean hasUppercase(char[] password) {
        for (char c : password) {
            if (c >= 'A' && c <= 'Z') return true;
        }
        return false;
    }

    /**
     * Checks if a password contains at least one digit.
     */
    private static boolean hasDigit(char[] password) {
        for (char c : password) {
            if (c >= '0' && c <= '9') return true;
        }
        return false;
    }

    /**
     * Checks if a password contains at least one special character.
     */
    private static boolean hasSpecialChar(char[] password) {
        for (char c : password) {
            if (c == '#' || c == '$' || c == '%' || c == '&') return true;
        }
        return false;
    }

    /**
     * Demonstrates content creation functionality.
     * Shows creating posts with images and text.
     *
     * @param platform the social media platform instance
     */
    private static void demonstrateContentCreation(SocialMediaPlatform platform) {
        System.out.println("-".repeat(80));
        System.out.println("4. CONTENT CREATION");
        System.out.println("-".repeat(80));

        // Get users
        Nutzer alice = platform.sucheNutzer("alice");
        Nutzer bob = platform.sucheNutzer("bob");
        Nutzer charlie = platform.sucheNutzer("charlie");

        // Create images
        System.out.println("\nCreating images...");
        Bild image1 = new Bild("sunset.jpg");
        Bild image2 = new Bild("vacation.jpg");
        Bild image3 = new Bild("coffee.jpg");
        Bild image4 = new Bild("mountains.jpg");
        System.out.println("  ✓ Created 4 images");

        // Alice creates a post
        System.out.println("\nAlice creates a post with image and text...");
        Beitrag alicePost1 = alice.erstelleBeitrag("Beautiful Sunset", image1,
            "What an amazing sunset today! The colors were absolutely stunning.");
        System.out.println("  ✓ Post created: '" + alicePost1.getTitel() + "'");
        System.out.println("    - Author: " + alicePost1.getAutor().getBenutzerName());
        System.out.println("    - Images: " + alicePost1.getBilder().size());
        System.out.println("    - Has text: " + (alicePost1.getText() != null ? "Yes" : "No"));

        // Bob creates a post
        System.out.println("\nBob creates a post with just an image...");
        Beitrag bobPost1 = bob.erstelleBeitrag("Vacation Memories", image2);
        System.out.println("  ✓ Post created: '" + bobPost1.getTitel() + "'");
        System.out.println("    - Author: " + bobPost1.getAutor().getBenutzerName());
        System.out.println("    - Images: " + bobPost1.getBilder().size());
        System.out.println("    - Has text: " + (bobPost1.getText() != null ? "Yes" : "No"));

        // Charlie creates multiple posts
        System.out.println("\nCharlie creates two posts...");
        Beitrag charliePost1 = charlie.erstelleBeitrag("Morning Coffee", image3,
            "Starting the day right!");
        Beitrag charliePost2 = charlie.erstelleBeitrag("Mountain Adventure", image4,
            "Just finished an amazing hike!");
        System.out.println("  ✓ Post 1 created: '" + charliePost1.getTitel() + "'");
        System.out.println("  ✓ Post 2 created: '" + charliePost2.getTitel() + "'");

        System.out.println("\nTotal posts on platform: " +
            (alice.getBeitraege().size() + bob.getBeitraege().size() + charlie.getBeitraege().size()));
    }

    /**
     * Demonstrates social interaction features.
     * Shows liking posts and subscribing to users.
     *
     * @param platform the social media platform instance
     */
    private static void demonstrateSocialInteractions(SocialMediaPlatform platform) {
        System.out.println("-".repeat(80));
        System.out.println("5. SOCIAL INTERACTIONS");
        System.out.println("-".repeat(80));

        // Get users
        Nutzer alice = platform.sucheNutzer("alice");
        Nutzer bob = platform.sucheNutzer("bob");
        Nutzer charlie = platform.sucheNutzer("charlie");
        Nutzer diana = platform.sucheNutzer("diana");

        // Get posts
        Beitrag alicePost = alice.getBeitraege().get(0);
        Beitrag bobPost = bob.getBeitraege().get(0);
        Beitrag charliePost1 = charlie.getBeitraege().get(0);

        // Demonstrate liking posts
        System.out.println("\nLiking posts:");
        System.out.println("  Initial likes on Alice's post: " + alicePost.getAnzahlLikes());

        bob.like(alicePost);
        System.out.println("  Bob likes Alice's post");
        System.out.println("  Likes on Alice's post: " + alicePost.getAnzahlLikes());

        charlie.like(alicePost);
        System.out.println("  Charlie likes Alice's post");
        System.out.println("  Likes on Alice's post: " + alicePost.getAnzahlLikes());

        // Try to like own post (should not work)
        System.out.println("\n  Alice attempts to like her own post...");
        int likesBefore = alicePost.getAnzahlLikes();
        alice.like(alicePost);
        int likesAfter = alicePost.getAnzahlLikes();
        if (likesBefore == likesAfter) {
            System.out.println("  ✓ Correctly prevented: Users cannot like their own posts");
        } else {
            System.out.println("  ✗ Error: User was able to like their own post");
        }

        // Demonstrate subscriptions
        System.out.println("\nSubscribing to users:");
        System.out.println("  Alice subscribes to Bob and Charlie...");
        alice.abonnieren(bob);
        alice.abonnieren(charlie);
        System.out.println("  ✓ Alice now follows " + alice.getAbonnierteNutzer().size() + " users");
        System.out.println("  ✓ Bob has " + bob.getAbonnenten().size() + " subscribers");
        System.out.println("  ✓ Charlie has " + charlie.getAbonnenten().size() + " subscribers");

        System.out.println("\n  Diana subscribes to Charlie...");
        diana.abonnieren(charlie);
        System.out.println("  ✓ Diana now follows " + diana.getAbonnierteNutzer().size() + " user");
        System.out.println("  ✓ Charlie now has " + charlie.getAbonnenten().size() + " subscribers");

        // Try to subscribe to oneself (should not work)
        System.out.println("\n  Bob attempts to subscribe to himself...");
        int subsBefore = bob.getAbonnierteNutzer().size();
        bob.abonnieren(bob);
        int subsAfter = bob.getAbonnierteNutzer().size();
        if (subsBefore == subsAfter) {
            System.out.println("  ✓ Correctly prevented: Users cannot subscribe to themselves");
        } else {
            System.out.println("  ✗ Error: User was able to subscribe to themselves");
        }
    }

    /**
     * Demonstrates feed functionality.
     * Shows finding subscribed users with new posts.
     *
     * @param platform the social media platform instance
     */
    private static void demonstrateFeedFunctionality(SocialMediaPlatform platform) {
        System.out.println("-".repeat(80));
        System.out.println("6. FEED FUNCTIONALITY - Finding New Posts");
        System.out.println("-".repeat(80));

        // Get users
        Nutzer alice = platform.sucheNutzer("alice");
        Nutzer bob = platform.sucheNutzer("bob");
        Nutzer charlie = platform.sucheNutzer("charlie");

        System.out.println("\nScenario: Alice checks for new posts from subscribed users");
        System.out.println("  Alice follows: Bob and Charlie");
        System.out.println("  Alice's last activity: " + formatDateTime(alice.getZuletztAktiv()));

        // Simulate some time passing and create new posts
        System.out.println("\n  Simulating activity...");

        // Wait a moment to ensure timestamps differ
        try { Thread.sleep(100); } catch (InterruptedException e) {}

        // Bob creates a new post (after Alice's last activity)
        Bild newImage1 = new Bild("dinner.jpg");
        bob.erstelleBeitrag("Dinner Time", newImage1, "Enjoying a great meal!");
        System.out.println("  ✓ Bob creates a new post: 'Dinner Time'");
        System.out.println("    Posted at: " + formatDateTime(bob.getBeitraege().get(bob.getBeitraege().size() - 1).getGepostet()));

        // Charlie creates a new post (after Alice's last activity)
        Bild newImage2 = new Bild("book.jpg");
        charlie.erstelleBeitrag("Reading Time", newImage2, "Great book!");
        System.out.println("  ✓ Charlie creates a new post: 'Reading Time'");
        System.out.println("    Posted at: " + formatDateTime(charlie.getBeitraege().get(charlie.getBeitraege().size() - 1).getGepostet()));

        // Check for new posts
        System.out.println("\n  Checking for new posts...");
        List<Nutzer> usersWithNewPosts = platform.ermittleAbonnierteNutzerMitNeuenBeitraegen(alice);

        System.out.println("\n  Results:");
        System.out.println("  ✓ Found " + usersWithNewPosts.size() + " subscribed users with new posts:");
        for (int i = 0; i < usersWithNewPosts.size(); i++) {
            Nutzer user = usersWithNewPosts.get(i);
            System.out.println("    - " + user.getBenutzerName());

            // Show their new posts
            List<Beitrag> posts = user.getBeitraege();
            for (int j = 0; j < posts.size(); j++) {
                Beitrag post = posts.get(j);
                if (post.getGepostet().isAfter(alice.getZuletztAktiv())) {
                    System.out.println("      * '" + post.getTitel() + "' (" + post.getAnzahlLikes() + " likes)");
                }
            }
        }

        // Now Alice performs an activity, which updates her last activity time
        System.out.println("\n  Alice views her feed (updating last activity time)...");
        Bild tempImage = new Bild("temp.jpg");
        alice.hinzufuegenBild(tempImage);
        System.out.println("  Alice's new last activity: " + formatDateTime(alice.getZuletztAktiv()));

        // Check again - should find no new posts
        System.out.println("\n  Checking again for new posts...");
        List<Nutzer> usersWithNewPosts2 = platform.ermittleAbonnierteNutzerMitNeuenBeitraegen(alice);
        System.out.println("  ✓ Found " + usersWithNewPosts2.size() + " subscribed users with new posts");
        if (usersWithNewPosts2.size() == 0) {
            System.out.println("    (All posts have been seen)");
        }
    }

    /**
     * Formats a DateTime object for display.
     *
     * @param dt the DateTime object
     * @return formatted string representation
     */
    private static String formatDateTime(DateTime dt) {
        if (dt == null) return "null";
        // The DateTime class should have a proper toString method
        // For now, we'll use a simple representation
        return dt.toString();
    }
}
