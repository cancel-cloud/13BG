package de.lukas.socialMedia.textAufgaben;

import de.lukas.socialMediaGPT.Bild;
import de.lukas.socialMediaGPT.Text;
import de.lukas.socialMediaGPT.List;

public class Beitrag {
    private DateTime geposted;
    private String titel;
    private int anzahlLikes;
    private List<Bild> bilder;
    private Text text;
    private Nutzer autor;


    public Beitrag(Nutzer author, String titel, Bild bild) {
        this.autor = author;
        this.titel = titel;
        this.bilder = new List<Bild>();
        this.bilder.add(bild);
        this.geposted = new DateTime();
        this.anzahlLikes = 0;
        this.text = null;
    }

    public DateTime getGeposted() {
        return geposted;
    }

    public String getTitel() {
        return titel;
    }

    public int getAnzahlLikes() {
        return anzahlLikes;
    }

    public List<Bild> getBilder() {
        return bilder;
    }

    public Text getText() {
        return text;
    }

    public Nutzer getAutor() {
        return autor;
    }

    public void hinzufuegen(Bild bild) {
        bilder.add(bild);
    }

    public void erstelleText(String text) {
        this.text = new Text(text);
    }

    public void like() {
        anzahlLikes++;
    }
}
