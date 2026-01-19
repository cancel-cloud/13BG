# Aufgabe 2.3 - SQL-Abfragen (16 BE)

## Aufgabe 2.3.1 - Wartungen mit HDD oder SSD (3 BE)

### Aufgabenstellung
Die Liste der Wartungen im letzten Jahr, bei denen in Beschreibung 'HDD' oder 'SSD'
enthalten ist, sollen ausgegeben werden.

### Lösung

```sql
SELECT *
FROM Wartung
WHERE (beschreibung LIKE '%HDD%' OR beschreibung LIKE '%SSD%')
  AND datum >= '2024-01-01'
  AND datum <= '2024-12-31';
```

**Erklärung:**
- `LIKE '%HDD%'` sucht nach Einträgen, die "HDD" irgendwo in der Beschreibung enthalten
- `%` ist ein Platzhalter für beliebig viele Zeichen
- `OR` verbindet beide Bedingungen (HDD oder SSD)
- Die Datumsbedingung filtert auf das letzte Jahr

**Alternative mit YEAR-Funktion:**
```sql
SELECT *
FROM Wartung
WHERE (beschreibung LIKE '%HDD%' OR beschreibung LIKE '%SSD%')
  AND YEAR(datum) = 2024;
```

---

## Aufgabe 2.3.2 - CREATE TABLE für Wartung (3 BE)

### Aufgabenstellung
Entwickeln Sie die SQL-Anweisung, mit der die Tabelle Wartung erzeugt wird. Geben Sie
auch die Primär- und Fremdschlüssel Definitionen an. Die Bezeichner der Schlüssel sollen
sinnvoll festgelegt werden. Die Beschreibung soll aus maximal 200 Zeichen bestehen.

### Lösung

```sql
CREATE TABLE Wartung (
    wid INT PRIMARY KEY,
    datum DATE NOT NULL,
    beschreibung VARCHAR(200),
    kosten DECIMAL(10,2),
    pcid INT NOT NULL,

    CONSTRAINT pk_wartung PRIMARY KEY (wid),
    CONSTRAINT fk_wartung_computer
        FOREIGN KEY (pcid) REFERENCES Computer(pcid)
);
```

**Erklärung:**
- `wid INT` - Wartungs-ID als Integer
- `PRIMARY KEY` - Definiert wid als Primärschlüssel
- `beschreibung VARCHAR(200)` - Text mit maximal 200 Zeichen
- `kosten DECIMAL(10,2)` - Dezimalzahl mit 2 Nachkommastellen für Euro-Beträge
- `FOREIGN KEY ... REFERENCES` - Definiert den Fremdschlüssel zu Computer
- `pk_wartung` - Sinnvoller Name für den Primärschlüssel-Constraint
- `fk_wartung_computer` - Sinnvoller Name für den Fremdschlüssel-Constraint

**Vereinfachte Version:**
```sql
CREATE TABLE Wartung (
    wid INT PRIMARY KEY,
    datum DATE NOT NULL,
    beschreibung VARCHAR(200),
    kosten DECIMAL(10,2),
    pcid INT REFERENCES Computer(pcid)
);
```

---

## Aufgabe 2.3.3 - Ausleihen nach Schulform gruppiert (4 BE)

### Aufgabenstellung
Zur Aufteilung der Kosten der Wartungen auf die verschiedenen Schulformen soll die Anzahl
der Ausleihen gruppiert nach Schulform, denen die ausleihenden Schüler zum Zeitpunkt der
Ausleihe angehörten, ausgegeben werden. Die Anzahl soll für das Schuljahr 2023/24 ermittelt
werden. Die Überschrift für die 1. Spalte soll "Anzahl Ausleihen" lauten, danach soll die
Schulform-ID und Schulform-Bezeichnung ausgegeben werden.

Hinweis: Das Schuljahr 2023/24 war vom 01.08.2023 bis 31.07.2024.

### Lösung

```sql
SELECT COUNT(*) AS "Anzahl Ausleihen",
       Schulform.sid,
       Schulform.bezeichnung
FROM Leiht
JOIN Schueler ON Leiht.schid = Schueler.schid
JOIN GehoertZu ON Schueler.schid = GehoertZu.schid
JOIN Schulform ON GehoertZu.sid = Schulform.sid
WHERE Leiht.von >= '2023-08-01'
  AND Leiht.von <= '2024-07-31'
  AND Schulform.ersterTag <= Leiht.von
  AND Schulform.letzterTag >= Leiht.von
GROUP BY Schulform.sid, Schulform.bezeichnung;
```

**Erklärung:**
- `COUNT(*) AS "Anzahl Ausleihen"` - Zählt die Ausleihen und benennt die Spalte
- Mehrere JOINs verbinden die Tabellen:
  - Leiht -> Schueler (über schid)
  - Schueler -> GehoertZu (über schid)
  - GehoertZu -> Schulform (über sid)
