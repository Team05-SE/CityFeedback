# AOP (Aspect-Oriented Programming) - Dokumentation

## 📋 Übersicht

Diese Dokumentation erklärt die AOP-Implementierung im CityFeedback-Projekt. AOP wird verwendet, um Cross-Cutting Concerns (querschnittliche Belange) wie Logging und Performance-Monitoring zentral und transparent zu behandeln.

---

## 🎯 Was ist AOP?

**Aspect-Oriented Programming (AOP)** ist ein Programmierparadigma, das darauf abzielt, **Querschnittsfunktionen (Cross-Cutting Concerns)** sauber vom eigentlichen Fachcode zu trennen.

### Cross-Cutting Concerns im Projekt:
- ✅ **Logging** - Methodenaufrufe protokollieren
- ✅ **Performance-Monitoring** - Ausführungszeiten messen
- ✅ **Exception-Handling** - Fehler zentral loggen

Diese Funktionen würden normalerweise in jeder Methode wiederholt auftauchen. AOP lagert sie zentral in **Aspects** aus.

---

## 📍 Wo greift AOP?

### Konfiguration: Pointcut-Definition

Die AOP-Konfiguration erfolgt in:
**`src/main/java/com/example/cityfeedback/config/LoggingAspect.java`**

### Pointcut (Zeile 34-35):

```java
@Pointcut("execution(public * com.example.cityfeedback.usermanagement.application.*Service.*(..)) || " +
          "execution(public * com.example.cityfeedback.feedbackmanagement.application.*Service.*(..))")
public void applicationServiceMethods() {}
```

**Bedeutung:**
- Greift auf **alle öffentlichen Methoden** in Klassen, die mit `*Service` enden
- In den Packages:
  - `usermanagement.application`
  - `feedbackmanagement.application`

### Abgedeckte Services:

#### 1. UserService (`usermanagement.application.UserService`)

| Methode | Greift AOP? | Logged |
|---------|-------------|--------|
| `getAllUsers()` | ✅ Ja | ✅ |
| `getUserById(UUID id)` | ✅ Ja | ✅ |
| `createUser(Email, Password, UserRole)` | ✅ Ja | ✅ |
| `login(String email, String password)` | ✅ Ja | ✅ |
| `validateFeedbackDTO()` (private) | ❌ Nein | ❌ (private Methoden werden nicht erfasst) |

#### 2. FeedbackService (`feedbackmanagement.application.FeedbackService`)

| Methode | Greift AOP? | Logged |
|---------|-------------|--------|
| `getAllFeedbacks()` | ✅ Ja | ✅ |
| `getFeedbackById(Long id)` | ✅ Ja | ✅ |
| `createFeedback(FeedbackDTO dto)` | ✅ Ja | ✅ |
| `getFeedbackStatusStatistics()` | ✅ Ja | ✅ |
| `getFeedbackTitlesByCategory()` | ✅ Ja | ✅ |
| `getPublishedActiveFeedbacksSummary()` | ✅ Ja | ✅ |
| `getFeedbackStatistics()` | ✅ Ja | ✅ |
| `validateFeedbackDTO()` (private) | ❌ Nein | ❌ (private Methoden werden nicht erfasst) |

### Nicht abgedeckte Komponenten:

- ❌ **Controller** (`UserController`, `FeedbackController`) - nicht im Pointcut
- ❌ **Repository-Implementierungen** (`UserRepositoryImpl`, `FeedbackRepositoryImpl`) - nicht im Pointcut
- ❌ **Domain Services** (`UserRegistrationService`) - nicht im Pointcut
- ❌ **Private Methoden** - Execution Pointcuts greifen nur auf öffentliche Methoden

---

## 🔧 Konfiguration

