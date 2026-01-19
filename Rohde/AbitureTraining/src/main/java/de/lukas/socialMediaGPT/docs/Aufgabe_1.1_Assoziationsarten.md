# Aufgabe 1.1: Assoziationsarten zwischen Beitrag und Bild/Text (3 BE)

## Benennung und Erläuterung der beiden Assoziationsarten

### 1. Assoziation zwischen Beitrag und Bild
**Art:** **Aggregation** (dargestellt durch die hohle Raute auf der Seite von Beitrag)

**Bedeutung:**
- Eine Aggregation beschreibt eine "besteht aus"-Beziehung, bei der die Teile (hier: Bild) unabhängig vom Ganzen (hier: Beitrag) existieren können.
- Die Kardinalität [1..*] auf der Seite von Bild bedeutet, dass ein Beitrag aus einem oder mehreren Bildern besteht.
- Die Kardinalität [0..*] auf der Seite von Beitrag bedeutet, dass ein Bild in keinem oder mehreren Beiträgen verwendet werden kann.
- **Wichtig:** Wird ein Beitrag gelöscht, bleiben die Bild-Objekte weiterhin bestehen, da sie unabhängig vom Beitrag existieren und potenziell in anderen Beiträgen wiederverwendet werden können.

### 2. Assoziation zwischen Beitrag und Text
**Art:** **Komposition** (dargestellt durch die ausgefüllte Raute auf der Seite von Beitrag)

**Bedeutung:**
- Eine Komposition beschreibt eine starke "besteht aus"-Beziehung, bei der die Teile (hier: Text) existenziell vom Ganzen (hier: Beitrag) abhängig sind.
- Die Kardinalität [0..1] auf der Seite von Text bedeutet, dass ein Beitrag optional aus maximal einem Text-Objekt bestehen kann (ein Beitrag kann auch nur aus Bildern bestehen).
- **Wichtig:** Wird ein Beitrag gelöscht, wird auch das zugehörige Text-Objekt automatisch gelöscht, da es ohne den Beitrag keine eigenständige Bedeutung hat. Das Text-Objekt kann nicht ohne seinen Beitrag existieren.

## Zusammenfassung
- **Aggregation (Bild):** Schwache Beziehung - Bilder können unabhängig existieren und wiederverwendet werden
- **Komposition (Text):** Starke Beziehung - Text ist existenziell vom Beitrag abhängig
