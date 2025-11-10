# Taxiunternehmen - Landesabitur 2023 Implementation

Complete Java 17 implementation of the "Taxiunternehmen" exam specification (Landesabitur 2023 - Praktische Informatik LK - Vorschlag B).

## Project Overview

This project implements a comprehensive taxi company management system with two main subsystems:
1. **Central Dispatch System (Taxizentrale)** - manages taxis, drivers, and order assignment
2. **Taxameter System** - in-vehicle fare calculation and trip management with Electronic Key System (EKS)

### Key Features

- ✅ Order data parsing from semicolon-separated format (Task 1.3.1)
- ✅ Intelligent taxi assignment by proximity (Task 1.3.3)
- ✅ Distance-based fare calculation with tiered pricing (Task 1.4.1)
- ✅ Electronic Key System communication protocol (Task 1.4.2)
- ✅ Complete taxameter state machine (Task 1.4.3)
- ✅ Comprehensive database schema with SQL queries (Section 2)

## Technology Stack

- **Language:** Pure Java 17 (no external dependencies)
- **Architecture:** Domain-driven design with clear package separation
- **Testing:** Console-based test applications
- **Database:** SQL schema documentation (MySQL/MariaDB compatible)

## Project Structure

```
src/main/java/de/lukas/taxi/
├── core/                          # Core domain classes
│   ├── Adresse.java              # Address with distance calculation
│   ├── Auftrag.java              # Order with parsing logic
│   ├── DateTime.java             # Date/time wrapper
│   ├── Fahrer.java               # Driver
│   ├── List.java                 # Generic list wrapper
│   └── Taxi.java                 # Taxi vehicle
│
├── zentrale/                      # Central dispatch system
│   └── TaxiZentrale.java         # Main dispatch logic
│
├── meter/                         # Taxameter subsystem
│   ├── Display.java              # Driver display
│   ├── EksAdapter.java           # Electronic Key System adapter
│   ├── Serial.java               # Serial port (mock)
│   ├── Taxameter.java            # Main taxameter logic
│   └── ThermalPrinter.java       # Receipt printer
│
├── tests/                         # Console test applications
│   ├── MainAuftragParsing.java   # Test order parsing
│   ├── MainNearestTaxis.java     # Test taxi sorting
│   ├── MainTarif.java            # Test fare calculation
│   ├── MainWriteData.java        # Test EKS protocol
│   └── MainRun.java              # Test taxameter state machine
│
└── db/                            # Database documentation
    └── README.md                  # SQL schema and queries
```

## How to Compile and Run

### Prerequisites

- Java Development Kit (JDK) 17 or higher
- No build tool required (plain Java compilation)

### Compilation

From the project root directory:

```bash
# Compile all source files
javac -d bin src/main/java/de/lukas/taxi/**/*.java

# Alternative: compile each package separately
javac -d bin src/main/java/de/lukas/taxi/core/*.java
javac -cp bin -d bin src/main/java/de/lukas/taxi/zentrale/*.java
javac -cp bin -d bin src/main/java/de/lukas/taxi/meter/*.java
javac -cp bin -d bin src/main/java/de/lukas/taxi/tests/*.java
```

### Running Tests

Each test can be run independently:

```bash
# Test 1: Order parsing (Task 1.3.1)
java -cp bin de.lukas.taxi.tests.MainAuftragParsing

# Test 2: Nearest taxis (Task 1.3.3)
java -cp bin de.lukas.taxi.tests.MainNearestTaxis

# Test 3: Fare calculation (Task 1.4.1)
java -cp bin de.lukas.taxi.tests.MainTarif

# Test 4: EKS protocol (Task 1.4.2)
java -cp bin de.lukas.taxi.tests.MainWriteData

# Test 5: Taxameter state machine (Task 1.4.3)
java -cp bin de.lukas.taxi.tests.MainRun
```

### Quick Test Script

Create a simple test script (Linux/Mac):

```bash
#!/bin/bash
echo "=== Compiling ==="
mkdir -p bin
javac -d bin src/main/java/de/lukas/taxi/**/*.java

echo -e "\n=== Running Tests ===\n"
for test in MainAuftragParsing MainNearestTaxis MainTarif MainWriteData MainRun
do
    echo "--- $test ---"
    java -cp bin de.lukas.taxi.tests.$test
    echo ""
done
```

Windows PowerShell:

```powershell
# Compile
New-Item -ItemType Directory -Force -Path bin
javac -d bin (Get-ChildItem -Recurse -Filter "*.java" src/main/java/de/lukas/taxi).FullName

# Run tests
$tests = @("MainAuftragParsing", "MainNearestTaxis", "MainTarif", "MainWriteData", "MainRun")
foreach ($test in $tests) {
    Write-Host "--- $test ---"
    java -cp bin de.lukas.taxi.tests.$test
}
```

## Design Documentation

### Task 1.3: Core Domain Implementation

#### 1.3.1 Auftrag Constructor (Order Parsing)

The `Auftrag` class constructor parses semicolon-separated order data:

