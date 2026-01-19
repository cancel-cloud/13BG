# Aufgabe 1.1 - Grundkonzepte der Objektorientierten Softwareentwicklung (5 BE)

## Aufgabenstellung
Beschreiben Sie die Grundkonzepte der objektorientierten Softwareentwicklung. Es ist dabei
darauf einzugehen, wie die Wirklichkeit in einer objektorientierten Software abgebildet wird
(Stichworte: Klasse, Objekt, Attribut, Methode, Vererbung, Assoziationsarten, Navigierbarkeit).

---

## Lösung

### 1. Klasse
Eine **Klasse** ist ein Bauplan oder eine Schablone für Objekte. Sie beschreibt die Eigenschaften (Attribute) und Verhaltensweisen (Methoden), die alle Objekte dieser Art haben werden.

**Beispiel aus dem Klassendiagramm:**
- Die Klasse `Computer` beschreibt alle Computer im System
- Sie definiert Attribute wie `nr`, `bezeichnung`, `leistungsindex`, `cpu`
- Sie definiert Methoden wie `toString()`

**Abbildung der Wirklichkeit:**
In der Realität gibt es "Arten von Dingen" (z.B. Computer). Die Klasse modelliert diese Art/Kategorie.

---

### 2. Objekt
Ein **Objekt** ist eine konkrete Instanz einer Klasse. Es ist ein tatsächliches "Ding" mit eigenen Werten für die Attribute.

**Beispiel:**
- `pc203` ist ein konkretes Objekt der Klasse `Computer`
- Es hat die Werte: nr=203, bezeichnung="Dell M4500", leistungsindex=7, cpu="2,4 GHz"

**Abbildung der Wirklichkeit:**
In der Realität gibt es konkrete einzelne Computer (PC Nr. 203). Diese werden als Objekte abgebildet.

---

### 3. Attribut
Ein **Attribut** ist eine Eigenschaft einer Klasse. Jedes Objekt hat eigene Werte für diese Attribute.

**Beispiel aus dem Klassendiagramm:**
- `Computer` hat die Attribute: `-nr: int`, `-bezeichnung: String`, `-leistungsindex: int`, `-cpu: String`
- Das `-` zeigt an, dass das Attribut **private** ist (von außen nicht direkt zugreifbar)

**Abbildung der Wirklichkeit:**
In der Realität haben Computer Eigenschaften wie eine Seriennummer, einen Namen, Leistungsdaten etc.

---

### 4. Methode
Eine **Methode** ist eine Funktion/Aktion, die ein Objekt ausführen kann. Sie beschreibt das Verhalten.

**Beispiel aus dem Klassendiagramm:**
- `Computer` hat die Methode `+toString(): String`
- Das `+` zeigt an, dass die Methode **public** ist (von außen aufrufbar)
- `: String` bedeutet, dass die Methode einen String zurückgibt

**Abbildung der Wirklichkeit:**
In der Realität können wir einen Computer "beschreiben" - toString() modelliert diese Aktion.

---

### 5. Vererbung
**Vererbung** ist ein Mechanismus, bei dem eine Klasse (Unterklasse/Kindklasse) die Eigenschaften und Methoden einer anderen Klasse (Oberklasse/Elternklasse) übernimmt und erweitert.

**Beispiel aus dem Klassendiagramm:**
- `Person` ist die Oberklasse (abstrakt)
- `Schueler` und `Lehrkraft` sind Unterklassen
- Beide erben `nr`, `nachname`, `vorname`, `meineKlassen` von `Person`
- `Schueler` fügt `geburtsdatum` hinzu
- `Lehrkraft` fügt `sollstunden` hinzu

**Abbildung der Wirklichkeit:**
Schüler und Lehrkräfte sind beides Personen - sie teilen gemeinsame Eigenschaften (Name), haben aber auch unterschiedliche (Geburtsdatum vs. Sollstunden).

---

### 6. Assoziationsarten

#### 6.1 Einfache Assoziation
Eine Beziehung zwischen zwei Klassen, die anzeigt, dass Objekte einer Klasse Objekte der anderen Klasse "kennen".

**Beispiel:** `Verwaltung` kennt `Computer` (1 zu *)

#### 6.2 Aggregation (Raute leer)
Eine "Hat-ein"-Beziehung, bei der das Teil auch ohne das Ganze existieren kann.

**Beispiel:** Eine `Verwaltung` "hat" `Computer`, aber die Computer können auch ohne die Verwaltung existieren.

#### 6.3 Komposition (Raute gefüllt)
Eine starke "Besteht-aus"-Beziehung, bei der das Teil nicht ohne das Ganze existieren kann.

**Beispiel:** In manchen Diagrammen würde eine `Ausleihe` nicht ohne ihren `Computer` existieren können.

#### 6.4 Kardinalitäten
- `1` = genau ein
- `*` = beliebig viele (0 bis n)
- `0..1` = null oder eins

**Beispiel:** `Person` zu `Ausleihe` ist `1` zu `*` - eine Person kann mehrere Ausleihen haben, aber jede Ausleihe gehört zu genau einer Person.

---

### 7. Navigierbarkeit
Die **Navigierbarkeit** zeigt an, von welcher Klasse aus auf welche andere zugegriffen werden kann. Sie wird durch Pfeile dargestellt.

**Beispiel aus dem Klassendiagramm:**
- `Verwaltung` -> `Computer` (Pfeil zeigt auf Computer)
- Das bedeutet: Von der Verwaltung aus kann man auf die Computer zugreifen
- Der Computer kennt die Verwaltung nicht direkt

**Praktische Bedeutung:**
- Mit Navigierbarkeit: `verwaltung.getComputer().get(0)` funktioniert
- Ohne Navigierbarkeit: `computer.getVerwaltung()` wäre nicht möglich

---

## Zusammenfassung

| Konzept         | Erklärung                    | Beispiel                  |
|-----------------|------------------------------|---------------------------|
| Klasse          | Bauplan für Objekte          | `Computer`                |
| Objekt          | Konkrete Instanz             | PC Nr. 203                |
| Attribut        | Eigenschaft                  | `bezeichnung: String`     |
| Methode         | Verhalten/Aktion             | `toString()`              |
| Vererbung       | Übernahme von Eigenschaften  | `Schueler extends Person` |
| Assoziation     | Beziehung zwischen Klassen   | Verwaltung -- Computer    |
| Navigierbarkeit | Richtung des Zugriffs        | Verwaltung -> Computer    |