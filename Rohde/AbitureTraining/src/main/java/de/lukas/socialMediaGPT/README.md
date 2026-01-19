# Social Media Platform - Abitur 2023 Practical Computer Science

## Projektübersicht

Dies ist die vollständige Lösung für die Abituraufgabe "Social-Media-Plattform" aus dem Landesabitur 2023 Hessen, Praktische Informatik, Leistungskurs, Vorschlag A.

Das Projekt umfasst:
- **Objektorientierte Programmierung:** Vollständige Java-Implementierung eines Client-Server-Systems
- **Datenbankentwurf:** ER-Modellierung, Transformation in relationale Modelle, SQL-Abfragen
- **Dokumentation:** Umfassende Erklärungen aller Lösungen

## Inhaltsverzeichnis

1. [Projektstruktur](#projektstruktur)
2. [Implementierte Klassen](#implementierte-klassen)
3. [Dokumentation der Aufgaben](#dokumentation-der-aufgaben)
4. [Verwendung](#verwendung)
5. [Architektur](#architektur)

---

## Projektstruktur

```
socialMedia/
├── README.md                          (Diese Datei)
├── docs/                              (Dokumentation der schriftlichen Aufgaben)
│   ├── Aufgabe_1.1_Assoziationsarten.md
│   ├── Aufgabe_1.3_Struktogramm.md
│   ├── Aufgabe_1.4.1_Socket_Server_Architektur.md
│   ├── Aufgabe_1.4.2_Sequenzdiagramm.md
│   ├── Aufgabe_1.5.1_Passwort_Algorithmus.md
│   ├── Aufgabe_2.1.1_ERM_Transformationsregeln.md
│   ├── Aufgabe_2.1.2_Relationales_Modell.md
│   ├── Aufgabe_2.1.3_Referentielle_Integritaet.md
│   ├── Aufgabe_2.2_SQL_Abfragen.md
│   └── Aufgabe_2.3_ER_Diagramm_Werbung.md
│
├── DateTime.java                      (Datums- und Zeitverwaltung)
├── List.java                          (Generische Listenimplementierung)
├── Random.java                        (Zufallszahlengenerator)
├── Socket.java                        (Client-Socket für Netzwerkkommunikation)
├── ServerSocket.java                  (Server-Socket für eingehende Verbindungen)
│
├── Bild.java                          (Bild-Entität)
├── Text.java                          (Text-Entität)
├── Beitrag.java                       (Beitrags-Entität)
├── Nutzer.java                        (Nutzer-Entität)
│
├── SocialMediaPlatform.java           (Zentrale Geschäftslogik)
├── Server.java                        (Server-Hauptklasse)
├── ServerThread.java                  (Client-Handler-Thread)
├── Client.java                        (Client-Implementierung)
│
└── LA23-PRIN-LK-A-AUFG.pdf            (Original-Aufgabenstellung)
```

---

## Implementierte Klassen

### Utility-Klassen

#### **DateTime.java**
Verwaltet Datum und Uhrzeit für die Plattform.
- Erstellt Zeitstempel für Benutzeraktivitäten
- Vergleicht Zeitpunkte (isBefore, isAfter, isEquals)
- Format: "dd.MM.yyyy HH:mm"

#### **List.java**
Generische Listenimplementierung für Datensammlungen.
- Typsicher mit Generics `<T>`
- Methoden: add, remove, contains, get, size

#### **Random.java**
Zufallszahlengenerator für Passwörter und andere Zufallsoperationen.
- `nextInt()`: Zufallszahl aus gesamtem Bereich
- `nextInt(n)`: Zufallszahl von 0 bis n-1

#### **Socket.java & ServerSocket.java**
Netzwerkkommunikation zwischen Client und Server.
- Socket: Bidirektionale Kommunikation
- ServerSocket: Wartet auf eingehende Verbindungen

---

### Domänenmodell-Klassen

#### **Bild.java** (Task 1.2)
Repräsentiert ein Bild auf der Plattform.
- Eindeutige ID (auto-generiert)
- Dateiname
- Kann in mehreren Beiträgen verwendet werden (Aggregation)

```java
Bild bild = new Bild("urlaub.jpg");
int id = bild.getId(); // Auto-generierte ID
```

#### **Text.java** (Task 1.2)
Repräsentiert den Textinhalt eines Beitrags.
- Existenziell vom Beitrag abhängig (Komposition)
- Wird mit dem Beitrag gelöscht

```java
Text text = new Text("Das ist mein erster Post!");
```

#### **Beitrag.java** (Task 1.2)
Ein Beitrag auf der Social-Media-Plattform.
- Titel, Erstellungszeitpunkt, Autor
- 1..* Bilder (mindestens ein Bild erforderlich)
- 0..1 Text (optional)
- Likes-Zähler

```java
Beitrag post = new Beitrag(nutzer, "Urlaubsfotos", bild);
post.erstelleText("Toller Urlaub in Italien!");
post.hinzufuegen(weiteresbildBild);
post.like(); // Erhöht Like-Counter
```

**Wichtige Features:**
- Zeitstempel bei Erstellung
- Aggregation mit Bildern (Bilder bleiben erhalten)
- Komposition mit Text (Text wird mitgelöscht)

#### **Nutzer.java** (Task 1.2)
Ein Benutzer der Plattform.
- Benutzername, Passwort, E-Mail
- Letzte Aktivität (wird bei jeder Aktion aktualisiert)
- Beiträge, Abonnenten, abonnierte Nutzer, Bilder

```java
Nutzer nutzer = new Nutzer("Anna", "passwort123", "anna@email.com");

// Beitrag erstellen
Beitrag beitrag = nutzer.erstelleBeitrag("Titel", bild);
Beitrag mitText = nutzer.erstelleBeitrag("Titel", bild, "Text");

// Anderen Nutzer abonnieren (bidirektional!)
nutzer.abonnieren(andererNutzer);

// Beitrag liken (nicht eigene Beiträge!)
nutzer.like(fremderBeitrag);
```

**Wichtige Features:**
- **Automatische Aktivitätsaktualisierung:** Jede Aktion aktualisiert `zuletztAktiv`
- **Kein Self-Like:** Nutzer können ihre eigenen Beiträge nicht liken
- **Kein Self-Subscribe:** Nutzer können sich nicht selbst abonnieren
- **Bidirektionale Abonnements:** Beim Abonnieren wird die Beziehung in beide Richtungen gespeichert

---

### Geschäftslogik

#### **SocialMediaPlatform.java** (Tasks 1.3, 1.5.2, 1.6)
Zentrale Plattform-Verwaltung.

**Methoden:**

1. **registrieren(name, passwort, email): int** (Task 1.6)
   ```java
   int ergebnis = platform.registrieren("Max", "pass123", "max@email.com");
   // 0 = Erfolg, -1 = Username vergeben, -2 = E-Mail vergeben
   ```

2. **anmelden(name, passwort): Nutzer**
   ```java
   Nutzer nutzer = platform.anmelden("Max", "pass123");
   // null bei fehlerhafter Anmeldung
   ```

3. **generierePasswort(): char[]** (Task 1.5.2)
   ```java
   char[] neuesPasswort = platform.generierePasswort();
   // Erzeugt sicheres 12-Zeichen-Passwort
   // - 1 Großbuchstabe [A-Z]
   // - 1 Ziffer [0-9]
   // - 1 Sonderzeichen [#, $, %, &]
   // - 9 Kleinbuchstaben [a-z]
   // Beispiel: %fivqwj2iKez
   ```

4. **ermittleAbonnierteNutzerMitNeuenBeitraegen(nutzer): List<Nutzer>** (Task 1.3)
   ```java
   List<Nutzer> nutzerMitNeuenPosts = platform.ermittleAbonnierteNutzerMitNeuenBeitraegen(anna);
   // Liefert alle abonnierten Nutzer, die seit Annas letzter Aktivität gepostet haben
   ```

5. **sucheNutzer(name): Nutzer**
   ```java
   Nutzer gefunden = platform.sucheNutzer("Anna");
   ```

---

### Client-Server-Architektur

#### **Server.java** (Task 1.4.3)
Server-Hauptklasse für die Netzwerkkommunikation.

```java
SocialMediaPlatform platform = new SocialMediaPlatform();
Server server = new Server(4711, platform);
server.runServer(); // Startet Server auf Port 4711
```

**Funktionsweise:**
1. Wartet auf eingehende Client-Verbindungen
2. Erzeugt für jeden Client einen eigenen ServerThread
3. Läuft in Endlosschleife

#### **ServerThread.java**
Behandelt einen einzelnen Client in eigenem Thread.

**Unterstützte Kommandos:**
- `<anmelden;username;passwort>` → `>+OK Willkommen`
- `<registrieren;username;passwort;email>` → `>+OK Registrierung erfolgreich`
- `<abmelden>` → `>+OK Abgemeldet`
- `<quit>` → `>+OK Verbindung wird beendet`

**Protokoll:**
- Client → Server: `<kommando;param1;param2\n`
- Server → Client: `>+OK Nachricht\n` (Erfolg) oder `>-ERR Fehler\n` (Fehler)

#### **Client.java**
Client-Implementierung für Verbindung zum Server.

```java
Client client = new Client();
client.verbinden("localhost", 4711);
client.anmelden("Anna", "passwort123");
String antwort = client.verarbeiteKommando("abmelden");
client.disconnect();
```

---

## Dokumentation der Aufgaben

### Aufgabe 1: Objektorientierte Entwicklung

| Aufgabe | Beschreibung | Datei |
|---------|-------------|-------|
| **1.1** | Assoziationsarten (Aggregation vs. Komposition) | [Aufgabe_1.1_Assoziationsarten.md](docs/Aufgabe_1.1_Assoziationsarten.md) |
| **1.2** | Implementierung Nutzer und Beitrag | `Nutzer.java`, `Beitrag.java` |
| **1.3** | Struktogramm für `ermittleAbonnierteNutzerMitNeuenBeitraegen()` | [Aufgabe_1.3_Struktogramm.md](docs/Aufgabe_1.3_Struktogramm.md) |
| **1.4.1** | Socket-Klassen und Server-Architektur | [Aufgabe_1.4.1_Socket_Server_Architektur.md](docs/Aufgabe_1.4.1_Socket_Server_Architektur.md) |
| **1.4.2** | UML-Sequenzdiagramm | [Aufgabe_1.4.2_Sequenzdiagramm.md](docs/Aufgabe_1.4.2_Sequenzdiagramm.md) |
| **1.4.3** | Implementierung Server-Klasse | `Server.java` |
| **1.5.1** | Algorithmus für Passwortgenerierung | [Aufgabe_1.5.1_Passwort_Algorithmus.md](docs/Aufgabe_1.5.1_Passwort_Algorithmus.md) |
| **1.5.2** | Implementierung `generierePasswort()` | `SocialMediaPlatform.java` |
| **1.6** | Implementierung `registrieren()` | `SocialMediaPlatform.java` |

### Aufgabe 2: Datenbankentwurf

| Aufgabe | Beschreibung | Datei |
|---------|-------------|-------|
| **2.1.1** | 4 ERM-Transformationsregeln | [Aufgabe_2.1.1_ERM_Transformationsregeln.md](docs/Aufgabe_2.1.1_ERM_Transformationsregeln.md) |
| **2.1.2** | Transformation ERM → Relationales Modell | [Aufgabe_2.1.2_Relationales_Modell.md](docs/Aufgabe_2.1.2_Relationales_Modell.md) |
| **2.1.3** | Referentielle Integrität | [Aufgabe_2.1.3_Referentielle_Integritaet.md](docs/Aufgabe_2.1.3_Referentielle_Integritaet.md) |
| **2.2.1-2.2.5** | SQL-Abfragen | [Aufgabe_2.2_SQL_Abfragen.md](docs/Aufgabe_2.2_SQL_Abfragen.md) |
| **2.3** | ER-Diagramm für Werbesystem | [Aufgabe_2.3_ER_Diagramm_Werbung.md](docs/Aufgabe_2.3_ER_Diagramm_Werbung.md) |

---

## Verwendung

### 1. Server starten

```java
public class ServerMain {
    public static void main(String[] args) {
        // Plattform initialisieren
        SocialMediaPlatform platform = new SocialMediaPlatform();

        // Testdaten erstellen
        platform.registrieren("Anna", "passwort123", "anna@email.com");
        platform.registrieren("Max", "pass456", "max@email.com");

        // Server starten
        Server server = new Server(4711, platform);
        System.out.println("Server wird gestartet auf Port 4711...");
        server.runServer();
    }
}
```

### 2. Client verbinden

```java
public class ClientMain {
    public static void main(String[] args) {
        Client client = new Client();

        // Verbindung herstellen
        if (client.verbinden("localhost", 4711)) {
            System.out.println("Verbindung erfolgreich!");

            // Anmelden
            if (client.anmelden("Anna", "passwort123")) {
                System.out.println("Anmeldung erfolgreich!");

                // Weitere Aktionen...

                // Abmelden
                client.abmelden();
            }

            client.disconnect();
        }
    }
}
```

### 3. Direkte Nutzung der Klassen (ohne Netzwerk)

```java
public class LocalTest {
    public static void main(String[] args) {
        SocialMediaPlatform platform = new SocialMediaPlatform();

        // Nutzer registrieren
        platform.registrieren("Anna", "pw123", "anna@email.com");
        platform.registrieren("Max", "pw456", "max@email.com");

        // Nutzer holen
        Nutzer anna = platform.sucheNutzer("Anna");
        Nutzer max = platform.sucheNutzer("Max");

        // Bilder erstellen
        Bild bild1 = new Bild("urlaub.jpg");
        Bild bild2 = new Bild("essen.jpg");

        // Beitrag erstellen
        Beitrag post = anna.erstelleBeitrag("Urlaubsfotos", bild1, "Toller Urlaub!");
        post.hinzufuegen(bild2);

        // Max abonniert Anna
        max.abonnieren(anna);

        // Max liked Annas Beitrag
        max.like(post);

        // Neue Beiträge prüfen
        List<Nutzer> neueInhalte = platform.ermittleAbonnierteNutzerMitNeuenBeitraegen(max);
        System.out.println("Nutzer mit neuen Beiträgen: " + neueInhalte.size());
    }
}
```

---

## Architektur

### Schichtenarchitektur

```
┌─────────────────────────────────────────────────────┐
│                  Client-Schicht                     │
│  (Client.java - Verbindung zum Server)              │
└────────────────────┬────────────────────────────────┘
                     │ TCP/IP Socket
                     │
┌────────────────────▼────────────────────────────────┐
│              Server-Schicht                         │
│  Server.java ──> ServerThread.java                  │
│  (Netzwerkkommunikation, Protokollverarbeitung)    │
└────────────────────┬────────────────────────────────┘
                     │
┌────────────────────▼────────────────────────────────┐
│           Geschäftslogik-Schicht                    │
│  SocialMediaPlatform.java                           │
│  (Registrierung, Anmeldung, Passwortgen., etc.)    │
└────────────────────┬────────────────────────────────┘
                     │
┌────────────────────▼────────────────────────────────┐
│            Domänenmodell-Schicht                    │
│  Nutzer.java, Beitrag.java, Bild.java, Text.java   │
│  (Geschäftsobjekte mit Logik)                       │
└────────────────────┬────────────────────────────────┘
                     │
┌────────────────────▼────────────────────────────────┐
│              Utility-Schicht                        │
│  List.java, DateTime.java, Random.java,            │
│  Socket.java, ServerSocket.java                    │
└─────────────────────────────────────────────────────┘
```

### Design-Patterns

1. **Thread-per-Connection:**
   - Jeder Client erhält einen eigenen ServerThread
   - Ermöglicht gleichzeitige Bearbeitung mehrerer Clients

2. **Facade-Pattern:**
   - SocialMediaPlatform bietet einheitliche Schnittstelle zur Geschäftslogik

3. **Composition vs. Aggregation:**
   - Beitrag-Text: Komposition (Text ist existenziell abhängig)
   - Beitrag-Bild: Aggregation (Bilder können wiederverwendet werden)

---

## Wichtige Konzepte

### 1. Referentielle Integrität
Die Beziehungen zwischen Objekten werden konsequent eingehalten:
- Kein Beitrag ohne Autor
- Keine Likes für nicht existierende Beiträge
- Bidirektionale Abonnements werden vollständig implementiert

### 2. Datenintegrität
Durch Validierung und Constraints:
- Eindeutige Benutzernamen
- Eindeutige E-Mail-Adressen
- Nutzer können sich nicht selbst abonnieren
- Nutzer können eigene Beiträge nicht liken

### 3. Aktivitätstracking
Jede Nutzeraktion aktualisiert automatisch `zuletztAktiv`:
- Beitrag erstellen
- Like setzen
- Nutzer abonnieren
- Bild hinzufügen

### 4. Sichere Passwörter
Generierte Passwörter erfüllen hohe Sicherheitsstandards:
- 12 Zeichen lang
- Mix aus Groß-/Kleinbuchstaben, Ziffern, Sonderzeichen
- Zufällige Positionierung aller Elemente

---

## Relationales Datenbankmodell

Das objektorientierte Design kann in folgendes relationales Schema überführt werden:

```sql
Nutzer(benutzerName, passwort, email, zuletztAktiv)

Beitrag(beitragId, titel, erstelltAm, text, benutzerName#)

Bild(bildId, dateiname)

Likes(benutzerName#, beitragId#, zeitstempel)

Enthaelt(beitragId#, bildId#)
```

Siehe [Aufgabe_2.1.2_Relationales_Modell.md](docs/Aufgabe_2.1.2_Relationales_Modell.md) für Details.

---

## Erweiterungsmöglichkeiten

Das System kann erweitert werden um:
- Kommentare zu Beiträgen
- Hashtags und Tag-Suche
- Benachrichtigungen für neue Beiträge
- Profilbilder für Nutzer
- Privatsphäre-Einstellungen
- Messaging zwischen Nutzern
- Werbesystem (siehe Aufgabe 2.3)

---

## Autor

Lösung entwickelt für das Landesabitur 2023 Hessen
Praktische Informatik, Leistungskurs

---

## Lizenz

Dieses Projekt dient ausschließlich zu Bildungszwecken.
