# Aufgabe 1.4.2: UML-Sequenzdiagramm für Verbindungsaufbau und Anmeldung (10 BE)

## Aufgabenstellung
Entwickeln und zeichnen Sie ein Sequenzdiagramm in UML-Notation, das den Verbindungsaufbau und den Ablauf bis zu Annas erfolgreicher Anmeldung darstellt.

**Gegeben:**
- Client-Anfrage: `<anmelden;Anna;%fivqwj2iKez\n`
- Server-Antwort: `>+OK Willkommen\n`
- Ein Objekt `n` der Klasse `Nutzer` existiert bereits (Anna)

## UML-Sequenzdiagramm (textuell beschrieben)

Da ich keine grafischen Diagramme zeichnen kann, beschreibe ich das Sequenzdiagramm detailliert mit allen Interaktionen:

### Beteiligte Objekte (Lifelines von links nach rechts):
1. `: Client` (ein bereits existierendes Client-Objekt)
2. `clientSocket : Socket` (wird erstellt)
3. `smpServer : Server` (bereits existierend)
4. `serverSocket : ServerSocket` (bereits existierend)
5. `: ServerThread` (wird erstellt)
6. `smp : SocialMediaPlatform` (bereits existierend)
7. `n : Nutzer` (bereits existierend - Anna)

### Sequenz der Nachrichten:

```
Actor                    → : Client
                         |
1. <<create>>            |
   Client()              |
                         |
2. verbinden(smpServer, 4711)
   ├─────────────────────┤
   │ 3. <<create>>        │
   │    Socket(smpServer, 4711)
   │                      │
   │ 4. connect() : true  │
   │ ←────────────────────┤
   │                      │
   │    [Verbindungsaufbau zum Server]
   │    ↓
   │    serverSocket wird kontaktiert
   │    │
   │    │ smpServer : Server
   │    │ (läuft bereits und ruft accept() auf)
   │    │
   │    5. accept()
   │    ├──────────────────────────────────┐
   │    │                                   │
   │    │ [wartet auf Verbindung]          │
   │    │                                   │
   │    │ [Verbindung erkannt]              │
   │    │                                   │
   │    6. : Socket (neues Socket-Objekt für Client)
   │    └──────────────────────────────────┤
   │                                        │
   │    7. <<create>>                       │
   │       ServerThread(clientSocket, smp) │
   │       ├───────────────────────────────┤
   │       │                                │
   │    8. start()                          │
   │       ├───────────────────────────────┤
   │       │                                │
   │       │ 9. run() [in neuem Thread]    │
   │       │    ↓                           │
   │       │                                │
   ├───────┴────────────────────────────────┤
   │                                        │
   │ [Server kehrt zu accept() zurück]     │
   │                                        │
5. ← true                                   │
   │                                        │
6. anmelden()                               │
   ├────────────────────────────────────────┤
   │                                        │
   │ 7. write("<anmelden;Anna;%fivqwj2iKez\n")
   │    ├────────────────────────────────────────→ [Daten werden über Socket gesendet]
   │                                                │
   │                                   10. readLine() : String
   │                                                ├───────────┐
   │                                                │ [blockiert und wartet]
   │                                                │           │
   │                                                ← "<anmelden;Anna;%fivqwj2iKez"
   │                                                │
   │                                   11. [Parse Kommando]
   │                                                │
   │                                   12. anmelden("Anna", "%fivqwj2iKez") : Nutzer
   │                                                ├──────────────────────────→
   │                                                                           │
   │                                                            13. getBenutzerName() : String
   │                                                                           ├────→ n : Nutzer
   │                                                                           ← "Anna"
   │                                                                           │
   │                                                            14. getPasswort() : String
   │                                                                           ├────→ n : Nutzer
   │                                                                           ← "%fivqwj2iKez"
   │                                                                           │
   │                                                            [Prüfung erfolgreich]
   │                                                            │
   │                                                ← n (Nutzer-Objekt)
   │                                                │
   │                                   15. write(">+OK Willkommen\n")
   │    ← [Daten über Socket] ──────────────────────┤
   │                                                │
8. readLine() : String                              │
   ├────────────────────────────────────────────────┤
   │                                                │
9. ← "+OK Willkommen"                               │
   │                                                │
   ↓                                                │
[Client ist angemeldet]                             │
                                                    ↓
                                    [ServerThread wartet auf nächstes Kommando]
```

## Textuelle Beschreibung des Ablaufs:

### Phase 1: Client-Erstellung und Verbindungsaufbau
1. Ein Actor erstellt ein Client-Objekt (<<create>>)
2. Client ruft `verbinden(smpServer, 4711)` auf
3. Client erstellt ein Socket-Objekt mit Server-Adresse und Port 4711
4. Client-Socket ruft `connect()` auf und baut die Verbindung auf

### Phase 2: Server akzeptiert Verbindung
5. Der bereits laufende Server hat `serverSocket.accept()` aufgerufen und wartet
6. ServerSocket erkennt die eingehende Verbindung und gibt ein neues Socket-Objekt zurück
7. Server erstellt einen neuen ServerThread mit dem Client-Socket und der SocialMediaPlatform-Referenz
8. Server startet den ServerThread (`start()`)
9. Die `run()`-Methode des ServerThreads beginnt (in einem neuen Thread)
10. Server kehrt zu `accept()` zurück und wartet auf weitere Clients

### Phase 3: Anmeldung
11. Client-Methode `verbinden()` gibt `true` zurück
12. Client ruft `anmelden()` auf
13. Client sendet über Socket: `<anmelden;Anna;%fivqwj2iKez\n`
14. ServerThread empfängt über `readLine()` das Kommando
15. ServerThread parst das Kommando (trennt nach Semikolon)
16. ServerThread ruft `smp.anmelden("Anna", "%fivqwj2iKez")` auf
17. SocialMediaPlatform ruft `n.getBenutzerName()` und `n.getPasswort()` auf
18. Prüfung ist erfolgreich, Nutzer-Objekt `n` wird zurückgegeben
19. ServerThread sendet: `>+OK Willkommen\n`
20. Client empfängt über `readLine()`: `+OK Willkommen`

### Ergebnis:
- Anna ist erfolgreich angemeldet
- Die Verbindung bleibt bestehen für weitere Kommandos
- ServerThread wartet auf das nächste Kommando

## Wichtige UML-Notationen im Diagramm:

- **<<create>>**: Objekterzeugung
- **Synchrone Nachricht**: Durchgezogener Pfeil mit ausgefüllter Pfeilspitze
- **Antwortnachricht**: Gestrichelter Pfeil zurück
- **Aktivierungsbalken**: Zeigt, wann ein Objekt aktiv ist
- **Lifeline**: Gestrichelte vertikale Linie unter jedem Objekt
- **Loop/alt frames**: Für Wiederholungen oder alternative Abläufe (hier nicht benötigt)
