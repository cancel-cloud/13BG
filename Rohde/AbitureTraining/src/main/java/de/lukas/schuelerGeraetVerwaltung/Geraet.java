package de.lukas.schuelerGeraetVerwaltung.model;

import de.lukas.schuelerGeraetVerwaltung.Geraetetyp;
import de.lukas.schuelerGeraetVerwaltung.Reservierung;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Konkretes Gerät eines Gerätetyps.
 */
public class Geraet {

    private final int geraeteNr;
    private boolean einsatzBereit;
    private final Geraetetyp typ;
    private final List<Reservierung> reservierungen;

    public Geraet(de.lukas.schuelerGeraetVerwaltung.model.Geraetetyp typ, int geraeteNr, boolean einsatzBereit) {
        this.typ = Objects.requireNonNull(typ, "typ");
        this.geraeteNr = geraeteNr;
        this.einsatzBereit = einsatzBereit;
        this.reservierungen = new ArrayList<>();
        typ.registriereGeraet(this);
    }

    public int getGeraeteNr() {
        return geraeteNr;
    }

    public boolean isEinsatzBereit() {
        return einsatzBereit;
    }

    public void setEinsatzBereit(boolean einsatzBereit) {
        this.einsatzBereit = einsatzBereit;
    }

    public de.lukas.schuelerGeraetVerwaltung.model.Geraetetyp getTyp() {
        return typ;
    }

    public List<de.lukas.schuelerGeraetVerwaltung.model.Reservierung> getReservierungen() {
        return Collections.unmodifiableList(reservierungen);
    }

    public boolean loescheReservierung(de.lukas.schuelerGeraetVerwaltung.model.Reservierung reservierung) {
        if (reservierung == null) {
            return false;
        }
        return reservierungen.remove(reservierung);
    }

    public void hinzufuegenReservierung(de.lukas.schuelerGeraetVerwaltung.model.Reservierung reservierung) {
        Objects.requireNonNull(reservierung, "reservierung");
        reservierungen.add(reservierung);
    }

    /**
     * Prüft, ob das Gerät einsatzbereit und im angegebenen Zeitraum frei ist.
     */
    public boolean istFrei(LocalDate von, LocalDate bis) {
        if (!einsatzBereit) {
            return false;
        }
        for (de.lukas.schuelerGeraetVerwaltung.model.Reservierung reservierung : reservierungen) {
            if (reservierung.kollidiertMit(von, bis)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Geraet)) {
            return false;
        }
        Geraet geraet = (Geraet) o;
        return geraeteNr == geraet.geraeteNr;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(geraeteNr);
    }
}
