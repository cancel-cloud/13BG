package de.slz.model;

import java.time.LocalDate;
import java.util.Objects;

public class Reservierung {

    private final int reservierungsNr;
    private final Geraet geraet;
    private final Schueler schueler;
    private final LocalDate vonDatum;
    private final LocalDate bisDatum;

    public Reservierung(int reservierungsNr, Geraet geraet, Schueler schueler,
                        LocalDate vonDatum, LocalDate bisDatum) {
        if (bisDatum.isBefore(vonDatum)) {
            throw new IllegalArgumentException("bisDatum vor vonDatum");
        }
        this.reservierungsNr = reservierungsNr;
        this.geraet = Objects.requireNonNull(geraet, "geraet");
        this.schueler = Objects.requireNonNull(schueler, "schueler");
        this.vonDatum = Objects.requireNonNull(vonDatum, "vonDatum");
        this.bisDatum = Objects.requireNonNull(bisDatum, "bisDatum");
    }

    public int getReservierungsNr() {
        return reservierungsNr;
    }

    public Geraet getGeraet() {
        return geraet;
    }

    public Schueler getSchueler() {
        return schueler;
    }

    public LocalDate getVonDatum() {
        return vonDatum;
    }

    public LocalDate getBisDatum() {
        return bisDatum;
    }

    boolean kollidiertMit(LocalDate von, LocalDate bis) {
        LocalDate start = Objects.requireNonNull(von, "von");
        LocalDate ende = Objects.requireNonNull(bis, "bis");
        return !(ende.isBefore(vonDatum) || start.isAfter(bisDatum));
    }
}
