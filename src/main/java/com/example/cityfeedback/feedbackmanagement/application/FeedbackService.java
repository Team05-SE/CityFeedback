package com.example.cityfeedback.feedbackmanagement.application;

import com.example.cityfeedback.feedbackmanagement.domain.exceptions.FeedbackNotFoundException;
import com.example.cityfeedback.feedbackmanagement.domain.model.Feedback;
import com.example.cityfeedback.feedbackmanagement.domain.repositories.FeedbackRepository;
import com.example.cityfeedback.feedbackmanagement.domain.valueobjects.Status;
import com.example.cityfeedback.feedbackmanagement.domain.valueobjects.UserId;
import com.example.cityfeedback.usermanagement.domain.exceptions.UserNotFoundException;
import com.example.cityfeedback.usermanagement.domain.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

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
                .orElseThrow(() -> new FeedbackNotFoundException(id));
    }

    public Feedback createFeedback(FeedbackDTO dto) {
        // Prüfe ob User existiert (über Bounded Context-Grenze)
        var user = userRepository.findById(dto.userId)
                .orElseThrow(() -> new UserNotFoundException(dto.userId));

        // Erstelle UserId Value Object
        UserId creatorId = new UserId(dto.userId);

        // Erstelle Feedback mit UserId statt User-Entität
        Feedback feedback = new Feedback(
                null,
                dto.title,
                dto.category,
                LocalDate.now(),
                dto.content,
                Status.OPEN,
                false,
                creatorId
        );

        feedbackRepository.save(feedback);

        return feedback;
    }
}
