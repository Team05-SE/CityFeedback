package com.example.cityfeedback.feedbackmanagement.application;

import com.example.cityfeedback.feedbackmanagement.domain.model.Feedback;
import com.example.cityfeedback.feedbackmanagement.domain.valueobjects.Category;
import com.example.cityfeedback.feedbackmanagement.domain.valueobjects.Status;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/feedback")
public class FeedbackController {

    private final FeedbackService feedbackService;

    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @GetMapping
    public List<Feedback> getAllFeedbacks() {
        return this.feedbackService.getAllFeedbacks();
    }

    @GetMapping("/{id}")
    public Feedback getFeedbackById(@PathVariable Long id) {
        return this.feedbackService.getFeedbackById(id);
    }

    @PostMapping
    public Feedback createFeedback(@Valid @RequestBody FeedbackDTO dto) {
        return feedbackService.createFeedback(dto);
    }

    // ===================================================================
    // Collection Processing Endpoints - Funktionale Programmierung
    // ===================================================================

    /**
     * Gruppierung von Feedbacks nach Status mit Counting.
     * GET /feedback/statistics/status
     */
    @GetMapping("/statistics/status")
    public Map<Status, Long> getFeedbackStatusStatistics() {
        return feedbackService.getFeedbackStatusStatistics();
    }

    /**
     * Gruppierung von Feedbacks nach Kategorie mit Titel-Mapping.
     * GET /feedback/statistics/category
     */
    @GetMapping("/statistics/category")
    public Map<Category, List<String>> getFeedbackTitlesByCategory() {
        return feedbackService.getFeedbackTitlesByCategory();
    }

    /**
     * Komplexe Transformation: Veröffentlichte, aktive Feedbacks als Summary.
     * GET /feedback/summary/published-active
     */
    @GetMapping("/summary/published-active")
    public List<FeedbackService.FeedbackSummaryDTO> getPublishedActiveFeedbacksSummary() {
        return feedbackService.getPublishedActiveFeedbacksSummary();
    }

    /**
     * Aggregation/Reduktion: Gesamtstatistiken über alle Feedbacks.
     * GET /feedback/statistics
     */
    @GetMapping("/statistics")
    public FeedbackService.FeedbackStatisticsDTO getFeedbackStatistics() {
        return feedbackService.getFeedbackStatistics();
    }
}