### 1. AOP-Dependency (pom.xml)

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-aop</artifactId>
</dependency>
```

Spring Boot Starter AOP wird benötigt, damit AspectJ-Annotationen funktionieren.

### 2. LoggingAspect-Klasse

**Lage:** `src/main/java/com/example/cityfeedback/config/LoggingAspect.java`

**Annotationen:**
- `@Aspect` - Markiert die Klasse als AOP-Aspekt
- `@Component` - Spring-Komponente (wird automatisch erkannt)

**Keine weitere Konfiguration nötig!** Spring Boot erkennt AOP-Aspekte automatisch.

---

## 🎭 Advice-Typen

Der `LoggingAspect` verwendet **drei verschiedene Advice-Typen**:

### 1. @Before Advice (Methodenaufruf-Logging)

**Code (Zeile 42-48):**
```java
@Before("applicationServiceMethods()")
public void logMethodCall(JoinPoint joinPoint) {
    logger.info("➡️ Aufruf: {}({})",
            joinPoint.getSignature().toShortString(),
            joinPoint.getArgs().length
    );
}
```

**Funktion:**
- Wird **vor** jeder Service-Methode ausgeführt
- Loggt Methodenname und Anzahl der Parameter
- Kann die Methode **nicht verhindern** oder ändern

**Beispiel-Log:**
```
INFO  LoggingAspect : ➡️ Aufruf: UserService.getAllUsers()(0)
INFO  LoggingAspect : ➡️ Aufruf: FeedbackService.createFeedback(..)(1)
```

---

### 2. @AfterThrowing Advice (Exception-Logging)

**Code (Zeile 54-61):**
```java
@AfterThrowing(pointcut = "applicationServiceMethods()", throwing = "error")
public void logException(JoinPoint joinPoint, Throwable error) {
    logger.error("❌ Fehler in {}: {} - {}",
            joinPoint.getSignature().toShortString(),
            error.getClass().getSimpleName(),
            error.getMessage()
    );
}
```

**Funktion:**
- Wird **nur bei Exceptions** ausgeführt
- Loggt Methode, Exception-Typ und Fehlermeldung
- Wird **vor** dem `GlobalExceptionHandler` ausgeführt

**Beispiel-Log:**
```
ERROR LoggingAspect : ❌ Fehler in FeedbackService.getFeedbackById(..): 
                      FeedbackNotFoundException - Feedback mit ID 999999 wurde nicht gefunden.
```

---

### 3. @Around Advice (Performance-Messung)

**Code (Zeile 67-87):**
```java
@Around("applicationServiceMethods()")
public Object measureExecutionTime(ProceedingJoinPoint pjp) throws Throwable {
    long start = System.currentTimeMillis();
    Object result = pjp.proceed();  // Methode wird hier ausgeführt
    long duration = System.currentTimeMillis() - start;

    // Nur langsame Methoden loggen (Performance-Warnung)
    if (duration > 100) {
        logger.warn("⏱️ Langsame Methode {}(): {} ms",
                pjp.getSignature().getName(),
                duration
        );
    } else {
        logger.debug("⏱️ Dauer {}(): {} ms",
                pjp.getSignature().getName(),
                duration
        );
    }

    return result;
}
```

**Funktion:**
- Umhüllt die Methode **komplett**
- Misst Ausführungszeit **vor und nach** der Methode
- Kann die Methode verhindern, ändern oder den Rückgabewert modifizieren
- Loggt nur Methoden >100ms als WARN, andere als DEBUG

**Beispiel-Logs:**
```
DEBUG LoggingAspect : ⏱️ Dauer getAllUsers(): 15 ms
WARN  LoggingAspect : ⏱️ Langsame Methode getFeedbackStatistics(): 156 ms
```

---

## 🔄 Ausführungsreihenfolge

Wenn eine Service-Methode aufgerufen wird:

```
1. @Around Advice START → start = System.currentTimeMillis()
2. @Before Advice → Log "➡️ Aufruf: ..."
3. @Around: pjp.proceed() → Original-Methode wird ausgeführt
   ├─ Erfolgreich → weiter zu 4
   └─ Exception → @AfterThrowing → Log "❌ Fehler" → Exception weiter
4. @Around Advice END → duration berechnen → Log Performance
5. Rückgabewert wird zurückgegeben
```

---

## 📊 Beispiel-Ablauf

### Beispiel 1: Erfolgreicher Methodenaufruf

**Request:** `GET /user` → `UserService.getAllUsers()`

**Logs:**
```
INFO  LoggingAspect : ➡️ Aufruf: UserService.getAllUsers()(0)
DEBUG LoggingAspect : ⏱️ Dauer getAllUsers(): 23 ms
```

### Beispiel 2: Exception in Service-Methode

**Request:** `GET /feedback/999999` → `FeedbackService.getFeedbackById(999999)`

**Logs:**
```
INFO  LoggingAspect : ➡️ Aufruf: FeedbackService.getFeedbackById(..)(1)
ERROR LoggingAspect : ❌ Fehler in FeedbackService.getFeedbackById(..): FeedbackNotFoundException - Feedback mit ID 999999 wurde nicht gefunden.
WARN  GlobalExceptionHandler : Feedback nicht gefunden: Feedback mit ID 999999 wurde nicht gefunden.
```

### Beispiel 3: Langsame Methode

**Request:** `GET /feedback/statistics` → `FeedbackService.getFeedbackStatistics()`

**Logs:**
```
INFO  LoggingAspect : ➡️ Aufruf: FeedbackService.getFeedbackStatistics()(0)
WARN  LoggingAspect : ⏱️ Langsame Methode getFeedbackStatistics(): 156 ms
```

---

## 🎨 Alternative Konfigurationen

### Option 1: Mit Annotationen (Selektives Logging)

Falls du nur bestimmte Methoden loggen willst:

#### 1. Custom Annotation erstellen:
```java
package com.example.cityfeedback.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Loggable {
}
```

#### 2. Pointcut ändern:
```java
@Pointcut("@annotation(com.example.cityfeedback.config.Loggable)")
public void loggableMethods() {}
```

#### 3. Annotationen hinzufügen:
```java
@Service
public class UserService {
    
