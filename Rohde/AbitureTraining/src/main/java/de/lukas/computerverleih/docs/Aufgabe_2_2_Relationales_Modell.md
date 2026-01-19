# Aufgabe 2.2 - Transformation ERM zum Relationalen Modell (6 BE)

## Aufgabenstellung
Überführen Sie das ERM in das Relationale Modell. Beschreiben Sie jede notwendige
Transformationsregel.

Hinweis: Alle Relationen sind in der Schreibweise Relation(PK, Attribut, ..., FK#)
anzugeben.

---

## Lösung

### Transformationsregeln und Anwendung

---

### Regel 1: Entitätstypen werden zu Relationen

**Regel:**
Jeder Entitätstyp wird zu einer eigenen Relation (Tabelle). Der Primärschlüssel
des Entitätstyps wird zum Primärschlüssel der Relation.

**Anwendung:**

#### Relation: Schulform
```
Schulform(sid, bezeichnung, ersterTag, letzterTag)
```
- `sid` ist der Primärschlüssel (unterstrichen)
- Alle Attribute werden übernommen

#### Relation: Schueler
```
Schueler(schid, gebDatum, nachname, vorname)
```
- `schid` ist der Primärschlüssel

#### Relation: Computer
```
Computer(pcid, bezeichnung)
```
- `pcid` ist der Primärschlüssel

#### Relation: Wartung
```
Wartung(wid, datum, beschreibung, kosten, pcid#)
```
- `wid` ist der Primärschlüssel
- `pcid#` ist Fremdschlüssel (siehe Regel 2)

---

### Regel 2: 1:n Beziehungen - Fremdschlüssel auf der n-Seite

**Regel:**
Bei einer 1:n Beziehung wird der Primärschlüssel der 1-Seite als Fremdschlüssel
in die Relation der n-Seite eingefügt.

**Anwendung auf "Computer erhält Wartung":**

```
Computer [1,1] ------- erhaelt ------- [0,n] Wartung
```

- Computer ist auf der 1-Seite
- Wartung ist auf der n-Seite
- Der Primärschlüssel `pcid` von Computer wird als Fremdschlüssel in Wartung eingefügt

**Ergebnis:**
```
Wartung(wid, datum, beschreibung, kosten, pcid#)
```
- `pcid#` verweist auf Computer(pcid)

---

### Regel 3: n:m Beziehungen - Neue Zwischentabelle

**Regel:**
Bei einer n:m Beziehung wird eine neue Relation (Zwischentabelle) erstellt.
Diese enthält die Primärschlüssel beider beteiligten Entitätstypen als
zusammengesetzten Primärschlüssel (oder als Fremdschlüssel mit eigenem PK).
Beziehungsattribute werden ebenfalls in diese Tabelle aufgenommen.

**Anwendung auf "Schueler leiht Computer":**

```
Schueler [0,n] ------- leiht (von, bis) ------- [0,m] Computer
```

- Schueler und Computer stehen in einer n:m Beziehung
- Die Beziehung hat Attribute: `von` und `bis`
- Eine neue Zwischentabelle `Leiht` wird erstellt

**Ergebnis:**
```
Leiht(schid#, pcid#, von, bis)
```
- `schid#` ist Fremdschlüssel zu Schueler(schid)
- `pcid#` ist Fremdschlüssel zu Computer(pcid)
- `(schid, pcid, von)` bilden zusammen den Primärschlüssel
  (Ein Schüler kann denselben PC mehrmals ausleihen, daher brauchen wir `von` im PK)

**Alternative mit eigenem Primärschlüssel:**
```
Leiht(leihid, von, bis, schid#, pcid#)
```

---

**Anwendung auf "Schulform - Schueler (gehoert zu)":**

```
Schulform [1,n] ------- gehoert zu ------- [0,m] Schueler
```

- Auch dies ist eine n:m Beziehung
- Ein Schüler kann zu verschiedenen Zeiten verschiedenen Schulformen angehören

**Ergebnis:**
```
GehoertZu(schid#, sid#)
```
- `schid#` ist Fremdschlüssel zu Schueler(schid)
- `sid#` ist Fremdschlüssel zu Schulform(sid)
- `(schid, sid)` bilden zusammen den Primärschlüssel

---

### Vollständiges Relationales Modell

```
Schulform(sid, bezeichnung, ersterTag, letzterTag)
    PK: sid

Schueler(schid, gebDatum, nachname, vorname)
    PK: schid

Computer(pcid, bezeichnung)
    PK: pcid

Wartung(wid, datum, beschreibung, kosten, pcid#)
    PK: wid
    FK: pcid# -> Computer(pcid)

Leiht(schid#, pcid#, von, bis)
    PK: (schid, pcid, von)
    FK: schid# -> Schueler(schid)
    FK: pcid# -> Computer(pcid)

GehoertZu(schid#, sid#)
    PK: (schid, sid)
    FK: schid# -> Schueler(schid)
    FK: sid# -> Schulform(sid)
```

---

### Zusammenfassung der Transformationsregeln

| Regel | ERM-Element | Relationales Modell |
|-------|-------------|---------------------|
| 1 | Entitätstyp | Relation mit gleichen Attributen |
| 2 | 1:n Beziehung | Fremdschlüssel auf n-Seite |
| 3 | n:m Beziehung | Neue Zwischentabelle |
| 4 | Beziehungsattribute | In Zwischentabelle aufnehmen |
| 5 | Primärschlüssel | Wird Primärschlüssel der Relation |

---

### Notation

- **Unterstrichen:** Primärschlüssel
- **#:** Fremdschlüssel
- **PK:** Primary Key (Primärschlüssel)
- **FK:** Foreign Key (Fremdschlüssel)
