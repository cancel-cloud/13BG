# Database Documentation - Taxiunternehmen

This document contains all database-related tasks from Section 2 of the exam specification.

---

## 2.1 Relational Schema in 3NF

Based on Material 7 (ERM for deployment planning), the relational schema in Third Normal Form:

### CREATE TABLE Statements

```sql
-- Ortsteil (District)
CREATE TABLE Ortsteil (
    oID INT AUTO_INCREMENT PRIMARY KEY,
    bezeichnung VARCHAR(100) NOT NULL UNIQUE
);

-- AdresseOrtsteil (Address)
CREATE TABLE AdresseOrtsteil (
    aID INT AUTO_INCREMENT PRIMARY KEY,
    strasse VARCHAR(200) NOT NULL,
    plz VARCHAR(10) NOT NULL,
    ort VARCHAR(100) NOT NULL,
    oID INT NOT NULL,
    FOREIGN KEY (oID) REFERENCES Ortsteil(oID)
);

-- Fahrer (Driver)
CREATE TABLE Fahrer (
    fahrerNr INT PRIMARY KEY,
    vorname VARCHAR(100) NOT NULL,
    nachname VARCHAR(100) NOT NULL,
    stundenlohn DECIMAL(10, 2) NOT NULL,
    iban VARCHAR(34) NOT NULL
);

-- Taxi
CREATE TABLE Taxi (
    wagenNr INT PRIMARY KEY,
    kennzeichen VARCHAR(20) NOT NULL UNIQUE,
    kmStand DOUBLE NOT NULL DEFAULT 0.0,
    status INT NOT NULL DEFAULT 1
);

-- EinsatzFahrer (Driver Deployment)
CREATE TABLE EinsatzFahrer (
    eID INT AUTO_INCREMENT PRIMARY KEY,
    fahrerNr INT NOT NULL,
    wagenNr INT NOT NULL,
    beginn DATETIME NOT NULL,
    ende DATETIME NOT NULL,
    FOREIGN KEY (fahrerNr) REFERENCES Fahrer(fahrerNr),
    FOREIGN KEY (wagenNr) REFERENCES Taxi(wagenNr)
);

-- Auftrag (Order)
CREATE TABLE Auftrag (
    auftragNr INT AUTO_INCREMENT PRIMARY KEY,
    wagenNr INT NOT NULL,
    fahrerNr INT NOT NULL,
    vonAID INT NOT NULL,
    nachAID INT NOT NULL,
    start DATETIME NOT NULL,
    ende DATETIME NOT NULL,
    fahrstrecke DOUBLE NOT NULL,
    fahrpreis DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (wagenNr) REFERENCES Taxi(wagenNr),
    FOREIGN KEY (fahrerNr) REFERENCES Fahrer(fahrerNr),
    FOREIGN KEY (vonAID) REFERENCES AdresseOrtsteil(aID),
    FOREIGN KEY (nachAID) REFERENCES AdresseOrtsteil(aID)
);
```

### Justification for 3NF:

**First Normal Form (1NF):**
- All attributes are atomic (no multi-valued attributes)
- Each table has a primary key
- No repeating groups

**Second Normal Form (2NF):**
- All non-key attributes are fully functionally dependent on the primary key
- No partial dependencies (relevant for composite keys)

**Third Normal Form (3NF):**
- No transitive dependencies
- Each non-key attribute depends only on the primary key
- Example: `AdresseOrtsteil.oID` references `Ortsteil` separately to avoid redundancy

**Key Design Decisions:**
1. `oID`, `aID`, `eID` use AUTO_INCREMENT as specified in Material 7
2. `Ortsteil` is separated to avoid redundant storage of district names
3. `AdresseOrtsteil` contains full address information plus reference to district
4. `EinsatzFahrer` represents the many-to-many relationship between drivers and taxis over time
5. `Auftrag` references addresses via foreign keys instead of storing address strings

---

