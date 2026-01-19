# Aufgabe 2.1.3: Referentielle Integrität (4 BE)

## Erklärung der referentiellen Integrität im Allgemeinen

### Definition
**Referentielle Integrität** (auch Beziehungsintegrität genannt) ist eine Integritätsbedingung in relationalen Datenbanken, die sicherstellt, dass Beziehungen zwischen Tabellen konsistent bleiben.

### Grundprinzip
Wenn ein Fremdschlüssel (Foreign Key) in einer Tabelle auf einen Primärschlüssel (Primary Key) in einer anderen Tabelle verweist, dann muss dieser Wert entweder:
- **existieren** in der referenzierten Tabelle, oder
- **NULL** sein (falls erlaubt)

### Wichtige Regeln

1. **Einfügen (INSERT):**
   - Ein Datensatz mit einem Fremdschlüsselwert kann nur eingefügt werden, wenn der referenzierte Primärschlüsselwert in der Elterntabelle existiert
   - Ausnahme: NULL-Werte sind erlaubt, falls die Spalte als nullable definiert ist

2. **Aktualisieren (UPDATE):**
   - Ein Fremdschlüsselwert kann nur auf einen Wert geändert werden, der in der Elterntabelle existiert
   - Eine Änderung des Primärschlüssels in der Elterntabelle betrifft alle referenzierenden Kindtabellen

3. **Löschen (DELETE):**
   - Ein Datensatz in der Elterntabelle kann nicht gelöscht werden, solange noch Datensätze in Kindtabellen auf ihn verweisen
   - Alternativ können CASCADE-Regeln definiert werden

### Strategien bei Verletzung der referentiellen Integrität

1. **RESTRICT / NO ACTION:**
   - Verhindert die Operation (DELETE oder UPDATE in Elterntabelle)
   - Standard-Verhalten

2. **CASCADE:**
   - DELETE CASCADE: Löscht automatisch alle abhängigen Datensätze
   - UPDATE CASCADE: Aktualisiert automatisch alle Fremdschlüssel in Kindtabellen

3. **SET NULL:**
   - Setzt Fremdschlüssel in Kindtabellen auf NULL

4. **SET DEFAULT:**
   - Setzt Fremdschlüssel auf einen Standardwert

## Referentielle Integrität am Beispiel des relationalen Modells (Aufgabe 2.1.2)

### Erinnerung: Unser relationales Modell

```
1. Nutzer(benutzerName, passwort, email, zuletztAktiv)

2. Beitrag(beitragId, titel, erstelltAm, text, benutzerName#)
                                              ^^^^^^^^^^^^^
                                              FK → Nutzer

3. Bild(bildId, dateiname)

4. Likes(benutzerName#, beitragId#, zeitstempel)
         ^^^^^^^^^^^^^  ^^^^^^^^^^
         FK → Nutzer    FK → Beitrag

5. Enthaelt(beitragId#, bildId#)
            ^^^^^^^^^^  ^^^^^^^
            FK → Beitrag FK → Bild
```

### Beispiel 1: Beziehung Nutzer → Beitrag

**Fremdschlüssel:** `Beitrag.benutzerName#` → `Nutzer.benutzerName`

**Referentielle Integritätsbedingungen:**

1. **Einfügen eines Beitrags:**
   ```sql
   INSERT INTO Beitrag (beitragId, titel, benutzerName, ...)
   VALUES (42, 'Neuer Post', 'Anna', ...);
   ```
   - ✓ **Erlaubt**, wenn der Nutzer 'Anna' in der Nutzer-Tabelle existiert
   - ✗ **Nicht erlaubt**, wenn 'Anna' nicht existiert
   - **Fehlermeldung:** "Foreign key constraint violation"

2. **Löschen eines Nutzers:**
   ```sql
   DELETE FROM Nutzer WHERE benutzerName = 'Anna';
   ```
   - ✗ **Nicht erlaubt**, solange Beiträge von Anna existieren
   - ✓ **Erlaubt** mit CASCADE: Alle Beiträge von Anna werden automatisch gelöscht
   - **Alternative:** Erst alle Beiträge von Anna löschen, dann den Nutzer

