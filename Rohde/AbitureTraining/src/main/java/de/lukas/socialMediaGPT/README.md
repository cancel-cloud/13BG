# Social Media Platform (Abitur 2023, Proposal A)

Java implementation of the social media platform tasks from the Hessian Abitur (Practical Computer Science, LK). The package includes a full domain model, platform logic, and a small client/server demo.

## What is included
- Domain model: `Nutzer`, `Beitrag`, `Text`, `Bild`
- Platform logic: registration, login, password generation, follow/like, feed filtering
- Networking demo: `Server`, `ServerThread`, `Client`, plus socket wrappers
- Utilities: `DateTime`, `Random`, `List`

## Entry points
- `de.lukas.socialMediaGPT.Main` - console demo of the platform features
- `de.lukas.socialMediaGPT.Server` / `de.lukas.socialMediaGPT.Client` - basic socket demo

## Run
```bash
./gradlew classes
java -cp build/classes/java/main de.lukas.socialMediaGPT.Main
```

## Documentation
- `docs/` contains written answers (mostly German) for the exam tasks.
- `LA23-PRIN-LK-A-AUFG.pdf` is the original assignment.
