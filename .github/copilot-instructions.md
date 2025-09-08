# Arduino Caesar Cipher Communication System

Always reference these instructions first and fallback to search or bash commands only when you encounter unexpected information that does not match the info here.

## Project Overview
Java-based Caesar cipher communication system with Arduino hardware integration. Three main applications:
- **CaesarArduinoCommunicator**: Direct serial communication with Arduino
- **ArduinoServer**: TCP server managing Arduino connection for multiple clients
- **ArduinoClient**: Client application connecting to the server

## Working Effectively

### Bootstrap and Build
Navigate to the Arduino-Caesar project directory:
```bash
cd Rohde/Arduino-Caesar
```

Build the project:
```bash
gradle build
```
- **TIMING**: First build takes 60-90 seconds. Subsequent builds take <1 second. NEVER CANCEL.
- **TIMEOUT**: Set timeout to 3+ minutes for first build, 1+ minute for subsequent builds.

Build with configuration cache (recommended for faster builds):
```bash
gradle --configuration-cache build
```

Clean build when needed:
```bash
gradle clean build
```
- **TIMING**: Takes 1-3 seconds after initial Gradle daemon startup. NEVER CANCEL.

### Running Applications

#### Method 1: Using JAR with explicit classpath
Build first, then run with full classpath:
```bash
gradle build
java -cp "build/libs/Arduino-Caesar-1.0-SNAPSHOT.jar:$(find ~/.gradle -name 'jSerialComm-2.10.4.jar' | head -1)" de.lukas.CaesarArduinoCommunicator
```

#### Method 2: Using Gradle application plugin (RECOMMENDED)
Add application plugin to build.gradle.kts:
```kotlin
plugins {
    id("java")
    id("application")
}
application {
    mainClass.set("de.lukas.CaesarArduinoCommunicator")  // or ArduinoServer or ArduinoClient
}
```

Then run directly:
```bash
gradle run
```

#### Method 3: Using distribution (BEST for production)
First add the application plugin to build.gradle.kts (see "Adding the Application Plugin" section below), then create full distribution with all dependencies:
```bash
gradle installDist
```
- **TIMING**: Takes 1-2 seconds. NEVER CANCEL.
- **REQUIREMENT**: Requires application plugin in build.gradle.kts

Run the generated script:
```bash
build/install/Arduino-Caesar/bin/Arduino-Caesar
```

### Testing
Run tests:
```bash
gradle test
```
- **TIMING**: Takes <1 second (no tests currently exist). NEVER CANCEL.

Run all checks:
```bash
gradle check
```
- **TIMING**: Takes <1 second. NEVER CANCEL.

## Application Entry Points

### 1. CaesarArduinoCommunicator (Main Application)
- **Main Class**: `de.lukas.CaesarArduinoCommunicator`
- **Purpose**: Direct serial communication with Arduino for Caesar cipher operations
- **Dependencies**: Requires Arduino connected via USB serial port
- **Behavior without Arduino**: Exits gracefully with "Keine seriellen Ports gefunden!"

### 2. ArduinoServer (Server Component)
- **Main Class**: `de.lukas.server.ArduinoServer`
- **Purpose**: TCP server (port 8080) managing Arduino connection for multiple clients
- **Dependencies**: Requires Arduino connected via USB serial port
- **Behavior without Arduino**: Exits gracefully with "Keine seriellen Ports gefunden!"

### 3. ArduinoClient (Client Component)
- **Main Class**: `de.lukas.client.ArduinoClient`
- **Purpose**: TCP client connecting to ArduinoServer (localhost:8080)
- **Dependencies**: Requires ArduinoServer running
- **Behavior without server**: Exits gracefully with "Connection refused"

## Validation Scenarios

### CRITICAL: Manual Validation Requirements
After making any changes, ALWAYS test the following scenarios:

#### Build Validation
1. **Clean build test**:
   ```bash
   gradle clean build
   ```
   - Expected time: 1-3 seconds
   - Must complete successfully

2. **Application can start test (Method 1 - JAR with classpath)**:
   ```bash
   gradle build
   timeout 10 java -cp "build/libs/Arduino-Caesar-1.0-SNAPSHOT.jar:$(find ~/.gradle -name 'jSerialComm-2.10.4.jar' | head -1)" de.lukas.CaesarArduinoCommunicator
   ```
   - Must show "=== Java Caesar-Verschluesselung mit Arduino ===" then exit gracefully
   - Must NOT throw ClassNotFoundException or other runtime errors

3. **Application can start test (Method 3 - Distribution)** - requires adding application plugin first:
   ```bash
   # Add application plugin to build.gradle.kts first
   gradle installDist
   timeout 10 build/install/Arduino-Caesar/bin/Arduino-Caesar
   ```
   - Must show "=== Java Caesar-Verschluesselung mit Arduino ===" then exit gracefully

#### Functionality Validation
**WITHOUT Arduino hardware** (expected behavior):
- **CaesarArduinoCommunicator**: Shows "=== Java Caesar-Verschluesselung mit Arduino ===" then "Keine seriellen Ports gefunden!"
- **ArduinoServer**: Shows "=== Arduino Server gestartet ===" then "Keine seriellen Ports gefunden!"
- **ArduinoClient**: Shows "=== Arduino Client ===" then "Verbindungsfehler: Connection refused"

