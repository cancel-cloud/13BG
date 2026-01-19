package de.lukas.schuelerGeraetVerwaltung;

import de.lukas.schuelerGeraetVerwaltung.model.Geraet;
import de.lukas.schuelerGeraetVerwaltung.model.Geraetetyp;

import java.time.LocalDate;

public final class Main {

    private Main() {
    }

    public static void main(String[] args) {
        de.lukas.schuelerGeraetVerwaltung.SLZVerwaltung verwaltung = new de.lukas.schuelerGeraetVerwaltung.SLZVerwaltung();

        Geraetetyp laptopTyp = new Geraetetyp(223, "Dell Latitude E5500");
        verwaltung.registriereTyp(laptopTyp);
        Geraet laptop1 = laptopTyp.erfasseGeraet(23301, true);
        Geraet laptop2 = laptopTyp.erfasseGeraet(23302, true);
        Geraet laptop3 = laptopTyp.erfasseGeraet(23303, false);
        verwaltung.registriereGeraet(laptop1);
        verwaltung.registriereGeraet(laptop2);
        verwaltung.registriereGeraet(laptop3);

        de.lukas.schuelerGeraetVerwaltung.Schueler kai = new de.lukas.schuelerGeraetVerwaltung.Schueler(45577, "Kai", "Muster");
        de.lukas.schuelerGeraetVerwaltung.Schueler laura = new de.lukas.schuelerGeraetVerwaltung.Schueler(23225, "Laura", "May");
        de.lukas.schuelerGeraetVerwaltung.Schueler lia = new de.lukas.schuelerGeraetVerwaltung.Schueler(89761, "Lia", "Meier");
        verwaltung.registriereSchueler(kai);
        verwaltung.registriereSchueler(laura);
        verwaltung.registriereSchueler(lia);

        de.lukas.schuelerGeraetVerwaltung.Reservierung r1 = verwaltung.reservieren(223, kai,
                LocalDate.of(2021, 6, 7), LocalDate.of(2021, 6, 9));
        de.lukas.schuelerGeraetVerwaltung.Reservierung r2 = verwaltung.reservieren(223, laura,
                LocalDate.of(2021, 6, 15), LocalDate.of(2021, 6, 16));

        System.out.println("Erste Reservierung: " + beschreibe(r1));
        System.out.println("Zweite Reservierung: " + beschreibe(r2));

        de.lukas.schuelerGeraetVerwaltung.Reservierung konflikt = verwaltung.reservieren(223, lia,
                LocalDate.of(2021, 6, 8), LocalDate.of(2021, 6, 10));
        System.out.println("Konflikt-Reservierung: " + beschreibe(konflikt));

        de.lukas.schuelerGeraetVerwaltung.Reservierung freieReservierung = verwaltung.reservieren(223, lia,
                LocalDate.of(2021, 6, 21), LocalDate.of(2021, 6, 23));
        System.out.println("Freie Reservierung: " + beschreibe(freieReservierung));

        Geraet freiesGeraet = verwaltung.sucheFreiesGeraetVonTyp(223,
                LocalDate.of(2021, 6, 10), LocalDate.of(2021, 6, 12));
        System.out.println("Verfügbares Gerät 10.–12.06.2021: "
                + (freiesGeraet != null ? freiesGeraet.getGeraeteNr() : "keins"));
    }

    private static String beschreibe(de.lukas.schuelerGeraetVerwaltung.Reservierung r) {
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
