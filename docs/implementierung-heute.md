# Implementierungs-Dokumentation - Heute

**Datum:** Heute  
**Version:** 1.0  
**Projekt:** CityFeedback

---

## 📋 Übersicht

Diese Dokumentation beschreibt alle Features und Funktionalitäten, die heute im CityFeedback-Projekt implementiert wurden. Das Projekt folgt den Prinzipien von Domain-Driven Design (DDD), Test-Driven Development (TDD) und funktionaler Programmierung.

---

## 🎯 Hauptfeatures

### 1. Admin-Funktionalitäten

#### 1.1 Benutzerverwaltung
- **Erstellen von Benutzern**: Admins können neue Benutzer (Bürger, Mitarbeiter, Admins) erstellen
- **Rollen ändern**: Admins können die Rollen anderer Benutzer ändern
- **Passwörter ändern**: Admins können Passwörter von Benutzern zurücksetzen
- **Benutzer löschen**: Admins können Benutzer löschen (mit automatischer Löschung aller zugehörigen Feedbacks)
- **Benutzerübersicht**: Vollständige Übersicht aller registrierten Benutzer

**Technische Details:**
- Endpunkt: `POST /user/admin/create`
- Endpunkt: `PUT /user/{id}/role`
- Endpunkt: `PUT /user/{id}/password`
- Endpunkt: `DELETE /user/{id}`
- Frontend: `/dashboard/admin/users`

#### 1.2 Demo-Daten System
- **Automatische Demo-Daten**: Beim Start der Anwendung werden automatisch Demo-Daten erstellt:
  - 4 Demo-Bürger (`demo.buerger1@example.com` bis `demo.buerger4@example.com`)
  - 2 Demo-Mitarbeiter (`demo.mitarbeiter1@stadt.de`, `demo.mitarbeiter2@stadt.de`)
  - 10 Demo-Feedbacks in verschiedenen Status
  - Passwort für alle Demo-User: `Demo123!`
- **Demo-Daten löschen**: Admins können alle Demo-Daten mit einem Button löschen
- **Intelligente Erkennung**: Demo-User werden anhand ihrer E-Mail-Adresse erkannt (beginnt mit "demo." oder enthält "@example.com")

**Technische Details:**
- Klasse: `DemoDataInitializer` (Infrastructure Layer)
- Service: `DemoDataService`
- Endpunkt: `DELETE /admin/demo-data`
- Frontend: Button in der Benutzerverwaltung

#### 1.3 Default Admin
- **Automatische Erstellung**: Beim ersten Start wird automatisch ein Admin-User erstellt
- **Login-Daten**: 
  - E-Mail: `admin@cityfeedback.de`
  - Passwort: `Admin123!`
- **Passwort-Änderung**: Der Admin kann sein Passwort später ändern

**Technische Details:**
- Klasse: `DefaultAdminInitializer` (Infrastructure Layer)
- Implementiert: `CommandLineRunner`

---

### 2. Mitarbeiter-Funktionalitäten

#### 2.1 Feedback-Verwaltung
- **Alle Feedbacks anzeigen**: Mitarbeiter und Admins sehen alle Feedbacks im System
- **Status ändern**: Mitarbeiter können den Status von Feedbacks ändern:
  - PENDING (Entwurf)
  - OPEN (Offen)
  - INPROGRESS (In Bearbeitung)
  - DONE (Erledigt)
  - CLOSED (Geschlossen)
- **Feedbacks freigeben**: PENDING-Feedbacks können auf OPEN gesetzt werden
- **Feedbacks veröffentlichen**: Feedbacks können öffentlich sichtbar gemacht werden
- **Statistiken**: Übersicht über Feedback-Status-Verteilung

**Technische Details:**
- Endpunkt: `GET /feedback` (alle Feedbacks)
- Endpunkt: `PUT /feedback/{id}/approve`
- Endpunkt: `PUT /feedback/{id}/status`
- Endpunkt: `PUT /feedback/{id}/publish`
- Frontend: `/dashboard/staff/feedbacks`

#### 2.2 Kommentar-System
- **Kommentare hinzufügen**: Mitarbeiter und Admins können Kommentare zu Feedbacks hinzufügen
- **Kommentare anzeigen**: Alle Kommentare werden chronologisch angezeigt
- **Status-Änderung mit Kommentar**: Beim Ändern des Status kann optional ein Kommentar hinzugefügt werden
- **Kommentar-Validierung**: Kommentare müssen zwischen 1 und 2000 Zeichen lang sein

