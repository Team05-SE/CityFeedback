package com.example.cityfeedback.feedbackmanagement.domain.model;

import com.example.cityfeedback.feedbackmanagement.domain.valueobjects.Category;
import com.example.cityfeedback.feedbackmanagement.domain.valueobjects.Status;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDate;

@Entity
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private Category category;
    private LocalDate feedbackDate;
    private String content;
    private Status stats;
    private boolean isPublished;

    public Feedback() {

    }

    public Feedback(Long id, String title, Category category, LocalDate feedbackDate, String content, Status stats, boolean isPublished) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.feedbackDate = feedbackDate;
        this.content = content;
        this.stats = stats;
        this.isPublished = isPublished;
    }

    public Long getId() {
        return id;
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

    public Status getStats() {
        return stats;
    }

    public void setStats(Status stats) {
        this.stats = stats;
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
                ", stats=" + stats +
                ", isPublished=" + isPublished +
                '}';
    }
}
