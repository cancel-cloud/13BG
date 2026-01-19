package de.lukas.socialMedia.textAufgaben;

public class SocialMediaManager {


    public static void main(String[] args) {
        Beitrag lucio131 = new Beitrag(new DateTime(), "Rosenthaler Platz", 0);

        System.out.println(lucio131.getTitel());
        lucio131.like();
        System.out.println(lucio131.getAnzahlLikes());
    }

}
