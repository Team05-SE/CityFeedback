package com.example.cityfeedback.feedbackmanagement.infrastructure;

import com.example.cityfeedback.feedbackmanagement.domain.model.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
}
