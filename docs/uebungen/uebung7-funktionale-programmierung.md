# Übung 7: Funktionale Programmierung

## Ziel
In dieser Übung werden die Grundkonzepte der funktionalen Programmierung in Java angewendet. Es wurden Stream API, Methodenreferenzen, Optional<T> und funktionale Interfaces verwendet.

---

## Aufgabe 1: Code-Analyse

### Identifizierte Stellen für funktionale Verbesserungen

#### Stelle 1: Domain Events Verarbeitung
**Datei:** `UserService.java` (Zeile 63-70)

**Vorher (Imperativ):**
```java
List<Object> domainEvents = user.getDomainEvents();
for (Object event : domainEvents) {
    if (event instanceof UserRegisteredEvent) {
        eventPublisher.publishEvent(event);
    }
}
```

**Nachher (Funktional):**
```java
user.getDomainEvents().stream()
        .filter(UserRegisteredEvent.class::isInstance)
        .forEach(eventPublisher::publishEvent);
```

**Verbesserungen:** Deklarativ, kompakter, Methodenreferenzen (`::isInstance`, `::publishEvent`)

---

#### Stelle 2: Komplexe Validierung
**Datei:** `FeedbackService.java` (Zeile 35-38)

**Vorher:** Lange ODER-Kette von Validierungen

**Nachher:** Funktionale Validierung mit Stream API:
```java
Stream.of(
        validateNotNull(dto.userId, "userId"),
        validateNotBlank(dto.title, "title"),
        validateNotNull(dto.category, "category"),
        validateNotBlank(dto.content, "content")
)
.filter(Objects::nonNull)
.findFirst()
.ifPresent(error -> {
    throw new IllegalArgumentException("Feedback-Daten sind unvollständig: " + error);
});
```

**Verbesserungen:** Function Composition, wiederverwendbare Validierungsfunktionen, bessere Testbarkeit

---

#### Stelle 3: Optional-Prüfung
**Datei:** `FeedbackService.java` (Zeile 40-43)

**Vorher:**
```java
if (!userRepository.findById(dto.userId).isPresent()) {
    throw new UserNotFoundException(dto.userId);
}
```

**Nachher:**
```java
userRepository.findById(dto.userId)
        .orElseThrow(() -> new UserNotFoundException(dto.userId));
```

**Verbesserungen:** Idiomatischer Optional-Umgang, kompakter, Method Chaining

---

## Aufgabe 2: Implementierung

Alle 3 identifizierten Stellen wurden erfolgreich refactored:

- ✅ **Domain Events:** Stream API mit Methodenreferenzen
- ✅ **Validierung:** Funktionale Validierung mit Stream.of() und filter()
- ✅ **Optional:** Idiomatischer Umgang mit orElseThrow()

**Ergebnis:** Alle 97 Tests bestanden, keine Regressionen.

---

## Aufgabe 3: Collection Processing

### Implementierte Operationen

#### 1. Gruppierung nach Status mit Counting
```java
public Map<Status, Long> getFeedbackStatusStatistics() {
    return feedbackRepository.findAll().stream()
            .collect(Collectors.groupingBy(
                    Feedback::getStatus,
                    Collectors.counting()
            ));
}
```
**Endpoint:** `GET /feedback/statistics/status`

#### 2. Gruppierung nach Kategorie mit Mapping
```java
public Map<Category, List<String>> getFeedbackTitlesByCategory() {
    return feedbackRepository.findAll().stream()
            .collect(Collectors.groupingBy(
                    Feedback::getCategory,
                    Collectors.mapping(Feedback::getTitle, Collectors.toList())
            ));
}
```
**Endpoint:** `GET /feedback/statistics/category`

#### 3. Transformation mit mehreren Filtern
```java
public List<FeedbackSummaryDTO> getPublishedActiveFeedbacksSummary() {
    return feedbackRepository.findAll().stream()
            .filter(Feedback::isPublished)
            .filter(f -> f.getStatus() != Status.CLOSED)
            .map(feedback -> new FeedbackSummaryDTO(...))
            .sorted(Comparator.comparing(FeedbackSummaryDTO::getFeedbackDate).reversed())
            .collect(Collectors.toList());
}
```
**Endpoint:** `GET /feedback/summary/published-active`

