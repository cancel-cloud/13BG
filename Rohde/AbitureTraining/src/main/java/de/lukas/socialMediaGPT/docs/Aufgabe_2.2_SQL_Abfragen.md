# Aufgabe 2.2: SQL-Abfragen für die Social-Media-Datenbank

## Erinnerung: Relationales Modell

```sql
Nutzer(benutzerName, passwort, email, zuletztAktiv)
Beitrag(beitragId, titel, erstelltAm, text, benutzerName#)
Bild(bildId, dateiname)
Likes(benutzerName#, beitragId#, zeitstempel)
Enthaelt(beitragId#, bildId#)
```

---

## Aufgabe 2.2.1: Beiträge von Anna mit Bildern (3 BE)

### Aufgabenstellung
Für die Nutzerin "Anna" sollen alle Titel ihrer Beiträge mit den IDs der enthaltenen Bilder und den Dateinamen der Bilder angezeigt werden.

### SQL-Lösung

```sql
SELECT
    B.titel AS Beitragstitel,
    Bi.bildId AS BildID,
    Bi.dateiname AS Bilddateiname
FROM
    Nutzer N
    JOIN Beitrag B ON N.benutzerName = B.benutzerName
    JOIN Enthaelt E ON B.beitragId = E.beitragId
    JOIN Bild Bi ON E.bildId = Bi.bildId
WHERE
    N.benutzerName = 'Anna'
ORDER BY
    B.titel, Bi.bildId;
```

### Erläuterung

1. **FROM-Klausel mit JOINs:**
   - Verbindet Nutzer → Beitrag (über benutzerName)
   - Verbindet Beitrag → Enthaelt (über beitragId)
   - Verbindet Enthaelt → Bild (über bildId)

2. **WHERE-Klausel:**
   - Filtert nur Beiträge von Anna

3. **SELECT-Klausel:**
   - Zeigt Beitragstitel, Bild-ID und Bilddateinamen

4. **ORDER BY:**
   - Sortiert nach Titel und dann nach Bild-ID für bessere Lesbarkeit

### Beispiel-Ausgabe

| Beitragstitel | BildID | Bilddateiname |
|--------------|--------|---------------|
| Annas Lieblingsessen | 5 | pizza.jpg |
| Annas Lieblingsessen | 7 | pasta.jpg |
| Gerne ein E-Bike! | 12 | ebike.jpg |

---

## Aufgabe 2.2.2: Neuer Nutzer Clark mit seinem ersten Beitrag (4 BE)

### Aufgabenstellung
Der neue Nutzer "Clark" mit Passwort "ert4$alPhawe" und E-Mail "clark@family.com" wird heute hinzugefügt. Er erstellt seinen ersten Beitrag (beitragId 42) und nutzt dafür das Bild mit der ID 3.

### SQL-Lösung

```sql
-- 1. Nutzer Clark hinzufügen
INSERT INTO Nutzer (benutzerName, passwort, email, zuletztAktiv)
VALUES ('Clark', 'ert4$alPhawe', 'clark@family.com', NOW());

-- 2. Clarks ersten Beitrag erstellen
INSERT INTO Beitrag (beitragId, titel, erstelltAm, text, benutzerName)
VALUES (42, 'Best of 2022', NOW(), 'Meine Highlights des Jahres', 'Clark');

-- 3. Bild 3 zum Beitrag 42 hinzufügen
INSERT INTO Enthaelt (beitragId, bildId)
VALUES (42, 3);
```

### Erläuterung

1. **INSERT Nutzer:**
   - NOW() liefert den aktuellen Zeitstempel
   - Alle Pflichtfelder werden ausgefüllt

2. **INSERT Beitrag:**
   - beitragId = 42 (wie angegeben)
   - Titel wurde sinnvoll ergänzt als "Best of 2022" (siehe Tabelle in Aufgabe 2.2.5)
   - Text wurde sinnvoll ergänzt
   - benutzerName = 'Clark' (Fremdschlüssel)

