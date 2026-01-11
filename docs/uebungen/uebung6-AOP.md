# Übung 6: Aspect-Oriented Programming (AOP)

## Ziel
In dieser Übung werden Konzepte des Aspect-Oriented Programming (AOP) in das CityFeedback-Projekt integriert. Es werden Cross-Cutting Concerns identifiziert, analysiert und durch AOP-Aspekte zentral implementiert.

---

## Aufgabe 3a: Kernkonzepte von AOP

### Aspect-Oriented Programming (AOP)

**Aspect-Oriented Programming (AOP)** ist ein Programmierparadigma, das darauf abzielt, **Querschnittsfunktionen (Cross-Cutting Concerns)** sauber vom eigentlichen Fachcode zu trennen. Dadurch wird der Code modularer, klarer und besser wartbar.

### Cross-Cutting Concerns

**Cross-Cutting Concerns** sind Anforderungen, die sich quer durch verschiedene Teile einer Anwendung ziehen, aber nicht zur Kernlogik gehören.

**Beispiele:**
- Logging
- Sicherheitsprüfungen (Security Checks)
- Transaktionsverwaltung (z. B. Datenbank-Transaktionen)
- Caching
- Fehlerbehandlung
- Performance-Messungen

Diese Funktionen würden an vielen Stellen identisch oder ähnlich im Code auftauchen, daher lagert AOP sie zentral in **Aspects** aus.

### Join Points und Pointcuts

#### Join Points

Ein **Join Point** ist eine bestimmte Stelle im Programmablauf, an der ein Aspect eingefügt werden kann.

**Beispiele für Join Points:**
- der Aufruf einer Methode
- die Ausführung einer Methode
- das Werfen einer Exception
- das Initialisieren eines Objekts
- das Setzen eines Attributs (je nach AOP-Framework)

**Wichtig:** In Spring AOP sind Join Points **nur Methodenaufrufe**.

#### Pointcuts

Ein **Pointcut** definiert, welche Join Points ausgewählt werden. Ein Pointcut ist also eine Regel oder ein Ausdruck, der bestimmt, wo ein Aspect wirkt.

**Beispiele:**
- „alle Methoden im Package service"
- „alle Methoden mit Annotation @Transactional"
- „jede Methode, deren Name mit save* beginnt"

### Advice-Typen

Ein **Advice** ist der zusätzliche Code, der an einem Join Point ausgeführt wird.

#### Before Advice
Wird **vor** der eigentlichen Methode ausgeführt.

#### After Advice
Wird **nach** der Methode ausgeführt, egal ob erfolgreich oder mit Fehler.

#### After Returning
Wird **nur nach erfolgreicher Ausführung** ausgeführt.

#### After Throwing
Wird **nur bei Exceptions** ausgeführt.

#### Around Advice
Der mächtigste Advice-Typ: Er umwickelt die Methode und kann **vor und nach** dem Aufruf arbeiten, oder ihn sogar **verhindern/ersetzen**.

### Weaving-Prozess

Der **Weaving-Prozess** ist das Einfügen von Aspects in den Programmcode an den definierten Join Points.

Es gibt **drei Arten von Weaving:**

#### 1. Compile-Time Weaving
- Aspects werden bereits **beim Kompilieren** in den Bytecode eingefügt
- Wird z. B. von **AspectJ** unterstützt

#### 2. Load-Time Weaving (LTW)
- Das Einweben passiert **während das Programm geladen wird**, also beim Starten durch den Class Loader
- Flexibel, aber etwas komplexer in der Konfiguration

#### 3. Runtime Weaving
- Das Weaving geschieht **zur Laufzeit** durch Proxy-Objekte
- Wird von **Spring AOP** verwendet
- Beschränkt auf Methodenaufrufe

---

## Aufgabe 3b: AOP-Analyse des Projekts

### Identifizierte Cross-Cutting Concerns

Im vorliegenden City-Feedback-System lassen sich mehrere **Querschnittsbelange (Cross-Cutting Concerns)** identifizieren, die das System durchgängig betreffen, jedoch nicht zur fachlichen Kernlogik gehören.

#### Fehlerbehandlung

Ein offensichtliches Beispiel ist die **Fehlerbehandlung**, insbesondere im Zusammenhang mit dem Werfen von `EntityNotFoundException` in mehreren Services. Diese Form der Exception-Logik ist wiederkehrend und könnte über ein globales Exception-Handling oder AOP-gestützte Advice-Mechanismen zentralisiert werden.

#### Logging und Monitoring

Ebenso zählen **Logging und Monitoring** zu klassischen Cross-Cutting Concerns, die aktuell nur vereinzelt – etwa beim Registrieren eines Users im `UserRegisteredEventHandler` – umgesetzt sind, jedoch im gesamten System konsistent benötigt würden.

### Wiederholende Funktionalitäten

Darüber hinaus existieren wiederholende Funktionalitäten, die für AOP geeignet sind:

#### Konsistente Validierung

