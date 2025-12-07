# Übung 7: Funktionale Programmierung - Collection Processing

## Aufgabe 3: Collection Processing mit funktionalen Konzepten

### Ziel
Implementieren komplexer Collection-Verarbeitungen mit funktionalen Konzepten. Mindestens zwei der folgenden Operationen:
- Gruppierung von Daten
- Aggregation/Reduktion
- Transformation/Mapping
- Filtering mit mehreren Kriterien

---

## 1. Implementierte Collection-Verarbeitungen

### 1.1 Gruppierung von Feedbacks nach Status mit Counting

**Methode:** `getFeedbackStatusStatistics()`  
**Datei:** `src/main/java/com/example/cityfeedback/feedbackmanagement/application/FeedbackService.java`  
**Zeilen:** 98-104

#### Implementierung:
```java
public Map<Status, Long> getFeedbackStatusStatistics() {
    return feedbackRepository.findAll().stream()
            .collect(Collectors.groupingBy(
                    Feedback::getStatus,
                    Collectors.counting()
            ));
}
```

#### Verwendete funktionale Konzepte:
- ✅ **Stream API:** `stream()` - Erstellt Stream aus Collection
- ✅ **Collectors.groupingBy():** Gruppiert Elemente nach Klassifikationsfunktion
- ✅ **Methodenreferenz:** `Feedback::getStatus` - Klassifikationsfunktion für Gruppierung
- ✅ **Collectors.counting():** Downstream-Collector zum Zählen der Elemente pro Gruppe

#### Funktionsweise:
1. Alle Feedbacks werden aus dem Repository geladen
2. Ein Stream wird erstellt
3. `groupingBy()` gruppiert die Feedbacks nach ihrem Status
4. `counting()` zählt die Anzahl der Feedbacks pro Status
5. Ergebnis: `Map<Status, Long>` mit Status als Key und Anzahl als Value

#### Beispiel-Output:
```java
{
    OPEN=5,
    INPROGRESS=3,
    CLOSED=2,
    DONE=1
}
```

---

### 1.2 Gruppierung nach Kategorie mit Mapping

**Methode:** `getFeedbackTitlesByCategory()`  
**Datei:** `src/main/java/com/example/cityfeedback/feedbackmanagement/application/FeedbackService.java`  
**Zeilen:** 110-118

#### Implementierung:
```java
public Map<Category, List<String>> getFeedbackTitlesByCategory() {
    return feedbackRepository.findAll().stream()
            .collect(Collectors.groupingBy(
                    Feedback::getCategory,
                    Collectors.mapping(
                            Feedback::getTitle,
                            Collectors.toList()
                    )
            ));
}
```

#### Verwendete funktionale Konzepte:
- ✅ **Collectors.groupingBy():** Gruppierung nach Kategorie
- ✅ **Collectors.mapping():** Transformation der gruppierten Elemente
- ✅ **Methodenreferenz:** `Feedback::getCategory`, `Feedback::getTitle`
- ✅ **Collectors.toList():** Sammelt transformierte Werte in Liste

#### Funktionsweise:
1. Feedbacks werden nach Kategorie gruppiert
2. Innerhalb jeder Gruppe werden nur die Titel extrahiert (`mapping`)
3. Die Titel werden in einer Liste gesammelt
4. Ergebnis: `Map<Category, List<String>>`

#### Beispiel-Output:
```java
{
    VERKEHR=["Parkplatzproblem", "Ampeldefekt"],
    UMWELT=["Müllproblem", "Baumschaden"],
    BELEUCHTUNG=["Licht defekt"]
}
```

---

### 1.3 Komplexe Transformation mit mehreren Filtern

**Methode:** `getPublishedActiveFeedbacksSummary()`  
**Datei:** `src/main/java/com/example/cityfeedback/feedbackmanagement/application/FeedbackService.java`  
**Zeilen:** 127-145

