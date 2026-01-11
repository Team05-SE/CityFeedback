# Beispieldaten für CityFeedback

Dieses Dokument enthält Beispieldaten für das CityFeedback-System.

## 1. Beispiel-User (über API erstellen)

### Bürger (Citizens)
```json
POST http://localhost:8080/user
Content-Type: application/json

{
  "email": "max.mustermann@example.com",
  "password": "Passwort123",
  "role": "CITIZEN"
}

{
  "email": "anna.schmidt@example.com",
  "password": "Passwort123",
  "role": "CITIZEN"
}

{
  "email": "peter.mueller@example.com",
  "password": "Passwort123",
  "role": "CITIZEN"
}
```

### Mitarbeiter (Staff)
```json
POST http://localhost:8080/user
Content-Type: application/json

{
  "email": "mitarbeiter1@stadt.de",
  "password": "Passwort123",
  "role": "STAFF"
}

{
  "email": "mitarbeiter2@stadt.de",
  "password": "Passwort123",
  "role": "STAFF"
}
```

## 2. Beispiel-Feedbacks (über API erstellen)

**WICHTIG:** Ersetzen Sie `{userId}` mit der tatsächlichen User-ID aus der Datenbank!

### PENDING Feedbacks (Entwürfe - warten auf Freigabe)

```json
POST http://localhost:8080/feedback
Content-Type: application/json

{
  "userId": "{userId}",
  "title": "Müllcontainer überfüllt",
  "category": "UMWELT",
  "content": "Der Müllcontainer an der Ecke Hauptstraße/Neue Straße ist seit Tagen überfüllt. Der Müll quillt über und verursacht Gerüche. Bitte leeren Sie den Container dringend."
}

{
  "userId": "{userId}",
  "title": "Straßenlaterne defekt",
  "category": "BELEUCHTUNG",
  "content": "Die Straßenlaterne vor Hausnummer 42 in der Musterstraße funktioniert nicht mehr. Es ist dort nachts sehr dunkel, was ein Sicherheitsrisiko darstellt. Bitte reparieren Sie die Beleuchtung."
}

{
  "userId": "{userId}",
  "title": "Schlagloch in der Fahrbahn",
  "category": "VERKEHR",
  "content": "In der Bahnhofstraße, Höhe Hausnummer 15, befindet sich ein großes Schlagloch. Es ist gefährlich für Autofahrer und Radfahrer. Bitte reparieren Sie die Straße."
}

{
  "userId": "{userId}",
  "title": "Grafitti an der Bushaltestelle",
  "category": "VANDALISMUS",
  "content": "Die Bushaltestelle am Marktplatz wurde mit Graffiti besprüht. Die Wände sind vollständig beschmiert. Bitte reinigen Sie die Haltestelle."
}

{
  "userId": "{userId}",
  "title": "Parkplatzmarkierungen verwischt",
  "category": "VERKEHR",
  "content": "Die Parkplatzmarkierungen auf dem Parkplatz am Rathaus sind durch Witterung und Abnutzung kaum noch sichtbar. Bitte erneuern Sie die Markierungen."
}
```

### OPEN Feedbacks (bereits freigegeben)

```json
POST http://localhost:8080/feedback
Content-Type: application/json

{
  "userId": "{userId}",
  "title": "Baum beschädigt nach Sturm",
  "category": "UMWELT",
  "content": "Nach dem letzten Sturm ist ein großer Ast von der Eiche im Stadtpark abgebrochen. Der Ast liegt auf dem Gehweg und blockiert den Weg. Bitte entfernen Sie den Ast und prüfen Sie den Baum auf weitere Schäden."
}

{
  "userId": "{userId}",
  "title": "Ampel zeigt dauerhaft rot",
  "category": "VERKEHR",
  "content": "Die Ampel an der Kreuzung Hauptstraße/Bahnhofstraße zeigt seit gestern dauerhaft rot. Der Verkehr staut sich erheblich. Bitte reparieren Sie die Ampelanlage."
}
```

