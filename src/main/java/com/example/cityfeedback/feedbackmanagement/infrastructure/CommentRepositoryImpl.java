package com.example.cityfeedback.feedbackmanagement.infrastructure;

import com.example.cityfeedback.feedbackmanagement.domain.model.Comment;
import com.example.cityfeedback.feedbackmanagement.domain.repositories.CommentRepository;
import com.example.cityfeedback.feedbackmanagement.infrastructure.persistence.CommentEntity;
import com.example.cityfeedback.feedbackmanagement.infrastructure.persistence.CommentJpaRepository;
import com.example.cityfeedback.feedbackmanagement.infrastructure.persistence.CommentMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementierung des CommentRepository-Interfaces aus dem Domain-Layer.
 * Diese Klasse verbindet die Domain-Schicht mit der Persistierung.
 */
@Repository
public class CommentRepositoryImpl implements CommentRepository {

    private final CommentJpaRepository jpaRepository;

    public CommentRepositoryImpl(CommentJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Comment save(Comment comment) {
        CommentEntity entity = CommentMapper.toEntity(comment);
        CommentEntity savedEntity = jpaRepository.save(entity);
        return CommentMapper.toDomain(savedEntity);
    }

    @Override
    public List<Comment> findByFeedbackId(Long feedbackId) {
        return jpaRepository.findByFeedbackIdOrderByCreatedAtAsc(feedbackId).stream()
                .map(CommentMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteByFeedbackId(Long feedbackId) {
        jpaRepository.deleteByFeedbackId(feedbackId);
    }
}