## 2.2 SQL Queries

### 2.2.1 INSERT: New Driver with First Deployment

**Task:** Lennard Knebel, driver number 333, hourly wage 12 EUR, first deployment 18.05.2023 08:00-17:00, taxi F-TT 661.

```sql
-- Insert new driver
INSERT INTO Fahrer (fahrerNr, vorname, nachname, stundenlohn, iban)
VALUES (333, 'Lennard', 'Knebel', 12.00, 'DE00000000000000000000');

-- Find the wagenNr for taxi with kennzeichen F-TT 661
-- Assuming the taxi exists, we need its wagenNr
-- If wagenNr is 10 for example:
INSERT INTO EinsatzFahrer (fahrerNr, wagenNr, beginn, ende)
VALUES (
    333,
    (SELECT wagenNr FROM Taxi WHERE kennzeichen = 'F-TT 661'),
    '2023-05-18 08:00:00',
    '2023-05-18 17:00:00'
);
```

**Alternative with known wagenNr:**
```sql
INSERT INTO Fahrer (fahrerNr, vorname, nachname, stundenlohn, iban)
VALUES (333, 'Lennard', 'Knebel', 12.00, 'DE00000000000000000000');

INSERT INTO EinsatzFahrer (fahrerNr, wagenNr, beginn, ende)
VALUES (333, 10, '2023-05-18 08:00:00', '2023-05-18 17:00:00');
```

---

### 2.2.2 UPDATE: Replace Driver Due to Illness

**Task:** Tobias Tischendorf is sick; replace his deployments from 01.05.2023 to 14.05.2023 with Knebel (333). Assume driver names are unique.

```sql
UPDATE EinsatzFahrer
SET fahrerNr = 333
WHERE fahrerNr = (
    SELECT fahrerNr
    FROM Fahrer
    WHERE vorname = 'Tobias' AND nachname = 'Tischendorf'
)
AND beginn >= '2023-05-01 00:00:00'
AND ende <= '2023-05-14 23:59:59';
```

**Alternative without subquery (if fahrerNr known):**
```sql
UPDATE EinsatzFahrer
SET fahrerNr = 333
WHERE fahrerNr = (SELECT fahrerNr FROM Fahrer WHERE nachname = 'Tischendorf' LIMIT 1)
AND DATE(beginn) >= '2023-05-01'
AND DATE(ende) <= '2023-05-14';
```

---

### 2.2.3 SELECT: Taxis Without Deployment on Specific Date

**Task:** Find all taxis that have no deployment on 30.05.2023.

```sql
SELECT t.wagenNr, t.kennzeichen
FROM Taxi t
WHERE t.wagenNr NOT IN (
    SELECT DISTINCT e.wagenNr
    FROM EinsatzFahrer e
    WHERE DATE(e.beginn) <= '2023-05-30'
      AND DATE(e.ende) >= '2023-05-30'
)
ORDER BY t.wagenNr;
```

**Alternative using LEFT JOIN:**
```sql
SELECT t.wagenNr, t.kennzeichen
FROM Taxi t
LEFT JOIN EinsatzFahrer e ON t.wagenNr = e.wagenNr
    AND '2023-05-30' BETWEEN DATE(e.beginn) AND DATE(e.ende)
WHERE e.eID IS NULL
ORDER BY t.wagenNr;
```

---

### 2.2.4 SELECT: Timesheet for Specific Driver and Month

**Task:** Create timesheet for Karl-Heinz Großfuss for March (showing from-to times and duration in hours).

Uses `TIMESTAMPDIFF(MINUTE, t1, t2)` to calculate duration.

```sql
SELECT
    f.vorname,
    f.nachname,
    e.beginn AS von,
    e.ende AS bis,
    TIMESTAMPDIFF(MINUTE, e.beginn, e.ende) / 60.0 AS stunden
FROM EinsatzFahrer e
JOIN Fahrer f ON e.fahrerNr = f.fahrerNr
WHERE f.vorname = 'Karl-Heinz'
  AND f.nachname = 'Großfuss'
  AND YEAR(e.beginn) = 2023
  AND MONTH(e.beginn) = 3
ORDER BY e.beginn;
```