- Die WHERE-Klausel filtert:
  - Nur Ausleihen im Schuljahr 2023/24
  - Nur Schulformen, die zum Zeitpunkt der Ausleihe gültig waren
- `GROUP BY` gruppiert nach Schulform

---

## Aufgabe 2.3.4 - Die drei am wenigsten ausgeliehenen Computer (4 BE)

### Aufgabenstellung
Die drei am wenigsten ausgeliehenen Computer sollen aussortiert werden. Dazu soll eine Liste
mit der ID, der Bezeichnung des Computers und der Summe der Tage, die der Computer
insgesamt ausgeliehen worden ist, ausgegeben werden, aufsteigend sortiert nach der Summe der
Ausleihtage.

Hinweis: Die Funktion DATEDIFF('dd', start, ende) gibt die Anzahl der Tage zurück.

### Lösung

```sql
SELECT Computer.pcid,
       Computer.bezeichnung,
       SUM(DATEDIFF('dd', Leiht.von, Leiht.bis)) AS summe_ausleihtage
FROM Computer
JOIN Leiht ON Computer.pcid = Leiht.pcid
GROUP BY Computer.pcid, Computer.bezeichnung
ORDER BY summe_ausleihtage ASC
LIMIT 3;
```

**Alternative mit TOP (SQL Server):**
```sql
SELECT TOP 3 Computer.pcid,
       Computer.bezeichnung,
       SUM(DATEDIFF('dd', Leiht.von, Leiht.bis)) AS summe_ausleihtage
FROM Computer
JOIN Leiht ON Computer.pcid = Leiht.pcid
GROUP BY Computer.pcid, Computer.bezeichnung
ORDER BY summe_ausleihtage ASC;
```

**Erklärung:**
- `DATEDIFF('dd', von, bis)` berechnet die Tage zwischen Start und Ende
- `SUM(...)` summiert alle Ausleihtage pro Computer
- `GROUP BY` gruppiert nach Computer
- `ORDER BY ... ASC` sortiert aufsteigend (wenigste Tage zuerst)
- `LIMIT 3` oder `TOP 3` begrenzt auf die ersten 3

### Mögliches Problem

**Problem:** Es kann mehrere Computer mit gleicher Ausleihdauer geben.

**Beispiel:**
- PC 101: 10 Tage
- PC 102: 15 Tage
- PC 103: 15 Tage (gleich wie PC 102!)
- PC 104: 20 Tage

Mit `LIMIT 3` erhalten wir PC 101, 102, 103 - aber PC 104 hat genauso wenige Tage wie einer der anderen!

**Lösung:** Verwendung von `FETCH FIRST 3 ROWS WITH TIES` (falls vom DBMS unterstützt)

---

## Aufgabe 2.3.5 - LEFT JOIN erklären (2 BE)

### Aufgabenstellung
Erläutern Sie die Funktionsweise von LEFT JOIN. Beschreiben Sie die Ausgabe, die folgende
Abfrage erzeugt:

```sql
SELECT Computer.pcid, bezeichnung, von, bis
FROM Computer
LEFT JOIN Leiht ON Computer.pcid = Leiht.pcid
WHERE von IS NULL
```

### Lösung

#### Funktionsweise von LEFT JOIN

Ein **LEFT JOIN** (auch LEFT OUTER JOIN genannt) verbindet zwei Tabellen so, dass:

1. **Alle Zeilen** der LINKEN Tabelle (Computer) im Ergebnis enthalten sind
2. Zeilen der rechten Tabelle (Leiht) werden hinzugefügt, wenn die JOIN-Bedingung erfüllt ist
3. Wenn es **keine passende Zeile** in der rechten Tabelle gibt, werden die Spalten der rechten Tabelle mit **NULL** gefüllt

**Visualisierung:**
```
LEFT JOIN:
+------------------+
|   Computer       |     Nur passende
|   (ALLE Zeilen)  |<--- Zeilen aus Leiht
+------------------+
```

#### Ausgabe der Abfrage

Die Abfrage gibt alle **Computer zurück, die noch NIE ausgeliehen wurden**.

**Erklärung:**
1. `LEFT JOIN` nimmt alle Computer
2. Für Computer ohne Ausleihe sind `von` und `bis` NULL
3. `WHERE von IS NULL` filtert genau diese Computer heraus

**Beispiel-Ausgabe:**
| pcid | bezeichnung | von | bis |
|------|-------------|-----|-----|
| 105 | Laptop HP | NULL | NULL |
| 109 | PC Lenovo | NULL | NULL |

Diese Computer wurden noch nie ausgeliehen, daher gibt es keinen Eintrag in der Leiht-Tabelle.

**Praktischer Nutzen:**
Man kann so herausfinden, welche Computer "Ladenhüter" sind und nie ausgeliehen werden.
