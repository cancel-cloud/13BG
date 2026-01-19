# Aufgabe 2.4 - Anomalien und Datenbankdesign (9 BE)

## Aufgabe 2.4.1 - Redundanzen und Anomalien (4 BE)

### Aufgabenstellung
Die Tabelle enthält Redundanzen und Inkonsistenzen. Zeigen Sie diese und beschreiben Sie
mithilfe von Beispielen aus der Tabelle die verschiedenen Arten von Anomalien.

### Lösung

#### Redundanzen in der Tabelle

**Redundanz 1: Verantwortlich für den Einkauf + ID**
- "Karl Müller" mit ID 3102 erscheint in Zeilen 17, 18, 21
- "Peter Doban" mit ID 3103 erscheint in Zeilen 19, 20

**Redundanz 2: Einkaufs-Rechnungs-Nr.**
- Rechnungsnummer 1070 erscheint mehrfach (Zeilen 17, 18)
- Rechnungsnummer 1071 erscheint mehrfach (Zeilen 19, 20)

**Redundanz 3: Preis bei gleichem Artikel**
- "SSD Corsair 120GB" hat dreimal den Preis 14,99 (Zeilen 17, 20, 21)

#### Inkonsistenz

**Inkonsistenz bei Namen:**
- Zeile 17: "Karl Müller" (mit Umlaut)
- Zeile 18: "Karl Mueller" (ohne Umlaut)
- Das ist die GLEICHE Person (ID 3102), aber unterschiedlich geschrieben!

---

### Anomalien

#### 1. Einfüge-Anomalie (Insert Anomaly)

**Problem:** Man kann bestimmte Daten nicht einfügen, ohne andere (eigentlich unabhängige) Daten zu kennen.

**Beispiel aus der Tabelle:**
- Man möchte einen neuen Verantwortlichen "Lisa Schmidt" mit ID 4400 anlegen
- Das geht nur, wenn man gleichzeitig ein Material einträgt
- Ohne Material kann man die Person nicht in die Tabelle einfügen

---

#### 2. Änderungs-Anomalie (Update Anomaly)

**Problem:** Wenn sich ein Wert ändert, muss er an mehreren Stellen geändert werden.

**Beispiel aus der Tabelle:**
- Karl Müller (ID 3102) ändert seinen Namen zu "Karl Müller-Schmidt"
- Man muss ALLE Zeilen ändern, in denen er vorkommt (17, 18, 21)
- Wird eine Zeile vergessen, entstehen inkonsistente Daten

**Tatsächliches Problem in der Tabelle:**
- In Zeile 17: "Karl Müller"
- In Zeile 18: "Karl Mueller"
- Das ist bereits eine Inkonsistenz durch eine Update-Anomalie!

---

#### 3. Lösch-Anomalie (Delete Anomaly)

**Problem:** Beim Löschen von Daten gehen unbeabsichtigt andere Informationen verloren.

**Beispiel aus der Tabelle:**
- Man löscht den Akku für Lenovo X1 (Zeile 22)
- Damit verliert man auch die Information über "Don John" (ID 4327)
- Die Person existiert nicht mehr in der Datenbank!

---

### Zusammenfassung der Anomalien

| Anomalie | Beschreibung | Beispiel |
|----------|--------------|----------|
| Einfüge | Keine Einfügung ohne abhängige Daten | Neuer Mitarbeiter nur mit Material möglich |
| Änderungs | Mehrfache Änderungen nötig | Name von Karl Müller in 3 Zeilen ändern |
| Lösch | Datenverlust bei Löschung | Löschen von Zeile 22 löscht Don John |

---

## Aufgabe 2.4.2 - Mehrfach auftretender Wert 1070 (2 BE)

### Aufgabenstellung
Diskutieren Sie die Funktion des mehrfach auftretenden Werts 1070 bei der EinkaufsRechnungs-Nr.

### Lösung

Der Wert **1070** bei der Einkaufs-Rechnungs-Nr. tritt in den Zeilen 17 und 18 auf.

**Bedeutung:**
- Beide Artikel (SSD Corsair 120GB und SSD Corsair 240GB) wurden auf derselben Rechnung gekauft
- Die Rechnungsnummer 1070 ist die interne Nummer für diesen Einkauf
- Am 01.07.24 wurden beide SSDs zusammen bestellt