**Technische Details:**
- Domain-Modell: `Comment` (Domain Layer)
- Repository: `CommentRepository` (Interface im Domain Layer)
- Service: `FeedbackService.addComment()`
- Endpunkt: `POST /feedback/{id}/comments`
- Endpunkt: `GET /feedback/{id}/comments`
- Frontend: Expandierbare Kommentar-Sektion in der Feedback-Verwaltung

---

### 3. Feedback-Status-System

#### 3.1 Status-Workflow
- **PENDING**: Neues Feedback startet als Entwurf (nicht öffentlich sichtbar)
- **OPEN**: Feedback wurde von einem Mitarbeiter freigegeben
- **INPROGRESS**: Feedback wird aktuell bearbeitet
- **DONE**: Feedback wurde erfolgreich bearbeitet
- **CLOSED**: Feedback wurde geschlossen

#### 3.2 Veröffentlichungslogik
- PENDING-Feedbacks sind nicht öffentlich sichtbar
- Feedbacks müssen erst auf OPEN gesetzt werden, bevor sie veröffentlicht werden können
- CLOSED-Feedbacks können nicht veröffentlicht werden
- Veröffentlichte Feedbacks sind auf der öffentlichen Seite sichtbar

**Technische Details:**
- Value Object: `Status` (Enum)
- Domain-Methoden: `Feedback.approve()`, `Feedback.publish()`, `Feedback.updateStatus()`

---

### 4. Öffentliche Seite

#### 4.1 Öffentliche Feedback-Übersicht
- **Ohne Login**: Öffentliche Seite ist für alle ohne Anmeldung zugänglich
- **Gruppierung nach Status**: Feedbacks werden nach Status gruppiert angezeigt
- **Kategorie-Filter**: Filterung nach Kategorien möglich:
  - Verkehr
  - Umwelt
  - Beleuchtung
  - Vandalismus
  - Verwaltung
- **Kommentare anzeigen**: Alle Kommentare zu veröffentlichten Feedbacks sind öffentlich sichtbar

**Technische Details:**
- Endpunkt: `GET /feedback/public`
- Frontend: `/public` oder `/` (Root-Route)
- Automatisches Laden von Kommentaren für alle Feedbacks

---

### 5. Admin-spezifische Feedback-Funktionen

#### 5.1 Feedback löschen
- **Komplettes Löschen**: Admins können Feedbacks komplett aus der Datenbank löschen
- **Kaskadierendes Löschen**: Beim Löschen eines Feedbacks werden automatisch alle zugehörigen Kommentare gelöscht
- **Sicherheit**: Nur Admins können Feedbacks löschen (Backend-Validierung)

**Technische Details:**
- Endpunkt: `DELETE /feedback/{id}`
- Service: `FeedbackService.deleteFeedback()`
- Frontend: Delete-Button in der Feedback-Verwaltung (nur für Admins sichtbar)

---

## 🏗️ Architektur

### Domain-Driven Design (DDD)

#### Domain Layer (Framework-unabhängig)
- **Aggregate Roots:**
  - `User` (usermanagement)
  - `Feedback` (feedbackmanagement)
  - `Comment` (feedbackmanagement) - **NEU**
- **Value Objects:**
  - `Email`, `Password`, `UserRole` (usermanagement)
  - `Category`, `Status` (feedbackmanagement)
- **Domain Services:**
  - `UserRegistrationService`
  - `FeedbackService` (erweitert)
- **Repository Interfaces:**
  - `UserRepository`
  - `FeedbackRepository`
  - `CommentRepository` - **NEU**

#### Application Layer
- **Application Services:**
  - `UserService` (erweitert)
  - `FeedbackService` (erweitert)
  - `DemoDataService` - **NEU**
- **Controllers:**
  - `UserController` (erweitert)
  - `FeedbackController` (erweitert)
  - `DemoDataController` - **NEU**
- **DTOs:**
  - `CommentDTO` - **NEU**
  - `UpdateStatusDTO`
  - `CreateUserByAdminDTO`

#### Infrastructure Layer
- **JPA Entities:**
  - `UserEntity`
  - `FeedbackEntity`
  - `CommentEntity` - **NEU**
