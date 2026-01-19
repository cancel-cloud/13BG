# Aufgabe 1.2 - Beschreibung der Beziehungen im Klassendiagramm (4 BE)

## Aufgabenstellung
Beschreiben Sie die im Klassendiagramm dargestellten Beziehungen jeweils an einem Beispiel.

---

## Lösung

### 1. Vererbung (Generalisierung/Spezialisierung)

**Darstellung im Diagramm:** Pfeil mit unausgefüllter Dreiecksspitze

**Beispiel:**
```
Person (abstrakt)
    ^
    |
    +-- Schueler
    +-- Lehrkraft
```

**Beschreibung:**
- `Schueler` und `Lehrkraft` erben von der abstrakten Klasse `Person`
- Beide Unterklassen übernehmen die Attribute `nr`, `nachname`, `vorname` und `meineKlassen`
- Beide übernehmen die Methoden `getAktuelleKlasse()` und `hinzufuegenAktuelleKlasse()`
- `Schueler` erweitert um das Attribut `geburtsdatum`
- `Lehrkraft` erweitert um das Attribut `sollstunden`

**Bedeutung:** Jeder Schüler und jede Lehrkraft IST eine Person.

---

### 2. Assoziation mit Kardinalität (1 zu *)

**Darstellung im Diagramm:** Linie mit Kardinalitäten an beiden Enden

**Beispiel 1: Verwaltung - Computer**
```
Verwaltung -------- Computer
    1        -computer     *
```

**Beschreibung:**
- Eine Verwaltung verwaltet beliebig viele Computer (*)
- Jeder Computer gehört zu genau einer Verwaltung (1)
- Das Attribut heißt `-computer` (privat)

**Beispiel 2: Person - Ausleihe (Leiher)**
```
Person -------- Ausleihe
   1    -leiher     *
```

**Beschreibung:**
- Eine Person kann beliebig viele Ausleihen haben (*)
- Jede Ausleihe hat genau einen Leiher (1)

---

### 3. Assoziation mit Kardinalität (1 zu 1)

**Darstellung im Diagramm:** Linie mit 1 an beiden Enden

**Beispiel: Computer - Ausleihe**
```
Computer -------- Ausleihe
    1    -computer    *
```

**Beschreibung:**
- Genauer betrachtet ist dies eine 1:* Beziehung
- Ein Computer kann mehrere Ausleihen haben (nacheinander)
- Aber jede Ausleihe bezieht sich auf genau einen Computer

---

### 4. Bidirektionale Assoziation

**Darstellung im Diagramm:** Linie ohne Pfeile oder mit Pfeilen an beiden Enden

**Beispiel: Person - Klasse (meineKlassen)**
```
Person -------- Klasse
   *  -meineKlassen  *
```

**Beschreibung:**
- Eine Person kann zu mehreren Klassen gehören (*)
- Eine Klasse kann mehrere Personen enthalten (*)
- Dies ist eine n:m Beziehung
- Bei Schülern: alle Klassen, in denen der Schüler war
- Bei Lehrkräften: alle Klassen, wo Klassenleitung

---

### 5. Unidirektionale Assoziation (Navigierbarkeit)

**Darstellung im Diagramm:** Pfeil zeigt nur in eine Richtung

**Beispiel: AusleihInfoServer - Verwaltung**
```
AusleihInfoServer ------> Verwaltung
                 -verwaltung
```

**Beschreibung:**
- Der `AusleihInfoServer` kennt die `Verwaltung` (er hat eine Referenz darauf)
- Die `Verwaltung` kennt den `AusleihInfoServer` NICHT
- Der Server kann `verwaltung.sucheFreieComputer()` aufrufen
- Die Verwaltung kann den Server nicht direkt ansprechen

---

### 6. Aggregation (schwache Komposition)

**Darstellung im Diagramm:** Leere Raute am "Ganzen"

**Beispiel: Verwaltung - Klassen**
```
Verwaltung <>-------- Klasse
           -klassen     *
```

**Beschreibung:**
- Die Verwaltung "hat" Klassen (Aggregation)
- Die Klassen könnten theoretisch auch ohne die Verwaltung existieren
- Wenn die Verwaltung gelöscht wird, bleiben die Klassen-Objekte bestehen

---

### 7. Abhängigkeit (Client-Server)

**Darstellung im Diagramm:** Gestrichelte Linie (im Diagramm als Wolke)

**Beispiel: AusleihInfoServer - AusleihInfoClient**
```
AusleihInfoServer - - - - AusleihInfoClient
       (Wolke)
```

**Beschreibung:**
- Die Wolke symbolisiert eine Netzwerkverbindung
- Server und Client kommunizieren über Sockets
- Dies ist keine direkte Objektbeziehung, sondern eine Kommunikationsbeziehung

---

## Übersicht der Kardinalitäten im Diagramm

| Beziehung | Kardinalität | Bedeutung |
|-----------|--------------|-----------|
| Verwaltung - Computer | 1 : * | Eine Verwaltung, viele Computer |
| Verwaltung - Klasse | 1 : * | Eine Verwaltung, viele Klassen |
| Verwaltung - Person | 1 : * | Eine Verwaltung, viele Personen |
| Person - Klasse | * : * | Viele Personen, viele Klassen |
| Person - Ausleihe | 1 : * | Ein Leiher, viele Ausleihen |
| Computer - Ausleihe | 1 : * | Ein Computer, viele Ausleihen |
| AusleihInfoServer - Verwaltung | * : 1 | Mehrere Server, eine Verwaltung |
