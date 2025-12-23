package com.example.cityfeedback.feedbackmanagement.domain.model;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Domain-Modell für einen Kommentar zu einem Feedback.
 * 
 * Diese Klasse ist framework-unabhängig und enthält keine JPA-Annotationen.
 * Die Persistierung wird über CommentEntity und CommentMapper in der Infrastructure-Schicht gehandhabt.
 */
public class Comment {

    private Long id;
    private Long feedbackId;
    private UUID authorId; // ID des Mitarbeiters/Admins, der den Kommentar geschrieben hat
    private String content;
    private LocalDateTime createdAt;

    /**
     * No-Args Konstruktor für Mapper in Infrastructure.
     */
    public Comment() {
    }

    /**
     * Konstruktor für die Erstellung eines neuen Kommentars.
     * 
     * @param feedbackId Die ID des Feedbacks, zu dem der Kommentar gehört
     * @param authorId Die ID des Autors (Mitarbeiter/Admin)
     * @param content Der Kommentar-Text
     */
    public Comment(Long feedbackId, UUID authorId, String content) {
        this.feedbackId = Objects.requireNonNull(feedbackId, "Feedback-ID darf nicht null sein");
        this.authorId = Objects.requireNonNull(authorId, "Autor-ID darf nicht null sein");
        validateContent(content);
        this.content = content;
        this.createdAt = LocalDateTime.now();
    }

    private void validateContent(String content) {
        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException("Kommentar-Text darf nicht null oder leer sein.");
        }
        if (content.length() > 2000) {
            throw new IllegalArgumentException("Kommentar-Text darf maximal 2000 Zeichen lang sein.");
        }
    }

    // Getter
    public Long getId() {
        return id;
    }

    public Long getFeedbackId() {
        return feedbackId;
    }

    public UUID getAuthorId() {
        return authorId;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    // Setter für Mapper in Infrastructure
    public void setId(Long id) {
        this.id = id;
    }

    public void setFeedbackId(Long feedbackId) {
        this.feedbackId = feedbackId;
    }

    public void setAuthorId(UUID authorId) {
        this.authorId = authorId;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return Objects.equals(id, comment.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", feedbackId=" + feedbackId +
                ", authorId=" + authorId +
                ", content='" + content + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}

