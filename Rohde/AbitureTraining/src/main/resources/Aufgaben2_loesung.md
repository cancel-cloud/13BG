# Lösungen zu Aufgaben 2

## 2.1 UML-Anwendungsfalldiagramm
- Zweck: Ein UML-Anwendungsfalldiagramm beschreibt aus Sicht der Beteiligten (Akteure) die funktionalen Anforderungen an ein System. Es zeigt, welche Leistungen das System bereitstellt und welche externen Rollen sie nutzen.
- Notationselemente im Diagramm aus Material 4: Rechteck repräsentiert das System („SLZ-Verwaltung“), Ovale stehen für Anwendungsfälle (z.B. „Gerät reservieren“), Strichmännchen bzw. rechteckiges Icon für Akteure (z.B. „Aufsicht“), Linien verbinden Akteure mit den Fällen, in denen sie interagieren.
- `<<include>>`: obligatorische Nutzung eines anderen Anwendungsfalls. „Gerät reservieren“ inkludiert „Gerät suchen“, weil jede Reservierung eine erfolgreiche Suche voraussetzt.
- `<<extend>>`: optionale Erweiterung, die bei erfüllter Bedingung eintritt (z.B. „Gerät ausleihen“ erweitert „Gerät reservieren“, sofern bereits eine Reservierung besteht). Die Basissituation bleibt ohne die Erweiterung vollständig funktionsfähig.

## 2.2 Objektorientierte Modellierung

### 2.2.1 UML-Objektdiagramm (Textform)
```
Geraetetyp: DellLatitude
  geraetetypNr = 223
  bezeichnung = "Dell Latitude E5500"
  geraete = {L223-01, L223-02, L223-03}

Geraet L223-01
  geraeteNr = 23301
  einsatzBereit = true
  reservierungen = {R1, R2, R3}

Geraet L223-02
  geraeteNr = 23302
  einsatzBereit = true
  reservierungen = {R4, R5, R6}

Geraet L223-03
  geraeteNr = 23303
  einsatzBereit = false
  reservierungen = {}

Schueler KaiMuster
  ausweisNr = 45577
  vorname = "Kai"
  nachname = "Muster"

Schueler LauraMay
  ausweisNr = 23225
  vorname = "Laura"
  nachname = "May"

Schueler LiaMeier
  ausweisNr = 89761
  vorname = "Lia"
  nachname = "Meier"

Schueler PaulSchnell
  ausweisNr = 87756
  vorname = "Paul"
  nachname = "Schnell"

Schueler MaxOpel
  ausweisNr = 23348
  vorname = "Max"
  nachname = "Opel"

Schueler LunaKurz
  ausweisNr = 33611
  vorname = "Luna"
  nachname = "Kurz"

Reservierung R1
  reservierungsNr = 1
  vonDatum = 26.05.2021
  bisDatum = 28.05.2021
  geraet = L223-01
  schueler = KaiMuster

Reservierung R2
  reservierungsNr = 2
  vonDatum = 15.06.2021
  bisDatum = 16.06.2021
  geraet = L223-01
  schueler = LauraMay

Reservierung R3
  reservierungsNr = 3
  vonDatum = 21.06.2021
  bisDatum = 23.06.2021
  geraet = L223-01
  schueler = LiaMeier

Reservierung R4
  reservierungsNr = 4
  vonDatum = 09.06.2021
  bisDatum = 10.06.2021
  geraet = L223-02
  schueler = PaulSchnell

Reservierung R5
  reservierungsNr = 5
  vonDatum = 15.06.2021
  bisDatum = 17.06.2021
  geraet = L223-02
  schueler = MaxOpel

Reservierung R6
  reservierungsNr = 6
  vonDatum = 21.06.2021
  bisDatum = 22.06.2021
  geraet = L223-02
  schueler = LunaKurz
```

### 2.2.2 Klassenimplementierung (Java)
- `src/main/java/de/slz/model/Geraetetyp.java:1`
- `src/main/java/de/slz/model/Geraet.java:1`

### 2.2.3 Methode `reservieren()` (Java)
Dateihinweis: `src/main/java/de/slz/model/SLZVerwaltung.java:61`.

## 2.3 Client-Server-System

### 2.3.1 Erweiterung Anwendungsfalldiagramm (Textbeschreibung)
- Akteur „Schüler“ interagiert mit den Anwendungsfällen `Authentisieren`, `Gerät reservieren`, `Exemplar ausleihen`, `Exemplar zurückgeben`, `Barcode scannen` und `Bestätigung drucken`.
- Akteur „Aufsicht“ nutzt zusätzlich `Überfällige Ausleihen prüfen` sowie `Schüler benachrichtigen`.
- Include-Beziehungen: `Exemplar ausleihen` und `Exemplar zurückgeben` inkludieren obligatorisch `Barcode scannen`. `Gerät reservieren` inkludiert `Authentisieren`.
- Extend-Beziehungen: `Bestätigung drucken` erweitert optional `Exemplar zurückgeben`. `Schüler benachrichtigen` erweitert den periodisch angestoßenen Anwendungsfall `Überfällige Ausleihen prüfen`, sobald Verstöße gefunden werden.

### 2.3.2 Struktogramm `benachrichtigeSchueler()`
```
+ benachrichtigeSchueler()
  ├─ für jede Ausleihe in ausgelieheneExemplare
  │    ├─ fristEnde = ausleihe.getVon().plusDays(14)
  │    ├─ wenn heute.isAfter(fristEnde)
  │    │    ├─ schueler = ausleihe.getSchueler()
  │    │    ├─ nachricht = baueText(ausleihe)
  │    │    └─ sendeEmail(schueler.getVorname()+"."+schueler.getNachname()+"@slz-bs.eu", nachricht)
  │    └─ EndeWenn
  └─ EndeFür
```

### 2.3.3 Sequenzdiagramm (ASCII)
```
:SLZClient        :SLZServer          :Socket s         :Schueler         :SLZVerwaltung
     |                 |                   |                 |                    |
     |---"ausleihen;SIGN"<-----------------|                 |                    |
     |                 |--readLine()------>|                 |                    |
     |                 |<--"ausleihen;SIGN"|                 |                    |
     |                 |--ermittleSchueler()->-------------->|                    |
     |                 |<-------------------[Schueler]-------|                    |
     |                 |--ausleihen(schueler,SIGN)------------------------------->|
     |                 |<------------------------------[Antworttext]-------------|
     |                 |--write("+OK ...")->|                 |                    |
     |                 |                   |                 |                    |
     |---"Ende"-------->                   |                 |                    |
     |                 |--readLine()------>|                 |                    |
     |                 |<--"Ende"----------|                 |                    |
     |                 |--write("bye")---->|                 |                    |
     |                 |--close()--------->|                 |                    |
     |                 |                   |                 |                    |
```

### 2.3.4 Implementierung `SLZClient`
Dateihinweise: `src/main/java/de/slz/client/SLZClient.java:1` und die Hilfsklasse `src/main/java/de/slz/net/Socket.java:1`.
