package de.lukas.schuelerGeraetVerwaltung.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Repräsentiert einen Gerätetyp inklusive seiner konkreten Geräteinstanzen.
 */
public class Geraetetyp {

    private final int geraetetypNr;
    private String bezeichnung;
    private final List<Geraet> geraete;

    public Geraetetyp(int typNr, String bezeichnung) {
        this.geraetetypNr = typNr;
        this.bezeichnung = Objects.requireNonNull(bezeichnung, "bezeichnung");
        this.geraete = new ArrayList<>();
    }

    public int getGeraetetypNr() {
        return geraetetypNr;
    }

    public String getBezeichnung() {
        return bezeichnung;
    }

    public void setBezeichnung(String bezeichnung) {
        this.bezeichnung = Objects.requireNonNull(bezeichnung, "bezeichnung");
    }

    public List<Geraet> getGeraete() {
        return Collections.unmodifiableList(geraete);
    }

    /**
     * Erfasst ein neues Gerät dieses Typs.
     *
     * @param geraeteNr     eindeutige Gerätenummer
     * @param einsatzBereit aktueller Einsatzstatus
     * @return angelegtes Gerät
     */
    public Geraet erfasseGeraet(int geraeteNr, boolean einsatzBereit) {
        Geraet geraet = new Geraet(this, geraeteNr, einsatzBereit);
        geraete.add(geraet);
        return geraet;
    }

    /**
     * Sucht ein freies, einsatzbereites Gerät in einem Zeitraum.
     *
     * @return Gerät oder {@code null}, wenn keines frei ist.
     */
    public Geraet sucheFreiesGeraet(LocalDate von, LocalDate bis) {
        for (Geraet geraet : geraete) {
            if (geraet.istFrei(von, bis)) {
                return geraet;
            }
        }
        return null;
    }

    void registriereGeraet(Geraet geraet) {
        if (!geraete.contains(geraet)) {
            geraete.add(geraet);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Geraetetyp)) {
            return false;
        }
        Geraetetyp that = (Geraetetyp) o;
        return geraetetypNr == that.geraetetypNr;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(geraetetypNr);
    }
}
