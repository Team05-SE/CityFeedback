package com.example.cityfeedback.feedbackmanagement.domain.repositories;

import com.example.cityfeedback.feedbackmanagement.domain.model.Feedback;

import java.util.List;
import java.util.Optional;

/**
 * Domain Repository Interface für Feedback.
 * Definiert die benötigten Operationen unabhängig von der Persistenz-Technologie.
 */
public interface FeedbackRepository {

    void save(Feedback feedback);

    Optional<Feedback> findById(Long id);

    List<Feedback> findAll();
}

