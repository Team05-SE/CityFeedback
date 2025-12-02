package com.example.cityfeedback.feedbackmanagement.infrastructure.persistence;

import com.example.cityfeedback.feedbackmanagement.domain.valueobjects.Category;
import com.example.cityfeedback.feedbackmanagement.domain.valueobjects.Status;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.UUID;

/**
 * JPA Entity für Feedback.
 * Diese Klasse ist nur für die Persistierung zuständig.
 * Die Domain-Logik befindet sich in der Feedback-Klasse im Domain-Layer.
 */
@Entity
@Table(name = "feedbacks")
public class FeedbackEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    private Category category;

    @Column(nullable = false)
    private LocalDate feedbackDate;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(nullable = false)
    private boolean isPublished;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    // JPA benötigt einen No-Args Konstruktor
    protected FeedbackEntity() {
    }

    public FeedbackEntity(Long id, String title, Category category, LocalDate feedbackDate,
                         String content, Status status, boolean isPublished, UUID userId) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.feedbackDate = feedbackDate;
        this.content = content;
        this.status = status;
        this.isPublished = isPublished;
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public LocalDate getFeedbackDate() {
        return feedbackDate;
    }

    public void setFeedbackDate(LocalDate feedbackDate) {
        this.feedbackDate = feedbackDate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public boolean isPublished() {
        return isPublished;
    }

    public void setPublished(boolean published) {
        isPublished = published;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }
}