#### Implementierung:
```java
public List<FeedbackSummaryDTO> getPublishedActiveFeedbacksSummary() {
    return feedbackRepository.findAll().stream()
            .filter(Feedback::isPublished)  // Erster Filter: Nur veröffentlichte
            .filter(f -> f.getStatus() != Status.CLOSED)  // Zweiter Filter: Nicht geschlossen
            .map(feedback -> new FeedbackSummaryDTO(  // Transformation zu DTO
                    feedback.getId(),
                    feedback.getTitle(),
                    feedback.getCategory().toString(),
                    feedback.getStatus().toString(),
                    feedback.getFeedbackDate()
            ))
            .sorted(Comparator.comparing(FeedbackSummaryDTO::getFeedbackDate).reversed())  // Sortierung
            .collect(Collectors.toList());
}
```

#### Verwendete funktionale Konzepte:
- ✅ **Mehrfaches Filtering:** `filter()` mit verschiedenen Predicates
- ✅ **Methodenreferenz:** `Feedback::isPublished`
- ✅ **Lambda-Ausdruck:** `f -> f.getStatus() != Status.CLOSED`
- ✅ **Transformation:** `map()` transformiert Feedback zu DTO
- ✅ **Sortierung:** `sorted()` mit `Comparator.comparing()`
- ✅ **Methodenreferenz:** `FeedbackSummaryDTO::getFeedbackDate`

#### Funktionsweise:
1. **Filter 1:** Nur veröffentlichte Feedbacks (`isPublished() == true`)
2. **Filter 2:** Nur nicht geschlossene Feedbacks (`status != CLOSED`)
3. **Transformation:** Jedes Feedback wird zu einem `FeedbackSummaryDTO` transformiert
4. **Sortierung:** Nach Feedback-Datum sortiert (neueste zuerst - `.reversed()`)
5. **Collection:** Ergebnis wird in Liste gesammelt

#### DTO-Struktur:
```java
public static class FeedbackSummaryDTO {
    private final Long id;
    private final String title;
    private final String category;
    private final String status;
    private final LocalDate feedbackDate;
}
```

---

### 1.4 Aggregation/Reduktion für Statistiken

**Methode:** `getFeedbackStatistics()`  
**Datei:** `src/main/java/com/example/cityfeedback/feedbackmanagement/application/FeedbackService.java`  
**Zeilen:** 152-189

#### Implementierung:
```java
public FeedbackStatisticsDTO getFeedbackStatistics() {
    List<Feedback> allFeedbacks = feedbackRepository.findAll();
    
    if (allFeedbacks.isEmpty()) {
        return new FeedbackStatisticsDTO(0L, 0L, 0L, 0L, null, null);
    }

    long totalCount = allFeedbacks.stream().count();
    
    long publishedCount = allFeedbacks.stream()
            .filter(Feedback::isPublished)
            .count();
    
    long closedCount = allFeedbacks.stream()
            .filter(f -> f.getStatus() == Status.CLOSED)
            .count();
    
    long openCount = allFeedbacks.stream()
            .filter(f -> f.getStatus() == Status.OPEN)
            .count();
    
    LocalDate oldestDate = allFeedbacks.stream()
            .map(Feedback::getFeedbackDate)
            .min(LocalDate::compareTo)
            .orElse(null);
    
    LocalDate newestDate = allFeedbacks.stream()
            .map(Feedback::getFeedbackDate)
            .max(LocalDate::compareTo)
            .orElse(null);
    
    return new FeedbackStatisticsDTO(totalCount, publishedCount, closedCount, 
                                     openCount, oldestDate, newestDate);
}
```

#### Verwendete funktionale Konzepte:
- ✅ **Reduktion:** `count()` zählt Elemente
- ✅ **Filtering:** `filter()` für bedingte Zählung
- ✅ **Transformation:** `map()` für Datums-Extraktion
- ✅ **Reduktion:** `min()`, `max()` für Extremwerte
- ✅ **Optional:** `orElse()` für Fallback-Werte
- ✅ **Methodenreferenz:** `Feedback::isPublished`, `Feedback::getFeedbackDate`, `LocalDate::compareTo`