## 3. SQL-Statements für direkte Datenbank-Inserts

**WICHTIG:** Diese Statements funktionieren nur, wenn Sie die User-IDs aus der Datenbank kennen!

```sql
-- Beispiel: Feedbacks direkt in die Datenbank einfügen
-- Ersetzen Sie die UUIDs mit tatsächlichen User-IDs!

-- PENDING Feedbacks
INSERT INTO feedbacks (title, category, feedback_date, content, status, is_published, user_id) VALUES
('Müllcontainer überfüllt', 'UMWELT', CURRENT_DATE - 2, 'Der Müllcontainer an der Ecke Hauptstraße/Neue Straße ist seit Tagen überfüllt. Der Müll quillt über und verursacht Gerüche.', 'PENDING', false, 'USER_ID_HIER'),
('Straßenlaterne defekt', 'BELEUCHTUNG', CURRENT_DATE - 1, 'Die Straßenlaterne vor Hausnummer 42 in der Musterstraße funktioniert nicht mehr. Es ist dort nachts sehr dunkel.', 'PENDING', false, 'USER_ID_HIER'),
('Schlagloch in der Fahrbahn', 'VERKEHR', CURRENT_DATE, 'In der Bahnhofstraße, Höhe Hausnummer 15, befindet sich ein großes Schlagloch. Es ist gefährlich für Autofahrer und Radfahrer.', 'PENDING', false, 'USER_ID_HIER');

-- OPEN Feedbacks (bereits freigegeben)
INSERT INTO feedbacks (title, category, feedback_date, content, status, is_published, user_id) VALUES
('Baum beschädigt nach Sturm', 'UMWELT', CURRENT_DATE - 5, 'Nach dem letzten Sturm ist ein großer Ast von der Eiche im Stadtpark abgebrochen. Der Ast liegt auf dem Gehweg.', 'OPEN', false, 'USER_ID_HIER'),
('Ampel zeigt dauerhaft rot', 'VERKEHR', CURRENT_DATE - 3, 'Die Ampel an der Kreuzung Hauptstraße/Bahnhofstraße zeigt seit gestern dauerhaft rot. Der Verkehr staut sich erheblich.', 'OPEN', false, 'USER_ID_HIER');

-- INPROGRESS Feedbacks (in Bearbeitung)
INSERT INTO feedbacks (title, category, feedback_date, content, status, is_published, user_id) VALUES
('Spielplatzgerät defekt', 'VERWALTUNG', CURRENT_DATE - 7, 'Die Schaukel auf dem Spielplatz am Park ist kaputt. Ein Seil ist gerissen. Bitte reparieren Sie das Gerät.', 'INPROGRESS', true, 'USER_ID_HIER'),
('Weg überschwemmt', 'VERKEHR', CURRENT_DATE - 4, 'Nach dem Regen ist der Fußweg am Bach überschwemmt. Man kann nicht mehr trockenen Fußes passieren.', 'INPROGRESS', true, 'USER_ID_HIER');

-- DONE Feedbacks (erledigt)
INSERT INTO feedbacks (title, category, feedback_date, content, status, is_published, user_id) VALUES
('Straßenschild fehlt', 'VERKEHR', CURRENT_DATE - 14, 'Das Straßenschild für die Neue Straße fehlt. Es wurde vermutlich bei einem Unfall beschädigt.', 'DONE', true, 'USER_ID_HIER'),
('Müll auf dem Spielplatz', 'UMWELT', CURRENT_DATE - 10, 'Auf dem Spielplatz liegt viel Müll herum. Bitte räumen Sie auf.', 'DONE', true, 'USER_ID_HIER');

-- CLOSED Feedbacks (geschlossen)
INSERT INTO feedbacks (title, category, feedback_date, content, status, is_published, user_id) VALUES
('Lärmbelästigung durch Baustelle', 'VERWALTUNG', CURRENT_DATE - 30, 'Die Baustelle in der Nachbarschaft verursacht erheblichen Lärm auch außerhalb der erlaubten Zeiten.', 'CLOSED', false, 'USER_ID_HIER');
```

