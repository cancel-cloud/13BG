package de.slz.model;

import java.util.Objects;

public class Schueler {

    private final int ausweisNr;
    private final String vorname;
    private final String nachname;

    public Schueler(int ausweisNr, String vorname, String nachname) {
        this.ausweisNr = ausweisNr;
        this.vorname = Objects.requireNonNull(vorname, "vorname");
        this.nachname = Objects.requireNonNull(nachname, "nachname");
    }

    public int getAusweisNr() {
        return ausweisNr;
    }

    public String getVorname() {
        return vorname;
    }

    public String getNachname() {
        return nachname;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Schueler)) {
            return false;
        }
        Schueler schueler = (Schueler) o;
        return ausweisNr == schueler.ausweisNr;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(ausweisNr);
    }
}