**With formatted output:**
```sql
SELECT
    CONCAT(f.vorname, ' ', f.nachname) AS fahrer,
    DATE_FORMAT(e.beginn, '%d.%m.%Y %H:%i') AS von,
    DATE_FORMAT(e.ende, '%d.%m.%Y %H:%i') AS bis,
    CONCAT(FLOOR(TIMESTAMPDIFF(MINUTE, e.beginn, e.ende) / 60), ' Stunden') AS dauer
FROM EinsatzFahrer e
JOIN Fahrer f ON e.fahrerNr = f.fahrerNr
WHERE f.vorname = 'Karl-Heinz'
  AND f.nachname = 'Großfuss'
  AND YEAR(e.beginn) = 2023
  AND MONTH(e.beginn) = 3
ORDER BY e.beginn;
```

---

### 2.2.5 SELECT: Monthly Salary Report

**Task:** List sorted by driver name with IBAN and monthly salary for March 2023. Consider deployments that END in March.

```sql
SELECT
    CONCAT(f.nachname, ', ', f.vorname) AS fahrer,
    f.iban,
    SUM(TIMESTAMPDIFF(MINUTE, e.beginn, e.ende) / 60.0 * f.stundenlohn) AS monatslohn
FROM Fahrer f
JOIN EinsatzFahrer e ON f.fahrerNr = e.fahrerNr
WHERE YEAR(e.ende) = 2023
  AND MONTH(e.ende) = 3
GROUP BY f.fahrerNr, f.vorname, f.nachname, f.iban
ORDER BY f.nachname, f.vorname;
```

**With formatted currency:**
```sql
SELECT
    CONCAT(f.nachname, ', ', f.vorname) AS fahrer,
    f.iban,
    CONCAT(FORMAT(SUM(TIMESTAMPDIFF(MINUTE, e.beginn, e.ende) / 60.0 * f.stundenlohn), 2), ' EUR') AS monatslohn
FROM Fahrer f
JOIN EinsatzFahrer e ON f.fahrerNr = e.fahrerNr
WHERE YEAR(e.ende) = 2023
  AND MONTH(e.ende) = 3
GROUP BY f.fahrerNr, f.vorname, f.nachname, f.iban
ORDER BY f.nachname, f.vorname;
```

---

## 2.3 Extended ER Model

### New Requirements:

1. **Additional taxi attributes** (e.g., color, year of manufacture, capacity)
2. **Vehicle models** with make, model name, and drive type
3. **Drive types** including hybrid support
4. **Damage and repair tracking**
5. **Workshop management** with contact persons

### Extended ER Model Description:

**New Entities:**

1. **FahrzeugModell** (Vehicle Model)
   - Attributes: modellID (PK), hersteller, modellName
   - Relationship: (0,n) to Taxi - one model can be used by many taxis

2. **Antriebsart** (Drive Type)
   - Attributes: antriebsartID (PK), bezeichnung (e.g., "Benzin", "Diesel", "Elektro", "Hybrid")
   - Relationship: (1,n) to FahrzeugModell - one drive type for many models

3. **HybridAntrieb** (Hybrid Drive) - subtype of Antriebsart
   - Additional attributes: hauptAntrieb, zusatzAntrieb
   - Example: "Hybrid Benzin-Elektro"

4. **Schaden** (Damage)
   - Attributes: schadenID (PK), wagenNr (FK), datum, beschreibung, schweregrad
   - Relationship: (0,n) from Taxi - one taxi can have multiple damages

5. **Reparatur** (Repair)
   - Attributes: reparaturID (PK), schadenID (FK), werkstattID (FK), datum, kosten, beschreibung
   - Relationship: (1,1) to Schaden - each damage can have one or more repairs
   - Relationship: (0,n) from Werkstatt - workshop handles many repairs

