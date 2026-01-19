package de.lukas.schuelerGeraetVerwaltung;


import de.lukas.schuelerGeraetVerwaltung.model.Geraet;
import de.lukas.schuelerGeraetVerwaltung.model.Geraetetyp;
import de.lukas.schuelerGeraetVerwaltung.model.Reservierung;
import de.lukas.schuelerGeraetVerwaltung.model.Schueler;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SLZVerwaltung {

    private final List<Geraetetyp> typen;
    private final List<Geraet> geraete;
    private final List<Reservierung> reservierungen;
    private final List<Schueler> schueler;
    private int naechsteReservierungsNr = 1;

    public SLZVerwaltung() {
        this.typen = new ArrayList<>();
        this.geraete = new ArrayList<>();
        this.reservierungen = new ArrayList<>();
        this.schueler = new ArrayList<>();
    }

    public void registriereTyp(Geraetetyp typ) {
        if (!typen.contains(typ)) {
            typen.add(typ);
        }
    }

    public void registriereGeraet(Geraet geraet) {
        if (!geraete.contains(geraet)) {
            geraete.add(geraet);
        }
    }

    public void registriereSchueler(de.lukas.schuelerGeraetVerwaltung.model.Schueler s) {
        if (!schueler.contains(s)) {
            schueler.add(s);
        }
    }

    public boolean storniereReservierung(Reservierung reservierung) {
        if (reservierung == null) {
            return false;
        }
        boolean entfernt = reservierungen.remove(reservierung);
        if (entfernt) {
            reservierung.getGeraet().loescheReservierung(reservierung);
        }
        return entfernt;
    }

    public Reservierung sucheReservierung(int reservierungsNr) {
        for (Reservierung reservierung : reservierungen) {
            if (reservierung.getReservierungsNr() == reservierungsNr) {
                return reservierung;
            }
        }
        return null;
    }

    public Reservierung reservieren(int typNr, de.lukas.schuelerGeraetVerwaltung.model.Schueler s, LocalDate von, LocalDate bis) {
        Objects.requireNonNull(s, "schueler");
        Objects.requireNonNull(von, "von");
        Objects.requireNonNull(bis, "bis");

        if (bis.isBefore(von)) {
            throw new IllegalArgumentException("bis liegt vor von");
        }

        Geraetetyp typ = findeGeraetetyp(typNr);
        if (typ == null) {
            return null;
        }
        Geraet geraet = typ.sucheFreiesGeraet(von, bis);
        if (geraet == null) {
            return null;
        }

        Reservierung reservierung = new Reservierung(naechsteReservierungsNr++, geraet, s, von, bis);
        reservierungen.add(reservierung);
        geraet.hinzufuegenReservierung(reservierung);
        registriereGeraet(geraet);
        registriereSchueler(s);
        return reservierung;
    }

    public Geraet sucheFreiesGeraetVonTyp(int typNr, LocalDate von, LocalDate bis) {
        Geraetetyp typ = findeGeraetetyp(typNr);
        if (typ == null) {
            return null;
        }
        return typ.sucheFreiesGeraet(von, bis);
    }

    public de.lukas.schuelerGeraetVerwaltung.model.Schueler sucheSchueler(int ausweisNr) {
        for (de.lukas.schuelerGeraetVerwaltung.model.Schueler s : schueler) {
            if (s.getAusweisNr() == ausweisNr) {
                return s;
            }
        }
        return null;
    }

    private Geraetetyp findeGeraetetyp(int typNr) {
        for (Geraetetyp typ : typen) {
            if (typ.getGeraetetypNr() == typNr) {
                return typ;
            }
        }
        return null;
    }
}