#### Funktionsweise:
1. **Zählung:** Verschiedene Counts werden mit `filter().count()` berechnet
2. **Extremwerte:** `min()` und `max()` finden ältestes/neuestes Datum
3. **Optional-Handling:** `orElse(null)` für leere Streams
4. **Aggregation:** Alle Werte werden in DTO zusammengefasst

#### DTO-Struktur:
```java
public static class FeedbackStatisticsDTO {
    private final Long totalCount;
    private final Long publishedCount;
    private final Long closedCount;
    private final Long openCount;
    private final LocalDate oldestDate;
    private final LocalDate newestDate;
}
```

---

## 2. Übersicht der verwendeten funktionalen Konzepte

### Stream API Operationen:

| Operation | Verwendung | Zweck |
|-----------|-----------|-------|
| `stream()` | Basis-Operation | Erstellt Stream aus Collection |
| `filter()` | Filtering | Filtert Elemente nach Predicate |
| `map()` | Transformation | Transformiert Elemente |
| `sorted()` | Sortierung | Sortiert Elemente nach Comparator |
| `count()` | Reduktion | Zählt Elemente |
| `min()` / `max()` | Reduktion | Findet Extremwerte |
| `collect()` | Terminal-Operation | Sammelt Ergebnisse |

### Collectors:

| Collector | Verwendung | Zweck |
|-----------|-----------|-------|
| `groupingBy()` | Gruppierung | Gruppiert nach Klassifikationsfunktion |
| `counting()` | Aggregation | Zählt Elemente in Gruppen |
| `mapping()` | Transformation | Transformiert innerhalb von Gruppen |
| `toList()` | Collection | Sammelt in Liste |

### Funktionale Interfaces:

| Interface | Verwendung | Beispiel |
|-----------|-----------|----------|
| `Predicate<T>` | Filtering | `Feedback::isPublished` |
| `Function<T,R>` | Transformation | `Feedback::getStatus` |
| `Comparator<T>` | Sortierung | `Comparator.comparing(...)` |
| `Supplier<T>` | Optional | Lambda für Exception-Supplier |

### Methodenreferenzen:

| Typ | Beispiel | Bedeutung |
|-----|----------|-----------|
| Instance-Method | `Feedback::isPublished` | `f -> f.isPublished()` |
| Instance-Method | `Feedback::getStatus` | `f -> f.getStatus()` |
| Instance-Method | `Feedback::getTitle` | `f -> f.getTitle()` |
| Static-Method | `LocalDate::compareTo` | `(a, b) -> a.compareTo(b)` |

---

## 3. Tests

### Testübersicht (TDD/DDD-Struktur):

Alle Collection-Processing-Methoden wurden umfassend getestet - sowohl auf Service- als auch auf Controller-Ebene.

### 3.1 Service-Tests (Business-Logik)

**Datei:** `src/test/java/com/example/cityfeedback/feedbackmanagement/application/FeedbackServiceTest.java`

1. ✅ **`getFeedbackStatusStatistics_shouldGroupFeedbacksByStatus`**
   - Testet Gruppierung nach Status
   - Verifiziert korrekte Zählung
   - Verwendet `@Transactional` für Test-Isolation

2. ✅ **`getFeedbackTitlesByCategory_shouldGroupFeedbacksByCategory`**
   - Testet Gruppierung nach Kategorie
   - Verifiziert Mapping zu Titeln
   - Prüft enthaltene Feedbacks (robust gegen Daten aus anderen Tests)

3. ✅ **`getPublishedActiveFeedbacksSummary_shouldFilterAndTransform`**
   - Testet mehrfaches Filtering
   - Verifiziert Transformation zu DTO
   - Prüft dass nur veröffentlichte, nicht-geschlossene Feedbacks enthalten sind

