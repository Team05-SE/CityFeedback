package com.example.cityfeedback.feedbackmanagement.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA Repository für FeedbackEntity.
 * Diese Schnittstelle ist nur für die Persistierung zuständig.
 */
public interface FeedbackJpaRepository extends JpaRepository<FeedbackEntity, Long> {
    // Zusätzliche Query-Methoden können hier hinzugefügt werden
}

