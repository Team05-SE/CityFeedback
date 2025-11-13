package com.example.cityfeedback.feedbackmanagement.application;

import com.example.cityfeedback.feedbackmanagement.domain.model.Feedback;
import com.example.cityfeedback.feedbackmanagement.infrastructure.FeedbackRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;

    public FeedbackService(FeedbackRepository feedbackRepository) {
        this.feedbackRepository = feedbackRepository;
    }

    public List<Feedback> getAllFeedbacks() {
        return this.feedbackRepository.findAll();
    }

    public Feedback getFeedbackById(Long id) {
        if(!this.feedbackRepository.existsById(id)) {
            throw new EntityNotFoundException("no feedback with given id found");
        }
        return this.feedbackRepository.findById(id).get();
    }

    public Feedback createFeedback(Feedback feedback) {
        return this.feedbackRepository.save(feedback);
    }
}
