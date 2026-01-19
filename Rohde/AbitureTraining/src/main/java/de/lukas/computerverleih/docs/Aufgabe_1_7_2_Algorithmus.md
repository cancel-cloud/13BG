# Aufgabe 1.7.2 - Algorithmus sucheAusComSorNacEnd() (6 BE)

## Aufgabenstellung
Die Methode `sucheAusComSorNacEnd()` der Klasse Verwaltung gibt eine Liste der
momentan ausgeliehenen Computer zurück, sortiert nach Leihende. Die Liste ist aufsteigend
sortiert, d.h. der Computer, dessen voraussichtliches Leihende zuerst kommt, steht am Anfang.

Entwickeln und formulieren Sie den Algorithmus für diese Methode sprachlich.

---

## Lösung - Sprachliche Formulierung des Algorithmus

### Algorithmus: sucheAusComSorNacEnd()

**Ziel:** Liste aller ausgeliehenen Computer zurückgeben, aufsteigend sortiert nach dem voraussichtlichen Leihende.

---

### Schritt 1: Vorbereitung
1. Erstelle eine neue leere Liste `ausgelieheneComputer` für das Ergebnis.

---

### Schritt 2: Ausgeliehene Computer sammeln
2. Durchlaufe alle Computer in der Verwaltung (mit einer Schleife von Index 0 bis Anzahl-1).
3. Für jeden Computer prüfe:
   - Ist der Computer momentan ausgeliehen? (Hat er eine aktive Ausleihe mit `rueckgabe == false`?)
4. Wenn ja: Füge den Computer zur Liste `ausgelieheneComputer` hinzu.
5. Wenn nein: Gehe zum nächsten Computer.
6. Wiederhole bis alle Computer geprüft wurden.

---

### Schritt 3: Liste sortieren (Bubble Sort)
7. Sortiere die Liste `ausgelieheneComputer` aufsteigend nach dem voraussichtlichen Leihende:

   a. Wiederhole folgende Schritte solange, bis die Liste sortiert ist:

   b. Durchlaufe die Liste von Anfang bis zum vorletzten Element:
      - Vergleiche das aktuelle Element mit dem nächsten Element
      - Hole das Leihende des aktuellen Computers (aus seiner aktiven Ausleihe)
      - Hole das Leihende des nächsten Computers (aus seiner aktiven Ausleihe)

   c. Wenn das Leihende des aktuellen Computers NACH dem Leihende des nächsten liegt:
      - Tausche die beiden Computer in der Liste
      - (Der Computer mit dem früheren Leihende rückt nach vorne)

   d. Wenn das Leihende des aktuellen Computers VOR oder GLEICH dem des nächsten liegt:
      - Keine Änderung nötig
      - Gehe zum nächsten Paar

---

### Schritt 4: Ergebnis zurückgeben
8. Gib die sortierte Liste `ausgelieheneComputer` zurück.

---

## Alternativer Algorithmus (mit Selection Sort)

### Schritt 3 Alternative: Sortieren mit Selection Sort

7. Für jede Position i von 0 bis Listengröße - 2:

   a. Setze `minimumIndex = i`

   b. Durchlaufe alle Positionen j von i+1 bis zum Ende der Liste:
      - Vergleiche das Leihende des Computers an Position j mit dem an Position `minimumIndex`
      - Wenn das Leihende an j FRÜHER ist:
        - Setze `minimumIndex = j`

   c. Wenn `minimumIndex != i`:
      - Tausche den Computer an Position i mit dem an Position `minimumIndex`

---

## Pseudocode

```
FUNKTION sucheAusComSorNacEnd() : Liste<Computer>

    // Schritt 1: Leere Ergebnisliste erstellen
    ausgelieheneComputer = neue Liste<Computer>()

    // Schritt 2: Ausgeliehene Computer sammeln
    FÜR i = 0 BIS computer.size() - 1
        comp = computer.get(i)
        WENN comp.istAusgeliehen() DANN
            ausgelieheneComputer.add(comp)
        ENDE WENN
    ENDE FÜR

    // Schritt 3: Bubble Sort nach Leihende (aufsteigend)
    FÜR i = 0 BIS ausgelieheneComputer.size() - 2
        FÜR j = 0 BIS ausgelieheneComputer.size() - 2 - i
            comp1 = ausgelieheneComputer.get(j)
            comp2 = ausgelieheneComputer.get(j + 1)

            ende1 = comp1.getAktuelleAusleihe().getLeihEnde()
            ende2 = comp2.getAktuelleAusleihe().getLeihEnde()

            // Wenn ende1 NACH ende2 liegt -> tauschen
            WENN Datum.differenzInTagen(ende1, ende2) > 0 DANN
                ausgelieheneComputer.set(j, comp2)
                ausgelieheneComputer.set(j + 1, comp1)
            ENDE WENN
        ENDE FÜR
    ENDE FÜR

    // Schritt 4: Ergebnis zurückgeben
    RETURN ausgelieheneComputer

ENDE FUNKTION
```

---

## Beispiel

### Ausgangssituation
| Computer | Leihende |
|----------|----------|
| PC 102 | 15.12.2024 |
| PC 203 | 01.07.2025 |
| PC 108 | 15.11.2024 |

### Nach Sortierung (aufsteigend nach Leihende)
| Computer | Leihende |
|----------|----------|
| PC 108 | 15.11.2024 | (zuerst zurück)
| PC 102 | 15.12.2024 |
| PC 203 | 01.07.2025 | (zuletzt zurück)

---

## Komplexität

- **Zeitkomplexität Sammeln:** O(n) - einmaliges Durchlaufen aller Computer
- **Zeitkomplexität Sortieren (Bubble Sort):** O(n²) - verschachtelte Schleifen
- **Gesamtkomplexität:** O(n²)

Für große Datenmengen wäre ein effizienterer Sortieralgorithmus wie Merge Sort (O(n log n)) besser geeignet.
