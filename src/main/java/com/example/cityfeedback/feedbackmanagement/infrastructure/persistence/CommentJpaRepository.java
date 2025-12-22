package com.example.cityfeedback.feedbackmanagement.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Spring Data JPA Repository für CommentEntity.
 * Diese Schnittstelle ist nur für die Persistierung zuständig.
 */
public interface CommentJpaRepository extends JpaRepository<CommentEntity, Long> {
    /**
     * Findet alle Kommentare zu einem bestimmten Feedback, sortiert nach Erstellungsdatum.
     * 
     * @param feedbackId Die ID des Feedbacks
     * @return Liste aller CommentEntities des Feedbacks, sortiert nach createdAt (aufsteigend)
     */
    List<CommentEntity> findByFeedbackIdOrderByCreatedAtAsc(Long feedbackId);

    /**
     * Löscht alle Kommentare zu einem bestimmten Feedback.
     * 
     * @param feedbackId Die ID des Feedbacks
     */
    void deleteByFeedbackId(Long feedbackId);
}

