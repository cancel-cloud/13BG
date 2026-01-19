# Aufgabe 2.1 - ERM Notation und Beziehungen (4 BE)

## Aufgabenstellung
Benennen Sie die Notationselemente des ERM jeweils an einem Beispiel und beschreiben Sie
die vorhandenen Beziehungen mit ihren Kardinalitäten.

---

## Lösung

### 1. Notationselemente des ERM

#### 1.1 Entitätstyp (Entity)
**Symbol:** Rechteck

**Beispiele aus dem ERM:**
- `Schulform` - Repräsentiert verschiedene Schulformen (BG, SFE, BBE)
- `Schueler` - Repräsentiert alle Schüler
- `Computer` - Repräsentiert alle Computer
- `Wartung` - Repräsentiert Wartungsarbeiten

```
+------------+
| Schulform  |
+------------+
```

---

#### 1.2 Attribut
**Symbol:** Oval (Ellipse), verbunden mit Entitätstyp

**Beispiele aus dem ERM:**

Schueler-Attribute:
- `schid` (unterstrichen = Primärschlüssel)
- `gebDatum`
- `nachname`
- `vorname`

Computer-Attribute:
- `pcid` (unterstrichen = Primärschlüssel)
- `bezeichnung`

Schulform-Attribute:
- `sid` (unterstrichen = Primärschlüssel)
- `bezeichnung`
- `ersterTag`
- `letzterTag`

---

#### 1.3 Primärschlüssel
**Symbol:** Unterstrichenes Attribut

**Beispiele:**
- `schid` bei Schueler
- `sid` bei Schulform
- `pcid` bei Computer
- `wid` bei Wartung

---

#### 1.4 Beziehung (Relationship)
**Symbol:** Raute, verbunden mit beteiligten Entitätstypen

**Beispiele aus dem ERM:**
- `gehoert zu` - zwischen Schulform und Schueler
- `leiht` - zwischen Schueler und Computer
- `erhaelt` - zwischen Computer und Wartung

```
+----------+     +----------+     +---------+
| Schulform|-----| gehoert  |-----| Schueler|
+----------+     |   zu     |     +---------+
                 +----------+
```

---

#### 1.5 Kardinalität
**Symbol:** Zahlenangabe an den Verbindungslinien in [min, max]-Notation

---

### 2. Beziehungen und ihre Kardinalitäten

#### 2.1 Beziehung: Schulform - Schueler ("gehoert zu")
```
Schulform [1,n] ------- gehoert zu ------- [0,m] Schueler
```

**Beschreibung:**
- Eine Schulform hat mindestens einen bis beliebig viele Schüler [1,n]
- Ein Schüler kann zu keiner oder mehreren Schulformen gehören [0,m]
  (mehrere durch Wechsel der Schulform im Laufe der Zeit)

**Kardinalität:** n:m (mit Einschränkungen)

---

#### 2.2 Beziehung: Schueler - Computer ("leiht")
```
Schueler [0,n] ------- leiht ------- [0,m] Computer
          |                |
          +-- von --+     |
          +-- bis --+     |
```

**Beschreibung:**
- Ein Schüler kann keinen oder mehrere Computer ausleihen [0,n]
- Ein Computer kann von keinem oder mehreren Schülern ausgeliehen werden [0,m]
  (nacheinander, nicht gleichzeitig)

**Attribute der Beziehung:**
- `von` - Startdatum der Ausleihe
- `bis` - Enddatum der Ausleihe

**Kardinalität:** n:m (mit Attributen)

---

#### 2.3 Beziehung: Computer - Wartung ("erhaelt")
```
Computer [1,1] ------- erhaelt ------- [0,n] Wartung
```

**Beschreibung:**
- Ein Computer kann keine oder mehrere Wartungen erhalten [0,n]
- Jede Wartung bezieht sich auf genau einen Computer [1,1]

**Kardinalität:** 1:n

---

### 3. Zusammenfassung der Kardinalitäten

| Beziehung | Entität 1 | Kardinalität | Entität 2 | Kardinalität | Typ |
|-----------|-----------|---------------|------------|---------------|-----|
| gehoert zu | Schulform | [1,n] | Schueler | [0,m] | n:m |
| leiht | Schueler | [0,n] | Computer | [0,m] | n:m |
| erhaelt | Computer | [1,1] | Wartung | [0,n] | 1:n |

---

### 4. Bedeutung der [min, max]-Notation

| Notation | Bedeutung |
|----------|-----------|
| [0,1] | Null oder eins (optional, maximal eins) |
| [1,1] | Genau eins (Pflicht, genau eins) |
| [0,n] | Null bis beliebig viele (optional, beliebig viele) |
| [1,n] | Mindestens eins bis beliebig viele (Pflicht, beliebig viele) |
| [0,m] | Null bis beliebig viele (alternative Schreibweise) |

---

### 5. ERM-Diagramm (vereinfacht)

```
+------------+                              +----------+
|  sid (PK)  |                              | schid(PK)|
| bezeichnung|     [1,n]        [0,m]       | gebDatum |
| ersterTag  |-----gehoert zu--------------->| nachname |
| letzterTag |                              | vorname  |
+------------+                              +----------+
  Schulform                                   Schueler
                                                |
                                                | [0,n]
                                                |
                                             leiht
                                             (von,bis)
                                                |
                                                | [0,m]
                                                |
+------------+                              +----------+
|  wid (PK)  |     [0,n]        [1,1]       | pcid(PK) |
| datum      |<-----erhaelt-----------------| bezeich- |
| beschreibung                              |  nung    |
| kosten     |                              +----------+
+------------+                                Computer
   Wartung
```
