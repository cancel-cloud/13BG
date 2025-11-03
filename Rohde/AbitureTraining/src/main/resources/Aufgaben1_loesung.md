# Lösung zu Aufgaben 1

## 1.1 Gegenüberstellung von Tabelle und ERM
- Die Tabelle aus Material 1 führt Gerätetyp-, Geräte- und Reservierungsdaten in einer einzigen Struktur zusammen, während das ERM diese Aspekte auf drei Entitätstypen (`Geraetetyp`, `Geraet`, `Schueler`) plus die Beziehung `Reserviert` verteilt.
- Personen- und Gerätedaten wiederholen sich in der Tabelle mehrfach (z.B. Name *Kai Muster*, Typbezeichnung *Dell Latitude E5500*), was laut ERM als eigene Entitäten geführt werden soll. Die Tabelle verletzt damit die angestrebte Redundanzfreiheit.
- Das Attribut `Bezeichnung` gehört laut ERM zum Gerätetyp, erscheint in Material 1 jedoch pro Reservierung. Außerdem fehlen in der Tabelle strukturierte Angaben zur Geräteverfügbarkeit (`einsatzBereit`), die im ERM als Attribut des Geräts vorgesehen ist (z.B. „Dell Latitude E5500 (in Reparatur)“ als Freitext).
- Die Tabelle weist Datenqualitätsprobleme auf (abweichende `TypNr` 233/223 für denselben Typ), die in einem normalisierten Modell durch referenzielle Integrität verhindert würden.

## 1.2 Relationales Modell in 3NF
```
Geraetetyp(typNr, bezeichnung)
Geraet(geraeteNr, einsatzBereit, typNr#)
Schueler(ausweisNr, name)
Reservierung(reservierungsNr, von, bis, geraeteNr#, ausweisNr#)
```
- `Geraetetyp` beherbergt Stammdaten jedes Typs, `typNr` ist der natürliche Schlüssel.
- `Geraet` referenziert genau einen Typ (`typNr#`) und enthält gerätespezifische Informationen wie `einsatzBereit`.
- `Schueler` speichert eindeutige Ausweisnummern. Der Name hängt funktional nur von `ausweisNr` ab.
- `Reservierung` modelliert die m:n-Beziehung zwischen `Geraet` und `Schueler`. Ein künstlicher Primärschlüssel (`reservierungsNr`) erleichtert Updates; alle Nicht-Schlüsselattribute hängen funktional voll vom Schlüssel ab. FKs sichern die referenzielle Integrität, damit besteht 3NF.

## 1.3 SQL-Anweisungen

### 1.3.1 Tabellen anlegen und Beispieldaten einfügen
```sql
CREATE TABLE Schueler (
    ausweisNr INTEGER PRIMARY KEY,
    name      VARCHAR(100) NOT NULL
);

CREATE TABLE Reservierung (
    reservierungsNr INTEGER PRIMARY KEY AUTO_INCREMENT,
    geraeteNr       INTEGER NOT NULL,
    ausweisNr       INTEGER NOT NULL,
    von             DATE    NOT NULL,
    bis             DATE    NOT NULL,
    CONSTRAINT ck_reservierung_von_bis CHECK (bis >= von),
    CONSTRAINT fk_reservierung_geraet FOREIGN KEY (geraeteNr)
        REFERENCES Geraet(geraeteNr),
    CONSTRAINT fk_reservierung_schueler FOREIGN KEY (ausweisNr)
        REFERENCES Schueler(ausweisNr)
);

INSERT INTO Schueler (ausweisNr, name)
VALUES (45577, 'Kai Muster');

INSERT INTO Reservierung (geraeteNr, ausweisNr, von, bis)
VALUES (12301, 45577, DATE '2021-06-07', DATE '2021-06-09');
```
*Hinweis:* Die Tabellen `Geraetetyp` und `Geraet` sind laut Aufgabenstellung bereits vorhanden; in diesen müssen passende Datensätze für `typNr=123` und `geraeteNr=12301` existieren.

### 1.3.2 Freie Geräte für einen Zeitraum ermitteln
```sql
SELECT g.geraeteNr
FROM Geraet AS g
JOIN Geraetetyp AS gt ON gt.typNr = g.typNr
WHERE gt.bezeichnung = 'Dell Latitude E5500'
  AND g.einsatzBereit = TRUE
  AND NOT EXISTS (
        SELECT 1
        FROM Reservierung AS r
        WHERE r.geraeteNr = g.geraeteNr
          AND r.von <= DATE '2021-06-18'
          AND r.bis >= DATE '2021-06-16'
    );
```

