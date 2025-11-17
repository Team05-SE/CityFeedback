package com.example.cityfeedback.feedbackmanagement.application;

import com.example.cityfeedback.feedbackmanagement.domain.model.Feedback;
import com.example.cityfeedback.feedbackmanagement.domain.valueobjects.Status;
import com.example.cityfeedback.feedbackmanagement.infrastructure.FeedbackRepository;
import com.example.cityfeedback.usermanagement.infrastructure.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final UserRepository userRepository;

    public FeedbackService(FeedbackRepository feedbackRepository, UserRepository userRepository) {
        this.feedbackRepository = feedbackRepository;
        this.userRepository = userRepository;
    }

    public List<Feedback> getAllFeedbacks() {
        return feedbackRepository.findAll();
    }

    public Feedback getFeedbackById(Long id) {
        return feedbackRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("no feedback with given id found"));
    }

    public Feedback createFeedback(FeedbackDTO dto) {

        var user = userRepository.findById(dto.userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Feedback feedback = new Feedback(
                null,
                dto.title,
                dto.category,
                LocalDate.now(),
                dto.content,
                Status.OPEN,
                false
        );

        feedback.setUser(user);

        return feedbackRepository.save(feedback);
    }
}
