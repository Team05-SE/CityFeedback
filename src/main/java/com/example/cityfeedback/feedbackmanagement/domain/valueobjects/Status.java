package com.example.cityfeedback.feedbackmanagement.domain.valueobjects;

public enum Status {
    PENDING,    // Entwurf - wartet auf Prüfung durch Mitarbeiter
    OPEN,       // Offen - von Mitarbeiter freigegeben
    INPROGRESS, // In Bearbeitung
    DONE,       // Erledigt
    CLOSED;     // Geschlossen
}
