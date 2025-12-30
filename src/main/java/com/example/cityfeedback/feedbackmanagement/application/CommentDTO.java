package com.example.cityfeedback.feedbackmanagement.application;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CommentDTO {

    @NotNull(message = "Autor-ID darf nicht null sein")
    public java.util.UUID authorId;

    @NotBlank(message = "Kommentar-Text darf nicht leer sein")
    public String content;
}

