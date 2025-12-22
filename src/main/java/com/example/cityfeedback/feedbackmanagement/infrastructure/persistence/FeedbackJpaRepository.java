package com.example.cityfeedback.feedbackmanagement.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

/**
 * Spring Data JPA Repository für FeedbackEntity.
 * Diese Schnittstelle ist nur für die Persistierung zuständig.
 */
public interface FeedbackJpaRepository extends JpaRepository<FeedbackEntity, Long> {
    /**
     * Findet alle Feedbacks eines bestimmten Users.
     * 
     * @param userId Die UUID des Users
     * @return Liste aller FeedbackEntities des Users
     */
    List<FeedbackEntity> findByUserId(UUID userId);

    /**
     * Löscht alle Feedbacks eines bestimmten Users.
     * 
     * @param userId Die UUID des Users
     */
    void deleteByUserId(UUID userId);
}

