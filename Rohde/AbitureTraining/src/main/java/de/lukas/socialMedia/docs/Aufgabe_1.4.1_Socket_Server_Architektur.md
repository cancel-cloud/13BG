# Aufgabe 1.4.1: Socket-Klassen und Server-Architektur (4 BE)

## Beschreibung der Aufgaben und Funktionsweisen

### 1. Socket-Klassen

#### Klasse `Socket`
**Aufgabe:** Stellt die Verbindung zwischen Client und Server her und ermöglicht die bidirektionale Kommunikation.

**Funktionsweise:**
- Kapselt einen TCP/IP-Socket für die Netzwerkkommunikation
- Verwaltet die IP-Adresse (`remoteHostIP`) und den Port (`remotePort`) des Kommunikationspartners
- Die Methode `connect()` baut die Verbindung zum Server auf
- `readLine()` liest zeilenweise Daten vom Socket (blockierend bis eine komplette Zeile empfangen wurde)
- `write(String)` sendet Daten über den Socket
- `close()` schließt die Verbindung und gibt Ressourcen frei

#### Klasse `ServerSocket`
**Aufgabe:** Wartet auf eingehende Verbindungsanfragen von Clients.

**Funktionsweise:**
- Bindet sich an einen lokalen Port (`localPort`)
- Die Methode `accept()` blockiert und wartet auf eingehende Verbindungen
- Bei einer erfolgreichen Verbindung erzeugt `accept()` ein neues `Socket`-Objekt für die Kommunikation mit dem Client
- Der ServerSocket selbst bleibt geöffnet, um weitere Clients zu akzeptieren
- `close()` schließt den ServerSocket

### 2. Klasse `Server`

**Aufgabe:** Verwaltet den ServerSocket und erzeugt für jeden Client einen eigenen ServerThread.

**Funktionsweise:**
- Besitzt eine Referenz auf die `SocialMediaPlatform` (für den Zugriff auf Geschäftslogik und Daten)
- Erzeugt im Konstruktor einen `ServerSocket` auf dem angegebenen Port
- Die Methode `runServer()`:
  - Läuft in einer Endlosschleife
  - Ruft `serverSocket.accept()` auf und wartet auf Clients
  - Erzeugt für jeden akzeptierenden Client einen neuen `ServerThread`
  - Übergibt dem Thread das Socket-Objekt und die Referenz auf die SocialMediaPlatform
  - Startet den Thread
- Ermöglicht somit die gleichzeitige Bearbeitung mehrerer Clients

### 3. Klasse `ServerThread`

**Aufgabe:** Behandelt die Kommunikation mit einem einzelnen Client in einem eigenen Thread.

**Funktionsweise:**
- Erweitert die Klasse `Thread` (oder implementiert `Runnable`)
- Erhält im Konstruktor:
  - Das `Socket`-Objekt für die Kommunikation mit dem Client (`clientSocket`)
  - Die Referenz auf `SocialMediaPlatform` (`smp`) für Geschäftslogik
- Die Methode `run()`:
  - Wird automatisch beim Start des Threads ausgeführt
  - Liest Kommandos vom Client über `clientSocket.readLine()`
  - Verarbeitet die Kommandos (z.B. "anmelden", "erstellen", etc.)
  - Ruft entsprechende Methoden der `SocialMediaPlatform` auf
  - Sendet Antworten an den Client über `clientSocket.write()`
  - Läuft in einer Schleife, bis die Verbindung beendet wird
- Jeder ServerThread arbeitet unabhängig von anderen Threads

### 4. Zusammenarbeit beim Verbindungsaufbau und der Anmeldung

**Ablauf:**

1. **Server-Start:**
   - Server erstellt einen ServerSocket auf Port 4711
   - Server ruft `runServer()` auf und wartet auf Clients

2. **Client-Verbindung:**
   - Client erstellt einen Socket mit Serveradresse und Port 4711
   - Client ruft `connect()` auf
   - ServerSocket.accept() registriert die Verbindung

3. **Thread-Erstellung:**
   - Server erstellt einen neuen ServerThread mit dem Client-Socket
   - Server startet den ServerThread
   - Server kehrt zu accept() zurück (wartet auf nächsten Client)

4. **Anmeldung:**
   - Client sendet Kommando: `<anmelden;Anna;%fivqwj2iKez\n`
   - ServerThread empfängt das Kommando über `readLine()`
   - ServerThread parst das Kommando (trennt nach Semikolon)
   - ServerThread ruft `smp.anmelden("Anna", "%fivqwj2iKez")` auf
   - SocialMediaPlatform prüft die Anmeldedaten
   - Bei Erfolg: SocialMediaPlatform gibt das Nutzer-Objekt zurück
   - ServerThread sendet Antwort: `>+OK Willkommen\n`
   - Bei Fehler würde eine entsprechende Fehlermeldung gesendet

5. **Weitere Kommunikation:**
   - ServerThread bleibt aktiv und verarbeitet weitere Kommandos
   - Jede Client-Aktion wird über das gleiche Socket-Objekt abgewickelt

## Wichtige Designentscheidungen

- **Mehrere Clients:** Durch die Verwendung von Threads kann der Server mehrere Clients gleichzeitig bedienen
- **Zustandsverwaltung:** Die SocialMediaPlatform ist zentral und wird von allen Threads gemeinsam genutzt (Thread-Synchronisation beachten!)
- **Protokoll:** Einfaches textbasiertes Protokoll mit Zeilenenden als Trennzeichen
- **Fehlerbehandlung:** Sollte in einer produktiven Anwendung umfassend implementiert werden
