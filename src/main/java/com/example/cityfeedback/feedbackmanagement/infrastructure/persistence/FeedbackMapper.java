package com.example.cityfeedback.feedbackmanagement.infrastructure.persistence;

import com.example.cityfeedback.feedbackmanagement.domain.model.Feedback;

/**
 * Mapper zwischen Domain-Modell (Feedback) und Persistence-Entity (FeedbackEntity).
 * Übersetzt zwischen der Domain-Schicht und der Datenbank-Schicht.
 */
public class FeedbackMapper {

    /**
     * Konvertiert ein Domain-Feedback-Objekt in eine Persistence-Entity.
     * 
     * @param feedback Domain-Feedback-Objekt
     * @return FeedbackEntity für die Persistierung
     */
    public static FeedbackEntity toEntity(Feedback feedback) {
        if (feedback == null) {
            return null;
        }

        FeedbackEntity entity = new FeedbackEntity();
        entity.setId(feedback.getId());
        entity.setTitle(feedback.getTitle());
        entity.setCategory(feedback.getCategory());
        entity.setFeedbackDate(feedback.getFeedbackDate());
        entity.setContent(feedback.getContent());
        entity.setStatus(feedback.getStatus());
        entity.setPublished(feedback.isPublished());
        // User wird nur als ID gespeichert (lose Kopplung zwischen Bounded Contexts)
        entity.setUserId(feedback.getUserId());
        return entity;
    }

    /**
     * Konvertiert eine Persistence-Entity in ein Domain-Feedback-Objekt.
     * 
     * @param entity FeedbackEntity aus der Datenbank
     * @return Domain-Feedback-Objekt
     */
    public static Feedback toDomain(FeedbackEntity entity) {
        if (entity == null) {
            return null;
        }

        // Verwende Reflection, um Feedback zu erstellen, da der Konstruktor privat ist
        // Oder wir verwenden einen Package-Private Konstruktor
        Feedback feedback = new Feedback();
        feedback.setId(entity.getId());
        feedback.setTitle(entity.getTitle());
        feedback.setCategory(entity.getCategory());
        feedback.setFeedbackDate(entity.getFeedbackDate());
        feedback.setContent(entity.getContent());
        feedback.setStatus(entity.getStatus());
        feedback.setPublished(entity.isPublished());
        feedback.setUserId(entity.getUserId());

        return feedback;
    }
}

