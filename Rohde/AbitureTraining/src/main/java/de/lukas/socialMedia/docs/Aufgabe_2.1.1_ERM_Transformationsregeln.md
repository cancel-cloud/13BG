# Aufgabe 2.1.1: Regeln zur Transformation des ERM in das relationale Modell (4 BE)

## Vier Regeln zur Überführung von Entity-Relationship-Modellen in das relationale Modell

### Regel 1: Transformation von Entitätstypen
**Jeder Entitätstyp wird zu einer Relation (Tabelle).**

- Der Name des Entitätstyps wird zum Relationennamen
- Jedes Attribut des Entitätstyps wird zu einem Attribut (Spalte) der Relation
- Der Schlüssel des Entitätstyps wird zum Primärschlüssel (Primary Key, PK) der Relation
- Zusammengesetzte Attribute werden in ihre atomaren Bestandteile zerlegt

**Beispiel:**
```
Entität: Nutzer (benutzerName, passwort, email, zuletztAktiv)
Schlüssel: benutzerName

wird zu:

Nutzer(benutzerName, passwort, email, zuletztAktiv)
       ^^^^^^^^^^^^^^
       Primärschlüssel
```

### Regel 2: Transformation von 1:N-Beziehungen
**Eine 1:N-Beziehung wird durch Aufnahme des Primärschlüssels der 1-Seite als Fremdschlüssel (Foreign Key) in die Relation der N-Seite umgesetzt.**

- Der Primärschlüssel der "1-Seite" wird als Fremdschlüssel in die Relation der "N-Seite" eingefügt
- Attribute der Beziehung werden ebenfalls in die Relation der N-Seite aufgenommen
- Die Beziehung selbst wird nicht zu einer eigenen Tabelle

**Beispiel:**
```
Nutzer (1) --- istAutor --- (N) Beitrag

wird zu:

Nutzer(benutzerName, passwort, email, zuletztAktiv)
Beitrag(beitragId, titel, erstelltAm, text, benutzerName#)
                                        ^^^^^^^^^^^^^^^
                                        Fremdschlüssel zu Nutzer
```

### Regel 3: Transformation von N:M-Beziehungen
**Eine N:M-Beziehung wird zu einer eigenständigen Relation (Beziehungstabelle).**

- Es entsteht eine neue Relation für die Beziehung
- Der Primärschlüssel dieser Relation setzt sich aus den Primärschlüsseln der beteiligten Entitäten zusammen (zusammengesetzter Schlüssel)
- Beide Primärschlüssel der beteiligten Entitäten werden als Fremdschlüssel in die Beziehungsrelation aufgenommen
- Attribute der Beziehung werden ebenfalls in diese Relation aufgenommen

**Beispiel:**
```
Nutzer (N) --- likes --- (M) Beitrag [zeitstempel]

wird zu:

Nutzer(benutzerName, ...)
Beitrag(beitragId, ...)
Likes(benutzerName#, beitragId#, zeitstempel)
      ^^^^^^^^^^^^^  ^^^^^^^^^^
      FK zu Nutzer   FK zu Beitrag
      \__________________________/
         Zusammengesetzter PK
```

### Regel 4: Transformation von 1:1-Beziehungen
**Eine 1:1-Beziehung kann auf verschiedene Arten umgesetzt werden:**

**Variante A (Standard):** Der Primärschlüssel einer Relation wird als Fremdschlüssel in die andere Relation aufgenommen. Die Wahl, welche Seite den Fremdschlüssel erhält, hängt von den Kardinalitäten ab:
- Bei [0,1] auf einer Seite: FK auf der [0,1]-Seite (kann NULL sein)
- Bei [1,1] auf beiden Seiten: FK kann auf beliebiger Seite sein

**Variante B (Verschmelzung):** Beide Entitäten werden zu einer einzigen Relation zusammengefasst (wenn beide Seiten [1,1] haben).

**Variante C (Beziehungstabelle):** Selten verwendet, aber möglich bei komplexen Beziehungsattributen.

**Beispiel (Variante A):**
```
Person (1) --- hat --- (1) Reisepass [ausstellungsdatum]
[1,1]                      [0,1]

wird zu:

Person(personId, name, ...)
Reisepass(reisepassNr, ausstellungsdatum, personId#)
                                          ^^^^^^^^^
                                          FK zu Person (kann NULL sein bei [0,1])
```

## Zusätzliche Hinweise

### Normalformen
Bei der Transformation sollte auch auf die Einhaltung der Normalformen geachtet werden:
- **1. Normalform (1NF):** Alle Attributwerte sind atomar (keine zusammengesetzten oder mehrwertigen Attribute)
- **2. Normalform (2NF):** 1NF + jedes Nicht-Schlüssel-Attribut ist voll funktional abhängig vom gesamten Primärschlüssel
- **3. Normalform (3NF):** 2NF + keine transitiven Abhängigkeiten zwischen Nicht-Schlüssel-Attributen

### Notation
**Standard-Notation für relationale Modelle:**
```
Relationenname(PK, Attribut1, Attribut2, ..., FK#)

Dabei gilt:
- PK = unterstrichenes Attribut (Primärschlüssel)
- FK# = Fremdschlüssel (mit # gekennzeichnet)
```
