# Arduino Server-Client Kommunikationsprotokoll

## Übersicht
Das System besteht aus drei Komponenten:
- **ArduinoServer**: Verwaltet die Arduino-Verbindung und behandelt Client-Anfragen
- **ArduinoClient**: Verbindet sich mit dem Server, um Arduino-Operationen anzufordern
- **Arduino**: Führt Caesar-Verschlüsselung aus (über serielle Verbindung)

## Server-Funktionen

### 🔌 Grundfunktionen
- **Port**: 8080 (Standard)
- **Protokoll**: TCP Socket-Verbindung
- **Multi-Client**: Unterstützt mehrere gleichzeitige Clients
- **Arduino-Integration**: Serielle Kommunikation mit jSerialComm

### 📡 Arduino-Kommunikation
- Automatische Port-Erkennung beim Start
- Kontinuierliches Lesen von Arduino-Antworten
- Broadcast von Arduino-Nachrichten an alle verbundenen Clients
- Reset und Initialisierung der Arduino-Verbindung

## Client-Server Protokoll

### 📤 Client → Server Befehle

| Befehl | Format | Beschreibung |
|--------|--------|--------------|
| `ENCRYPT` | `ENCRYPT:<shift>:<text>` | Verschlüsselt Text mit Caesar-Cipher |
| `DECRYPT` | `DECRYPT:<shift>:<text>` | Entschlüsselt Text mit Caesar-Cipher |
| `SEND` | `SEND:<arduino_command>` | Sendet direkten Befehl an Arduino |
| `PING` | `PING` | Überprüft Server-Verbindung |
| `STATUS` | `STATUS` | Fragt Server-Status ab |
| `QUIT` | `QUIT` | Beendet Client-Verbindung |

### 📥 Server → Client Antworten

| Antwort-Typ | Format | Beschreibung |
|-------------|--------|--------------|
| `CONNECTED` | `CONNECTED:<message>` | Willkommensnachricht |
| `HELP` | `HELP:<help_text>` | Hilfeinformationen |
| `PONG` | `PONG:<message>` | Antwort auf PING |
| `STATUS` | `STATUS:<status>` | Server-Status |
| `LOCAL_ENCRYPT` | `LOCAL_ENCRYPT:<result>` | Java-seitige Verschlüsselung |
| `LOCAL_DECRYPT` | `LOCAL_DECRYPT:<result>` | Java-seitige Entschlüsselung |
| `ARDUINO_RESPONSE` | `ARDUINO_RESPONSE:<response>` | Weiterleitung der Arduino-Antwort |
| `COMMAND_SENT` | `COMMAND_SENT:<status>` | Status der Arduino-Übertragung |
| `ERROR` | `ERROR:<error_message>` | Fehlermeldung |
| `BYE` | `BYE:<message>` | Abschiedsnachricht |

## Client-Funktionen

### 🎯 Benutzerfreundliche Befehle
Der Client transformiert benutzerfreundliche Eingaben in Protokoll-konforme Befehle:

```bash
# Benutzer-Eingabe → Server-Befehl
encrypt 3 hallo welt    → ENCRYPT:3:hallo welt
decrypt 3 kdoor zhow    → DECRYPT:3:kdoor zhow
send E:test:5           → SEND:E:test:5
ping                    → PING
status                  → STATUS
```

### 🔄 Automatische Funktionen
- **Server-Listener**: Kontinuierliches Empfangen von Server-Nachrichten
- **Befehl-Transformation**: Übersetzung von benutzerfreundlichen zu Protokoll-Befehlen
- **Antwort-Kategorisierung**: Intelligente Anzeige verschiedener Nachrichtentypen
- **Verbindungsüberwachung**: Automatische Erkennung von Verbindungsverlusten

## Arduino-Integration

### 📋 Unterstützte Arduino-Befehle
Basierend auf Ihrem ursprünglichen Code:
- `E:<text>:<shift>` - Verschlüsseln
- `D:<text>:<shift>` - Entschlüsseln

### 🔄 Datenfluss
1. **Client** sendet Befehl an **Server**
2. **Server** verarbeitet Befehl lokal (Java-seitig)
3. **Server** sendet entsprechenden Befehl an **Arduino**
4. **Arduino** verarbeitet und antwortet
5. **Server** broadcastet Arduino-Antwort an alle **Clients**

## Verwendung

### 🖥️ Server starten
```bash
java -cp ".:jSerialComm-2.x.x.jar" de.lukas.server.ArduinoServer
```

