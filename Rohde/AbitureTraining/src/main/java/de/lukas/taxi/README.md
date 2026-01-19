# Taxiunternehmen

Java implementation of the taxi company tasks (Abitur 2023, LK, Proposal B). This package groups the dispatch system, taxameter subsystem, console tests, and database documentation.

## Quick start
```bash
./gradlew classes
java -cp build/classes/java/main de.lukas.taxi.tests.MainRun
```

## Package layout
- `core/` - domain model (`Taxi`, `Fahrer`, `Auftrag`, `Adresse`, `DateTime`)
- `zentrale/` - dispatch logic (`TaxiZentrale`)
- `meter/` - taxameter implementation and device stubs
- `tests/` - console test mains
- `db/` - SQL schema and queries (`db/README.md`)

## More details
- `README_TAXIUNTERNEHMEN.md` contains the full project description and task writeup.
