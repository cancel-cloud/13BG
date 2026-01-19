# Aufgabe 2.5 - Erweiterung des ERM (5 BE)

## Aufgabenstellung
Die Datenbank soll um weitere Anforderungen erweitert werden:

1. Ein Schüler kann eine Patin oder einen Paten (Pate) für mehrere Schüler sein, um jenen bei Computerproblemen zu helfen. Ein Schüler darf maximal einen Paten haben.

2. Zu jeder Wartung gehört genau ein Auftrag. Ein Auftrag wird mit einer eindeutigen ID, dem Erstelldatum, Fertigstelldatum und Beschreibung gespeichert. Jeder Auftrag wird von ein bis zwei Schülern aufgenommen.

3. Jede Durchführung einer Arbeit gehört zu genau einer Wartung und hierzu werden eine eindeutige ID, die verwendete Zeit, das Datum der Ausführung, eine Bemerkung und der Typ der Tätigkeit gespeichert. Eine Durchführung wird von genau einem Schüler ausgeführt. Zu einer Wartung kann es mehrere Durchführungen geben.

4. Der Typ der Tätigkeit wird mit einer eindeutigen ID, der Richtzeit und Bezeichnung gespeichert.

---

## Lösung

### Erweitertes ERM (textuelle Darstellung)

```
                                    +-------------+
                                    |   Schueler  |
                                    +-------------+
                                          |
                         +----------------+----------------+
                         |                |                |
                    [0,1]|           [0,n]|           [1,n]|
                         |                |                |
                   +-----+-----+    +-----+-----+    +-----+-----+
                   | ist Pate  |    |  nimmt    |    | führt     |
                   |   für     |    |   auf     |    |  durch    |
                   +-----------+    +-----------+    +-----------+
                         |                |                |
                    [0,n]|           [1,2]|           [1,1]|
                         |                |                |
                    +----+----+     +-----+-----+    +-----+-----+
                    | Schueler|     |  Auftrag  |    | Durch-    |
                    |(betreut)|     +-----------+    | führung   |
                    +---------+     | aid (PK)  |    +-----------+
                                    | erstell-  |    | did (PK)  |
                                    |   datum   |    | zeit      |
                                    | fertig-   |    | datum     |
                                    |   datum   |    | bemerkung |
                                    | beschreib.|    +-----------+
                                    +-----------+          |
                                          |           [1,1]|
                                     [1,1]|                |
                                          |          +-----+-----+
                                    +-----+-----+    | gehört    |
                                    | gehört    |    |    zu     |
                                    |    zu     |    +-----------+
                                    +-----------+          |
                                          |           [0,n]|
                                     [1,1]|                |
                                          |          +-----+-----+
                                    +-----+-----+    | Tätig-    |
                                    |  Wartung  |    | keitstyp  |
                                    +-----------+    +-----------+
                                                     | tid (PK)  |
                                                     | richtzeit |
                                                     | bezeich-  |
                                                     |   nung    |
                                                     +-----------+
```

---

### Detaillierte Beschreibung der Erweiterungen

#### 1. Patenschaft (Schüler - Schüler)

**Beziehung:** `ist Pate für` (rekursive Beziehung)

```
Schueler [0,1] ----- ist Pate für ----- [0,n] Schueler
(Pate)                                   (betreuter Schüler)
```

**Kardinalitäten:**
- Ein Schüler kann Pate für beliebig viele Schüler sein [0,n]
- Ein Schüler hat maximal einen Paten [0,1]

**Erklärung:**
- Rekursive Beziehung: Die Beziehung geht von Schueler zu Schueler
- Rollen: "Pate" und "betreuter Schüler"

---

#### 2. Auftrag (neue Entität)

**Neue Entität:** `Auftrag`

```
+-------------+
|   Auftrag   |
+-------------+
| aid (PK)    |  <- Eindeutige ID
| erstelldatum|  <- Wann erstellt
| fertigdatum |  <- Wann fertig
| beschreibung|  <- Beschreibung
+-------------+
```

**Beziehung zu Wartung:**
```
Wartung [1,1] ----- gehört zu ----- [1,1] Auftrag
```

- Jede Wartung gehört zu genau einem Auftrag [1,1]
- Jeder Auftrag gehört zu genau einer Wartung [1,1]
- (1:1 Beziehung)

**Beziehung zu Schüler:**
```
Schueler [1,n] ----- nimmt auf ----- [1,2] Auftrag
```