**Format:** `"von; nach; start; ende; km; preis"`
- Addresses use format: `"[strasse],[plz],[ort]"`
- Dates use format: `"dd.MM.yyyy HH:mm"`

**Implementation:**
- Splits input by semicolon
- Validates exactly 6 parts
- Parses addresses using comma-separated format
- Creates DateTime objects for start/end times
- Throws IllegalArgumentException on invalid format

**Example:**
```java
String data = "Sonnenweg 15,60487,Frankfurt a.M.; Markt 17,60311,Frankfurt a.M.; " +
              "14.04.2022 16:28; 14.04.2022 17:10; 20.0; 42.25";
Auftrag auftrag = new Auftrag(taxi, fahrer, data);
```

#### 1.3.2 TaxiZentrale.bearbeiteAuftrag() Sequence

**Algorithm:**
1. Call `getTaxisSortiertNachStandort(von)` to get sorted list of free taxis
2. Iterate through available taxis
3. For each taxi, call `anfragenBereitschaft(fahrer, von, nach)`
4. If driver accepts, call `vergebeAuftrag(fahrer, von, nach)`
5. Return true on successful assignment, false otherwise

**Sequence Diagram (textual):**
```
User -> TaxiZentrale: bearbeiteAuftrag(von, nach)
TaxiZentrale -> TaxiZentrale: getTaxisSortiertNachStandort(von)
TaxiZentrale -> TaxiZentrale: [for each taxi in sorted list]
TaxiZentrale -> Fahrer: anfragenBereitschaft(von, nach)
Fahrer -> TaxiZentrale: boolean (accept/reject)
TaxiZentrale -> TaxiZentrale: [if accepted] vergebeAuftrag(fahrer, von, nach)
TaxiZentrale -> Taxi: setStatus(ANFAHRT_KUNDE)
TaxiZentrale -> User: return true/false
```

#### 1.3.3 getTaxisSortiertNachStandort() Implementation

**Algorithm:**
1. Filter taxis: only include those with `status == FREI`
2. Calculate distance from `von` to each taxi's current location using `ermittleEntfernungNach()`
3. Sort taxis by distance (ascending)
4. Return first 5 (or fewer if less available)

**Complexity:** O(n log n) where n = number of free taxis

### Task 1.4: Taxameter Subsystem

#### 1.4.1 calculatePrice() - Fare Calculation

**Tariff Structure:**
- Base fare: 3.50 EUR
- First 15 km: 2.00 EUR per km
- From 16th km onwards: 1.75 EUR per km

**Algorithm (Nassi-Shneiderman structure):**
```
┌─────────────────────────────────────┐
│ price = GRUNDPREIS (3.50 EUR)       │
└─────────────────────────────────────┘
           │
┌──────────▼──────────┐
│ distanceKm <= 15?   │
├──────────┬──────────┤
│   YES    │    NO    │
├──────────┤──────────┤
│ price += │ price += │
│ distance │ 15 * 2.0 │
│ * 2.0    │ +        │
│          │ (dist-15)│
│          │ * 1.75   │
└──────────┴──────────┘
```

**Implementation:**
```java
public double calculatePrice(double distanceKm) {
    double preis = GRUNDPREIS;
    if (distanceKm <= 0) {
        return preis;
    }
    if (distanceKm <= 15.0) {
        preis += distanceKm * TARIF1;
    } else {
        preis += 15.0 * TARIF1;
        preis += (distanceKm - 15.0) * TARIF2;
    }
    return preis;
}
```

**Examples:**
- 0 km: 3.50 EUR
- 10 km: 3.50 + (10 × 2.00) = 23.50 EUR
- 15 km: 3.50 + (15 × 2.00) = 33.50 EUR
- 20 km: 3.50 + (15 × 2.00) + (5 × 1.75) = 42.25 EUR

#### 1.4.2 EksAdapter.writeData() - Electronic Key Protocol

**Handshake Protocol:**

```
Step 1: Send STX (0x02)
           │
           ▼
Step 2: Wait for response
           ├─── DLE (0x10) ──► Continue to Step 3
           ├─── NAK (0x15) ──► Retry from Step 1
           └─── EM (0x19)  ──► Return 1 (memory full)
           │
           ▼
Step 3: Send data bytes + DLE + ETX + checksum
           │
           ▼
Step 4: Wait for acknowledgement
           ├─── DLE ──► Return 0 (success)
           ├─── NAK ──► Retry from Step 1
           └─── EM  ──► Return 1 (memory full)
```

**Return Codes:**
- 0: Success
- 1: Memory full (EM received)
- 2: Serial not open or key missing

**Retry Logic:**
- Maximum 3 retry attempts
- Retry on NAK response
- Abort on EM (no memory)

#### 1.4.3 Taxameter.run() - State Machine

**Button Functions:**

| Button | Function | Taxi Status | Taxi Sign | Action |
|--------|----------|-------------|-----------|--------|
| 1 | Free | FREI | ON | Ready for orders |
| 2 | Approaching customer | ANFAHRT_KUNDE | OFF | Heading to pickup |
| 3 | Approaching destination | ANFAHRT_ZIEL | OFF | Record start data (km, time, address) |
| 4 | Destination reached | → FREI | ON | Calculate fare, save to key, display price |
| 5 | Print receipt | (no change) | (no change) | Print last trip data |