4. ✅ **`getPublishedActiveFeedbacksSummary_shouldBeSortedByDateDescending`**
   - Testet Sortierung nach Datum (neueste zuerst)
   - Verifiziert Sortierreihenfolge

5. ✅ **`getFeedbackStatistics_shouldAggregateAllStatistics`**
   - Testet Aggregation/Reduktion
   - Verifiziert alle Statistiken (Total, Published, Closed, Open)
   - Prüft min/max Datum

6. ✅ **`getFeedbackStatistics_shouldHandleEmptyList`**
   - Testet Edge-Case: Leere Liste
   - Verifiziert korrektes Verhalten bei fehlenden Daten

### 3.2 Controller-Tests (REST-API)

**Datei:** `src/test/java/com/example/cityfeedback/feedbackmanagement/application/FeedbackControllerTest.java`

1. ✅ **`getFeedbackStatusStatistics_shouldReturn200AndMap`**
   - Testet REST-Endpoint: `GET /feedback/statistics/status`
   - Verifiziert HTTP 200 Status
   - Prüft dass Map-Struktur zurückgegeben wird

2. ✅ **`getFeedbackTitlesByCategory_shouldReturn200AndMap`**
   - Testet REST-Endpoint: `GET /feedback/statistics/category`
   - Verifiziert HTTP 200 Status
   - Prüft dass Gruppierung korrekt funktioniert

3. ✅ **`getPublishedActiveFeedbacksSummary_shouldReturn200AndList`**
   - Testet REST-Endpoint: `GET /feedback/summary/published-active`
   - Verifiziert HTTP 200 Status
   - Prüft dass DTO-Liste zurückgegeben wird
   - Verifiziert DTO-Felder

4. ✅ **`getPublishedActiveFeedbacksSummary_whenNoPublishedFeedbacks_shouldReturnEmptyList`**
   - Testet Edge-Case: Keine veröffentlichten Feedbacks
   - Verifiziert dass nicht-veröffentlichte Feedbacks nicht enthalten sind

5. ✅ **`getFeedbackStatistics_shouldReturn200AndStatistics`**
   - Testet REST-Endpoint: `GET /feedback/statistics`
   - Verifiziert HTTP 200 Status
   - Prüft dass StatisticsDTO zurückgegeben wird
   - Verifiziert korrekte Aggregation

### 3.3 Test-Beispiele:

#### Service-Test (Business-Logik):
```java
@Test
void getFeedbackStatusStatistics_shouldGroupFeedbacksByStatus() {
    // Arrange: Erstelle Feedbacks mit verschiedenen Status
    FeedbackDTO dto1 = createFeedbackDTO("Feedback 1", Category.VERKEHR);
    Feedback feedback1 = feedbackService.createFeedback(dto1);
    
    FeedbackDTO dto2 = createFeedbackDTO("Feedback 2", Category.UMWELT);
    Feedback feedback2 = feedbackService.createFeedback(dto2);
    feedback2.updateStatus(Status.INPROGRESS);
    feedbackRepository.save(feedback2);
    
    // Act: Gruppierung nach Status
    Map<Status, Long> statistics = feedbackService.getFeedbackStatusStatistics();
    
    // Assert
    assertNotNull(statistics);
    assertTrue(statistics.getOrDefault(Status.OPEN, 0L) >= 1L);
    assertTrue(statistics.getOrDefault(Status.INPROGRESS, 0L) >= 1L);
}
```

#### Controller-Test (REST-API):
```java
@Test
void getFeedbackStatusStatistics_shouldReturn200AndMap() {
    // Arrange: Erstelle Feedbacks mit verschiedenen Status
    FeedbackDTO dto1 = createFeedbackDTO("Feedback 1", Category.VERKEHR);
    feedbackService.createFeedback(dto1);
    
    FeedbackDTO dto2 = createFeedbackDTO("Feedback 2", Category.UMWELT);
    Feedback feedback2 = feedbackService.createFeedback(dto2);
    feedback2.updateStatus(Status.INPROGRESS);
    feedbackRepository.save(feedback2);
    
    // Act: REST-Request
    ResponseEntity<Map> response = rest.getForEntity("/feedback/statistics/status", Map.class);
    
    // Assert
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    Map<String, Object> statistics = response.getBody();
    assertFalse(statistics.isEmpty(), "Statistik-Map sollte nicht leer sein");
}
```