3. **INSERT Enthaelt:**
   - Verknüpft Beitrag 42 mit Bild 3
   - Erfüllt die Anforderung der N:M-Beziehung

### Alternative (mit Transaktion für Atomarität)

```sql
BEGIN TRANSACTION;

INSERT INTO Nutzer (benutzerName, passwort, email, zuletztAktiv)
VALUES ('Clark', 'ert4$alPhawe', 'clark@family.com', NOW());

INSERT INTO Beitrag (beitragId, titel, erstelltAm, text, benutzerName)
VALUES (42, 'Best of 2022', NOW(), 'Meine Highlights des Jahres', 'Clark');

INSERT INTO Enthaelt (beitragId, bildId)
VALUES (42, 3);

COMMIT;
```

---

## Aufgabe 2.2.3: Beliebte Beiträge im Januar 2023 (5 BE)

### Aufgabenstellung
Ermitteln Sie die Titel aller Beiträge mit den Namen der Autoren und der Anzahl der Likes, die im Januar 2023 mehr als 10 Likes bekommen haben, absteigend sortiert nach der Anzahl ihrer Likes.

### SQL-Lösung

```sql
SELECT
    B.titel AS Beitragstitel,
    N.benutzerName AS Autor,
    COUNT(L.beitragId) AS AnzahlLikes
FROM
    Beitrag B
    JOIN Nutzer N ON B.benutzerName = N.benutzerName
    JOIN Likes L ON B.beitragId = L.beitragId
WHERE
    L.zeitstempel >= '2023-01-01 00:00:00'
    AND L.zeitstempel < '2023-02-01 00:00:00'
GROUP BY
    B.beitragId, B.titel, N.benutzerName
HAVING
    COUNT(L.beitragId) > 10
ORDER BY
    AnzahlLikes DESC;
```

### Erläuterung

1. **FROM-Klausel mit JOINs:**
   - Beitrag JOIN Nutzer: Um den Autorennamen zu erhalten
   - Beitrag JOIN Likes: Um die Likes zu zählen

2. **WHERE-Klausel:**
   - Filtert Likes, die im Januar 2023 gegeben wurden
   - >= '2023-01-01' UND < '2023-02-01' (gesamter Januar)

3. **GROUP BY:**
   - Gruppiert nach Beitrag (beitragId, titel, benutzerName)
   - Notwendig für die Aggregatfunktion COUNT()

4. **HAVING:**
   - Filtert nur Gruppen mit mehr als 10 Likes
   - HAVING kommt nach GROUP BY, WHERE kommt vor GROUP BY

5. **ORDER BY:**
   - Sortiert absteigend nach Anzahl der Likes (beliebteste zuerst)

### Beispiel-Ausgabe

| Beitragstitel | Autor | AnzahlLikes |
|--------------|-------|-------------|
| Winterwunderland | MaxMüller2 | 25 |
| Neujahrs-Party | Anna | 18 |
| Skiurlaub 2023 | Benedict | 15 |

---

## Aufgabe 2.2.4: Ungenutzte Bilder (3 BE)

### Aufgabenstellung
Entwickeln Sie eine SQL-Anweisung für die Ausgabe der IDs und Dateinamen aller Bilder, die nicht in Beiträgen verwendet werden.

### SQL-Lösung (mit LEFT JOIN)

```sql
SELECT
    Bi.bildId AS BildID,
    Bi.dateiname AS Dateiname
FROM
    Bild Bi
    LEFT JOIN Enthaelt E ON Bi.bildId = E.bildId
WHERE
    E.bildId IS NULL
ORDER BY
    Bi.bildId;
```

### Alternative Lösung (mit NOT IN)

```sql
SELECT
    bildId AS BildID,
    dateiname AS Dateiname
FROM
    Bild
WHERE
    bildId NOT IN (
        SELECT bildId
        FROM Enthaelt
    )
ORDER BY
    bildId;
```

### Alternative Lösung (mit NOT EXISTS)

