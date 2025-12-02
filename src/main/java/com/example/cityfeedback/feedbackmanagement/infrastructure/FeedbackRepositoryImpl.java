package com.example.cityfeedback.feedbackmanagement.infrastructure;

import com.example.cityfeedback.feedbackmanagement.domain.model.Feedback;
import com.example.cityfeedback.feedbackmanagement.domain.repositories.FeedbackRepository;
import com.example.cityfeedback.feedbackmanagement.infrastructure.persistence.FeedbackEntity;
import com.example.cityfeedback.feedbackmanagement.infrastructure.persistence.FeedbackJpaRepository;
import com.example.cityfeedback.feedbackmanagement.infrastructure.persistence.FeedbackMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementierung des FeedbackRepository-Interfaces aus dem Domain-Layer.
 * Diese Klasse verbindet die Domain-Schicht mit der Persistierung.
 */
@Repository
public class FeedbackRepositoryImpl implements FeedbackRepository {

    private final FeedbackJpaRepository jpaRepository;

    public FeedbackRepositoryImpl(FeedbackJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Feedback save(Feedback feedback) {
        FeedbackEntity entity = FeedbackMapper.toEntity(feedback);
        FeedbackEntity savedEntity = jpaRepository.save(entity);
        return FeedbackMapper.toDomain(savedEntity);
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

    @Override
    public void delete(Feedback feedback) {
        FeedbackEntity entity = FeedbackMapper.toEntity(feedback);
        jpaRepository.delete(entity);
    }
}