## 4. Python-Script zum Erstellen von Beispieldaten

```python
#!/usr/bin/env python3
"""
Script zum Erstellen von Beispieldaten für CityFeedback
Verwendung: python create_sample_data.py
"""

import requests
import json
from datetime import datetime, timedelta

API_BASE = "http://localhost:8080"

# Beispiel-User erstellen
users = [
    {"email": "max.mustermann@example.com", "password": "Passwort123", "role": "CITIZEN"},
    {"email": "anna.schmidt@example.com", "password": "Passwort123", "role": "CITIZEN"},
    {"email": "peter.mueller@example.com", "password": "Passwort123", "role": "CITIZEN"},
    {"email": "mitarbeiter1@stadt.de", "password": "Passwort123", "role": "STAFF"},
]

# Feedbacks erstellen
feedbacks = [
    {
        "title": "Müllcontainer überfüllt",
        "category": "UMWELT",
        "content": "Der Müllcontainer an der Ecke Hauptstraße/Neue Straße ist seit Tagen überfüllt. Der Müll quillt über und verursacht Gerüche. Bitte leeren Sie den Container dringend."
    },
    {
        "title": "Straßenlaterne defekt",
        "category": "BELEUCHTUNG",
        "content": "Die Straßenlaterne vor Hausnummer 42 in der Musterstraße funktioniert nicht mehr. Es ist dort nachts sehr dunkel, was ein Sicherheitsrisiko darstellt."
    },
    {
        "title": "Schlagloch in der Fahrbahn",
        "category": "VERKEHR",
        "content": "In der Bahnhofstraße, Höhe Hausnummer 15, befindet sich ein großes Schlagloch. Es ist gefährlich für Autofahrer und Radfahrer."
    },
    {
        "title": "Grafitti an der Bushaltestelle",
        "category": "VANDALISMUS",
        "content": "Die Bushaltestelle am Marktplatz wurde mit Graffiti besprüht. Die Wände sind vollständig beschmiert. Bitte reinigen Sie die Haltestelle."
    },
    {
        "title": "Parkplatzmarkierungen verwischt",
        "category": "VERKEHR",
        "content": "Die Parkplatzmarkierungen auf dem Parkplatz am Rathaus sind durch Witterung und Abnutzung kaum noch sichtbar. Bitte erneuern Sie die Markierungen."
    },
    {
        "title": "Baum beschädigt nach Sturm",
        "category": "UMWELT",
        "content": "Nach dem letzten Sturm ist ein großer Ast von der Eiche im Stadtpark abgebrochen. Der Ast liegt auf dem Gehweg und blockiert den Weg."
    },
    {
        "title": "Ampel zeigt dauerhaft rot",
        "category": "VERKEHR",
        "content": "Die Ampel an der Kreuzung Hauptstraße/Bahnhofstraße zeigt seit gestern dauerhaft rot. Der Verkehr staut sich erheblich."
    },
    {
        "title": "Spielplatzgerät defekt",
        "category": "VERWALTUNG",
        "content": "Die Schaukel auf dem Spielplatz am Park ist kaputt. Ein Seil ist gerissen. Bitte reparieren Sie das Gerät."
    },
    {
        "title": "Weg überschwemmt",
        "category": "VERKEHR",
        "content": "Nach dem Regen ist der Fußweg am Bach überschwemmt. Man kann nicht mehr trockenen Fußes passieren."
    },
    {
        "title": "Straßenschild fehlt",
        "category": "VERKEHR",
        "content": "Das Straßenschild für die Neue Straße fehlt. Es wurde vermutlich bei einem Unfall beschädigt."
    },
]

def create_users():
    """Erstellt Beispiel-User"""
    created_users = []
    for user in users:
        try:
            response = requests.post(f"{API_BASE}/user", json=user)
            if response.status_code == 200:
                created_user = response.json()
                created_users.append(created_user)
                print(f"✓ User erstellt: {user['email']} (ID: {created_user.get('id')})")
            else:
                print(f"✗ Fehler beim Erstellen von {user['email']}: {response.text}")
        except Exception as e:
            print(f"✗ Fehler: {e}")
    return created_users

def create_feedbacks(user_ids):
    """Erstellt Beispiel-Feedbacks"""
    if not user_ids:
        print("Keine User-IDs verfügbar. Bitte erstellen Sie zuerst User.")
        return
    
    for i, feedback in enumerate(feedbacks):
        # Verteile Feedbacks auf verschiedene User
        user_id = user_ids[i % len(user_ids)]['id']
        feedback_data = {
            **feedback,
            "userId": str(user_id)
        }
        try:
            response = requests.post(f"{API_BASE}/feedback", json=feedback_data)
            if response.status_code == 200:
                created_feedback = response.json()
                print(f"✓ Feedback erstellt: {feedback['title']} (ID: {created_feedback.get('id')})")
            else:
                print(f"✗ Fehler beim Erstellen von '{feedback['title']}': {response.text}")
        except Exception as e:
            print(f"✗ Fehler: {e}")

if __name__ == "__main__":
    print("Erstelle Beispieldaten für CityFeedback...\n")
    print("=" * 50)
    
    print("\n1. Erstelle User...")
    created_users = create_users()
    
    print(f"\n2. Erstelle Feedbacks...")
    create_feedbacks(created_users)
    
    print("\n" + "=" * 50)
    print("Fertig! Beispieldaten wurden erstellt.")
    print(f"\nErstellt: {len(created_users)} User, {len(feedbacks)} Feedbacks")
```