3. **Ändern eines Nutzernamens:**
   ```sql
   UPDATE Nutzer SET benutzerName = 'Anna2' WHERE benutzerName = 'Anna';
   ```
   - ✗ **Nicht erlaubt** ohne CASCADE
   - ✓ **Erlaubt** mit UPDATE CASCADE: Alle Beiträge werden automatisch aktualisiert

### Beispiel 2: N:M-Beziehung Likes

**Fremdschlüssel:**
- `Likes.benutzerName#` → `Nutzer.benutzerName`
- `Likes.beitragId#` → `Beitrag.beitragId`

**Referentielle Integritätsbedingungen:**

1. **Einfügen eines Likes:**
   ```sql
   INSERT INTO Likes (benutzerName, beitragId, zeitstempel)
   VALUES ('MaxMüller2', 5, NOW());
   ```
   - ✓ **Erlaubt**, wenn beide existieren:
     - Nutzer 'MaxMüller2' existiert in Nutzer-Tabelle
     - Beitrag mit ID 5 existiert in Beitrag-Tabelle
   - ✗ **Nicht erlaubt**, wenn einer der beiden nicht existiert

2. **Löschen eines Beitrags:**
   ```sql
   DELETE FROM Beitrag WHERE beitragId = 5;
   ```
   - ✗ **Nicht erlaubt**, solange Likes für diesen Beitrag existieren
   - ✓ **Erlaubt** mit CASCADE: Alle Likes für diesen Beitrag werden automatisch gelöscht
   - **Empfehlung:** CASCADE ist hier sinnvoll, da Likes ohne Beitrag keinen Sinn machen

### Beispiel 3: N:M-Beziehung Enthaelt

**Fremdschlüssel:**
- `Enthaelt.beitragId#` → `Beitrag.beitragId`
- `Enthaelt.bildId#` → `Bild.bildId`

**Referentielle Integritätsbedingungen:**

1. **Einfügen einer Bild-Beitrag-Zuordnung:**
   ```sql
   INSERT INTO Enthaelt (beitragId, bildId)
   VALUES (42, 3);
   ```
   - ✓ **Erlaubt**, wenn:
     - Beitrag mit ID 42 existiert
     - Bild mit ID 3 existiert
   - ✗ **Nicht erlaubt** sonst

2. **Löschen eines Bildes:**
   ```sql
   DELETE FROM Bild WHERE bildId = 3;
   ```
   - ✗ **Nicht erlaubt**, solange Beiträge dieses Bild verwenden
   - **Wichtig:** Ohne CASCADE könnten Bilder nicht gelöscht werden
   - **Empfehlung:** RESTRICT (kein CASCADE), da Bilder wiederverwendbar sind

3. **Löschen eines Beitrags:**
   ```sql
   DELETE FROM Beitrag WHERE beitragId = 42;
   ```
   - ✗ **Nicht erlaubt**, solange Enthaelt-Einträge existieren
   - ✓ **Erlaubt** mit CASCADE: Alle Enthaelt-Einträge für diesen Beitrag werden gelöscht
   - **Wichtig:** Die Bilder selbst bleiben erhalten (Aggregation, nicht Komposition)

## Zusammenfassung der Integritätsregeln für unser Modell

| Fremdschlüssel | Elterntabelle | Kindtabelle | Empfohlene DELETE-Regel | Begründung |
|----------------|---------------|-------------|-------------------------|------------|
| Beitrag.benutzerName# | Nutzer | Beitrag | RESTRICT oder CASCADE | Nutzer sollten nicht ohne Warnung gelöscht werden |
| Likes.benutzerName# | Nutzer | Likes | CASCADE | Likes ohne Nutzer sind sinnlos |
| Likes.beitragId# | Beitrag | Likes | CASCADE | Likes ohne Beitrag sind sinnlos |
| Enthaelt.beitragId# | Beitrag | Enthaelt | CASCADE | Bild-Zuordnungen ohne Beitrag sind sinnlos |
| Enthaelt.bildId# | Bild | Enthaelt | RESTRICT | Bilder sollen wiederverwendbar bleiben |

## Vorteile der referentiellen Integrität

1. **Datenkonsistenz:** Verhindert "verwaiste" Datensätze (Orphaned Records)
2. **Datenqualität:** Stellt sicher, dass Beziehungen immer gültig sind
3. **Automatische Pflege:** CASCADE-Regeln reduzieren manuellen Aufwand
4. **Fehlerprävention:** Verhindert logische Fehler in der Anwendung
