# Aufgabe 2.3: ER-Diagramm für das Werbesystem (8 BE)

## Anforderungen

Die Datenbank soll folgende Anforderungen für Werbekunden umsetzen:

1. **Werbekunde (Advertiser):**
   - Jeder Werbekunde hat einen Namen und eine Adresse
   - Jeder Werbekunde hat einen eindeutigen Schlüssel

2. **Vertrag (Contract):**
   - Jeder Werbekunde schließt einen Vertrag über die Platzierung eines Werbespots ab
   - Jeder Vertrag hat:
     - Eine eindeutige Vertragsnummer
     - Ein Vertragsdatum
     - Eine Laufzeit mit Beginn und Ende
     - Den Preis, der pro Platzierung zu zahlen ist
     - Die Anzahl der Einblendungen des Werbespots pro Tag

3. **Werbespot (Advertisement):**
   - Jeder Werbespot hat eine Bezeichnung und eine eindeutige ID
   - Er kann entweder ein Bild oder ein Video sein
   - Jeder Werbespot kann Gegenstand mehrerer Verträge sein

4. **Bild-Werbespot:**
   - Jedes Bild hat ein Bildformat
   - Eine Auflösung, angegeben durch Höhe und Breite in Pixeln

5. **Video-Werbespot:**
   - Jedes Video hat ein Videoformat
   - Eine Länge

## ER-Diagramm (textuell beschrieben mit ASCII-Notation)

```
                    ┌─────────────────────┐
                    │   Werbekunde        │
                    ├─────────────────────┤
                    │ kundennummer (PK)   │◄──────┐
                    │ name                │       │
                    │ adresse             │       │
                    └─────────────────────┘       │
                             │                    │
                             │                    │
                             │ [1,n]              │
                             │                    │
                    ┌────────▼────────┐           │
                    │   schliesst     │           │
                    └────────┬────────┘           │
                             │ [1,1]              │
                             │                    │
                    ┌────────▼────────────────────────┐
                    │       Vertrag                   │
                    ├─────────────────────────────────┤
                    │ vertragsnummer (PK)             │
                    │ vertragsdatum                   │
                    │ laufzeitBeginn                  │
                    │ laufzeitEnde                    │
                    │ preisProPlatzierung             │
                    │ anzahlEinblendungenProTag       │
                    └─────────────────────────────────┘
                             │
                             │ [1,1]
                             │
                    ┌────────▼────────┐
                    │   ist_fuer      │
                    └────────┬────────┘
                             │ [0,n]
                             │
                    ┌────────▼────────────────────────┐
                    │      Werbespot                  │
                    ├─────────────────────────────────┤
                    │ spotId (PK)                     │
                    │ bezeichnung                     │
                    └─────────────────────────────────┘
                             △
                             │
                ┌────────────┴────────────┐
                │                         │
                │ is-a                    │ is-a
                │                         │
     ┌──────────▼──────────┐   ┌─────────▼──────────┐
     │   Bild              │   │   Video             │
     ├─────────────────────┤   ├────────────────────┤
     │ bildformat          │   │ videoformat         │
     │ hoehe               │   │ laenge              │
     │ breite              │   │                     │
     └─────────────────────┘   └────────────────────┘
```

## ER-Diagramm mit [min, max]-Notation (detailliert)

### Entitäten und Attribute:

**1. Werbekunde**
- **Schlüssel:** kundennummer (eindeutig, PK)
- **Attribute:**
  - name
  - adresse

**2. Vertrag**
- **Schlüssel:** vertragsnummer (eindeutig, PK)
- **Attribute:**
  - vertragsdatum
  - laufzeitBeginn
  - laufzeitEnde
  - preisProPlatzierung
  - anzahlEinblendungenProTag

**3. Werbespot** (Supertyp)
- **Schlüssel:** spotId (eindeutig, PK)
- **Attribute:**
  - bezeichnung

**4. Bild** (Subtyp von Werbespot)
- **Attribute:**
  - bildformat
  - hoehe
  - breite

**5. Video** (Subtyp von Werbespot)
- **Attribute:**
  - videoformat
  - laenge

### Beziehungen mit Kardinalitäten:

**1. schliesst: Werbekunde [1,1] ←→ [1,n] Vertrag**
- Ein Werbekunde kann einen oder mehrere Verträge schließen [1,n]
- Jeder Vertrag wird von genau einem Werbekunden geschlossen [1,1]
- **Bedeutung:** Jeder Werbekunde muss mindestens einen Vertrag haben, kann aber mehrere haben

**2. ist_fuer: Vertrag [1,1] ←→ [0,n] Werbespot**
- Jeder Vertrag bezieht sich auf genau einen Werbespot [1,1]
- Ein Werbespot kann in keinem, einem oder mehreren Verträgen verwendet werden [0,n]
- **Bedeutung:** Werbespots können wiederverwendet werden (z.B. von verschiedenen Kunden oder in verschiedenen Zeiträumen)

**3. Spezialisierung: Werbespot → {Bild, Video}**
- **is-a-Beziehung:** Jeder Werbespot ist entweder ein Bild ODER ein Video
- **Disjunkt:** Ein Werbespot kann nicht gleichzeitig Bild und Video sein
- **Total:** Jeder Werbespot muss entweder ein Bild oder ein Video sein

## Grafische Darstellung (Chen-Notation)