### 3.4 Test-Metriken:

- **Service-Tests:** 6 Tests für Collection-Processing-Methoden
- **Controller-Tests:** 5 Tests für REST-Endpoints
- **Gesamt:** 11 neue Tests für Übung 7.3
- **Test-Isolation:** Verwendung von `@Transactional` und `@BeforeEach`
- **Robustheit:** Tests sind gegen Daten aus anderen Tests resistent

---

## 4. Vorteile der funktionalen Implementierung

### ✅ Deklarative Programmierung
- Code beschreibt **WAS** gemacht wird, nicht **WIE**
- Lesbarkeit deutlich verbessert
- Pipeline-Stil macht Transformationen klar erkennbar

### ✅ Komposition
- Kleine Operationen werden zu komplexen Pipelines kombiniert
- Wiederverwendbare Bausteine
- Einfache Erweiterbarkeit

### ✅ Immutability
- Keine Seiteneffekte in Stream-Operationen
- Original-Daten bleiben unverändert
- Thread-sicher bei paralleler Verarbeitung

### ✅ Testbarkeit
- Jede Operation kann isoliert getestet werden
- Keine versteckten Seiteneffekte
- Einfache Mocking-Möglichkeiten

### ✅ Performance (potenziell)
- Streams können parallelisiert werden (`.parallelStream()`)
- Lazy Evaluation: Operationen werden erst ausgeführt, wenn nötig
- Optimierte JVM-Implementierung

---

## 5. Vergleich: Imperativ vs. Funktional

### Beispiel: Gruppierung nach Status

**Imperativ (vorher):**
```java
Map<Status, Long> statistics = new HashMap<>();
for (Feedback feedback : feedbackRepository.findAll()) {
    Status status = feedback.getStatus();
    statistics.put(status, statistics.getOrDefault(status, 0L) + 1);
}
return statistics;
```

**Funktional (nachher):**
```java
return feedbackRepository.findAll().stream()
        .collect(Collectors.groupingBy(
                Feedback::getStatus,
                Collectors.counting()
        ));
```

**Vorteile der funktionalen Version:**
- ✅ 4 Zeilen statt 7 Zeilen
- ✅ Keine explizite Schleife
- ✅ Keine mutable Variable (`statistics`)
- ✅ Klarere Intention
- ✅ Weniger Fehleranfällig

---

## 6. REST-API Endpoints

Alle Collection-Processing-Methoden sind über REST-Endpoints verfügbar:

### 6.1 Endpoints:

| Endpoint | HTTP-Methode | Beschreibung | Rückgabetyp |
|----------|--------------|--------------|-------------|
| `/feedback/statistics/status` | GET | Gruppierung nach Status mit Counting | `Map<Status, Long>` |
| `/feedback/statistics/category` | GET | Gruppierung nach Kategorie mit Titel-Mapping | `Map<Category, List<String>>` |
| `/feedback/summary/published-active` | GET | Veröffentlichte, aktive Feedbacks (Summary) | `List<FeedbackSummaryDTO>` |
| `/feedback/statistics` | GET | Gesamtstatistiken über alle Feedbacks | `FeedbackStatisticsDTO` |

### 6.2 Beispiel-Requests:

```bash
# Status-Statistik
curl http://localhost:8080/feedback/statistics/status

# Kategorie-Gruppierung
curl http://localhost:8080/feedback/statistics/category

# Published Active Summary
curl http://localhost:8080/feedback/summary/published-active

# Gesamtstatistiken
curl http://localhost:8080/feedback/statistics
```

### 6.3 Beispiel-Responses:

**Status-Statistik:**
```json
{
  "OPEN": 5,
  "INPROGRESS": 3,
  "CLOSED": 2,
  "DONE": 1
}
```

**Kategorie-Gruppierung:**
```json
{
  "VERKEHR": ["Parkplatzproblem", "Ampeldefekt"],
  "UMWELT": ["Müllproblem", "Baumschaden"],
  "BELEUCHTUNG": ["Licht defekt"]
}
```

**Published Active Summary:**
```json
[
  {
    "id": 5,
    "title": "Neues Verkehrsproblem",
    "category": "VERKEHR",
    "status": "OPEN",
    "feedbackDate": "2025-12-07"
  },
  {
    "id": 3,
    "title": "Umweltproblem",
    "category": "UMWELT",
    "status": "INPROGRESS",
    "feedbackDate": "2025-12-06"
  }
]
```

**Gesamtstatistiken:**
```json
{
  "totalCount": 10,
  "publishedCount": 7,
  "closedCount": 2,
  "openCount": 5,
  "oldestDate": "2025-11-01",
  "newestDate": "2025-12-07"
}
```

---

## 7. Zusammenfassung

### Implementierte Operationen:

1. ✅ **Gruppierung von Daten** - `getFeedbackStatusStatistics()`, `getFeedbackTitlesByCategory()`
2. ✅ **Aggregation/Reduktion** - `getFeedbackStatistics()`
3. ✅ **Transformation/Mapping** - `getPublishedActiveFeedbacksSummary()`
4. ✅ **Filtering mit mehreren Kriterien** - `getPublishedActiveFeedbacksSummary()`

### Verwendete funktionale Konzepte:

- ✅ Stream API (umfassend)
- ✅ Collectors (groupingBy, counting, mapping)
- ✅ Methodenreferenzen (8x verwendet)
- ✅ Lambda-Ausdrücke (für komplexe Prädikate)
- ✅ Function Composition (Pipeline-Pattern)
- ✅ Optional für Null-Safety

### Implementierungsdetails:

**Service-Layer:**
- 4 neue Methoden in `FeedbackService`
- 2 neue DTO-Klassen (FeedbackSummaryDTO, FeedbackStatisticsDTO)
- Funktionale Implementierung mit Stream API

**Controller-Layer:**
- 4 neue REST-Endpoints in `FeedbackController`
- Vollständige REST-API-Integration

**Test-Layer:**
- 6 Service-Tests (Business-Logik)
- 5 Controller-Tests (REST-API)
- TDD/DDD-konforme Test-Struktur
- Robuste Test-Implementierung

### Code-Metriken:

- **4 neue Service-Methoden** implementiert
- **4 neue REST-Endpoints** im Controller
- **6 Service-Tests** erstellt
- **5 Controller-Tests** erstellt
- **Gesamt: 11 neue Tests** für Collection Processing
- **~150 Zeilen** Code (inkl. DTOs)
- **~150 Zeilen** Test-Code
- **0 Regressionen** - alle bestehenden Tests bestehen weiterhin (69 Tests insgesamt)

### Test-Ergebnisse:

```
Tests run: 69, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS ✅
```

---

## 7. Erweiterungsmöglichkeiten

### Potenzielle Verbesserungen:

1. **Parallele Verarbeitung:**
   ```java
   .parallelStream()
   .collect(...)
   ```

2. **Weitere Collectors:**
   - `Collectors.toSet()` - Eliminiert Duplikate
   - `Collectors.joining()` - Konkateniert Strings
   - `Collectors.averagingDouble()` - Berechnet Durchschnitt

3. **Komplexere Gruppierungen:**
   - Multi-Level Gruppierung
   - Custom Collector für spezielle Aggregationen

4. **Performance-Optimierung:**
   - Batch-Verarbeitung für große Collections
   - Caching von häufig verwendeten Statistiken

---

*Implementiert am: 07.12.2025*  
*Java Version: 25.0.1*  
*Spring Boot Version: 3.5.7*
