package de.slz;

import de.slz.model.Geraet;
import de.slz.model.Geraetetyp;
import de.slz.model.Reservierung;
import de.slz.model.SLZVerwaltung;
import de.slz.model.Schueler;

import java.time.LocalDate;

public final class Main {

    private Main() {
    }

    public static void main(String[] args) {
        SLZVerwaltung verwaltung = new SLZVerwaltung();

        Geraetetyp laptopTyp = new Geraetetyp(223, "Dell Latitude E5500");
        verwaltung.registriereTyp(laptopTyp);
        Geraet laptop1 = laptopTyp.erfasseGeraet(23301, true);
        Geraet laptop2 = laptopTyp.erfasseGeraet(23302, true);
        Geraet laptop3 = laptopTyp.erfasseGeraet(23303, false);
        verwaltung.registriereGeraet(laptop1);
        verwaltung.registriereGeraet(laptop2);
        verwaltung.registriereGeraet(laptop3);

        Schueler kai = new Schueler(45577, "Kai", "Muster");
        Schueler laura = new Schueler(23225, "Laura", "May");
        Schueler lia = new Schueler(89761, "Lia", "Meier");
        verwaltung.registriereSchueler(kai);
        verwaltung.registriereSchueler(laura);
        verwaltung.registriereSchueler(lia);

        Reservierung r1 = verwaltung.reservieren(223, kai,
                LocalDate.of(2021, 6, 7), LocalDate.of(2021, 6, 9));
        Reservierung r2 = verwaltung.reservieren(223, laura,
                LocalDate.of(2021, 6, 15), LocalDate.of(2021, 6, 16));

        System.out.println("Erste Reservierung: " + beschreibe(r1));
        System.out.println("Zweite Reservierung: " + beschreibe(r2));

        Reservierung konflikt = verwaltung.reservieren(223, lia,
                LocalDate.of(2021, 6, 8), LocalDate.of(2021, 6, 10));
        System.out.println("Konflikt-Reservierung: " + beschreibe(konflikt));

        Reservierung freieReservierung = verwaltung.reservieren(223, lia,
                LocalDate.of(2021, 6, 21), LocalDate.of(2021, 6, 23));
        System.out.println("Freie Reservierung: " + beschreibe(freieReservierung));

        Geraet freiesGeraet = verwaltung.sucheFreiesGeraetVonTyp(223,
                LocalDate.of(2021, 6, 10), LocalDate.of(2021, 6, 12));
        System.out.println("Verfügbares Gerät 10.–12.06.2021: "
                + (freiesGeraet != null ? freiesGeraet.getGeraeteNr() : "keins"));
    }

    private static String beschreibe(Reservierung r) {
        if (r == null) {
            return "keine Reservierung möglich";
        }
        return String.format("#%d Gerät %d für %s %s [%s – %s]",
                r.getReservierungsNr(),
                r.getGeraet().getGeraeteNr(),
                r.getSchueler().getVorname(),
                r.getSchueler().getNachname(),
                r.getVonDatum(),
                r.getBisDatum());
    }
}