### 1.3.3 Gerätetypen mit Geräteanzahl und mittlerer Reservierungsdauer
```sql
SELECT
    gt.typNr,
    gt.bezeichnung,
    COUNT(g.geraeteNr) AS anzahl_geraete,
    AVG(DATEDIFF(r.bis, r.von) + 1) AS mittlere_reservierungsdauer_tage
FROM Geraetetyp AS gt
JOIN Geraet AS g ON g.typNr = gt.typNr
LEFT JOIN Reservierung AS r ON r.geraeteNr = g.geraeteNr
GROUP BY gt.typNr, gt.bezeichnung
ORDER BY mittlere_reservierungsdauer_tage DESC;
```

### 1.3.4 Löschen von `Schueler` mit abhängigen Reservierungen
- Die Fehlermeldung zeigt, dass es Reservierungen mit `ausweisNr = 12345` gibt; der Fremdschlüssel in `Reservierung` verhindert das Löschen des zugehörigen Schülers.
- Vorgehensmöglichkeiten:
  1. Abhängige Reservierungen vor dem Löschen explizit entfernen (`DELETE FROM Reservierung WHERE ausweisNr = 12345;`) oder an einen anderen Schüler übertragen.
  2. Die Fremdschlüsselbeziehung mit `ON DELETE CASCADE` definieren, sodass Reservierungen automatisch mit gelöscht werden (sinnvoll, wenn Reservierungsdaten nach Ausscheiden nicht aufbewahrt werden sollen).
  3. Alternativ könnten Reservierungen auf einen Archivbereich verschoben oder `ausweisNr` auf `NULL` gesetzt werden – letzteres setzt jedoch `ON DELETE SET NULL` und optionale Fremdschlüssel voraus.

## 1.4 ER-Modell für Bibliothekserweiterung
Entitätstypen:
- **Buch** (`isbn`, `titel`)
- **BuchExemplar** (`signatur`, `status`) – referenziert `Buch`
- **Zeitschrift** (`zeitschriftId`, `titel`)
- **Ausgabe** (`ausgabeId`, `erscheinungsjahr`, `heftNr`) – referenziert `Zeitschrift`
- **Artikel** (`artikelId`, `titel`, `inhalt`) – referenziert `Ausgabe`
- **Autor** (`autorId`, `name`)
- **Fachgebiet** (`fachgebietId`, `bezeichnung`)
- **Schueler** (`ausweisNr`, `name`) – bereits im System vorhanden
- **Ausleihe** (`ausleiheId`, `von`, `bis`) – verbindet `BuchExemplar` und `Schueler`

Beziehungen mit [min,max]-Notation (je Richtung):
- `[Buch] --besitzt--> [BuchExemplar]`: Buch `[1,1]`, BuchExemplar `[1,*]`
- `[Zeitschrift] --hat--> [Ausgabe]`: Zeitschrift `[1,1]`, Ausgabe `[1,*]`
- `[Ausgabe] --enthält--> [Artikel]`: Ausgabe `[1,1]`, Artikel `[1,*]`
- `[Buch] --geschrieben_von--> [Autor]`: Buch `[1,*]`, Autor `[1,*]`
- `[Artikel] --geschrieben_von--> [Autor]`: Artikel `[1,*]`, Autor `[1,*]`
- `[Buch] --gehört_zu--> [Fachgebiet]`: Buch `[1,*]`, Fachgebiet `[0,*]`
- `[Artikel] --gehört_zu--> [Fachgebiet]`: Artikel `[1,*]`, Fachgebiet `[0,*]`
- `[Schueler] --leiht--> [Ausleihe] --für--> [BuchExemplar]`: Schueler `[0,*]`, Ausleihe `[1,1]`, BuchExemplar `[0,*]`

Zusatzregeln:
- `BuchExemplar` kann nur im Status „ausleihbar“ in `Ausleihe` verwendet werden; Zeitschriften und Artikel haben keine Ausleihbeziehung, wodurch die Anforderung „Zeitschriften nur vor Ort“ sichergestellt wird.
- Mehrere Exemplare eines Buches werden über `BuchExemplar` mit individueller `signatur` abgebildet; Zeitschriftenausgaben sind eindeutig über (`zeitschriftId`, `erscheinungsjahr`, `heftNr`) identifizierbar, hier als Surrogatschlüssel `ausgabeId` modelliert.
- Die m:n-Beziehungen zu Autoren und Fachgebieten werden im relationalen Modell über Join-Tabellen (`BuchAutor`, `ArtikelAutor`, `BuchFachgebiet`, `ArtikelFachgebiet`) umgesetzt.