```
     ┌──────────────┐
     │ kundennummer │
     └──────┬───────┘
            │
            │         ┌──────────┐
            │    ┌────┤   name   │
            │    │    └──────────┘
            │    │
     ┌──────▼────▼──┐
     │              │
     │ Werbekunde   │
     │              │
     └──────┬───────┘    ┌──────────┐
            │       └────┤ adresse  │
            │            └──────────┘
            │
            │ [1,n]
            │
     ┌──────▼────────┐
     │   schliesst   │
     └──────┬────────┘
            │ [1,1]
            │
     ┌──────▼───────────────┐    ┌──────────────────┐
     │                      ├────┤ vertragsnummer   │
     │     Vertrag          │    └──────────────────┘
     │                      ├────┤ vertragsdatum    │
     └──────┬───────────────┘    ├──────────────────┤
            │                    │ laufzeitBeginn   │
            │                    ├──────────────────┤
            │ [1,1]              │ laufzeitEnde     │
            │                    ├──────────────────┤
     ┌──────▼────────┐           │ preisProPlatz.   │
     │   ist_fuer    │           ├──────────────────┤
     └──────┬────────┘           │ anzahlEinblend.  │
            │ [0,n]              └──────────────────┘
            │
     ┌──────▼───────────┐
     │                  ├────┬──────────────┐
     │   Werbespot      │    │   spotId     │
     │                  ├────┴──────────────┤
     └──────┬───────────┘    │ bezeichnung  │
            │                └──────────────┘
            │
            │ is-a (Spezialisierung)
            │ disjunkt, total
            │
     ┌──────┴──────┐
     │             │
     │             │
┌────▼────┐   ┌────▼────┐
│         │   │         │
│  Bild   │   │  Video  │
│         │   │         │
└────┬────┘   └────┬────┘
     │             │
     ├─────────┐   ├─────────┐
     │bildform.│   │videform.│
     ├─────────┤   ├─────────┤
     │ hoehe   │   │ laenge  │
     ├─────────┤   └─────────┘
     │ breite  │
     └─────────┘
```

## Semantische Erklärung

### Geschäftslogik:

1. **Werbekunden und Verträge:**
   - Werbekunden (z.B. "Nike", "Coca-Cola") registrieren sich auf der Plattform
   - Sie schließen Verträge ab, um ihre Werbespots zu platzieren
   - Ein Kunde kann mehrere Verträge haben (z.B. für verschiedene Kampagnen)

2. **Verträge und Werbespots:**
   - Jeder Vertrag bezieht sich auf genau einen Werbespot
   - Ein Werbespot kann in mehreren Verträgen verwendet werden:
     - Beispiel: Nike könnte denselben Werbespot in Q1 und Q2 schalten (2 Verträge)
     - Oder: Verschiedene Kunden nutzen denselben Werbespot (unwahrscheinlich, aber möglich)

3. **Werbespots (Spezialisierung):**
   - Werbespots sind abstrakt - sie sind entweder Bilder oder Videos
   - **Bild-Werbespots:** Banner, statische Anzeigen
     - Haben technische Spezifikationen: Format (JPEG, PNG), Auflösung (Breite × Höhe)
   - **Video-Werbespots:** Bewegte Anzeigen
     - Haben technische Spezifikationen: Format (MP4, AVI), Länge (in Sekunden)

### Wichtige Design-Entscheidungen:

1. **Kardinalität Werbekunde - Vertrag [1,n]:**
   - Jeder Werbekunde MUSS mindestens einen Vertrag haben
   - Begründung: Nur registrierte Kunden mit aktivem Vertrag sind relevant

2. **Kardinalität Werbespot - Vertrag [0,n]:**
   - Ein Werbespot kann noch nicht in einem Vertrag verwendet werden (z.B. in Vorbereitung)
   - Ermöglicht Wiederverwendung von Werbespots

3. **Spezialisierung (is-a):**
   - **Disjunkt:** Ein Werbespot ist ENTWEDER Bild ODER Video, nie beides
   - **Total:** Jeder Werbespot MUSS einer der beiden Kategorien angehören

## Transformation in das relationale Modell (Vorschau)

```
Werbekunde(kundennummer, name, adresse)

Vertrag(vertragsnummer, vertragsdatum, laufzeitBeginn, laufzeitEnde,
        preisProPlatzierung, anzahlEinblendungenProTag,
        kundennummer#, spotId#)

Werbespot(spotId, bezeichnung, typ)

Bild(spotId#, bildformat, hoehe, breite)

Video(spotId#, videoformat, laenge)
```

Oder alternativ mit Single-Table-Inheritance:

```
Werbespot(spotId, bezeichnung, typ,
          bildformat, hoehe, breite,
          videoformat, laenge)
          -- wobei entweder Bild- ODER Video-Attribute gesetzt sind
```

## Überprüfung der Anforderungen

✓ Jeder Werbekunde hat Name und Adresse
✓ Jeder Werbekunde hat eindeutigen Schlüssel (kundennummer)
✓ Jeder Vertrag hat alle geforderten Attribute
✓ Jeder Vertrag hat eindeutigen Schlüssel (vertragsnummer)
✓ Jeder Werbespot hat Bezeichnung und ID
✓ Werbespots sind entweder Bild oder Video
✓ Bilder haben Bildformat und Auflösung (Höhe, Breite)
✓ Videos haben Videoformat und Länge
✓ Ein Werbespot kann in mehreren Verträgen verwendet werden
