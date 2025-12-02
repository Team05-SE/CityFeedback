package com.example.cityfeedback.feedbackmanagement.domain.repositories;

import com.example.cityfeedback.feedbackmanagement.domain.model.Feedback;

import java.util.List;
import java.util.Optional;

/**
 * Repository-Interface für Feedback im Domain-Layer.
 * Definiert die Verträge für die Persistierung von Feedback-Aggregaten.
 * 
 * Die Implementierung erfolgt in der Infrastructure-Schicht.
 */
public interface FeedbackRepository {

    /**
     * Speichert ein Feedback.
     * 
     * @param feedback Das zu speichernde Feedback
     * @return Das gespeicherte Feedback (mit generierter ID)
     */
    Feedback save(Feedback feedback);

    /**
     * Findet ein Feedback anhand seiner ID.
     * 
     * @param id Die ID des Feedbacks
     * @return Optional mit dem gefundenen Feedback oder leer
     */
    Optional<Feedback> findById(Long id);

    /**
     * Findet alle Feedbacks.
     * 
     * @return Liste aller Feedbacks
     */
    List<Feedback> findAll();

    /**
     * Löscht ein Feedback.
     * 
     * @param feedback Das zu löschende Feedback
     */
    void delete(Feedback feedback);
}

