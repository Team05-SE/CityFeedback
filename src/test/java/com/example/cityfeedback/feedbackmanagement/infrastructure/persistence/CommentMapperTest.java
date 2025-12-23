package com.example.cityfeedback.feedbackmanagement.infrastructure.persistence;

import com.example.cityfeedback.feedbackmanagement.domain.model.Comment;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CommentMapperTest {

    @Test
    void toEntity_shouldConvertDomainToEntity() {
        // Arrange
        Long id = 1L;
        Long feedbackId = 2L;
        UUID authorId = UUID.randomUUID();
        String content = "Test Comment";
        LocalDateTime createdAt = LocalDateTime.now();

        Comment comment = new Comment(feedbackId, authorId, content);
        comment.setId(id);
        comment.setCreatedAt(createdAt);

        // Act
        CommentEntity entity = CommentMapper.toEntity(comment);

        // Assert
        assertNotNull(entity);
        assertEquals(id, entity.getId());
        assertEquals(feedbackId, entity.getFeedbackId());
        assertEquals(authorId, entity.getAuthorId());
        assertEquals(content, entity.getContent());
        assertEquals(createdAt, entity.getCreatedAt());
    }

    @Test
    void toEntity_withNullComment_shouldReturnNull() {
        // Act
        CommentEntity entity = CommentMapper.toEntity(null);

        // Assert
        assertNull(entity);
    }

    @Test
    void toDomain_shouldConvertEntityToDomain() {
        // Arrange
        Long id = 1L;
        Long feedbackId = 2L;
        UUID authorId = UUID.randomUUID();
        String content = "Test Comment";
        LocalDateTime createdAt = LocalDateTime.now();

        CommentEntity entity = new CommentEntity(id, feedbackId, authorId, content, createdAt);

        // Act
        Comment comment = CommentMapper.toDomain(entity);

        // Assert
        assertNotNull(comment);
        assertEquals(id, comment.getId());
        assertEquals(feedbackId, comment.getFeedbackId());
        assertEquals(authorId, comment.getAuthorId());
        assertEquals(content, comment.getContent());
        assertEquals(createdAt, comment.getCreatedAt());
    }

    @Test
    void toDomain_withNullEntity_shouldReturnNull() {
        // Act
        Comment comment = CommentMapper.toDomain(null);

        // Assert
        assertNull(comment);
    }

    @Test
    void roundTrip_shouldPreserveData() {
        // Arrange
        Long id = 1L;
        Long feedbackId = 2L;
        UUID authorId = UUID.randomUUID();
        String content = "Test Comment";
        LocalDateTime createdAt = LocalDateTime.now();

        Comment originalComment = new Comment(feedbackId, authorId, content);
        originalComment.setId(id);
        originalComment.setCreatedAt(createdAt);

        // Act
        CommentEntity entity = CommentMapper.toEntity(originalComment);
        Comment convertedComment = CommentMapper.toDomain(entity);

        // Assert
        assertNotNull(convertedComment);
        assertEquals(originalComment.getId(), convertedComment.getId());
        assertEquals(originalComment.getFeedbackId(), convertedComment.getFeedbackId());
        assertEquals(originalComment.getAuthorId(), convertedComment.getAuthorId());
        assertEquals(originalComment.getContent(), convertedComment.getContent());
        assertEquals(originalComment.getCreatedAt(), convertedComment.getCreatedAt());
    }
}

