package com.example.cityfeedback.feedbackmanagement.domain.exceptions;

/**
 * Domain Exception: Wird geworfen, wenn ein Feedback nicht gefunden wird.
 */
public class FeedbackNotFoundException extends RuntimeException {

    public FeedbackNotFoundException(Long feedbackId) {
        super("Feedback nicht gefunden: " + feedbackId);
    }

    public FeedbackNotFoundException(String message) {
        super(message);
    }

    public FeedbackNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}

