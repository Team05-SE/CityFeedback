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

    // ===================================================================
    // Mitarbeiter/Admin Endpunkte
    // ===================================================================


    /**
     * Aktualisiert den Status eines Feedbacks.
     * PUT /feedback/{id}/status
     */
    @PutMapping("/{id}/status")
    public Feedback updateStatus(@PathVariable Long id, @Valid @RequestBody UpdateStatusDTO dto) {
        return feedbackService.updateFeedbackStatus(id, dto.status);
    }

    /**
     * Veröffentlicht ein Feedback.
     * PUT /feedback/{id}/publish
     */
    @PutMapping("/{id}/publish")
    public Feedback publishFeedback(@PathVariable Long id) {
        return feedbackService.publishFeedback(id);
    }

    /**
     * Nimmt ein Feedback aus der Veröffentlichung.
     * PUT /feedback/{id}/unpublish
     */
    @PutMapping("/{id}/unpublish")
    public Feedback unpublishFeedback(@PathVariable Long id) {
        return feedbackService.unpublishFeedback(id);
    }

    /**
     * Löscht ein Feedback komplett (nur für Admins).
     * DELETE /feedback/{id}
     */
    @DeleteMapping("/{id}")
    @org.springframework.web.bind.annotation.ResponseStatus(org.springframework.http.HttpStatus.NO_CONTENT)
    public void deleteFeedback(
            @RequestHeader(value = "X-Admin-Id", required = true) java.util.UUID adminId,
            @PathVariable Long id) {
        feedbackService.deleteFeedback(adminId, id);
    }

    // ===================================================================
    // Kommentar-Endpunkte
    // ===================================================================

    /**
     * Fügt einen Kommentar zu einem Feedback hinzu.
     * POST /feedback/{id}/comments
     */
    @PostMapping("/{id}/comments")
    public com.example.cityfeedback.feedbackmanagement.domain.model.Comment addComment(
            @PathVariable Long id,
            @Valid @RequestBody com.example.cityfeedback.feedbackmanagement.application.CommentDTO dto) {
        return feedbackService.addComment(id, dto.authorId, dto.content);
    }

    /**
     * Gibt alle Kommentare zu einem Feedback zurück.
     * GET /feedback/{id}/comments
     */
    @GetMapping("/{id}/comments")
    public List<com.example.cityfeedback.feedbackmanagement.domain.model.Comment> getComments(
            @PathVariable Long id) {
        return feedbackService.getCommentsByFeedbackId(id);
    }

    // ===================================================================
    // Öffentliche Endpunkte (ohne Login)
    // ===================================================================

    /**
     * Gibt alle veröffentlichten Feedbacks zurück.
     * GET /feedback/public
     */
    @GetMapping("/public")
    public List<Feedback> getPublishedFeedbacks() {
        return feedbackService.getPublishedFeedbacks();
    }
}
