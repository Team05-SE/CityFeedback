# Systemarchitektur modellieren
## 1 Eventstorming 
Im Rahmen des Feedbackprojekts wurde ein Eventstorming durchgeführt, um alle relevanten Abläufe, Ereignisse und Beteiligten transparent darzustellen. Ziel war es, ein gemeinsames Verständnis über den aktuellen Prozess zu schaffen und Optimierungspotenziale klar zu identifizieren. Durch die visuelle Modellierung der Ereignisse konnten Zusammenhänge schneller erkannt, offene Fragen sichtbar gemacht und erste Lösungsansätze entwickelt werden. Die gewonnenen Erkenntnisse bilden die Grundlage für die weitere Konzeption und Umsetzung eines verbesserten Feedbacksystems.

![CityFeedback -1- Eventstorming](https://github.com/user-attachments/assets/fded9597-e9cc-4f3a-9d98-73d9f29e3042)

**Identifizierte Events im Feedbacksystem:**
- **Bürger registriert**: Ein neuer Bürgeraccount wird im System angelegt.  
- **Bürgerprofil angepasst**: Ein Bürger aktualisiert seine persönlichen Daten.  
- **Bürgeraccount gelöscht**: Ein Bürger entfernt seinen Account aus dem System.  
- **Mitarbeiteraccount angelegt**: Ein Mitarbeiteraccount wird durch den IT-Admin erstellt.  
- **Benutzer authentifiziert**: Ein Bürger oder Mitarbeiter meldet sich erfolgreich im System an.  
- **Feedback erstellt**: Ein Bürger oder Mitarbeiter erstellt ein neues Feedback mit Beschreibung, Kategorie und optionalen Anhängen.  
- **Feedback veröffentlicht**: Das Feedback wird öffentlich sichtbar gemacht.  
- **Vorgang angelegt**: Das System legt automatisch einen neuen Vorgang zum Feedback an.  
- **Vorgang zugewiesen**: Das System oder ein Administrator weist den Vorgang einem Mitarbeiter zu.  
- **Sachbearbeitung begonnen**: Ein Mitarbeiter startet die Bearbeitung des Vorgangs.  
- **Status geändert**: Der Bearbeitungsstatus eines Vorgangs wird aktualisiert (z. B. „in Bearbeitung“, „abgeschlossen“).  
- **Kommentar hinzugefügt**: Ein Mitarbeiter fügt dem Vorgang oder Feedback einen Kommentar hinzu.  
- **Feedback zurückgenommen**: Der Bürger zieht sein Feedback oder Anliegen zurück.  
- **Benachrichtigung versendet**: Das System sendet eine E-Mail-Benachrichtigung an Bürger oder Mitarbeiter.  
- **Dashboard aktualisiert**: Das System aktualisiert die Statusübersicht und Statistiken für Bürger- oder Mitarbeiterdashboard.  

Für die Startphase haben wir uns dann für eine reduzierte Variante entschieden, da wir zunächst die wichtigsten Ereignisse sichtbar machen wollten, ohne den Prozess zu überfrachten. So konnten wir uns Schritt für Schritt auf die zentralen Abläufe konzentrieren und ein gemeinsames Verständnis dafür entwickeln, welche Events für das Feedbackprojekt wirklich entscheidend sind. Diese klare Fokussierung hat uns geholfen, echte Kernthemen herauszuarbeiten und eine solide Basis für die weitere Detaillierung zu legen.

- **User registriert**: Ein neuer Useraccount wird im System angelegt.  
- **Userprofil angepasst**: Ein User aktualisiert seine persönlichen Daten.  
- **Useraccount gelöscht**: Ein User entfernt seinen Account aus dem System.  
- **User authentifiziert**: Ein User meldet sich erfolgreich im System an.  
- **Feedback erstellt**: Ein User erstellt ein neues Feedback mit Beschreibung, Kategorie und optionalen Anhängen.  
- **Feedback veröffentlicht**: Ein Feedback wird öffentlich sichtbar gemacht.  
- **Feedback zurückgenommen**: Ein User zieht sein Feedback oder Anliegen zurück.  
- **Kommentar hinzugefügt**: Ein Mitarbeiter fügt dem Feedback einen Kommentar hinzu.  

## Klassendiagramm
Das erste Klassendiagramm dient dazu, die grundlegende Struktur des Feedbacksystems zu visualisieren. Es zeigt die zentralen Klassen, ihre Eigenschaften sowie die Beziehungen untereinander. Ziel dieser Darstellung ist es, ein gemeinsames Verständnis der fachlichen Domänenobjekte zu schaffen und eine erste Grundlage für die weitere fachliche und technische Modellierung zu legen. Das Diagramm bildet damit den Ausgangspunkt für die spätere Verfeinerung und Erweiterung des Daten- und Prozessmodells.

![CityFeedback -2- Klassendiagramm](https://github.com/user-attachments/assets/f35a3069-ab1a-48d8-9d2b-ad1ce669f5ff)

## 2 Domänenmodell
Das Domänenmodell definiert die zentralen fachlichen Strukturen des Feedbacksystems und beschreibt die Kernentitäten sowie deren Beziehungen innerhalb der Domäne. Es bildet die konzeptionelle Grundlage für die spätere technische Umsetzung und dient als Referenz für Architektur, Datenmodell und Schnittstellendesign.

Im Fokus stehen die Entitäten **User** und **Feedback**, deren Attribute, Verantwortlichkeiten und Interaktionen die fachliche Logik des Systems maßgeblich bestimmen. Durch die Abbildung dieser Beziehungen ermöglicht das Domänenmodell eine klare Trennung fachlicher Zuständigkeiten und schafft die Basis für eine konsistente und erweiterbare Systemarchitektur.

<img src="https://github.com/user-attachments/assets/02196330-8fa3-49ed-bf77-9a5814075775" 
     alt="cityfeedback-3-domänenmodell" 
     style="width:30%;" />

## 3 Bounded Contexts identifizieren
Um die Domäne des CityFeedback-Systems übersichtlich zu strukturieren, wird die Anwendung in klar abgegrenzte Bounded Contexts unterteilt.
Jeder Kontext kapselt eigene Geschäftslogik, Datenmodelle und Verantwortlichkeiten, interagiert nur über definierte Schnittstellen und bleibt unabhängig von anderen Kontexten.

Für die erste Umsetzung wurden folgende Kontexte identifiziert: Usermanagement zur Verwaltung von Benutzern, Rollen und Authentifizierung sowie Feedbackmanagement zur Erstellung, Verwaltung und Veröffentlichung von Feedback.

### Usermanagement

**Zuständig für:**
- Registrierung und Verwaltung von Bürgern und Mitarbeitern  
- Authentifizierung (Login)  
- Rollen- und Berechtigungsverwaltung  

Der **Usermanagement-Kontext** liefert Benutzeridentitäten und Rollen, die vom Feedbackmanagement genutzt werden.

### Feedbackmanagement

**Verantwortlich für:**
- Erstellung und Verwaltung von Feedback  
- Veröffentlichung und Rücknahme von Feedback  
- Verwaltung von Kommentaren und Kategorien  

Das **Feedbackmanagement** speichert Feedback-Inhalte und referenziert die Benutzer über ihre IDs, ohne direkt auf User-Entitäten zuzugreifen.
<img src="https://github.com/user-attachments/assets/d3f88956-37e0-4d27-a3a7-c1dfb8d80423"
     alt="cityfeedback-3-BoundedContext"
     width="500" />

## 4 Entitäten und Aggregates definieren
### Usermanagement Context
**Aggregate Root:** User  

- **User-ID**  
- **Name**  
- **E-Mail**  
- **Passwort/Hash**  
- **Rolle** (Bürger/Mitarbeiter/Admin)  
- **Status** (aktiv/deaktiviert)  
- **Letzte Anmeldung**  

### Feedback Management Context
**Aggregate Root:** Feedback
- **ID**
- **Betreff**
- **Beschreibung**
- **Kategorie**
- **Ersteller** (User-ID, Value Object)
- **Erstellungsdatum**
- **Status** (Entwurf, veröffentlicht, zurückgenommen)
- **öffentliche Sichtbarkeit**

**Kommentare** (separates Aggregate)
- **ID**
- **Feedback-ID** (Value Object)
- **Ersteller** (User-ID)
- **Text**
- **Datum**

## 5 Domainservices und Repositories
### Domainservices

- Userregistrierungsdienst: erstellt neue Bürger-/Mitarbeiterkonten  
- Authentifizierungsdienst: prüft Login-Daten  
- Feedback-Erstellungsdienst: legt neues Feedback an; optional erste Validierungen  
- Feedback-Veröffentlichungsservice: setzt Status „veröffentlicht“, prüft Sichtbarkeit  
- Feedback-Rücknahmeservice: setzt Status „zurückgenommen“ 
- Kommentarservice (optional): fügt Kommentar zu Feedback hinzu  

### Repositories

- Benutzer-Repository: Verwaltung und Persistenz von Benutzerkonten.
- Feedback-Repository: Speicherung und Abruf von Feedback-Daten.
- Kommentar-Repository: Speicherung von Kommentaren zu Feedback oder Vorgängen.

### Übersicht der Repositories und Methoden

**BenutzerRepository**
- findeBenutzerNachId(String id)
- speichereBenutzer(Benutzer benutzer)

**FeedbackRepository**
- findeFeedbackNachId(String id)
- speichereFeedback(Feedback feedback)
- *(optional)* findeFeedbackNachBenutzer(String benutzerId)

**KommentarRepository**(nur falls Kommentare jetzt schon benötigt sind)*
- findeKommentareZuFeedback(String feedbackId)
- speichereKommentar(Kommentar kommentar)


## 6 Implementierungsstrategie
Die Umsetzung des Feedbacksystems erfolgt in mehreren Schichten, basierend auf dem Spring-Boot-Framework:
- Entitäten implementieren: Java Klassen werden für jede Entität implementiert.
- Services implementieren: Geschäftslogik implementieren.
- Repositories implementieren: Definition von Interfaces, die Methoden zum Abrufen und zur Speicherung der Entitäten enthalten
- Controller implementieren: Verarbeitung der HTTP-Anfragen (GET, POST, PUT, DELETE)

Die Implementierung des Feedbackprojekts folgt einer mehrschichtigen Architektur innerhalb der definierten Bounded Contexts. Jeder Layer übernimmt dabei klar definierte Aufgaben:
- Client-Layer: Nimmt HTTP-Anfragen über den Browser oder die UI entgegen und zeigt Antworten an.
- Application-Layer: Enthält die Controller und Services, die Anfragen verarbeiten, Geschäftslogik ausführen und Services aufrufen.
- Domain-Layer: Modelliert die Kernkonzepte (Aggregates, Value Objects, Events) und kapselt die zentrale Geschäftslogik.
- Infrastruktur-Layer: Stellt den Zugriff auf Persistenzsysteme (z. B. H2 In-Memory-Datenbank) bereit und integriert Framework-spezifische Komponenten wie Repositories und Konfigurationen.

CI/CD & Automatisierung: Build, Test und Deployment werden über Maven und GitHub Actions automatisiert, um Konsistenz, Testbarkeit und kontinuierliche Bereitstellung sicherzustellen.

Diese Architektur sorgt für eine klare Trennung von Verantwortlichkeiten, modulare Erweiterbarkeit und effiziente Automatisierung entlang des gesamten Entwicklungszyklus.
---


![CityFeedback -6- Implementierungsstrategie-BC](https://github.com/user-attachments/assets/66ac22f7-4c7b-41ab-b13e-67812bb9c2ac)

## Anhang
Das nachfolgende Klassendiagramm zeigt die implementierten Java-Klassen des Feedbackprojekts, ihre Attribute, Methoden sowie die Beziehungen zwischen den Entitäten nach der Umsetzung in Spring Boot.

Klassendiagramm nach Implementierung:
<img width="2112" height="3532" alt="cityfeedback" src="https://github.com/user-attachments/assets/0aaf8bf8-fa14-4b3e-ab2b-57413e6065fe" />

