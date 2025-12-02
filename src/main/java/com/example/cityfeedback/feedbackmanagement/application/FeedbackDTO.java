package com.example.cityfeedback.feedbackmanagement.application;

import com.example.cityfeedback.feedbackmanagement.domain.valueobjects.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class FeedbackDTO {

    @NotNull(message = "Benutzer-ID darf nicht null sein")
    public UUID userId;

    @NotBlank(message = "Titel darf nicht leer sein")
    public String title;

    @NotNull(message = "Kategorie darf nicht null sein")
    public Category category;

    @NotBlank(message = "Inhalt darf nicht leer sein")
    public String content;
}
