package com.example.cityfeedback.feedbackmanagement.application;

import com.example.cityfeedback.feedbackmanagement.domain.valueobjects.Status;
import jakarta.validation.constraints.NotNull;

public class UpdateStatusDTO {

    @NotNull(message = "Status darf nicht null sein")
    public Status status;
}