Test commands (using Method 1):
```bash
# Test main communicator
java -cp "build/libs/Arduino-Caesar-1.0-SNAPSHOT.jar:$(find ~/.gradle -name 'jSerialComm-2.10.4.jar' | head -1)" de.lukas.CaesarArduinoCommunicator

# Test server component
java -cp "build/libs/Arduino-Caesar-1.0-SNAPSHOT.jar:$(find ~/.gradle -name 'jSerialComm-2.10.4.jar' | head -1)" de.lukas.server.ArduinoServer

# Test client component
java -cp "build/libs/Arduino-Caesar-1.0-SNAPSHOT.jar:$(find ~/.gradle -name 'jSerialComm-2.10.4.jar' | head -1)" de.lukas.client.ArduinoClient
```

**WITH Arduino hardware** (if available):
- Test basic encryption: Send command "E:3:hello" → should get encrypted response
- Test basic decryption: Send command "D:3:khoor" → should get decrypted response

## Key Dependencies

### Runtime Dependencies
- **Java 17+**: Required for Gradle 9.0.0
- **jSerialComm 2.10.4**: Serial port communication with Arduino
- **Arduino with Caesar cipher firmware**: For hardware functionality

### Build Dependencies
- **Gradle 9.0.0**: Build system
- **JUnit 5.10.0**: Testing framework (tests not implemented yet)

## Build System Details

### Build Performance
- **First build**: 60-90 seconds (Gradle daemon startup + dependency download)
- **Subsequent builds**: <1 second (up-to-date checks)
- **Clean builds**: 1-3 seconds (daemon already running)
- **Configuration cache**: Recommended for faster builds

### Available Gradle Tasks
**With basic Java plugin only (current setup):**
- `gradle build` - Build entire project
- `gradle clean` - Clean build directory
- `gradle test` - Run tests (currently no tests exist)
- `gradle check` - Run all verification tasks
- `gradle jar` - Create JAR file (without dependencies)

**With application plugin added:**
- `gradle run` - Run application
- `gradle installDist` - Create distribution with all dependencies
- `gradle distZip` - Create ZIP distribution
- `gradle distTar` - Create TAR distribution

## Repository Structure
```
Rohde/Arduino-Caesar/
├── build.gradle.kts          # Build configuration
├── settings.gradle.kts       # Project settings
├── src/main/java/de/lukas/
│   ├── CaesarArduinoCommunicator.java   # Main direct communication app
│   ├── server/ArduinoServer.java        # TCP server component
│   ├── client/ArduinoClient.java        # TCP client component
│   └── Readme.md             # Detailed protocol documentation
└── build/                    # Generated build artifacts
    ├── libs/                 # JAR files
    └── install/              # Distribution (after installDist)
```

## Common Tasks

### Adding the Application Plugin
When you need to run applications via `gradle run`, modify `build.gradle.kts`:
```kotlin
plugins {
    id("java")
    id("application")
}

application {
    mainClass.set("de.lukas.CaesarArduinoCommunicator")  // Change as needed
}
```

### Switching Between Applications
To run different applications, update the mainClass in build.gradle.kts:
- Direct communication: `"de.lukas.CaesarArduinoCommunicator"`
- Server: `"de.lukas.server.ArduinoServer"`
- Client: `"de.lukas.client.ArduinoClient"`

### Building Standalone Distribution
```bash
gradle installDist
```
Creates `build/install/Arduino-Caesar/` with:
- `bin/Arduino-Caesar` - Unix startup script
- `bin/Arduino-Caesar.bat` - Windows startup script
- `lib/` - All required JAR files

## Troubleshooting

### Common Build Issues
**"ClassNotFoundException: com.fazecast.jSerialComm.SerialPort"**
- Problem: Running JAR without dependencies
- Solution: Use `gradle installDist` or `gradle run` instead

**"Gradle daemon not found"**
- Problem: First-time Gradle usage
- Solution: Wait for daemon startup (adds ~30 seconds to first command)

**"Port 8080 already in use"**
- Problem: ArduinoServer already running
- Solution: Kill existing process or change port in ArduinoServer.java

### Hardware Issues
**"Keine seriellen Ports gefunden"**
- Expected behavior when no Arduino connected
- Check Arduino drivers and USB connection if Arduino should be present

**"Connection refused"**
- Expected behavior when ArduinoServer not running
- Start ArduinoServer first, then ArduinoClient

## Development Notes

### No Tests Currently
- JUnit 5 is configured but no tests exist
- `gradle test` completes immediately
- Consider adding tests for Caesar cipher logic

### No Linting/Formatting
- No checkstyle, spotbugs, or formatting plugins configured
- `gradle check` only runs tests (which don't exist)
- Code style is manual

### Architecture
- Protocol documentation in `src/main/java/de/lukas/Readme.md`
- Client-server uses custom TCP protocol
- Arduino communication via serial port at 9600 baud
- Caesar cipher implemented in both Java and Arduino for verification