# Übung 7: Funktionale Programmierung - Code-Analyse

## Ziel der Übung
In dieser Übung lernen Sie die Grundkonzepte der funktionalen Programmierung kennen und wenden diese in Ihrem Projekt an.

---

## 1. Identifizierte Stellen für funktionale Verbesserungen

### Stelle 1: Domain Events Verarbeitung mit imperativer Schleife

**Datei:** `src/main/java/com/example/cityfeedback/usermanagement/application/UserService.java`  
**Zeilen:** 63-70

#### Aktueller Code:
```java
// Domain Events aus dem Aggregat holen und publishen
List<Object> domainEvents = user.getDomainEvents();
for (Object event : domainEvents) {
    if (event instanceof UserRegisteredEvent) {
        eventPublisher.publishEvent(event);
    }
}
user.clearDomainEvents();
```

#### Problem:
- Imperative Schleife mit `for-each`
- Typ-Check mit `instanceof` innerhalb der Schleife
- Nebenwirkungen (side effects) innerhalb der Iteration
- Wenig deklarativ und schwer zu testen

#### Geplante Überarbeitung (funktional):
```java
// Domain Events aus dem Aggregat holen und publishen
user.getDomainEvents().stream()
    .filter(UserRegisteredEvent.class::isInstance)
    .map(UserRegisteredEvent.class::cast)
    .forEach(eventPublisher::publishEvent);
user.clearDomainEvents();
```

**Alternative (noch deklarativer mit Methodenreferenz):**
```java
user.getDomainEvents().stream()
    .filter(UserRegisteredEvent.class::isInstance)
    .forEach(eventPublisher::publishEvent);
user.clearDomainEvents();
```

#### Erwartete Verbesserungen:
- ✅ **Deklarative statt imperative Programmierung:** Der Code beschreibt WAS gemacht wird, nicht WIE
- ✅ **Bessere Lesbarkeit:** Pipeline-Stil macht die Transformation klar erkennbar
- ✅ **Wiederverwendbarkeit:** Stream-Operationen können leicht erweitert werden (z.B. mit `map`, `flatMap`)
- ✅ **Immutability:** Keine direkte Manipulation der Liste während der Iteration
- ✅ **Bessere Testbarkeit:** Stream-Operationen können isoliert getestet werden
- ✅ **Methodenreferenzen:** Reduzieren Boilerplate-Code (`::isInstance`, `::publishEvent`)

---

### Stelle 2: Komplexe Validierung mit verschachtelten if-Bedingungen

**Datei:** `src/main/java/com/example/cityfeedback/feedbackmanagement/application/FeedbackService.java`  
**Zeilen:** 35-38

#### Aktueller Code:
```java
@Transactional
public Feedback createFeedback(FeedbackDTO dto) {
    if (dto == null || dto.userId == null || dto.title == null || dto.title.isBlank() 
            || dto.category == null || dto.content == null || dto.content.isBlank()) {
        throw new IllegalArgumentException("Feedback-Daten sind unvollständig.");
    }
    // ...
}
```

#### Problem:
- Lange, schwer lesbare Kette von ODER-Verknüpfungen
- Keine klare Trennung der Validierungslogik
- Schwer zu erweitern oder zu testen
- Keine spezifischen Fehlermeldungen für einzelne Felder
- Verletzt Single Responsibility Principle

