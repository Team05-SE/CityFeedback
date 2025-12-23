package com.example.cityfeedback.feedbackmanagement.infrastructure.persistence;

import com.example.cityfeedback.feedbackmanagement.domain.model.Comment;

/**
 * Mapper zwischen Domain-Modell (Comment) und Persistence-Entity (CommentEntity).
 * Übersetzt zwischen der Domain-Schicht und der Datenbank-Schicht.
 */
public class CommentMapper {

    /**
     * Konvertiert ein Domain-Comment-Objekt in eine Persistence-Entity.
     * 
     * @param comment Domain-Comment-Objekt
     * @return CommentEntity für die Persistierung
     */
    public static CommentEntity toEntity(Comment comment) {
        if (comment == null) {
            return null;
        }

        CommentEntity entity = new CommentEntity();
        entity.setId(comment.getId());
        entity.setFeedbackId(comment.getFeedbackId());
        entity.setAuthorId(comment.getAuthorId());
        entity.setContent(comment.getContent());
        entity.setCreatedAt(comment.getCreatedAt());
        return entity;
    }

    /**
     * Konvertiert eine Persistence-Entity in ein Domain-Comment-Objekt.
     * 
     * @param entity CommentEntity aus der Datenbank
     * @return Domain-Comment-Objekt
     */
    public static Comment toDomain(CommentEntity entity) {
        if (entity == null) {
            return null;
        }

        Comment comment = new Comment();
        comment.setId(entity.getId());
        comment.setFeedbackId(entity.getFeedbackId());
        comment.setAuthorId(entity.getAuthorId());
        comment.setContent(entity.getContent());
        comment.setCreatedAt(entity.getCreatedAt());
        return comment;
    }
}

