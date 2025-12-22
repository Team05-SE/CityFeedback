package com.example.cityfeedback.feedbackmanagement.domain.repositories;

import com.example.cityfeedback.feedbackmanagement.domain.model.Comment;

import java.util.List;

/**
 * Repository-Interface für Kommentare im Domain-Layer.
 * Definiert die Verträge für die Persistierung von Kommentaren.
 * 
 * Die Implementierung erfolgt in der Infrastructure-Schicht.
 */
public interface CommentRepository {

    /**
     * Speichert einen Kommentar.
     * 
     * @param comment Der zu speichernde Kommentar
     * @return Der gespeicherte Kommentar (mit generierter ID)
     */
    Comment save(Comment comment);

    /**
     * Findet alle Kommentare zu einem bestimmten Feedback.
     * 
     * @param feedbackId Die ID des Feedbacks
     * @return Liste aller Kommentare des Feedbacks, sortiert nach Erstellungsdatum (älteste zuerst)
     */
    List<Comment> findByFeedbackId(Long feedbackId);

    /**
     * Löscht alle Kommentare zu einem bestimmten Feedback.
     * Wird verwendet, wenn ein Feedback gelöscht wird.
     * 
     * @param feedbackId Die ID des Feedbacks
     */
    void deleteByFeedbackId(Long feedbackId);
}