#### Geplante Überarbeitung (funktional):
```java
@Transactional
public Feedback createFeedback(FeedbackDTO dto) {
    validateFeedbackDTO(dto);
    // ...
}

private void validateFeedbackDTO(FeedbackDTO dto) {
    Optional.ofNullable(dto)
        .orElseThrow(() -> new IllegalArgumentException("Feedback-DTO darf nicht null sein"));
    
    Stream.of(
        validateField(dto.userId, "userId"),
        validateField(dto.title, "title", String::isBlank),
        validateField(dto.category, "category"),
        validateField(dto.content, "content", String::isBlank)
    )
    .filter(Optional::isPresent)
    .findFirst()
    .ifPresent(error -> {
        throw new IllegalArgumentException("Feedback-Daten sind unvollständig: " + error.get());
    });
}

private <T> Optional<String> validateField(T field, String fieldName) {
    return Optional.ofNullable(field)
        .map(f -> (String) null)  // Field exists, no error
        .or(() -> Optional.of(fieldName + " darf nicht null sein"));
}

private <T> Optional<String> validateField(String field, String fieldName, Predicate<String> additionalCheck) {
    return Optional.ofNullable(field)
        .filter(f -> !additionalCheck.test(f))
        .map(f -> (String) null)
        .or(() -> Optional.of(fieldName + " ist ungültig oder leer"));
}
```

**Alternative (einfacherer Ansatz mit Stream):**
```java
private void validateFeedbackDTO(FeedbackDTO dto) {
    if (dto == null) {
        throw new IllegalArgumentException("Feedback-DTO darf nicht null sein");
    }
    
    List<String> errors = Stream.of(
        validateNotNull(dto.userId, "userId"),
        validateNotBlank(dto.title, "title"),
        validateNotNull(dto.category, "category"),
        validateNotBlank(dto.content, "content")
    )
    .filter(Objects::nonNull)
    .collect(Collectors.toList());
    
    if (!errors.isEmpty()) {
        throw new IllegalArgumentException("Feedback-Daten sind unvollständig: " + String.join(", ", errors));
    }
}

private String validateNotNull(Object field, String fieldName) {
    return field == null ? fieldName + " darf nicht null sein" : null;
}

private String validateNotBlank(String field, String fieldName) {
    return (field == null || field.isBlank()) ? fieldName + " darf nicht leer sein" : null;
}
```

#### Erwartete Verbesserungen:
- ✅ **Komposition:** Validierungslogik wird aus kleinen, wiederverwendbaren Funktionen zusammengesetzt
- ✅ **Bessere Fehlerbehandlung:** Spezifischere Fehlermeldungen möglich
- ✅ **Testbarkeit:** Einzelne Validierungsfunktionen können isoliert getestet werden
- ✅ **Erweiterbarkeit:** Neue Validierungen können leicht hinzugefügt werden
- ✅ **Immutability:** Validierung erstellt keine veränderlichen Zwischenzustände
- ✅ **Separation of Concerns:** Validierungslogik ist von der Business-Logik getrennt
- ✅ **Stream API:** Nutzt funktionale Konzepte wie `filter`, `collect`, `findFirst`

---

### Stelle 3: Imperative Optional-Prüfung mit isPresent()

**Datei:** `src/main/java/com/example/cityfeedback/feedbackmanagement/application/FeedbackService.java`  
**Zeilen:** 40-43

#### Aktueller Code:
```java
// Prüfen, ob User existiert (lose Kopplung: nur ID-Prüfung)
if (!userRepository.findById(dto.userId).isPresent()) {
    throw new UserNotFoundException(dto.userId);
}
```

#### Problem:
- Verwendet `isPresent()` negiert - weniger idiomatisch
- Imperativer Stil trotz Optional vorhanden
- Optional wird nicht funktional genutzt (keine Methoden-Komposition)
- Doppelter Repository-Zugriff möglich (erst hier, später eventuell erneut)

#### Geplante Überarbeitung (funktional):
```java
// Prüfen, ob User existiert (lose Kopplung: nur ID-Prüfung)
userRepository.findById(dto.userId)
    .orElseThrow(() -> new UserNotFoundException(dto.userId));
```

**Alternative (wenn der User später nicht benötigt wird):**
```java
// Prüfen, ob User existiert - werfen Exception falls nicht vorhanden
if (userRepository.findById(dto.userId).isEmpty()) {
    throw new UserNotFoundException(dto.userId);
}
```