6. **Werkstatt** (Workshop)
   - Attributes: werkstattID (PK), name, strasse, plz, ort, telefon
   - Relationship: (1,n) to Ansprechpartner - workshop has contact persons

7. **Ansprechpartner** (Contact Person)
   - Attributes: ansprechpartnerID (PK), werkstattID (FK), name, telefon, email, position
   - Relationship: (1,1) to Werkstatt - belongs to one workshop

### SQL CREATE TABLE Statements for Extension:

```sql
-- Antriebsart (Drive Type)
CREATE TABLE Antriebsart (
    antriebsartID INT AUTO_INCREMENT PRIMARY KEY,
    bezeichnung VARCHAR(50) NOT NULL UNIQUE,
    istHybrid BOOLEAN NOT NULL DEFAULT FALSE,
    hauptAntrieb VARCHAR(50),
    zusatzAntrieb VARCHAR(50)
);

-- FahrzeugModell (Vehicle Model)
CREATE TABLE FahrzeugModell (
    modellID INT AUTO_INCREMENT PRIMARY KEY,
    hersteller VARCHAR(100) NOT NULL,
    modellName VARCHAR(100) NOT NULL,
    antriebsartID INT NOT NULL,
    FOREIGN KEY (antriebsartID) REFERENCES Antriebsart(antriebsartID),
    UNIQUE KEY (hersteller, modellName)
);

-- Extended Taxi table
ALTER TABLE Taxi
ADD COLUMN modellID INT,
ADD COLUMN farbe VARCHAR(50),
ADD COLUMN baujahr INT,
ADD COLUMN sitzplaetze INT DEFAULT 4,
ADD FOREIGN KEY (modellID) REFERENCES FahrzeugModell(modellID);

-- Schaden (Damage)
CREATE TABLE Schaden (
    schadenID INT AUTO_INCREMENT PRIMARY KEY,
    wagenNr INT NOT NULL,
    meldeDatum DATETIME NOT NULL,
    beschreibung TEXT NOT NULL,
    schweregrad ENUM('gering', 'mittel', 'schwer') NOT NULL,
    status ENUM('gemeldet', 'in_reparatur', 'repariert') DEFAULT 'gemeldet',
    FOREIGN KEY (wagenNr) REFERENCES Taxi(wagenNr)
);

-- Werkstatt (Workshop)
CREATE TABLE Werkstatt (
    werkstattID INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    strasse VARCHAR(200) NOT NULL,
    plz VARCHAR(10) NOT NULL,
    ort VARCHAR(100) NOT NULL,
    telefon VARCHAR(20) NOT NULL
);

-- Ansprechpartner (Contact Person)
CREATE TABLE Ansprechpartner (
    ansprechpartnerID INT AUTO_INCREMENT PRIMARY KEY,
    werkstattID INT NOT NULL,
    vorname VARCHAR(100) NOT NULL,
    nachname VARCHAR(100) NOT NULL,
    telefon VARCHAR(20),
    email VARCHAR(200),
    position VARCHAR(100),
    FOREIGN KEY (werkstattID) REFERENCES Werkstatt(werkstattID)
);

-- Reparatur (Repair)
CREATE TABLE Reparatur (
    reparaturID INT AUTO_INCREMENT PRIMARY KEY,
    schadenID INT NOT NULL,
    werkstattID INT NOT NULL,
    annahmeDatum DATETIME NOT NULL,
    abschlussDatum DATETIME,
    kosten DECIMAL(10, 2),
    beschreibung TEXT,
    FOREIGN KEY (schadenID) REFERENCES Schaden(schadenID),
    FOREIGN KEY (werkstattID) REFERENCES Werkstatt(werkstattID)
);
```

### Cardinalities:

- Taxi (0,1) --- (1,1) FahrzeugModell: Each taxi has exactly one model
- FahrzeugModell (1,1) --- (0,n) Antriebsart: Each model has one drive type
- Taxi (0,n) --- (1,1) Schaden: Each taxi can have multiple damages
- Schaden (1,1) --- (1,n) Reparatur: Each damage can have multiple repairs
- Werkstatt (1,n) --- (0,1) Reparatur: Workshop handles many repairs
- Werkstatt (1,n) --- (1,1) Ansprechpartner: Workshop has multiple contact persons

---

## 2.4 Anomalies Analysis (Material 8)

### Material 8: "Einsatzfahrten in Wiesbaden 2023" Table

The table in Material 8 shows a non-normalized structure with redundant information.

### Identified Redundancies:

1. **Driver telephone numbers** are repeated for each trip
2. **Vehicle type information** is repeated for each use of the same taxi
3. **Driver names** are stored multiple times
4. **Address information** may be redundant if the same locations are used repeatedly

### Inconsistencies:

1. **Spelling variations** in driver names (e.g., different formatting)
2. **Phone number format** inconsistencies
3. **Address abbreviations** may vary

### Anomalies with Examples:

#### 1. **Insert Anomaly (Einfügeanomalie)**

**Problem:** Cannot insert information about a new taxi or driver without having a completed trip.

**Example:**
- We want to add a new driver "Thomas Meyer" with phone "0611-123456" to our database
- **Cannot do this** without also creating a fake trip record
- Must wait until the driver completes their first trip
- This violates the principle that entities should be independently manageable

#### 2. **Update Anomaly (Änderungsanomalie)**

**Problem:** When updating information, must update multiple rows, risking inconsistency.

**Example from Material 8:**
- Driver "Max Müller" changes his phone number from "0611-724567" to "0611-999999"
- Must update **every row** where Max Müller appears
- If we miss even one row, we have **inconsistent data** (some rows show old number, some show new)
- Same problem applies to vehicle type changes (e.g., if "VW Caddy" specifications change)

**Specific scenario:**
- If taxi "WI-TF 25" (VW Caddy) gets upgraded and we need to change vehicle type
- Must find and update every single trip record for that taxi
- Risk of partial updates leading to inconsistency

#### 3. **Delete Anomaly (Löschanomalie)**

**Problem:** Deleting trip data causes loss of other important information.

**Example:**
- Driver "Finn Kager" has completed only one trip so far
- If we delete that trip record (e.g., it was cancelled or erroneous)
- We **lose all information** about Finn Kager, including:
  - Phone number: "0611-894328"
  - The fact that this driver exists in our system
  - Any other metadata we might have stored

**Another example:**
- Taxi with kennzeichen "WI-TF 28" (Toyota Corolla) has only one recorded trip
- Deleting that trip means we lose the information that this taxi exists
- We lose the link between the kennzeichen and the vehicle type

### Solution:

**Normalize the database** as shown in sections 2.1 and 2.3:
- Separate entities: Fahrer, Taxi, FahrzeugModell, Einsatzfahrt
- Use foreign keys to establish relationships
- Each entity can exist independently
- Updates occur in one place only
- Deletes don't cause loss of unrelated information

**Benefits of normalization:**
1. **No insert anomaly:** Can add drivers and taxis independently
2. **No update anomaly:** Phone number stored once in Fahrer table
3. **No delete anomaly:** Deleting a trip doesn't delete driver or taxi information

---

## Summary

This database documentation covers:
- ✅ 2.1: Relational schema in 3NF with CREATE TABLE statements
- ✅ 2.2.1-2.2.5: All SQL queries (INSERT, UPDATE, SELECT)
- ✅ 2.3: Extended ER model with new entities and relationships
- ✅ 2.4: Anomalies analysis with specific examples from Material 8

All SQL statements follow standard syntax and can be executed on MySQL/MariaDB or similar RDBMS.