    @Loggable  // ← Nur diese Methode wird geloggt
    public List<User> getAllUsers() {
        return this.userRepository.findAll();
    }
    
    // Ohne @Loggable → wird nicht geloggt
    public User getUserById(UUID id) {
        return this.userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }
}
```

### Option 2: Controller auch loggen

Pointcut erweitern:
```java
@Pointcut("execution(* com.example.cityfeedback..*Controller.*(..))")
public void controllerMethods() {}

@Before("controllerMethods()")
public void logControllerCall(JoinPoint joinPoint) {
    logger.info("🌐 Controller-Aufruf: {}", joinPoint.getSignature().toShortString());
}
```

### Option 3: Nur @Transactional Methoden

```java
@Pointcut("@annotation(org.springframework.transaction.annotation.Transactional)")
public void transactionalMethods() {}
```

---

## ✅ Vorteile der aktuellen Implementierung

1. **Keine Annotationen nötig** - Automatisch für alle Service-Methoden
2. **Zentral konfiguriert** - Ein Pointcut für alle Services
3. **Transparent** - Keine Änderungen in Service-Klassen nötig
4. **Wartbar** - Logging-Logik an einem Ort
5. **Testbar** - AOP kann in Tests deaktiviert werden

---

## 🔍 AOP-Prinzipien im Detail

### Join Points
Stellen im Programmablauf, an denen ein Aspect eingreifen kann:
- ✅ **Methodenaufrufe** (in Spring AOP)
- ❌ Objekt-Initialisierung (nicht in Spring AOP)
- ❌ Attribut-Zugriffe (nicht in Spring AOP)

### Pointcuts
Regeln, die definieren, welche Join Points ausgewählt werden:
```java
// Alle öffentlichen Methoden in *Service Klassen
execution(public * ..*Service.*(..))
```

### Advice
Code, der an Join Points ausgeführt wird:
- **@Before** - Vor der Methode
- **@After** - Nach der Methode (erfolgreich oder mit Fehler)
- **@AfterReturning** - Nach erfolgreicher Ausführung
- **@AfterThrowing** - Bei Exceptions
- **@Around** - Umhüllt die Methode komplett

### Weaving
Das Einfügen von Aspects in den Code:
- **Runtime Weaving** - Spring AOP verwendet Proxy-Objekte zur Laufzeit
- Funktioniert nur bei Methodenaufrufen über Spring Beans

---

## 📁 Datei-Struktur

```
CityFeedback/
├── src/
│   └── main/
│       └── java/
│           └── com/example/cityfeedback/
│               ├── config/
│               │   └── LoggingAspect.java    ← AOP-Konfiguration
│               ├── usermanagement/
│               │   └── application/
│               │       └── UserService.java  ← Wird von AOP abgedeckt
│               └── feedbackmanagement/
│                   └── application/
│                       └── FeedbackService.java  ← Wird von AOP abgedeckt
└── pom.xml                                    ← AOP-Dependency
```

---

## 🧪 Testing

AOP funktioniert automatisch, auch in Integration-Tests:

```java
@SpringBootTest
class FeedbackServiceTest {
    
    @Autowired
    private FeedbackService feedbackService;
    
    @Test
    void testMethod() {
        // AOP greift hier automatisch!
        feedbackService.getAllFeedbacks();
        // → Logs werden geschrieben
    }
}
```

---

## 📝 Zusammenfassung

### Was wurde implementiert:
- ✅ **LoggingAspect** mit 3 Advice-Typen (@Before, @AfterThrowing, @Around)
- ✅ **Pointcut** für alle Service-Methoden
- ✅ **Automatisches Logging** ohne Code-Änderungen in Services
- ✅ **Performance-Monitoring** für langsame Methoden
- ✅ **Exception-Logging** zentral

### Abgedeckte Stellen:
- ✅ **UserService** - alle öffentlichen Methoden
- ✅ **FeedbackService** - alle öffentlichen Methoden

### Nicht abgedeckt:
- ❌ Controller
- ❌ Repository-Implementierungen
- ❌ Domain Services
- ❌ Private Methoden

### Konfiguration:
- ✅ **Keine Annotationen nötig** - funktioniert automatisch
- ✅ **Pointcut-basiert** - Package/Methoden-Namen
- ✅ **Central konfiguriert** - in LoggingAspect.java

---

*Dokumentation erstellt am: 11.12.2025*  
*Projekt: CityFeedback*  
*Übung 6: Aspect-Oriented Programming (AOP)*