**Noch besser (mit Optional.ifPresentOrElse - Java 9+):**
```java
userRepository.findById(dto.userId)
    .ifPresentOrElse(
        user -> { /* User existiert - optional: Logging */ },
        () -> { throw new UserNotFoundException(dto.userId); }
    );
```

**Beste Lösung (direktes orElseThrow):**
```java
userRepository.findById(dto.userId)
    .orElseThrow(() -> new UserNotFoundException(dto.userId));
```

#### Erwartete Verbesserungen:
- ✅ **Idiomatischer Optional-Umgang:** Nutzt `Optional` als Monad für funktionale Komposition
- ✅ **Kompakterer Code:** Weniger Boilerplate, direkter Ausdruck der Intention
- ✅ **Method Chaining:** Ermöglicht weitere Operationen in der Pipeline
- ✅ **Keine doppelte Negation:** `isEmpty()` ist klarer als `!isPresent()`
- ✅ **Bessere Lesbarkeit:** Der Code liest sich wie: "Finde User, oder wirf Exception"
- ✅ **Einheitlicher Stil:** Konsistent mit anderen Stellen im Code (z.B. `getUserById`, `getFeedbackById`)

---

## 2. Zusammenfassung der funktionalen Konzepte

### Verwendete funktionale Konzepte:

1. **Stream API:**
   - Deklarative Datenverarbeitung
   - Filter, Map, ForEach-Operationen
   - Pipeline-Pattern

2. **Optional:**
   - Null-Safety
   - Monadic-Operations (orElseThrow, ifPresent, filter)
   - Komposition von null-behandelnden Operationen

3. **Methodenreferenzen:**
   - `::isInstance`
   - `::publishEvent`
   - `String::isBlank`

4. **Lambda-Ausdrücke:**
   - Funktionen als First-Class Citizens
   - Predicates für Validierung

5. **Immutability:**
   - Keine Seiteneffekte in Stream-Operationen
   - Unveränderliche Zwischenergebnisse

6. **Function Composition:**
   - Kombination kleinerer Funktionen zu komplexeren Operationen
   - Wiederverwendbare Validierungsfunktionen

---

## 3. Bewertung des Refactoring-Potenzials

### Vorteile der funktionalen Überarbeitung:
- ✅ **Lesbarkeit:** Code ist deklarativer und ausdrucksstärker
- ✅ **Wartbarkeit:** Kleinere, testbare Funktionen
- ✅ **Erweiterbarkeit:** Neue Operationen können leicht hinzugefügt werden
- ✅ **Fehlerresistenz:** Weniger mutable State reduziert Bugs
- ✅ **Parallele Verarbeitung:** Streams können bei Bedarf parallelisiert werden

### Potenzielle Herausforderungen:
- ⚠️ **Lernkurve:** Team muss mit Stream API und Optional vertraut sein
- ⚠️ **Performance:** Bei sehr kleinen Collections kann imperative Schleife minimal schneller sein (meist vernachlässigbar)
- ⚠️ **Debugging:** Stacktraces können bei komplexen Stream-Pipelines tiefer sein

### Empfehlung:
Die identifizierten Stellen sollten **schrittweise refactored** werden, beginnend mit:
1. **Stelle 3** (Optional-Prüfung) - einfachste Änderung, sofortiger Nutzen
2. **Stelle 1** (Domain Events) - klarer Verbesserung bei Lesbarkeit
3. **Stelle 2** (Validierung) - größte Verbesserung, aber mehr Refactoring-Aufwand

---

## 4. Nächste Schritte

1. ✅ Code-Analyse abgeschlossen
2. ⏳ Code-Review durch Team
3. ⏳ Implementierung der funktionalen Überarbeitungen
4. ⏳ Tests anpassen/erweitern
5. ⏳ Dokumentation aktualisieren

---

*Erstellt am: [Datum]*  
*Autor: [Name]*
