package com.example.cityfeedback.feedbackmanagement.application;

import com.example.cityfeedback.feedbackmanagement.domain.valueobjects.Category;

import java.util.UUID;

public class FeedbackDTO {

    public UUID userId;
    public String title;
    public Category category;
    public String content;
}
