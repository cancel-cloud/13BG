# Aufgabe 1.5.1: Algorithmus für generierePasswort() (5 BE)

## Anforderungen
Die Methode `generierePasswort()` der Klasse `SocialMediaPlatform` soll ein sicheres Passwort generieren mit folgenden Eigenschaften:
- **Länge:** 12 Zeichen
- **Pflichtbestandteile:**
  - 1 Großbuchstabe [A-Z] (ASCII 65-90)
  - 1 Ziffer [0-9] (ASCII 48-57)
  - 1 Sonderzeichen [#, $, %, &] (ASCII 35, 36, 37, 38)
- **Verbleibende Zeichen:** Die restlichen 9 Stellen werden mit zufälligen Kleinbuchstaben [a-z] (ASCII 97-122) gefüllt
- **Positionierung:** Jedes Pflichtelement wird an einer zufälligen Position platziert

## Algorithmus in Pseudocode

```
FUNKTION generierePasswort() : char[]
    // 1. Initialisierung
    passwortArray ← neues char-Array der Länge 12
    random ← neue Random-Instanz

    // 2. Alle 12 Positionen initial mit Kleinbuchstaben füllen
    FÜR i VON 0 BIS 11
        zufallsASCII ← random.nextInt(26) + 97  // 97 = 'a', 26 Kleinbuchstaben
        passwortArray[i] ← (char)zufallsASCII
    ENDE FÜR

    // 3. Großbuchstaben an zufälliger Position einfügen
    positionGross ← random.nextInt(12)  // Position 0-11
    grossbuchstabeASCII ← random.nextInt(26) + 65  // 65 = 'A', 26 Großbuchstaben
    passwortArray[positionGross] ← (char)grossbuchstabeASCII

    // 4. Ziffer an zufälliger Position einfügen
    positionZiffer ← random.nextInt(12)  // Position 0-11
    zifferASCII ← random.nextInt(10) + 48  // 48 = '0', 10 Ziffern
    passwortArray[positionZiffer] ← (char)zifferASCII

    // 5. Sonderzeichen an zufälliger Position einfügen
    // Sonderzeichen: # (35), $ (36), % (37), & (38)
    positionSonder ← random.nextInt(12)  // Position 0-11
    sonderzeichenWahl ← random.nextInt(4)  // 0-3 für die 4 Sonderzeichen

    WENN sonderzeichenWahl = 0 DANN
        sonderzeichenASCII ← 35  // #
    SONST WENN sonderzeichenWahl = 1 DANN
        sonderzeichenASCII ← 36  // $
    SONST WENN sonderzeichenWahl = 2 DANN
        sonderzeichenASCII ← 37  // %
    SONST
        sonderzeichenASCII ← 38  // &
    ENDE WENN

    passwortArray[positionSonder] ← (char)sonderzeichenASCII

    // 6. Passwort zurückgeben
    RETURN passwortArray
ENDE FUNKTION
```

## Alternative Formulierung (kompakter)

```
FUNKTION generierePasswort() : char[]
    1. Erstelle char-Array der Länge 12
    2. Erzeuge Random-Objekt

    3. Fülle alle 12 Positionen mit zufälligen Kleinbuchstaben:
       FÜR jede Position:
           Generiere Zufallszahl zwischen 97 und 122 (a-z)
           Wandle in char um und speichere im Array

    4. Wähle zufällige Position (0-11) für Großbuchstaben
       Generiere Zufallszahl zwischen 65 und 90 (A-Z)
       Ersetze Zeichen an dieser Position

    5. Wähle zufällige Position (0-11) für Ziffer
       Generiere Zufallszahl zwischen 48 und 57 (0-9)
       Ersetze Zeichen an dieser Position

    6. Wähle zufällige Position (0-11) für Sonderzeichen
       Wähle zufällig eines der Zeichen: # (35), $ (36), % (37), & (38)
       Ersetze Zeichen an dieser Position

    7. Gib das Passwort-Array zurück
ENDE FUNKTION
```

## Beispiel-Durchlauf

**Schritt 1:** Array mit 12 Kleinbuchstaben initialisieren
```
[f, i, v, q, w, j, k, l, m, n, o, p]
```

**Schritt 2:** Großbuchstaben 'K' an zufälliger Position 8 einfügen
```
[f, i, v, q, w, j, k, l, K, n, o, p]
```

**Schritt 3:** Ziffer '2' an zufälliger Position 7 einfügen
```
[f, i, v, q, w, j, k, 2, K, n, o, p]
```

**Schritt 4:** Sonderzeichen '%' an zufälliger Position 0 einfügen
```
[%, i, v, q, w, j, k, 2, K, n, o, p]
```

**Ergebnis:** `%ivqwjk2Knop`

## Wichtige Hinweise

1. **Kollisionen:** Da die Positionen zufällig gewählt werden, können Pflichtzeichen sich gegenseitig überschreiben. Dies ist akzeptabel, da das Passwort dadurch nicht weniger sicher wird (es kann dann z.B. 10 Kleinbuchstaben, 1 Großbuchstabe und 1 Ziffer enthalten).

2. **ASCII-Werte:**
   - Kleinbuchstaben: 97-122 (a-z)
   - Großbuchstaben: 65-90 (A-Z)
   - Ziffern: 48-57 (0-9)
   - Sonderzeichen: 35 (#), 36 ($), 37 (%), 38 (&)

3. **Rückgabetyp:** char[] (Array) statt String für bessere Sicherheit (Passwörter sollten nicht als unveränderliche Strings im Speicher liegen).