```sql
SELECT
    bildId AS BildID,
    dateiname AS Dateiname
FROM
    Bild Bi
WHERE
    NOT EXISTS (
        SELECT 1
        FROM Enthaelt E
        WHERE E.bildId = Bi.bildId
    )
ORDER BY
    bildId;
```

### Erläuterung der LEFT JOIN-Lösung

1. **LEFT JOIN:**
   - Liefert alle Bilder, auch wenn sie keine Einträge in Enthaelt haben
   - Für nicht verwendete Bilder ist E.bildId NULL

2. **WHERE E.bildId IS NULL:**
   - Filtert nur die Bilder, die KEINE Verknüpfung in Enthaelt haben

3. **Vorteil:** Sehr effizient und gut lesbar

### Beispiel-Ausgabe

| BildID | Dateiname |
|--------|-----------|
| 8 | unused_photo.jpg |
| 15 | old_image.png |
| 23 | deleted_post.jpg |

---

## Aufgabe 2.2.5: Nutzer mit ihren Beiträgen (LEFT OUTER JOIN) (4 BE)

### Aufgabenstellung
Im Folgenden ist ein Ausschnitt einer gewünschten Ergebnistabelle gegeben. In der Tabelle sind die Namen aller Nutzer aufgeführt sowie die Titel ihrer Beiträge, sofern die Nutzer Beiträge erstellt haben.

**Gegebene Ergebnistabelle:**

| Benutzername | Beitragstitel | Erstellungsdatum |
|--------------|---------------|------------------|
| Anna | Annas Lieblingsessen | 01.01.2023 12:00 |
| Anna | Gerne ein E-Bike! | 01.01.2023 14:00 |
| MaxMüller2 | happy-end | 03.01.2023 01:00 |
| Benedict | | |
| Amelie | | |
| Clark | Best of 2022 | 20.01.2023 10:00 |
| MaxMüller3 | | |
| Don Eric | Porsche 911 | 23.01.2023 02:00 |
| Martin | | |

**Gegebene (fehlerhafte) SQL-Anweisung:**

```sql
SELECT benutzerName AS Benutzername, titel AS Beitragstitel,
       erstelltAm AS Erstellungsdatum
FROM Nutzer N, Beitrag B;
```

### Erörterung der gegebenen SQL-Anweisung

**Probleme:**

1. **Kartesisches Produkt:**
   - Die Anweisung verwendet eine veraltete JOIN-Syntax ohne JOIN-Bedingung
   - `FROM Nutzer N, Beitrag B` ohne WHERE erzeugt ein Kartesisches Produkt
   - Jeder Nutzer wird mit jedem Beitrag kombiniert
   - Bei 9 Nutzern und 5 Beiträgen: 9 × 5 = 45 Zeilen (statt der gewünschten 9)

2. **Fehlende JOIN-Bedingung:**
   - Es fehlt die Verknüpfung über `N.benutzerName = B.benutzerName`

3. **Fehlender OUTER JOIN:**
   - Die Anforderung ist, ALLE Nutzer anzuzeigen, auch die ohne Beiträge
   - Ein normaler (INNER) JOIN würde Nutzer ohne Beiträge ausschließen
   - Notwendig ist ein LEFT OUTER JOIN

### Verbesserte SQL-Anweisung

```sql
SELECT
    N.benutzerName AS Benutzername,
    B.titel AS Beitragstitel,
    DATE_FORMAT(B.erstelltAm, '%d.%m.%Y %H:%i') AS Erstellungsdatum
FROM
    Nutzer N
    LEFT OUTER JOIN Beitrag B ON N.benutzerName = B.benutzerName
ORDER BY
    N.benutzerName, B.erstelltAm;
```

### Erläuterung der verbesserten Lösung

1. **LEFT OUTER JOIN:**
   - Liefert ALLE Nutzer aus der linken Tabelle (Nutzer)
   - Auch wenn sie keine passenden Einträge in der rechten Tabelle (Beitrag) haben
   - Für Nutzer ohne Beiträge werden die Beitrag-Spalten mit NULL gefüllt

2. **ON-Bedingung:**
   - `N.benutzerName = B.benutzerName` verknüpft die Tabellen korrekt