## 5. cURL-Befehle für schnelles Testen

```bash
# User erstellen
curl -X POST http://localhost:8080/user \
  -H "Content-Type: application/json" \
  -d '{"email":"max.mustermann@example.com","password":"Passwort123","role":"CITIZEN"}'

# User-ID aus der Antwort kopieren und in {userId} einsetzen

# Feedback erstellen
curl -X POST http://localhost:8080/feedback \
  -H "Content-Type: application/json" \
  -d '{
    "userId":"{userId}",
    "title":"Müllcontainer überfüllt",
    "category":"UMWELT",
    "content":"Der Müllcontainer an der Ecke Hauptstraße/Neue Straße ist seit Tagen überfüllt."
  }'
```

## 6. Beispiel-Workflow

1. **Bürger erstellt Feedback** → Status: PENDING
   ```json
   POST /feedback
   {
     "userId": "user-id-1",
     "title": "Müllcontainer überfüllt",
     "category": "UMWELT",
     "content": "..."
   }
   ```

2. **Mitarbeiter gibt Feedback frei** → Status: OPEN
   ```json
   PUT /feedback/{id}/approve
   ```

3. **Mitarbeiter setzt Status auf INPROGRESS**
   ```json
   PUT /feedback/{id}/status
   {
     "status": "INPROGRESS"
   }
   ```

4. **Mitarbeiter veröffentlicht Feedback**
   ```json
   PUT /feedback/{id}/publish
   ```

5. **Feedback ist jetzt öffentlich sichtbar** → GET /feedback/public

## 7. Verschiedene Kategorien-Beispiele

### UMWELT
- Müllcontainer überfüllt
- Baum beschädigt
- Müll auf dem Spielplatz
- Hundekot auf dem Gehweg

### VERKEHR
- Schlagloch in der Fahrbahn
- Ampel defekt
- Parkplatzmarkierungen verwischt
- Straßenschild fehlt

### BELEUCHTUNG
- Straßenlaterne defekt
- Beleuchtung am Spielplatz funktioniert nicht
- Dunkle Ecke am Park

### VANDALISMUS
- Graffiti an der Bushaltestelle
- Vandalismus am Spielplatz
- Beschädigte Bänke im Park

### VERWALTUNG
- Spielplatzgerät defekt
- Öffnungszeiten des Bürgeramts
- Formulare nicht verfügbar