- **JPA Repositories:**
  - `UserJpaRepository`
  - `FeedbackJpaRepository`
  - `CommentJpaRepository` - **NEU**
- **Repository Implementations:**
  - `UserRepositoryImpl`
  - `FeedbackRepositoryImpl`
  - `CommentRepositoryImpl` - **NEU**
- **Mappers:**
  - `UserMapper`
  - `FeedbackMapper`
  - `CommentMapper` - **NEU**
- **Initializers:**
  - `DefaultAdminInitializer`
  - `DemoDataInitializer` - **NEU**

---

## 🔐 Sicherheit & Berechtigungen

### Rollenbasierte Zugriffskontrolle

#### CITIZEN (Bürger)
- Eigene Feedbacks erstellen
- Eigene Feedbacks ansehen
- Öffentliche Feedbacks ansehen

#### STAFF (Mitarbeiter)
- Alle Feedbacks ansehen
- Feedback-Status ändern
- Feedbacks freigeben
- Feedbacks veröffentlichen
- Kommentare hinzufügen

#### ADMIN (Administrator)
- Alle STAFF-Funktionen
- Benutzer erstellen
- Benutzer-Rollen ändern
- Benutzer-Passwörter ändern
- Benutzer löschen
- Feedbacks löschen
- Demo-Daten löschen

### Backend-Validierung
- Alle Admin-Funktionen prüfen die Rolle im Backend
- UnauthorizedException wird geworfen bei unberechtigtem Zugriff
- Transaktionale Sicherheit bei Löschoperationen

---

## 📊 Datenbank-Schema

### Neue Tabelle: `comments`

```sql
CREATE TABLE comments (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    feedback_id BIGINT NOT NULL,
    author_id UUID NOT NULL,
    content TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL,
    FOREIGN KEY (feedback_id) REFERENCES feedbacks(id) ON DELETE CASCADE
);
```

**Beziehungen:**
- `comments.feedback_id` → `feedbacks.id` (Many-to-One)
- Kaskadierendes Löschen: Beim Löschen eines Feedbacks werden alle Kommentare gelöscht

---

## 🎨 Frontend-Implementierung

### Neue Seiten

1. **Admin Benutzerverwaltung** (`/dashboard/admin/users`)
   - Tabelle aller Benutzer
   - Buttons zum Erstellen, Bearbeiten, Löschen
   - Demo-Daten löschen Button

2. **Mitarbeiter Feedback-Verwaltung** (`/dashboard/staff/feedbacks`)
   - Tabelle aller Feedbacks
   - Status-Änderung mit Dropdown
   - Expandierbare Kommentar-Sektion
   - Status-Änderungs-Dialog mit optionalem Kommentar

3. **Öffentliche Seite** (`/public` oder `/`)
   - Gruppierte Feedbacks nach Status
   - Kategorie-Filter
   - Kommentare für jedes Feedback

### Neue API-Funktionen

```typescript
// User Management
createUserByAdmin(email, password, role)
changeUserRole(userId, newRole)
changeUserPassword(userId, newPassword)
deleteUser(userId)
deleteAllDemoData()

// Feedback Management
approveFeedback(feedbackId)
updateFeedbackStatus(feedbackId, newStatus)
publishFeedback(feedbackId)
deleteFeedback(feedbackId) // Nur Admin

// Comments
getCommentsByFeedbackId(feedbackId)
addComment(feedbackId, content)
```

---

## 🔄 Workflows

### 1. Feedback-Lebenszyklus

```
1. Bürger erstellt Feedback → Status: PENDING
2. Mitarbeiter prüft Feedback → Status: OPEN (mit Kommentar optional)
3. Mitarbeiter bearbeitet Feedback → Status: INPROGRESS (mit Kommentar optional)
4. Mitarbeiter schließt Feedback ab → Status: DONE (mit Kommentar optional)
5. Feedback wird geschlossen → Status: CLOSED
```

### 2. Kommentar-Workflow

```
1. Mitarbeiter ändert Status → Optional: Kommentar hinzufügen
2. Kommentar wird gespeichert → Sichtbar für alle
3. Kommentare werden chronologisch angezeigt
4. Öffentliche Seite zeigt alle Kommentare
```

### 3. Admin-Workflow

