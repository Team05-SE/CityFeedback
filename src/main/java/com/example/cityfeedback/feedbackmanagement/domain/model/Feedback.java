package com.example.cityfeedback.feedbackmanagement.domain.model;

import com.example.cityfeedback.feedbackmanagement.domain.valueobjects.Category;
import com.example.cityfeedback.feedbackmanagement.domain.valueobjects.Status;
import com.example.cityfeedback.feedbackmanagement.domain.valueobjects.UserId;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Aggregate Root: Feedback
 * Framework-unabhängige Domain-Entität.
 */
public class Feedback {

    private Long id;
    private String title;
    private Category category;
    private LocalDate feedbackDate;
    private String content;
    private Status status;
    private boolean isPublished;
    private UserId creatorId;

    public Feedback() {
        // Für Framework-Unabhängigkeit
    }

    public Feedback(Long id, String title, Category category, LocalDate feedbackDate, String content, Status status, boolean isPublished, UserId creatorId) {
        this.id = id;
        this.title = Objects.requireNonNull(title, "Titel darf nicht null sein");
        this.category = Objects.requireNonNull(category, "Kategorie darf nicht null sein");
        this.feedbackDate = Objects.requireNonNull(feedbackDate, "Datum darf nicht null sein");
        this.content = Objects.requireNonNull(content, "Inhalt darf nicht null sein");
        this.status = Objects.requireNonNull(status, "Status darf nicht null sein");
        this.isPublished = isPublished;
        this.creatorId = Objects.requireNonNull(creatorId, "Ersteller-ID darf nicht null sein");
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserId getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(UserId creatorId) {
        this.creatorId = Objects.requireNonNull(creatorId, "Ersteller-ID darf nicht null sein");
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


    @Override
    public String toString() {
        return "Feedback{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", category=" + category +
                ", feedbackDate=" + feedbackDate +
                ", content='" + content + '\'' +
                ", status=" + status +
                ", isPublished=" + isPublished +
                ", creatorId=" + creatorId +
                '}';
    }
}