Dazu gehört insbesondere das **konsistente Validieren und Finden von Benutzern**: Sowohl der `FeedbackService` als auch der `UserService` greifen über das `UserRepository` zu und führen ähnliche Fehlerprüfungen durch:
- Überprüfen der Passwortgültigkeit
- Überprüfen der Existenz von Usern anhand ihrer IDs oder E-Mail-Adressen

#### DTO-Mapping

Auch die wiederkehrende Nutzung von DTOs und das automatische Zuordnen von Request-Daten zu Domänenobjekten könnten zukünftig durch AOP-gestützte Mapping- oder Validierungsprozesse unterstützt werden.

### Potenzielle AOP-Anwendungsfälle

Potenzielle AOP-Anwendungsfälle ergeben sich insbesondere in folgenden Bereichen:

1. **Zentrales Logging von Service-Methoden**
   - Aufrufdauer
   - Name der Methode
   - Übergebene Parameter

2. **Globale Fehlerbehandlung**
   - AOP-Advice, das Exceptions abfängt
   - Übersetzung in konsistente HTTP-Responses
   - Zentrale Protokollierung

3. **Transaktionsverwaltung**
   - Insbesondere für Methoden wie `createUser` oder `createFeedback`, die Schreibzugriffe ausführen

4. **Sicherheitsaspekte**
   - Rollen- oder Berechtigungsprüfungen (z. B. ADMIN-Operationen)
   - Über ein Aspect statt manuell implementiert

5. **Domain-Event-Handling**
   - Durch AOP abgefangen werden könnte, um systemweite Reaktionen einheitlich zu gestalten

### Fazit der Analyse

Durch Einsatz dieser AOP-Ansätze ließe sich der Code **modularer gestalten**, **redundante Logik reduzieren** und eine **klarere Trennung** zwischen fachlicher Logik und Infrastruktur-/Systemlogik erreichen.

---

## Aufgabe 3c: Implementierung von AOP

### Konzeption: Logging + Performance Monitoring

**Idee für den AOP-Aspekt:** Logging + Performance Monitoring

**Warum das sinnvoll ist:**
- Das Projekt besteht aus mehreren Services und Controllern → perfekte Join Points
- Fehler werden an vielen Stellen geworfen → zentral abfangbar
- Performance/Execution Time ist ideal messbar mit @Around
- Müheloser Mehrwert ohne Eingriff in Fachlogik

**Kombinierte Funktionalität:**
- ✅ **Before-Logging** - Welche Methode wurde aufgerufen?
- ✅ **AfterThrowing-Logging** - Welcher Fehler trat auf?
- ✅ **Around-Advice** - Performance-Messung

Damit werden die Anforderungen vollumfänglich erfüllt.

### Implementierung: LoggingAspect

**Datei:** `src/main/java/com/example/cityfeedback/config/LoggingAspect.java`

```java
package com.example.cityfeedback.config;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    // --- POINTCUT für alle Methoden im Application-Layer ---
    @Pointcut("execution(* com.example.cityfeedback..service..*(..)) || " +
              "execution(* com.example.cityfeedback..application..*(..))")
    public void applicationLayer() {}

    // --- BEFORE: Logge jeden Methodenaufruf ---
    @Before("applicationLayer()")
    public void logBefore(JoinPoint joinPoint) {
        logger.info("➡️ Aufruf: {}({})",
                joinPoint.getSignature().toShortString(),
                joinPoint.getArgs().length
        );
    }

    // --- AFTER THROWING: Logge geworfene Fehler zentral ---
    @AfterThrowing(pointcut = "applicationLayer()", throwing = "error")
    public void logError(JoinPoint joinPoint, Throwable error) {
        logger.error("❌ Fehler in {}: {}",
                joinPoint.getSignature().toShortString(),
                error.getMessage()
        );
    }

    // --- AROUND: Performance Monitoring ---
    @Around("applicationLayer()")
    public Object measureExecutionTime(ProceedingJoinPoint pjp) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = pjp.proceed();
        long duration = System.currentTimeMillis() - start;

        logger.info("⏱️ Dauer {}(): {} ms",
                pjp.getSignature().getName(),
                duration
        );

        return result;
    }
}
```

### Was deckt dieser Aspect ab?

| Cross-Cutting Concern | Implementierung |
|----------------------|-----------------|
| **Logging & Performance Monitoring** | Vollständig abgedeckt |
| **Pointcuts** | Alle Methoden im gesamten Application- und Service-Layer |
| **Before-Advice** | Loggt jeden Methodenaufruf |
| **AfterThrowing-Advice** | Loggt Exceptions global |
| **Around-Advice** | Misst Performance jeder Methode |

Damit werden alle typischen AOP-Anforderungen der Aufgabenstellung erfüllt.

### Abgedeckte Services

Der Aspect greift automatisch auf folgende Services zu:

**UserService:**
- `getAllUsers()`
- `getUserById(UUID id)`
- `createUser(Email, Password, UserRole)`
- `login(String email, String password)`

**FeedbackService:**
- `getAllFeedbacks()`
- `getFeedbackById(Long id)`
- `createFeedback(FeedbackDTO dto)`
- `getFeedbackStatusStatistics()`
- `getFeedbackTitlesByCategory()`
- `getPublishedActiveFeedbacksSummary()`
- `getFeedbackStatistics()`