1. Server zeigt verfügbare serielle Ports an
2. Arduino-Port auswählen (z.B. COM3 oder /dev/ttyUSB0)
3. Server initialisiert Arduino-Verbindung
4. Server wartet auf Client-Verbindungen auf Port 8080

### 👤 Client starten
```bash
java -cp "." de.lukas.client.ArduinoClient
```

Der Client verbindet sich automatisch mit `localhost:8080`.

### 💡 Beispiel-Session

**Client-Eingaben:**
```
encrypt 5 hello world
decrypt 5 mjqqt btwqi
send E:test:3
ping
status
quit
```

**Server-Output:**
```
📡 An Arduino gesendet: E:helloworld:5
🤖 Arduino: Verschlüsselt: mjqqtbtwqi
👤 Client /127.0.0.1:54321: ENCRYPT:5:hello world
```

## Erweiterte Funktionen

### 🔀 Multi-Client Support
- Mehrere Clients können gleichzeitig verbunden sein
- Alle Clients erhalten Arduino-Antworten (Broadcast)
- Jeder Client kann unabhängig Befehle senden

### 🛡️ Fehlerbehandlung
- **Verbindungsverlust**: Clients werden automatisch entfernt
- **Arduino-Fehler**: Fehlermeldungen werden an Clients weitergeleitet
- **Ungültige Befehle**: Benutzerfreundliche Fehlermeldungen
- **Port-Probleme**: Graceful Shutdown bei seriellen Problemen

### 🔧 Konfiguration
**Server-Konfiguration (ArduinoServer.java):**
```java
private final int SERVER_PORT = 8080;          // Server-Port ändern
private final String SERVER_HOST = "0.0.0.0";  // Für externe Verbindungen
```

**Client-Konfiguration (ArduinoClient.java):**
```java
private final String SERVER_HOST = "localhost"; // Server-IP ändern
private final int SERVER_PORT = 8080;           // Server-Port anpassen
```

## Sicherheitshinweise

### 🔒 Netzwerksicherheit
- Server läuft standardmäßig nur auf localhost
- Keine Authentifizierung implementiert
- Für Produktionsumgebung: SSL/TLS und Authentifizierung hinzufügen

### 🎛️ Arduino-Sicherheit
- Nur vertrauenswürdige Clients sollten direkten Arduino-Zugriff haben
- `SEND`-Befehle können beliebige Arduino-Kommandos ausführen
- Rate-Limiting für Arduino-Befehle empfohlen

## Erweiterungsmöglichkeiten

### 📊 Monitoring
- Client-Verbindungsprotokollierung
- Arduino-Befehl-Statistiken
- Performance-Monitoring

### 🔌 Zusätzliche Protokolle
- WebSocket-Support für Web-Clients
- REST-API für HTTP-basierte Anfragen
- MQTT für IoT-Integration

### 🎮 Zusätzliche Arduino-Funktionen
- Sensor-Datenabfrage
- GPIO-Steuerung
- Weitere Verschlüsselungsalgorithmen

## Troubleshooting

### ❌ Häufige Probleme

**Server startet nicht:**
```
Lösung: Port 8080 bereits belegt
→ SERVER_PORT in ArduinoServer.java ändern
```

**Arduino nicht gefunden:**
```
Lösung: Arduino-Treiber installieren oder anderen Port wählen
→ Device Manager (Windows) oder lsusb (Linux) prüfen
```

**Client kann nicht verbinden:**
```
Lösung: Server-Host/Port prüfen
→ Firewall-Einstellungen überprüfen
→ Server-Logs auf Fehlermeldungen prüfen
```

**Keine Arduino-Antworten:**
```
Lösung: Arduino-Code prüfen
→ Serial Monitor öffnen und manuell testen
→ Baudrate überprüfen (9600)
```

### 📝 Debug-Tipps
- Server-Konsole zeigt alle Client-Befehle
- Client zeigt Server-Antworten kategorisiert an
- Arduino Serial Monitor parallel verwenden
- Netzwerk-Tools wie `netstat` für Port-Überwachung

## Abhängigkeiten

### 📚 Java-Libraries
```xml
<!-- Maven Dependency -->
<dependency>
    <groupId>com.fazecast</groupId>
    <artifactId>jSerialComm</artifactId>
    <version>2.10.2</version>
</dependency>
```

### 🔧 System-Anforderungen
- Java 8 oder höher
- Arduino mit Caesar-Verschlüsselungs-Sketch
- Serielle USB-Verbindung
- Freier TCP-Port (Standard: 8080)