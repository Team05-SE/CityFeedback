package com.example.cityfeedback.feedbackmanagement.domain.exceptions;

/**
 * Wird geworfen, wenn ein Feedback mit der angegebenen ID nicht gefunden wird.
 */
public class FeedbackNotFoundException extends RuntimeException {
    
    public FeedbackNotFoundException(String message) {
        super(message);
    }
    
    public FeedbackNotFoundException(Long id) {
        super("Feedback mit ID " + id + " wurde nicht gefunden.");
    }
}