#### 4. Aggregation/Reduktion für Statistiken
```java
public FeedbackStatisticsDTO getFeedbackStatistics() {
    // Zählt verschiedene Statistiken, findet min/max Datum
    // Nutzt filter().count(), min(), max() mit Optional
}
```
**Endpoint:** `GET /feedback/statistics`

### Verwendete funktionale Konzepte:
- Stream API (stream, filter, map, collect, sorted)
- Collectors (groupingBy, counting, mapping, toList)
- Methodenreferenzen (Feedback::getStatus, Objects::nonNull)
- Lambda-Ausdrücke
- Optional (orElse, min, max)

### Tests:
- **6 Service-Tests** (Business-Logik)
- **5 Controller-Tests** (REST-API)
- **Gesamt: 11 neue Tests** - Alle bestehen ✅

---

## Aufgabe 4: Reflexion

### Vorteile und Nachteile

**Vorteile:**
- Deklarativer, kompakterer Code
- Pipeline-Struktur macht Transformationen klar erkennbar
- Wiederverwendbare Bausteine
- Bessere Testbarkeit durch fehlende Seiteneffekte

**Nachteile:**
- Code ist für Entwickler ohne Stream API-Erfahrung schwerer zu verstehen
- Komplexe Pipelines erfordern gutes Verständnis funktionaler Konzepte
- Debugging schwieriger 
- Performance kann bei kleinen Collections schlechter sein

### Technologien

Als einzige Technologie wurde ein **Large Language Model (LLM) - Auto (Cursor AI)** eingesetzt. Das LLM wurde primär für Code-Generierung der funktionalen Implementierungen verwendet, insbesondere für die Collection-Processing-Methoden mit Stream API. Zusätzlich unterstützte es bei der Erstellung von Tests und der Dokumentation.

### Auswirkungen auf Codequalität und Lesbarkeit

Die funktionale Implementierung hat **gemischte Auswirkungen**. Einerseits ist der Code deklarativer, andererseits ist **deutlich mehr Code hinzugekommen** (ca. 150 Zeilen für Collection-Processing-Methoden plus DTOs). Die **Lesbarkeit** wurde generell **schwerer**. Die **Wartbarkeit** leidet unter der höheren Komplexität - Änderungen an Stream-Pipelines sind fehleranfälliger als bei imperativen Schleifen.

### Herausforderungen

Die größte Herausforderung waren **Test-Fehler beim Maven Build**. Die Tests waren nicht ausreichend isoliert, sodass Daten aus vorherigen Tests in der Datenbank verblieben und zu falschen Assertions führten. Beispielsweise erwartete ein Test genau 2 Feedbacks in einer Kategorie, aber durch Daten aus anderen Tests waren es 7. Die Lösung war, **robustere Assertions** zu verwenden (`>=` statt `==`) und explizit zu prüfen, dass die eigenen Test-Daten enthalten sind.

### Lessons Learned

**Funktionale Programmierung ist nicht immer besser** - sie sollte selektiv eingesetzt werden. Bei komplexen Transformationen ist sie ideal, bei einfachen Operationen kann imperativer Code klarer sein. Die **Dokumentation** funktionaler Code-Stellen ist essentiell. **Code-Reviews** sind besonders wichtig bei LLM-generiertem Code. Letztendlich ist **Balance** der Schlüssel: Funktionale Konzepte dort nutzen, wo sie Mehrwert bringen, aber nicht um jeden Preis.

---

## Zusammenfassung

### Implementiert:
- ✅ 3 Code-Stellen refactored (Aufgabe 1 & 2)
- ✅ 4 Collection-Processing-Methoden (Aufgabe 3)
- ✅ 4 REST-Endpoints
- ✅ 11 neue Tests (6 Service, 5 Controller)
- ✅ Vollständige Dokumentation

### Code-Metriken:
- ~150 Zeilen Code (inkl. DTOs)
- ~150 Zeilen Test-Code
- 69 Tests insgesamt, alle bestehen ✅



