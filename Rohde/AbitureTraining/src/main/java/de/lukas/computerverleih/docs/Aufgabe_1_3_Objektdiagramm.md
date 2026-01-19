# Aufgabe 1.3 - UML-Objektdiagramm (5 BE)

## Aufgabenstellung
Die Schülerin Kaja Njo leiht am 28.09.2024 den PC mit Nr. 203 mit folgender Konfiguration
aus: Dell M4500, 2,4 GHz, Leistungsindex 7. Der Computer wird voraussichtlich am
01.07.2025 zurückgebracht. Im aktuellen Schuljahr 2024/25 (01.08.2024 bis 31.07.2025) ist sie
Schülerin der Klasse 11BGP3 der Schulform BG. Im vergangenen Schuljahr 2023/24
(01.08.2023 bis 31.07.2024) war sie Schülerin der Klasse 12SFE1 der Schulform SFE und hat
den gleichen PC vom 01.02.2024 bis 30.06.2024 ausgeliehen und hat diesen zurückgebracht.

Entwickeln und zeichnen Sie ein UML-Objektdiagramm unter Berücksichtigung des
Klassendiagramms in Material 1.

---

## Lösung

### Objektdiagramm (Textuelle Darstellung)

```
+---------------------------+
| kaja : Schueler           |
+---------------------------+
| nr = 1000                 |
| nachname = "Njo"          |
| vorname = "Kaja"          |
| geburtsdatum = 15.05.2006 |
+---------------------------+
        |
        | meineKlassen
        |
        v
+-----------------------------+     +-----------------------------+
| klasse1 : Klasse            |     | klasse2 : Klasse            |
+-----------------------------+     +-----------------------------+
| nr = 10                     |     | nr = 11                     |
| bezeichnung = "12SFE1"      |     | bezeichnung = "11BGP3"      |
| schulform = "SFE"           |     | schulform = "BG"            |
| beginn = 01.08.2023         |     | beginn = 01.08.2024         |
| ende = 31.07.2024           |     | ende = 31.07.2025           |
+-----------------------------+     +-----------------------------+


+---------------------------+
| pc203 : Computer          |
+---------------------------+
| nr = 203                  |
| bezeichnung = "Dell M4500"|
| leistungsindex = 7        |
| cpu = "2,4 GHz"           |
| autowert = 100            |
+---------------------------+
        |
        | ausleihen
        |
        v
+-----------------------------+     +-----------------------------+
| ausleihe1 : Ausleihe        |     | ausleihe2 : Ausleihe        |
+-----------------------------+     +-----------------------------+
| ausleihdatum = 01.02.2024   |     | ausleihdatum = 28.09.2024   |
| leihEnde = 30.06.2024       |     | leihEnde = 01.07.2025       |
| rueckgabe = true            |     | rueckgabe = false           |
+-----------------------------+     +-----------------------------+
        |                                   |
        | leiher                            | leiher
        +-----------------------------------+
                        |
                        v
               +---------------------------+
               | kaja : Schueler           |
               +---------------------------+
```

---

### Detaillierte Objektbeschreibung

#### Objekt: kaja (Instanz von Schueler)
```
kaja : Schueler
----------------------------
nr = 1000
nachname = "Njo"
vorname = "Kaja"
geburtsdatum = 15.05.2006
meineKlassen = [klasse1, klasse2]
```

#### Objekt: klasse1 (Instanz von Klasse) - Vergangenes Schuljahr
```
klasse1 : Klasse
----------------------------
nr = 10
bezeichnung = "12SFE1"
schulform = "SFE"
beginn = 01.08.2023
ende = 31.07.2024
```

#### Objekt: klasse2 (Instanz von Klasse) - Aktuelles Schuljahr
```
klasse2 : Klasse
----------------------------
nr = 11
bezeichnung = "11BGP3"
schulform = "BG"
beginn = 01.08.2024
ende = 31.07.2025
```

#### Objekt: pc203 (Instanz von Computer)
```
pc203 : Computer
----------------------------
nr = 203
bezeichnung = "Dell M4500"
leistungsindex = 7
cpu = "2,4 GHz"
ausleihen = [ausleihe1, ausleihe2]
```

#### Objekt: ausleihe1 (Instanz von Ausleihe) - Vergangene Ausleihe
```
ausleihe1 : Ausleihe
----------------------------
ausleihdatum = 01.02.2024
leihEnde = 30.06.2024
rueckgabe = true
computer = pc203
leiher = kaja
```

#### Objekt: ausleihe2 (Instanz von Ausleihe) - Aktuelle Ausleihe
```
ausleihe2 : Ausleihe
----------------------------
ausleihdatum = 28.09.2024
leihEnde = 01.07.2025
rueckgabe = false
computer = pc203
leiher = kaja
```

---

### Beziehungen zwischen den Objekten

1. **kaja -> klasse1, klasse2** (meineKlassen)
   - Kaja war in zwei Klassen: zuerst 12SFE1, dann 11BGP3
   - Die Liste enthält beide Klassen in chronologischer Reihenfolge

2. **pc203 -> ausleihe1, ausleihe2** (ausleihen)
   - Der PC wurde zweimal ausgeliehen
   - Beide Male an dieselbe Person (Kaja)

3. **ausleihe1 -> kaja** (leiher)
   - Die erste Ausleihe gehört zu Kaja

4. **ausleihe2 -> kaja** (leiher)
   - Die zweite Ausleihe gehört ebenfalls zu Kaja

5. **ausleihe1 -> pc203** (computer)
   - Die erste Ausleihe bezieht sich auf PC 203

6. **ausleihe2 -> pc203** (computer)
   - Die zweite Ausleihe bezieht sich ebenfalls auf PC 203

---

### Hinweise zur Erstellung

1. **Objektname : Klassenname** - z.B. `kaja : Schueler`
2. Alle Attributwerte werden konkret angegeben (keine Typen!)
3. Beziehungen werden durch Linien dargestellt
4. Die Linien sind mit dem Rollennamen beschriftet (z.B. "meineKlassen")
5. Es werden keine Methoden dargestellt (nur bei Klassendiagrammen)
6. Autowert-Attribute wurden sinnvoll ergänzt basierend auf dem Klassendiagramm
