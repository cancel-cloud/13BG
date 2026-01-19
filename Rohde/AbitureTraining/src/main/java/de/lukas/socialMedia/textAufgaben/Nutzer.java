package de.lukas.socialMedia.textAufgaben;

import de.lukas.socialMediaGPT.Bild;
import de.lukas.socialMediaGPT.List;

public class Nutzer {
    private String benutzerName;
    private String passwort;
    private String emailAdresse;
    private DateTime zuletztAktiv;

    private List<Nutzer> abonnierteNutzer;
    private List<Nutzer> abonnenten;




    public Nutzer(String benutzerName, String passwort, String emailAdresse) {
        this.benutzerName = benutzerName;
        this.passwort = passwort;
        this.emailAdresse = emailAdresse;
        this.zuletztAktiv = new DateTime();
        this.abonnierteNutzer = new List();
        this.abonnenten = new List();
    }

    public void zuletztAktiv() {
        this.zuletztAktiv = new DateTime();
    }

    public Beitrag erstelleBeitrag(String titel, Bild bild) {
        return new Beitrag(this, titel, bild);
    }

    public Beitrag erstelleBeitrag(String titel, Bild bild, String text) {
        Beitrag tempo = new Beitrag(this, titel, bild);
        tempo.erstelleText(text);
        return tempo;
    }

    public void abbonieren(Nutzer n) {
        zuletztAktiv();
        if (n == null || n == this) {
            return;
        }
        if (!abonnierteNutzer.contains(n)) {
            // Bidirectional relationship: add this user as subscriber to the other user
            if (!n.abonnenten.contains(this)) {
                n.abonnenten.add(this);
            }
        }
    }

    public void like(Beitrag b) {
        zuletztAktiv();
        // A user cannot like their own posts
        if (b != null && b.getAutor() != this) {
            b.like();
        }
    }



}