3. **DATE_FORMAT:**
   - Formatiert das Datum im gewünschten Format "dd.mm.yyyy hh:mm"
   - Anpassung je nach Datenbanksystem (MySQL-Syntax gezeigt)

4. **ORDER BY:**
   - Sortiert zuerst nach Benutzername, dann nach Erstellungsdatum
   - Zeigt die Beiträge eines Nutzers chronologisch

### Alternative ohne DATE_FORMAT (wenn nicht unterstützt)

```sql
SELECT
    N.benutzerName AS Benutzername,
    B.titel AS Beitragstitel,
    B.erstelltAm AS Erstellungsdatum
FROM
    Nutzer N
    LEFT OUTER JOIN Beitrag B ON N.benutzerName = B.benutzerName
ORDER BY
    N.benutzerName, B.erstelltAm;
```

### Vergleich: Original vs. Korrigierte Abfrage

| Aspekt | Originale Abfrage | Korrigierte Abfrage |
|--------|-------------------|---------------------|
| JOIN-Typ | Kartesisches Produkt | LEFT OUTER JOIN |
| JOIN-Bedingung | Fehlt | N.benutzerName = B.benutzerName |
| Nutzer ohne Beiträge | Nicht angezeigt | Werden angezeigt (mit NULL) |
| Ergebniszeilen | n × m (viel zu viele) | n (Anzahl Nutzer + Mehrfachbeiträge) |
| Korrektheit | ✗ Falsch | ✓ Korrekt |

---

## Zusammenfassung der SQL-Techniken

### Verwendete JOIN-Typen:

1. **INNER JOIN** (2.2.1, 2.2.3):
   - Nur Datensätze, die in beiden Tabellen existieren

2. **LEFT OUTER JOIN** (2.2.4, 2.2.5):
   - Alle Datensätze der linken Tabelle, auch ohne Match

### Verwendete Klauseln:

1. **GROUP BY + HAVING** (2.2.3):
   - Gruppierung für Aggregatfunktionen
   - HAVING für Filterung nach Aggregation

2. **ORDER BY**:
   - Sortierung der Ergebnisse

3. **WHERE vs. HAVING**:
   - WHERE: Filtert vor der Gruppierung
   - HAVING: Filtert nach der Gruppierung

### Verwendete Funktionen:

1. **COUNT()**: Zählt Datensätze
2. **NOW()**: Aktueller Zeitstempel
3. **DATE_FORMAT()**: Formatiert Datumsangaben

---

## SQL-Schema zum Nachvollziehen der Abfragen

```sql
-- Erstellen der Tabellen (zur Referenz)

CREATE TABLE Nutzer (
    benutzerName VARCHAR(50) PRIMARY KEY,
    passwort VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    zuletztAktiv DATETIME NOT NULL
);

CREATE TABLE Beitrag (
    beitragId INT PRIMARY KEY,
    titel VARCHAR(200) NOT NULL,
    erstelltAm DATETIME NOT NULL,
    text TEXT,
    benutzerName VARCHAR(50) NOT NULL,
    FOREIGN KEY (benutzerName) REFERENCES Nutzer(benutzerName)
);

CREATE TABLE Bild (
    bildId INT PRIMARY KEY,
    dateiname VARCHAR(255) NOT NULL
);

CREATE TABLE Likes (
    benutzerName VARCHAR(50),
    beitragId INT,
    zeitstempel DATETIME NOT NULL,
    PRIMARY KEY (benutzerName, beitragId),
    FOREIGN KEY (benutzerName) REFERENCES Nutzer(benutzerName),
    FOREIGN KEY (beitragId) REFERENCES Beitrag(beitragId)
);

CREATE TABLE Enthaelt (
    beitragId INT,
    bildId INT,
    PRIMARY KEY (beitragId, bildId),
    FOREIGN KEY (beitragId) REFERENCES Beitrag(beitragId),
    FOREIGN KEY (bildId) REFERENCES Bild(bildId)
);
```
