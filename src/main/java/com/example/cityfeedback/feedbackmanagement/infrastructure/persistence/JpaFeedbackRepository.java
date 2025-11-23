package com.example.cityfeedback.feedbackmanagement.infrastructure.persistence;

import com.example.cityfeedback.feedbackmanagement.domain.model.Feedback;
import com.example.cityfeedback.feedbackmanagement.domain.repositories.FeedbackRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Spring Data JPA Repository für FeedbackJpaEntity.
 */
interface FeedbackJpaRepository extends JpaRepository<FeedbackJpaEntity, Long> {
}

/**
 * Infrastructure-Implementierung des FeedbackRepository-Interfaces.
 * Verwendet Spring Data JPA und mappt zwischen Domain- und JPA-Entities.
 */
@Repository
public class JpaFeedbackRepository implements FeedbackRepository {

    private final FeedbackJpaRepository jpaRepository;

    public JpaFeedbackRepository(FeedbackJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public void save(Feedback feedback) {
        FeedbackJpaEntity jpaEntity;
        if (feedback.getId() == null) {
            // Neues Feedback - erstelle neue JPA-Entity
            jpaEntity = FeedbackMapper.toJpa(feedback);
            jpaEntity = jpaRepository.save(jpaEntity);
            feedback.setId(jpaEntity.getId());
        } else {
            // Existierendes Feedback - prüfe ob es existiert
            Optional<FeedbackJpaEntity> existing = jpaRepository.findById(feedback.getId());
            if (existing.isPresent()) {
                // Update existierende Entity
                jpaEntity = existing.get();
                jpaEntity.setTitle(feedback.getTitle());
                jpaEntity.setCategory(feedback.getCategory());
                jpaEntity.setFeedbackDate(feedback.getFeedbackDate());
                jpaEntity.setContent(feedback.getContent());
                jpaEntity.setStatus(feedback.getStatus());
                jpaEntity.setPublished(feedback.isPublished());
                jpaEntity.setCreatorId(feedback.getCreatorId().getValue());
                jpaRepository.save(jpaEntity);
            } else {
                // Feedback mit ID existiert nicht - speichere als neues
                jpaEntity = FeedbackMapper.toJpa(feedback);
                jpaEntity = jpaRepository.save(jpaEntity);
                feedback.setId(jpaEntity.getId());
            }
        }
    }

    @Override
    public Optional<Feedback> findById(Long id) {
        return jpaRepository.findById(id)
                .map(FeedbackMapper::toDomain);
    }

    @Override
    public List<Feedback> findAll() {
        return jpaRepository.findAll().stream()
                .map(FeedbackMapper::toDomain)
                .collect(Collectors.toList());
    }
}

