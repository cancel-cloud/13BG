# AbitureTraining

Collection of Java packages that implement exam tasks and practice material for the Hessian Abitur (Practical Computer Science, LK).

## Packages
- `src/main/java/de/lukas/socialMediaGPT` - Social media platform solution (domain model + client/server demo).
- `src/main/java/de/lukas/computerverleih` - School computer rental system with a console demo.
- `src/main/java/de/lukas/taxi` - Taxi company dispatch + taxameter system.
- `src/main/java/de/lukas/socialMedia/textAufgaben` - Small OOP exercises for social media tasks.

Each package includes its own README with details and entry points.

## Build
- `./gradlew build`

## Run examples
- `./gradlew classes`
- `java -cp build/classes/java/main de.lukas.socialMediaGPT.Main`
- `java -cp build/classes/java/main de.lukas.computerverleih.Main`
- `java -cp build/classes/java/main de.lukas.socialMedia.textAufgaben.SocialMediaManager`
- `java -cp build/classes/java/main de.lukas.taxi.tests.MainRun`

## Resources
- `src/main/resources` contains the exam materials and written solutions.