```
1. Admin erstellt Benutzer → Wählt Rolle (CITIZEN, STAFF, ADMIN)
2. Admin ändert Rollen → Benutzer erhält neue Berechtigungen
3. Admin löscht Benutzer → Alle Feedbacks werden gelöscht
4. Admin löscht Feedback → Alle Kommentare werden gelöscht
```

---

## 🧪 Test-Daten

### Demo-User

**Bürger:**
- `demo.buerger1@example.com` / `Demo123!`
- `demo.buerger2@example.com` / `Demo123!`
- `demo.buerger3@example.com` / `Demo123!`
- `demo.buerger4@example.com` / `Demo123!`

**Mitarbeiter:**
- `demo.mitarbeiter1@stadt.de` / `Demo123!`
- `demo.mitarbeiter2@stadt.de` / `Demo123!`

**Admin:**
- `admin@cityfeedback.de` / `Admin123!`

### Demo-Feedbacks
- 10 Feedbacks in verschiedenen Status
- Verschiedene Kategorien
- Verschiedene Veröffentlichungsstatus

---

## 📝 API-Endpunkte Übersicht

### User Management
- `POST /user/admin/create` - Benutzer erstellen (Admin)
- `PUT /user/{id}/role` - Rolle ändern (Admin)
- `PUT /user/{id}/password` - Passwort ändern (Admin)
- `DELETE /user/{id}` - Benutzer löschen (Admin)
- `GET /user/all` - Alle Benutzer (Admin)

### Feedback Management
- `GET /feedback` - Alle Feedbacks
- `GET /feedback/{id}` - Feedback abrufen
- `POST /feedback` - Feedback erstellen
- `PUT /feedback/{id}/approve` - Feedback freigeben (Staff/Admin)
- `PUT /feedback/{id}/status` - Status ändern (Staff/Admin)
- `PUT /feedback/{id}/publish` - Veröffentlichen (Staff/Admin)
- `DELETE /feedback/{id}` - Feedback löschen (Admin)
- `GET /feedback/public` - Öffentliche Feedbacks

### Comments
- `POST /feedback/{id}/comments` - Kommentar hinzufügen (Staff/Admin)
- `GET /feedback/{id}/comments` - Kommentare abrufen

### Demo Data
- `DELETE /admin/demo-data` - Demo-Daten löschen (Admin)

---

## 🚀 Deployment & Konfiguration

### Automatische Initialisierung

1. **Default Admin**: Wird beim ersten Start automatisch erstellt
2. **Demo-Daten**: Werden beim Start automatisch erstellt (wenn noch nicht vorhanden)

### Datenbank-Migrationen

Die neue `comments`-Tabelle wird automatisch von JPA erstellt, wenn `spring.jpa.hibernate.ddl-auto=update` gesetzt ist.

---

## 🔍 Code-Qualität

### Prinzipien
- ✅ Domain-Driven Design (DDD)
- ✅ Test-Driven Development (TDD)
- ✅ Funktionale Programmierung (Stream API, Optional)
- ✅ Clean Architecture
- ✅ Separation of Concerns

### Best Practices
- Framework-unabhängiger Domain Layer
- Repository Pattern
- Mapper Pattern
- DTO Pattern
- Transaktionale Sicherheit

---

## 📚 Weitere Dokumentation

- `docs/DDD-TDD-Zusammenfassung.md` - DDD & TDD Konformität
- `docs/DDD-Refactoring-Dokumentation.md` - Refactoring-Dokumentation
- `docs/beispieldaten.md` - Beispiel-Daten Dokumentation

---

## 🎯 Zusammenfassung

Heute wurden folgende Hauptfeatures implementiert:

1. ✅ **Admin-Funktionalitäten**: Vollständige Benutzerverwaltung
2. ✅ **Mitarbeiter-Funktionalitäten**: Feedback-Verwaltung und Status-Änderungen
3. ✅ **Kommentar-System**: Kommentare für Feedbacks
4. ✅ **Öffentliche Seite**: Erweitert um Kommentar-Anzeige
5. ✅ **Demo-Daten**: Automatische Erstellung und Löschung
6. ✅ **Feedback-Löschung**: Admin-Funktion zum Löschen von Feedbacks

Alle Implementierungen folgen den etablierten Architektur-Prinzipien und sind vollständig in das bestehende System integriert.

---

**Ende der Dokumentation**