- Ein Auftrag wird von 1-2 Schülern aufgenommen [1,2]
- Ein Schüler kann beliebig viele Aufträge aufnehmen [1,n]

---

#### 3. Durchführung (neue Entität)

**Neue Entität:** `Durchführung`

```
+---------------+
| Durchführung  |
+---------------+
| did (PK)      |  <- Eindeutige ID
| zeit          |  <- Verwendete Zeit
| datum         |  <- Datum der Ausführung
| bemerkung     |  <- Anmerkungen
+---------------+
```

**Beziehung zu Wartung:**
```
Wartung [1,1] ----- hat ----- [0,n] Durchführung
```

- Eine Durchführung gehört zu genau einer Wartung [1,1]
- Eine Wartung kann mehrere Durchführungen haben [0,n]

**Beziehung zu Schüler:**
```
Schueler [0,n] ----- führt durch ----- [1,1] Durchführung
```

- Jede Durchführung wird von genau einem Schüler ausgeführt [1,1]
- Ein Schüler kann mehrere Durchführungen machen [0,n]

**Beziehung zu Tätigkeitstyp:**
```
Tätigkeitstyp [0,n] ----- hat ----- [1,1] Durchführung
```

- Jede Durchführung hat genau einen Tätigkeitstyp [1,1]
- Ein Tätigkeitstyp kann bei mehreren Durchführungen vorkommen [0,n]

---

#### 4. Tätigkeitstyp (neue Entität)

**Neue Entität:** `Tätigkeitstyp`

```
+---------------+
| Tätigkeitstyp |
+---------------+
| tid (PK)      |  <- Eindeutige ID
| richtzeit     |  <- Geplante Zeit
| bezeichnung   |  <- Name der Tätigkeit
+---------------+
```

Beispiele für Tätigkeitstypen:
- "HDD einbauen" (Richtzeit: 30 Min)
- "Betriebssystem installieren" (Richtzeit: 60 Min)
- "Diagnose durchführen" (Richtzeit: 15 Min)

---

### Zusammenfassung der Kardinalitäten

| Beziehung | Entität 1 | Kardinalität | Entität 2 | Kardinalität |
|-----------|-----------|---------------|------------|---------------|
| ist Pate für | Schueler (Pate) | [0,n] | Schueler (betreut) | [0,1] |
| gehört zu | Wartung | [1,1] | Auftrag | [1,1] |
| nimmt auf | Schueler | [1,n] | Auftrag | [1,2] |
| hat | Wartung | [0,n] | Durchführung | [1,1] |
| führt durch | Schueler | [0,n] | Durchführung | [1,1] |
| hat | Tätigkeitstyp | [0,n] | Durchführung | [1,1] |

---

### Grafische Darstellung für die Zeichnung

```
                      +----------+
                      | Schueler |
                      +----+-----+
                           |
          +----------------+----------------+----------------+
          |                |                |                |
     [0,1]|           [0,n]|           [1,n]|           [0,n]|
          |                |                |                |
    +-----+-----+    +-----+-----+    +-----+-----+    +-----+-----+
    | ist Pate  |    | führt     |    |  nimmt    |    |   leiht   |
    |   für     |    |  durch    |    |   auf     |    |           |
    +-----------+    +-----------+    +-----------+    +-----------+
          |                |                |                |
     [0,n]|           [1,1]|           [1,2]|           [0,m]|
          |                |                |                |
    +-----+-----+    +-----+-----+    +-----+-----+    +-----+-----+
    | Schueler  |    | Durch-    |    |  Auftrag  |    | Computer  |
    | (betreut) |    | führung   |    +-----------+    +-----------+
    +-----------+    +-----+-----+    | aid       |
                           |          | erstell.  |
                      [1,1]|          | fertig.   |
                           |          | beschr.   |
                     +-----+-----+    +-----+-----+
                     | gehört    |          |
                     |    zu     |     [1,1]|
                     +-----------+          |
                           |          +-----+-----+
                      [0,n]|          | gehört    |
                           |          |    zu     |
                     +-----+-----+    +-----------+
                     | Wartung   |          |
                     +-----------+     [1,1]|
                                            |
                                      +-----+-----+
                     +-----+-----+    | Wartung   |
                     | Tätig-    |    +-----------+
                     | keitstyp  |
                     +-----------+
                     | tid       |
                     | richtzeit |
                     | bezeich.  |
                     +-----------+
```
