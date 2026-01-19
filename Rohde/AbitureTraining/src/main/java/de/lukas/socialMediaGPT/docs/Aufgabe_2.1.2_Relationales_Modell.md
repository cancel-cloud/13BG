# Aufgabe 2.1.2: Überführung des ERM in das relationale Modell (5 BE)

## Analyse des gegebenen ERM (Material 5)

### Entitätstypen:
1. **Nutzer** (benutzerName, passwort, email, zuletztAktiv)
   - Schlüssel: benutzerName

2. **Beitrag** (beitragId, erstelltAm, text, titel)
   - Schlüssel: beitragId

3. **Bild** (bildId, dateiname)
   - Schlüssel: bildId

### Beziehungen:
1. **istAutor**: Nutzer [1,1] ←→ [0,n] Beitrag
   - 1:N-Beziehung: Ein Nutzer kann mehrere Beiträge erstellen, jeder Beitrag hat genau einen Autor

2. **likes**: Nutzer [0,n] ←→ [0,m] Beitrag
   - N:M-Beziehung: Ein Nutzer kann mehrere Beiträge liken, ein Beitrag kann von mehreren Nutzern geliked werden
   - Attribut: zeitstempel

3. **enthaelt**: Beitrag [0,m] ←→ [1,n] Bild
   - N:M-Beziehung: Ein Beitrag kann mehrere Bilder enthalten, ein Bild kann in mehreren Beiträgen verwendet werden

## Transformation in das relationale Modell (3. Normalform)

### Relationen:

```
Nutzer(benutzerName, passwort, email, zuletztAktiv)
      ^^^^^^^^^^^^^^
      PK

Beitrag(beitragId, titel, erstelltAm, text, benutzerName#)
        ^^^^^^^^^^                           ^^^^^^^^^^^^^
        PK                                   FK → Nutzer

Bild(bildId, dateiname)
     ^^^^^^
     PK

Likes(benutzerName#, beitragId#, zeitstempel)
      ^^^^^^^^^^^^^  ^^^^^^^^^^
      FK → Nutzer    FK → Beitrag
      \____________________________/
           Zusammengesetzter PK

Enthaelt(beitragId#, bildId#)
         ^^^^^^^^^^  ^^^^^^^
         FK → Beitrag FK → Bild
         \_____________________/
           Zusammengesetzter PK
```

## Detaillierte Erläuterung der Transformation

### 1. Entität Nutzer
**Regel angewendet:** Regel 1 (Entitätstyp → Relation)

```
Nutzer(benutzerName, passwort, email, zuletztAktiv)
```

- **Primärschlüssel:** benutzerName
- **Attribute:** passwort, email, zuletztAktiv
- **3NF geprüft:** ✓ Alle Attribute sind atomar, keine transitiven Abhängigkeiten

### 2. Entität Beitrag mit Beziehung istAutor
**Regeln angewendet:** Regel 1 (Entitätstyp) + Regel 2 (1:N-Beziehung)

```
Beitrag(beitragId, titel, erstelltAm, text, benutzerName#)
```

- **Primärschlüssel:** beitragId
- **Attribute:** titel, erstelltAm, text
- **Fremdschlüssel:** benutzerName# (referenziert Nutzer)
  - Da die Kardinalität auf Beitrag-Seite [1,1] ist, darf benutzerName# nicht NULL sein
  - Jeder Beitrag MUSS einen Autor haben
- **3NF geprüft:** ✓ Keine transitiven Abhängigkeiten

### 3. Entität Bild
**Regel angewendet:** Regel 1 (Entitätstyp → Relation)

```
Bild(bildId, dateiname)
```

- **Primärschlüssel:** bildId
- **Attribute:** dateiname
- **3NF geprüft:** ✓ Einfache Struktur ohne Abhängigkeitsprobleme

### 4. Beziehung likes (N:M)
**Regel angewendet:** Regel 3 (N:M-Beziehung → Beziehungsrelation)

```
Likes(benutzerName#, beitragId#, zeitstempel)
```

- **Primärschlüssel:** (benutzerName, beitragId) - zusammengesetzter Schlüssel
  - Ein Nutzer kann einen bestimmten Beitrag nur einmal liken
- **Fremdschlüssel:**
  - benutzerName# → Nutzer(benutzerName)
  - beitragId# → Beitrag(beitragId)
- **Attribute:** zeitstempel (Zeitpunkt des Likes)
- **3NF geprüft:** ✓ zeitstempel ist funktional abhängig vom gesamten Primärschlüssel

### 5. Beziehung enthaelt (N:M)
**Regel angewendet:** Regel 3 (N:M-Beziehung → Beziehungsrelation)

```
Enthaelt(beitragId#, bildId#)
```

- **Primärschlüssel:** (beitragId, bildId) - zusammengesetzter Schlüssel
  - Ein bestimmtes Bild kann nur einmal in einem bestimmten Beitrag enthalten sein
- **Fremdschlüssel:**
  - beitragId# → Beitrag(beitragId)
  - bildId# → Bild(bildId)
- **Keine weiteren Attribute:** Die Beziehung hat keine zusätzlichen Attribute
- **3NF geprüft:** ✓ Keine Nicht-Schlüssel-Attribute vorhanden

## Zusammenfassung: Alle Relationen

```
1. Nutzer(benutzerName, passwort, email, zuletztAktiv)

2. Beitrag(beitragId, titel, erstelltAm, text, benutzerName#)

3. Bild(bildId, dateiname)

4. Likes(benutzerName#, beitragId#, zeitstempel)

5. Enthaelt(beitragId#, bildId#)
```

## Prüfung der 3. Normalform

### 1. Normalform (1NF):
✓ Alle Attribute sind atomar (keine zusammengesetzten oder mehrwertigen Attribute)

### 2. Normalform (2NF):
✓ Alle Nicht-Schlüssel-Attribute sind voll funktional abhängig vom gesamten Primärschlüssel
- Bei einfachen PKs trivial erfüllt (Nutzer, Beitrag, Bild)
- Bei zusammengesetzten PKs geprüft (Likes, Enthaelt)

### 3. Normalform (3NF):
✓ Keine transitiven Abhängigkeiten zwischen Nicht-Schlüssel-Attributen
- Jedes Nicht-Schlüssel-Attribut hängt direkt vom Primärschlüssel ab
- Keine Attribut-zu-Attribut-Abhängigkeiten außerhalb des Schlüssels

## Kardinalitäten und Constraints

### NOT NULL Constraints:
- `Beitrag.benutzerName#` darf nicht NULL sein (Kardinalität [1,1])
- `Enthaelt.bildId#` darf nicht NULL sein (Kardinalität [1,n] - mindestens ein Bild)

### Weitere Constraints:
- `Likes`: Ein Nutzer kann einen Beitrag nur einmal liken (durch PK erzwungen)
- `Enthaelt`: Ein Bild kann nur einmal in einem Beitrag vorkommen (durch PK erzwungen)
