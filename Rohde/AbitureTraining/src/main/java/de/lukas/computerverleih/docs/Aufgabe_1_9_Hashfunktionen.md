# Aufgabe 1.9 - Hashfunktionen vs CRCs (4 BE)

## Aufgabenstellung
Diskutieren Sie die Vorteile der Verwendung von Hashfunktionen gegenüber CRCs für die
Integrität von Daten. Beschreiben Sie weiterhin zwei zusätzliche kryptografische Eigenschaften
von Hashfunktionen im Vergleich zu CRCs.

---

## Lösung

### 1. Was sind CRCs und Hashfunktionen?

#### CRC (Cyclic Redundancy Check)
- Ein **Fehlererkennungscode** für Datenübertragung
- Schnelle Berechnung durch Bitoperationen
- Feste Ausgabelänge (z.B. CRC-32 = 32 Bit)
- Primär entwickelt für Hardware-Implementierung
- Beispiel: Wird in Ethernet-Frames, ZIP-Dateien verwendet

#### Kryptografische Hashfunktion
- Ein **Einweg-Algorithmus** der Daten auf einen festen Hash-Wert abbildet
- Beispiele: SHA-256, SHA-3, MD5 (veraltet)
- Ausgabelänge: z.B. 256 Bit bei SHA-256
- Primär entwickelt für Sicherheitsanwendungen

---

### 2. Vorteile von Hashfunktionen für Datenintegrität

#### Vorteil 1: Höhere Kollisionsresistenz
- **Hashfunktionen:** Es ist praktisch unmöglich, zwei verschiedene Eingaben mit gleichem Hash-Wert zu finden
- **CRCs:** Kollisionen sind relativ einfach zu erzeugen (absichtlich oder zufällig)
- **Bedeutung:** Bei Hashfunktionen kann ein Angreifer keine manipulierten Daten mit gleichem Hash erstellen

#### Vorteil 2: Schutz vor absichtlicher Manipulation
- **Hashfunktionen:** Sind kryptografisch sicher gegen gezielte Angriffe
- **CRCs:** Wurden nur für zufällige Fehler entwickelt, nicht gegen bösartige Manipulation
- **Bedeutung:** CRCs können von einem Angreifer leicht "repariert" werden

#### Vorteil 3: Längere Ausgabe
- **Hashfunktionen:** SHA-256 hat 256 Bit (2^256 mögliche Werte)
- **CRCs:** Typischerweise nur 16-32 Bit
- **Bedeutung:** Viel mehr mögliche Hash-Werte, daher sicherer

---

### 3. Kryptografische Eigenschaften von Hashfunktionen

#### Eigenschaft 1: Einweg-Funktion (Pre-Image Resistance)
**Definition:**
Aus dem Hash-Wert kann man die ursprünglichen Daten NICHT zurückrechnen.

**Erklärung:**
- Wenn ich `hash("Passwort123") = abc123...` habe
- Kann ich aus `abc123...` NICHT auf "Passwort123" schließen
- Dies ist bei CRCs NICHT gegeben - CRCs sind keine Einweg-Funktionen

**Beispiel:**
```
SHA-256("Hallo") = 3a3e...
Aus "3a3e..." kann man "Hallo" nicht berechnen
```

**Anwendung:**
- Passwortspeicherung: Man speichert nur den Hash, nicht das Passwort
- Selbst wenn die Datenbank gestohlen wird, kennt der Angreifer die Passwörter nicht

---

#### Eigenschaft 2: Lawinen-Effekt (Avalanche Effect)
**Definition:**
Eine kleine Änderung in der Eingabe führt zu einer komplett anderen Ausgabe.

**Erklärung:**
- Ändert man nur ein Bit der Eingabe
- Ändert sich im Durchschnitt die Hälfte aller Bits des Hash-Wertes
- Bei CRCs ist die Änderung vorhersagbar und begrenzt

**Beispiel:**
```
SHA-256("Hallo")  = 3a3e2b...
SHA-256("Hbllo")  = 9f1c8a...   (komplett anders!)

CRC-32("Hallo")   = 0x12345678
CRC-32("Hbllo")   = 0x12345abc   (nur kleine Änderung)
```

**Anwendung:**
- Macht es unmöglich, durch kleine Änderungen den Hash vorherzusagen
- Schützt vor "Probieren" von Eingaben

---

### 4. Vergleichstabelle

| Eigenschaft | CRC | Kryptografische Hashfunktion |
|-------------|-----|------------------------------|
| Zweck | Fehlererkennung | Sicherheit & Integrität |
| Kollisionsresistenz | Gering | Hoch |
| Einweg-Funktion | Nein | Ja |
| Lawinen-Effekt | Gering | Stark |
| Geschwindigkeit | Sehr schnell | Langsamer |
| Schutz vor Manipulation | Nein | Ja |
| Typische Länge | 16-32 Bit | 256-512 Bit |

---

### 5. Zusammenfassung

**Für die Datenintegrität sind Hashfunktionen besser geeignet, weil:**

1. Sie **kollisionsresistent** sind - ein Angreifer kann keine zwei Dateien mit gleichem Hash erstellen

2. Sie eine **Einweg-Funktion** darstellen - aus dem Hash kann man nicht auf die Originaldaten zurückschließen

3. Sie einen **Lawinen-Effekt** haben - kleine Änderungen führen zu komplett anderen Hash-Werten

4. Sie **kryptografisch sicher** sind - sie wurden entwickelt, um gegen bösartige Angriffe zu schützen, nicht nur gegen zufällige Fehler

**CRCs sind hingegen geeignet für:**
- Schnelle Fehlererkennung bei Datenübertragung
- Hardware-Implementierung (z.B. in Netzwerkkarten)
- Erkennung von zufälligen Bitfehlern

---

### 6. Praxisbeispiel

**Szenario:** Download einer Datei aus dem Internet

**Mit CRC:**
- Angreifer fängt die Datei ab
- Fügt Malware ein
- Berechnet neuen CRC (einfach möglich)
- Opfer lädt manipulierte Datei mit "gültigem" CRC

**Mit SHA-256:**
- Angreifer fängt die Datei ab
- Fügt Malware ein
- Kann keinen passenden SHA-256 Hash berechnen (praktisch unmöglich)
- Opfer erkennt Manipulation durch falschen Hash
