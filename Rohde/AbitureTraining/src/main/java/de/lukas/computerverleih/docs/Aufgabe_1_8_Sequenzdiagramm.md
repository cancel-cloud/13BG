# Aufgabe 1.8 - Sequenzdiagramm berechneAusleihtage() (6 BE)

## Aufgabenstellung
Computer, die eine bestimmte Anzahl an Tagen verliehen worden sind, haben eine höhere
Chance auszufallen. Daher soll eine Methode implementiert werden, die für einen übergebenen
Computer die Summe der insgesamt ausgeliehenen Tage ermittelt.

Entwickeln und zeichnen Sie ein Sequenzdiagramm in Material 3 der Methode
`berechneAusleihtage()` der Klasse Verwaltung.

---

## Lösung

### Sequenzdiagramm (textuelle Darstellung)

```
     :Datum        com:Computer       a:Ausleihe      :Verwaltung
        |               |                 |                |
        |               |                 |   berechneAusleihtage(comNr)
        |               |                 |<---------------|
        |               |                 |                |
        |               |  sucheComputerNachNr(comNr)      |
        |               |<---------------------------------|
        |               |                 |                |
        |               |  return com     |                |
        |               |--------------------------------->|
        |               |                 |                |
        |               |  getAusleihen() |                |
        |               |<---------------------------------|
        |               |                 |                |
        |               |  return ausleihen                |
        |               |--------------------------------->|
        |               |                 |                |
        |               |                 |   loop [für jede Ausleihe]
        |               |                 |   +----------------------+
        |               |                 |   |                      |
        |               |                 |   | getAusleihdatum()    |
        |               |                 |<--|                      |
        |               |                 |   |                      |
        |               |                 |   | return start         |
        |               |                 |-->|                      |
        |               |                 |   |                      |
        |               |                 |   | isRueckgabe()        |
        |               |                 |<--|                      |
        |               |                 |   |                      |
        |               |                 |   | return boolean       |
        |               |                 |-->|                      |
        |               |                 |   |                      |
        |               |                 |   | [wenn rueckgabe]     |
        |               |                 |   | getLeihEnde()        |
        |               |                 |<--|                      |
        |               |                 |   |                      |
        |               |                 |   | return ende          |
        |               |                 |-->|                      |
        |               |                 |   |                      |
        |               |                 |   | [sonst: heute]       |
        |  Datum()      |                 |   |                      |
        |<--------------------------------|---|                      |
        |               |                 |   |                      |
        |  return heute |                 |   |                      |
        |-------------------------------->|---|                      |
        |               |                 |   |                      |
        |  differenzInTagen(ende, start)  |   |                      |
        |<--------------------------------|---|                      |
        |               |                 |   |                      |
        |  return tage  |                 |   |                      |
        |-------------------------------->|---|                      |
        |               |                 |   |                      |
        |               |                 |   +----------------------+
        |               |                 |                |
        |               |                 |   return gesamtTage
        |               |                 |--------------->|
        |               |                 |                |
```

---

### Erklärung der Nachrichten

| Nr. | Nachricht | Von | An | Beschreibung |
|-----|-----------|-----|----|--------------|
| 1 | berechneAusleihtage(comNr) | Aufrufer | :Verwaltung | Start der Methode |
| 2 | sucheComputerNachNr(comNr) | :Verwaltung | (intern) | Computer nach Nummer suchen |
| 3 | return com | - | :Verwaltung | Rückgabe des Computers |
| 4 | getAusleihen() | :Verwaltung | com:Computer | Liste der Ausleihen holen |
| 5 | return ausleihen | com:Computer | :Verwaltung | Rückgabe der Ausleihe-Liste |
| 6 | getAusleihdatum() | :Verwaltung | a:Ausleihe | Startdatum holen |
| 7 | isRueckgabe() | :Verwaltung | a:Ausleihe | Prüfen ob zurückgegeben |
| 8 | getLeihEnde() | :Verwaltung | a:Ausleihe | Enddatum holen (wenn zurückgegeben) |
| 9 | Datum() | :Verwaltung | :Datum | Aktuelles Datum erstellen (wenn nicht zurückgegeben) |
| 10 | differenzInTagen(ende, start) | :Verwaltung | :Datum | Tage berechnen |
| 11 | return gesamtTage | :Verwaltung | Aufrufer | Summe zurückgeben |

---

### Legende

- **Rechteck oben:** Objekt (z.B. `:Verwaltung`, `com:Computer`)
- **Gestrichelte Linie nach unten:** Lebenslinie
- **Durchgezogener Pfeil:** Synchroner Methodenaufruf
- **Gestrichelter Pfeil:** Rückgabe
- **Rechteck auf Lebenslinie:** Aktivierung (Methode wird ausgeführt)
- **loop [...]:** Wiederholung (Schleife)
- **alt [...]:** Verzweigung (if/else)

---

### Wichtige Elemente

1. **Schleife (loop):** Die Berechnung wird für JEDE Ausleihe des Computers wiederholt

2. **Verzweigung (alt):**
   - Wenn `isRueckgabe() == true`: Verwende `getLeihEnde()`
   - Sonst: Erstelle neues `Datum()` für aktuelles Datum

3. **Statische Methode:** `Datum.differenzInTagen()` wird als statische Methode aufgerufen

---

### Visualisierung für die Zeichnung

```
+--------+    +------------+    +-----------+    +------------+
| :Datum |    | com:Computer|   | a:Ausleihe|    | :Verwaltung|
+--------+    +------------+    +-----------+    +------------+
    |              |                 |                 |
    |              |                 |    berechneAusleihtage(comNr)
    |              |                 |    <============|
    |              |                 |                 |
    |              |  sucheComputerNachNr             |
    |              |  <-------------------------------|
    |              |  - - - - - - - - - - - - - - - ->|
    |              |                 |                 |
    |              |  getAusleihen() |                 |
    |              |  <-------------------------------|
    |              |  - - - - - - - - - - - - - - - ->|
    |              |                 |                 |
    |              |                 |    +===========+|
    |              |                 |    | loop      ||
    |              |                 |    +===========+|
    |              |  getAusleihdatum|                ||
    |              |  <--------------|                ||
    |              |  - - - - - - - >|                ||
    |              |                 |                ||
    |              |  isRueckgabe()  |                ||
    |              |  <--------------|                ||
    |              |  - - - - - - - >|                ||
    |              |                 |                ||
    |  Datum()     |                 |                ||
    |  <-----------------------------------          ||
    |  - - - - - - - - - - - - - - - - - - >         ||
    |              |                 |                ||
    |  differenzInTagen(ende, start) |                ||
    |  <-----------------------------------          ||
    |  - - - - - - - - - - - - - - - - - - >         ||
    |              |                 |    +===========+|
    |              |                 |                 |
    |              |                 |    return gesamtTage
    |              |                 |    = = = = = = >|
```

---

### Hinweis für die Zeichnung auf Papier

1. Zeichne die 4 Objekte als Rechtecke oben
2. Zeichne die Lebenslinien als gestrichelte Linien nach unten
3. Zeichne die Methodenaufrufe als Pfeile nach links
4. Zeichne die Rückgaben als gestrichelte Pfeile nach rechts
5. Zeichne die Schleife als Rechteck mit "loop" gekennzeichnet
6. Zeichne die Verzweigung innerhalb der Schleife