**Funktion:**
1. **Gruppierung:** Die Rechnungsnummer gruppiert Artikel, die zusammen eingekauft wurden
2. **Rückverfolgbarkeit:** Man kann alle Artikel einer Rechnung finden
3. **Buchhaltung:** Die Rechnung kann mit der internen Nummer archiviert werden

**Problem:**
- Die Rechnungsnummer ist hier nicht der Primärschlüssel
- Daher ist sie redundant gespeichert
- Besser wäre eine separate Rechnungs-Tabelle mit der Nummer als Primärschlüssel

---

## Aufgabe 2.4.3 - NULL-Werte im Datenbankdesign (3 BE)

### Aufgabenstellung
In der vorliegenden Tabelle sind null-Werte enthalten. Diskutieren Sie unter diesem
Gesichtspunkt das Datenbankdesign. In der Diskussion ist auch auf alternative Tabellendesigns
einzugehen, mit deren Vor- und Nachteilen, um null-Werte zu vermeiden.

### Lösung

#### NULL-Werte in der Tabelle

| Spalte | Zeilen mit NULL | Bedeutung |
|--------|-----------------|-----------|
| Ausgang | 18, 20, 21, 22 | Material wurde noch nicht verwendet |
| Verwendet für Gerät Nr. | 18, 20, 21, 22 | Material wurde noch nicht eingebaut |

#### Probleme mit NULL-Werten

1. **Unklare Bedeutung:**
   - NULL bei "Ausgang" = nie verwendet? Unbekannt? Noch nicht eingetragen?

2. **Abfrageprobleme:**
   - `WHERE ausgang = NULL` funktioniert nicht (muss `IS NULL` sein)
   - Aggregatfunktionen ignorieren NULL-Werte

3. **Speicherplatz:**
   - NULL-Werte benötigen trotzdem Platz für die Null-Markierung

---

#### Alternative 1: Tabellensplitting (Normalisierung)

**Neue Tabellenstruktur:**

```
Material(nr, bezeichnung, preis, eingang, verantwortlich_id, rechnung_nr)

Verwendung(nr, material_nr#, ausgang, geraet_nr#)
```

**Vorteile:**
- Keine NULL-Werte mehr in der Haupttabelle
- Nur verwendetes Material hat einen Eintrag in "Verwendung"
- Klarere Trennung: Einkauf vs. Verwendung

**Nachteile:**
- Mehr Tabellen = komplexere Abfragen
- JOINs nötig für vollständige Informationen

---

#### Alternative 2: Separate Tabellen für Status

**Neue Tabellenstruktur:**

```
MaterialAufLager(nr, bezeichnung, preis, eingang, verantwortlich_id, rechnung_nr)

MaterialVerwendet(nr, bezeichnung, preis, eingang, ausgang, geraet_nr#, verantwortlich_id, rechnung_nr)
```

**Vorteile:**
- Keine NULL-Werte
- Klarer Status: Material ist entweder auf Lager oder verwendet

**Nachteile:**
- Redundanz zwischen den Tabellen
- Wenn Material verwendet wird, muss es von einer Tabelle in die andere verschoben werden
- Historische Daten schwerer nachzuvollziehen

---

#### Alternative 3: Default-Werte statt NULL

**Anpassung:**
- `ausgang` = '9999-12-31' für "noch nicht verwendet"
- `verwendet_fuer_geraet_nr` = 0 für "noch nicht eingebaut"

**Vorteile:**
- Keine NULL-Werte
- Einfache Abfragen möglich

**Nachteile:**
- "Magische Werte" sind fehleranfällig
- Können versehentlich als echte Daten interpretiert werden
- Nicht semantisch korrekt

---

#### Empfehlung

Die beste Lösung ist **Alternative 1 (Tabellensplitting)**, weil:

1. Sie der 3. Normalform entspricht
2. NULL-Werte komplett vermieden werden
3. Die Bedeutung der Daten klar ist
4. Redundanzen reduziert werden

**Normalisierte Struktur:**

```
Verantwortlicher(id, name)
    PK: id

Rechnung(nr, datum)
    PK: nr

Material(nr, bezeichnung, preis, eingang, verantwortlich_id#, rechnung_nr#)
    PK: nr
    FK: verantwortlich_id -> Verantwortlicher(id)
    FK: rechnung_nr -> Rechnung(nr)

Verwendung(material_nr#, ausgang, geraet_nr#)
    PK: material_nr
    FK: material_nr -> Material(nr)
    FK: geraet_nr -> Computer(pcid)
```