**State Transitions:**
```
NICHT_IN_BETRIEB (no key)
    ↓ [key inserted]
FREI (button 1)
    ↓ [button 2]
ANFAHRT_KUNDE
    ↓ [button 3]
ANFAHRT_ZIEL
    ↓ [button 4]
FREI (with saved trip data)
    ↓ [button 5]
(receipt printed)
```

**Data Format for Export:**
```
"[wagenNr];[fahrerNr];[von];[nach];[start];[ende];[fahrstrecke];[fahrpreis]"
```

Example:
```
"1;101;Hauptstraße 1,60311,Frankfurt;Bahnhof 10,60329,Frankfurt;01.05.2023 10:00;01.05.2023 10:30;10.0;23.50"
```

### Database Design (Section 2)

See `src/main/java/de/lukas/taxi/db/README.md` for complete documentation including:
- Relational schema in 3NF
- All SQL CREATE TABLE statements
- SQL queries for tasks 2.2.1-2.2.5
- Extended ER model with vehicle models, damages, and workshops
- Anomalies analysis

**Key Tables:**
- `Fahrer` (Driver)
- `Taxi` (Vehicle)
- `EinsatzFahrer` (Driver Deployment)
- `Auftrag` (Order)
- `AdresseOrtsteil` (Address)
- `Ortsteil` (District)

## Testing and Verification

### Test Coverage

1. **MainAuftragParsing**: Validates order parsing logic, error handling
2. **MainNearestTaxis**: Verifies taxi sorting, filtering by status, distance calculation
3. **MainTarif**: Tests all fare calculation scenarios including boundary cases
4. **MainWriteData**: Simulates all EKS protocol responses (DLE, NAK, EM)
5. **MainRun**: Executes complete trip sequence through state machine

### Expected Output Examples

**MainTarif output:**
```
Distance (km) | Fare (EUR) | Calculation
-------------|-----------|------------
         0.0 |      3.50 | Base fare only
        15.0 |     33.50 | 3.50 + 15.0 * 2.00
        20.0 |     42.25 | 3.50 + 15.0 * 2.00 + 5.0 * 1.75
```

**MainNearestTaxis output:**
```
Found 5 free taxi(s):
  1. Taxi 1 - Distance: 300m - Location: Hauptstraße 1,60311,Frankfurt a.M.
  2. Taxi 2 - Distance: 1500m - Location: Bahnhofstraße 10,60329,Frankfurt a.M.
  ...
```

## Coding Standards and Conventions

### Java Conventions
- **Package naming:** lowercase, domain-style (de.lukas.taxi.*)
- **Class naming:** PascalCase
- **Method naming:** camelCase
- **Constants:** UPPER_SNAKE_CASE
- **Javadoc:** All public classes and methods documented

### Design Patterns
- **Factory pattern:** DateTime constructors
- **Wrapper pattern:** List<T> wraps ArrayList
- **State pattern:** Taxi status management
- **Strategy pattern:** Distance calculation (stub for testing)

### Error Handling
- IllegalArgumentException for invalid input formats
- Null checks with appropriate defaults
- Return codes for protocol operations (0/1/2)

## Implementation Notes

### Distance Calculation
The `Adresse.ermittleEntfernungNach()` method uses a deterministic stub implementation based on:
- Postal code numerical difference
- String hash codes for street names
- City equality check

This ensures consistent test results without requiring actual GPS/geocoding.

### Serial Communication Mock
The `Serial` class provides a complete mock implementation with:
- Simulated response injection for testing
- Buffer management
- Blocking read operations (with timeout consideration)

### DateTime Wrapper
Uses Java 17's `java.time` API internally but provides a simplified interface matching the exam specification.

## Exam Task Mapping

| Exam Task | Implementation | Test |
|-----------|---------------|------|
| 1.3.1 | Auftrag constructor | MainAuftragParsing |
| 1.3.2 | TaxiZentrale.bearbeiteAuftrag() | (documented in code) |
| 1.3.3 | TaxiZentrale.getTaxisSortiertNachStandort() | MainNearestTaxis |
| 1.4.1 | Taxameter.calculatePrice() | MainTarif |
| 1.4.2 | EksAdapter.writeData() | MainWriteData |
| 1.4.3 | Taxameter.run() | MainRun |
| 2.1 | db/README.md - SQL CREATE | N/A |
| 2.2.1-5 | db/README.md - SQL queries | N/A |
| 2.3 | db/README.md - Extended ER | N/A |
| 2.4 | db/README.md - Anomalies | N/A |

## License and Attribution

This is an educational implementation of the "Taxiunternehmen" exam specification from Landesabitur 2023 - Praktische Informatik (LK) - Vorschlag B.

Implemented in pure Java 17 without external dependencies as per exam requirements.

## Author

Implementation by: Lukas (2025)
Based on: Landesabitur 2023 exam specification

---

**Project Status:** ✅ Complete

All exam tasks implemented and tested. Ready for evaluation.
