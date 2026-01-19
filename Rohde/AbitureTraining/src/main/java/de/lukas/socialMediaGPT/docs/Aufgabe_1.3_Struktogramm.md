# Aufgabe 1.3: Struktogramm für ermittleAbonnierteNutzerMitNeuenBeitraegen (7 BE)

## Aufgabenstellung
Die Methode `ermittleAbonnierteNutzerMitNeuenBeitraegen(n: Nutzer)` der Klasse `SocialMediaPlatform` ermittelt alle Nutzer, die der Nutzer n abonniert hat und die neue Beiträge erstellt haben, seit er zuletzt aktiv war.

**Hinweis:** Ein abonnierter Nutzer soll nur einmal in der Liste enthalten sein, auch wenn er mehrere Beiträge im relevanten Zeitraum erstellt hat.

## Struktogramm (Nassi-Shneiderman-Diagramm)

```
┌─────────────────────────────────────────────────────────────────┐
│ ergebnisListe ← neue leere Liste<Nutzer>                        │
├─────────────────────────────────────────────────────────────────┤
│ abonnierteListe ← n.getAbonnierteNutzer()                       │
├─────────────────────────────────────────────────────────────────┤
│ FÜR JEDEN abonnierterNutzer IN abonnierteListe                  │
│ ┌───────────────────────────────────────────────────────────────┤
│ │ hatNeuenBeitrag ← false                                       │
│ ├───────────────────────────────────────────────────────────────┤
│ │ beitragsListe ← abonnierterNutzer.getBeitraege()              │
│ ├───────────────────────────────────────────────────────────────┤
│ │ FÜR JEDEN beitrag IN beitragsListe                            │
│ │ ┌─────────────────────────────────────────────────────────────┤
│ │ │ WENN beitrag.getGepostet().isAfter(n.getZuletztAktiv())    │
│ │ │ ┌───────────────────────────────────────────────────────────┤
│ │ │ │ hatNeuenBeitrag ← true                                    │
│ │ │ └───────────────────────────────────────────────────────────┤
│ │ └─────────────────────────────────────────────────────────────┤
│ ├───────────────────────────────────────────────────────────────┤
│ │ WENN hatNeuenBeitrag = true                                   │
│ │ ┌───────────────────────────────────────────────────────────┤
│ │ │ WENN NICHT ergebnisListe.contains(abonnierterNutzer)      │
│ │ │ ┌─────────────────────────────────────────────────────────┤
│ │ │ │ ergebnisListe.add(abonnierterNutzer)                    │
│ │ │ └─────────────────────────────────────────────────────────┤
│ │ └───────────────────────────────────────────────────────────┤
│ └───────────────────────────────────────────────────────────────┤
├─────────────────────────────────────────────────────────────────┤
│ RETURN ergebnisListe                                            │
└─────────────────────────────────────────────────────────────────┘
```

## Erläuterung des Algorithmus

1. **Initialisierung:** Eine leere Ergebnisliste wird erstellt
2. **Abonnierte Nutzer holen:** Die Liste der vom Nutzer n abonnierten Nutzer wird geholt
3. **Äußere Schleife:** Für jeden abonnierten Nutzer:
   - Eine Boolean-Variable `hatNeuenBeitrag` wird auf false gesetzt
   - Die Beiträge des abonnierten Nutzers werden geholt
4. **Innere Schleife:** Für jeden Beitrag des abonnierten Nutzers:
   - Prüfung, ob der Beitrag nach der letzten Aktivität von Nutzer n erstellt wurde
   - Falls ja, wird `hatNeuenBeitrag` auf true gesetzt
5. **Duplikatsprüfung:** Wenn `hatNeuenBeitrag` true ist:
   - Prüfung, ob der abonnierte Nutzer noch nicht in der Ergebnisliste ist
   - Falls noch nicht enthalten, wird er hinzugefügt (verhindert Duplikate)
6. **Rückgabe:** Die Ergebnisliste wird zurückgegeben

## Alternative Optimierung

Der Algorithmus kann optimiert werden, indem die innere Schleife frühzeitig abgebrochen wird, sobald ein neuer Beitrag gefunden wurde:

```
┌─────────────────────────────────────────────────────────────────┐
│ ergebnisListe ← neue leere Liste<Nutzer>                        │
├─────────────────────────────────────────────────────────────────┤
│ abonnierteListe ← n.getAbonnierteNutzer()                       │
├─────────────────────────────────────────────────────────────────┤
│ FÜR JEDEN abonnierterNutzer IN abonnierteListe                  │
│ ┌───────────────────────────────────────────────────────────────┤
│ │ beitragsListe ← abonnierterNutzer.getBeitraege()              │
│ ├───────────────────────────────────────────────────────────────┤
│ │ i ← 0                                                          │
│ ├───────────────────────────────────────────────────────────────┤
│ │ SOLANGE i < beitragsListe.size()                              │
│ │ ┌─────────────────────────────────────────────────────────────┤
│ │ │ beitrag ← beitragsListe.get(i)                              │
│ │ ├─────────────────────────────────────────────────────────────┤
│ │ │ WENN beitrag.getGepostet().isAfter(n.getZuletztAktiv())    │
│ │ │ ┌───────────────────────────────────────────────────────────┤
│ │ │ │ ergebnisListe.add(abonnierterNutzer)                      │
│ │ │ ├───────────────────────────────────────────────────────────┤
│ │ │ │ i ← beitragsListe.size() // Schleife beenden             │
│ │ │ └───────────────────────────────────────────────────────────┤
│ │ │ SONST                                                       │
│ │ │ ┌───────────────────────────────────────────────────────────┤
│ │ │ │ i ← i + 1                                                 │
│ │ │ └───────────────────────────────────────────────────────────┤
│ │ └─────────────────────────────────────────────────────────────┤
│ └───────────────────────────────────────────────────────────────┤
├─────────────────────────────────────────────────────────────────┤
│ RETURN ergebnisListe                                            │
└─────────────────────────────────────────────────────────────────┘
```

Diese Variante vermeidet die separate `contains()`-Prüfung, da jeder Nutzer nur einmal hinzugefügt wird und die Schleife sofort abbricht.