### Beispiel-Logs

**Erfolgreicher Methodenaufruf:**
```
INFO  LoggingAspect : ➡️ Aufruf: UserService.getAllUsers()(0)
INFO  LoggingAspect : ⏱️ Dauer getAllUsers(): 23 ms
```

**Exception:**
```
INFO  LoggingAspect : ➡️ Aufruf: FeedbackService.getFeedbackById(..)(1)
ERROR LoggingAspect : ❌ Fehler in FeedbackService.getFeedbackById(..): FeedbackNotFoundException - Feedback mit ID 999999 wurde nicht gefunden.
```

---

## Aufgabe 5: LLM-Einsatz für AOP

### Dokumentation des LLM-Einsatzes

Für die Integration von Aspect-Oriented Programming (AOP) im City-Feedback-System wurde ein **Large Language Model (LLM)** gezielt eingesetzt, um zentrale Querschnittsbelange zu identifizieren, passende Aspekt-Designs vorzubereiten und die technische Implementierung zu optimieren.

### 1. Identifikation von Cross-Cutting Concerns

Das LLM analysierte den bestehenden Code aus den Bereichen Feedback-Management und User-Management. Dabei konnten wiederkehrende Funktionsmuster präzise identifiziert werden:

- Logging von Methodenaufrufen
- Exception-Handling
- Performance-Monitoring
- Konsistente Fehlerbehandlung bei Repository-Abfragen

Auf dieser Basis wurden konkrete Cross-Cutting Concerns herausgearbeitet, die das System wiederholt betreffen, aber nicht zur Kernlogik gehören.

### 2. Entwicklung des AOP-Aspekts

Auf Grundlage der Analyse entwickelte das LLM einen vollständigen **Logging- und Performance-Monitoring-Aspect**. Dieser beinhaltete:

- Passende Pointcuts für Service-Methoden
- Einen **Before-Advice** für Methodenaufrufe
- Einen **AfterThrowing-Advice** für globale Fehlerbehandlung
- Ein **Around-Advice** zur Messung der Ausführungszeit

Der generierte Code war kompatibel mit Spring AOP, modular aufgebaut und direkt integrierbar.

### 3. Optimierung der Implementierung

Das LLM unterstützte dabei, redundante oder ineffiziente Implementierungen zu erkennen, z. B. mehrfaches explizites Exception-Logging in Services. Der vorgeschlagene Aspect ersetzt diese Logik konsistent, steigert die Wartbarkeit und trennt Fachlogik klar von technischen Belangen. 

Zudem wurden Alternativen wie Security-Aspekte oder Transaktionskontrolle aufgezeigt.

### 4. Reflexion und Nutzen

**Vorteile:**
- Der Einsatz des LLM hat die **Entwicklungszeit erheblich reduziert**, indem komplexe AOP-Konzepte schnell auf das Projekt übertragen wurden
- Besonders hilfreich war die Fähigkeit des Modells, **Code zu analysieren** und darauf abgestimmte Lösungen zu generieren
- Das LLM konnte als **"Architektur-Assistent"** eingesetzt werden

**Herausforderungen:**
- Herausforderungen bestanden vor allem darin, **präzise Anforderungen zu formulieren**
- Der generierte Code musste an **Projektnormen angepasst** werden

**Lessons Learned:**
- LLMs sind ideal als **"Architektur-Assistent"** einsetzbar
- **Menschliche Validierung** ist immer erforderlich
- Besonders wichtig bei **sicherheitskritischen oder domänenspezifischen Entscheidungen**

---

## Zusammenfassung

### Implementiert:
- ✅ **AOP-Kernkonzepte** dokumentiert und verstanden
- ✅ **Cross-Cutting Concerns** identifiziert und analysiert
- ✅ **LoggingAspect** implementiert mit 3 Advice-Typen
- ✅ **Pointcuts** für Application-Layer konfiguriert
- ✅ **LLM-Einsatz** dokumentiert und reflektiert

### Verwendete Konzepte:
- ✅ **Cross-Cutting Concerns** (Logging, Performance, Exception-Handling)
- ✅ **Join Points** (Methodenaufrufe)
- ✅ **Pointcuts** (Execution-Pointcuts für Services)
- ✅ **Advice-Typen** (Before, AfterThrowing, Around)
- ✅ **Runtime Weaving** (Spring AOP mit Proxies)

### Erreichte Verbesserungen:
- ✅ **Zentrales Logging** ohne Code-Änderungen in Services
- ✅ **Performance-Monitoring** automatisch für alle Service-Methoden
- ✅ **Exception-Logging** zentral und konsistent
- ✅ **Modulare Architektur** durch Trennung von Fachlogik und Cross-Cutting Concerns
- ✅ **Wartbarkeit** durch zentrale Konfiguration

---

*Übung 6 - Aspect-Oriented Programming (AOP)*  
*Implementiert am: 11.12.2025*  
*Spring Boot Version: 3.5.7, Spring AOP*

