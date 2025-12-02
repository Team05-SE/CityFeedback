package com.example.cityfeedback.feedbackmanagement.application;

import com.example.cityfeedback.feedbackmanagement.domain.model.Feedback;
import com.example.cityfeedback.feedbackmanagement.domain.exceptions.FeedbackNotFoundException;
import com.example.cityfeedback.feedbackmanagement.domain.repositories.FeedbackRepository;
import com.example.cityfeedback.usermanagement.domain.exceptions.UserNotFoundException;
import com.example.cityfeedback.usermanagement.domain.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public Feedback createFeedback(FeedbackDTO dto) {
        if (dto == null || dto.userId == null || dto.title == null || dto.title.isBlank() 
                || dto.category == null || dto.content == null || dto.content.isBlank()) {
            throw new IllegalArgumentException("Feedback-Daten sind unvollständig.");
        }

        // Prüfen, ob User existiert (lose Kopplung: nur ID-Prüfung)
        if (!userRepository.findById(dto.userId).isPresent()) {
            throw new UserNotFoundException(dto.userId);
        }

        // Factory-Methode des Aggregats verwenden
        Feedback feedback = Feedback.create(
                dto.title,
                dto.category,
                dto.content,
                dto.userId
        );

        return feedbackRepository.save(feedback);
    }
}
