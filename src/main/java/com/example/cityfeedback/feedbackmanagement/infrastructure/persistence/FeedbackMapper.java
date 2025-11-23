package com.example.cityfeedback.feedbackmanagement.infrastructure.persistence;

import com.example.cityfeedback.feedbackmanagement.domain.model.Feedback;
import com.example.cityfeedback.feedbackmanagement.domain.valueobjects.UserId;

/**
 * Mapper zwischen Domain-Entity Feedback und JPA-Entity FeedbackJpaEntity.
 */
public class FeedbackMapper {

    public static FeedbackJpaEntity toJpa(Feedback feedback) {
        FeedbackJpaEntity jpaEntity = new FeedbackJpaEntity();
        jpaEntity.setId(feedback.getId());
        jpaEntity.setTitle(feedback.getTitle());
        jpaEntity.setCategory(feedback.getCategory());
        jpaEntity.setFeedbackDate(feedback.getFeedbackDate());
        jpaEntity.setContent(feedback.getContent());
        jpaEntity.setStatus(feedback.getStatus());
        jpaEntity.setPublished(feedback.isPublished());
        jpaEntity.setCreatorId(feedback.getCreatorId().getValue());
        return jpaEntity;
    }

    public static Feedback toDomain(FeedbackJpaEntity jpaEntity) {
        Feedback feedback = new Feedback(
                jpaEntity.getId(),
                jpaEntity.getTitle(),
                jpaEntity.getCategory(),
                jpaEntity.getFeedbackDate(),
                jpaEntity.getContent(),
                jpaEntity.getStatus(),
                jpaEntity.isPublished(),
                new UserId(jpaEntity.getCreatorId())
        );
        return feedback;
    }
}

