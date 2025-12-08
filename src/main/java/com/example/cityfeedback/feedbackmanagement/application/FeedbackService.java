package com.example.cityfeedback.feedbackmanagement.application;

import com.example.cityfeedback.feedbackmanagement.domain.model.Feedback;
import com.example.cityfeedback.feedbackmanagement.domain.exceptions.FeedbackNotFoundException;
import com.example.cityfeedback.feedbackmanagement.domain.repositories.FeedbackRepository;
import com.example.cityfeedback.usermanagement.domain.exceptions.UserNotFoundException;
import com.example.cityfeedback.usermanagement.domain.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        validateFeedbackDTO(dto);

        // Prüfen, ob User existiert (lose Kopplung: nur ID-Prüfung) - funktional mit Optional
        userRepository.findById(dto.userId)
                .orElseThrow(() -> new UserNotFoundException(dto.userId));

        // Factory-Methode des Aggregats verwenden
        Feedback feedback = Feedback.create(
                dto.title,
                dto.category,
                dto.content,
                dto.userId
        );

        return feedbackRepository.save(feedback);
    }

    /**
     * Validiert das FeedbackDTO funktional mit Stream API.
     * Nutzt funktionale Interfaces und Methodenreferenzen.
     */
    private void validateFeedbackDTO(FeedbackDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("Feedback-DTO darf nicht null sein");
        }

        Stream.of(
                validateNotNull(dto.userId, "userId"),
                validateNotBlank(dto.title, "title"),
                validateNotNull(dto.category, "category"),
                validateNotBlank(dto.content, "content")
        )
        .filter(Objects::nonNull)
        .findFirst()
        .ifPresent(error -> {
            throw new IllegalArgumentException("Feedback-Daten sind unvollständig: " + error);
        });
    }

    /**
     * Validiert, dass ein Feld nicht null ist.
     * Gibt eine Fehlermeldung zurück, wenn das Feld null ist, sonst null.
     */
    private String validateNotNull(Object field, String fieldName) {
        return field == null ? fieldName + " darf nicht null sein" : null;
    }

    /**
     * Validiert, dass ein String-Feld nicht null oder leer ist.
     * Nutzt Methodenreferenz String::isBlank für die Prüfung.
     */
    private String validateNotBlank(String field, String fieldName) {
        return (field == null || field.isBlank()) ? fieldName + " darf nicht leer sein" : null;
    }

    // ===================================================================
    // Collection Processing - Funktionale Programmierung
    // ===================================================================

    /**
     * Gruppierung von Feedbacks nach Status mit Counting.
     * Nutzt funktionale Konzepte: Stream API, Collectors.groupingBy(), Collectors.counting()
     * 
     * @return Map mit Status als Key und Anzahl der Feedbacks als Value
     */
    public Map<com.example.cityfeedback.feedbackmanagement.domain.valueobjects.Status, Long> getFeedbackStatusStatistics() {
        return feedbackRepository.findAll().stream()
                .collect(Collectors.groupingBy(
                        Feedback::getStatus,
                        Collectors.counting()
                ));
    }

    /**
     * Gruppierung von Feedbacks nach Kategorie mit Aggregation.
     * Zeigt erweiterte Collection-Processing-Funktionalität.
     * 
     * @return Map mit Kategorie als Key und Liste der Feedback-Titel als Value
     */
    public Map<com.example.cityfeedback.feedbackmanagement.domain.valueobjects.Category, List<String>> getFeedbackTitlesByCategory() {
        return feedbackRepository.findAll().stream()
                .collect(Collectors.groupingBy(
                        Feedback::getCategory,
                        Collectors.mapping(
                                Feedback::getTitle,
                                Collectors.toList()
                        )
                ));
    }

    /**
     * Komplexe Transformation mit mehreren Filtern.
     * Filtert veröffentlichte Feedbacks, die nicht geschlossen sind,
     * transformiert sie zu SummaryDTOs und sortiert nach Datum.
     * 
     * Nutzt funktionale Konzepte:
     * - Mehrfaches Filtering
     * - Transformation (Mapping)
     * - Sorting mit Comparator
     * 
     * @return Liste von FeedbackSummaryDTO, sortiert nach Feedback-Datum (neueste zuerst)
     */
    public List<FeedbackSummaryDTO> getPublishedActiveFeedbacksSummary() {
        return feedbackRepository.findAll().stream()
                .filter(Feedback::isPublished)  // Erster Filter: Nur veröffentlichte Feedbacks
                .filter(f -> f.getStatus() != com.example.cityfeedback.feedbackmanagement.domain.valueobjects.Status.CLOSED)  // Zweiter Filter: Nicht geschlossen
                .map(feedback -> new FeedbackSummaryDTO(  // Transformation zu DTO
                        feedback.getId(),
                        feedback.getTitle(),
                        feedback.getCategory().toString(),
                        feedback.getStatus().toString(),
                        feedback.getFeedbackDate()
                ))
                .sorted(Comparator.comparing(FeedbackSummaryDTO::getFeedbackDate).reversed())  // Sortierung nach Datum (neueste zuerst)
                .collect(Collectors.toList());
    }

    /**
     * Aggregation/Reduktion: Berechnet Statistiken über alle Feedbacks.
     * Nutzt funktionale Konzepte: Stream API, Reduktion, Optionale Werte
     * 
     * @return FeedbackStatisticsDTO mit aggregierten Werten
     */
    public FeedbackStatisticsDTO getFeedbackStatistics() {
        List<Feedback> allFeedbacks = feedbackRepository.findAll();
        
        if (allFeedbacks.isEmpty()) {
            return new FeedbackStatisticsDTO(0L, 0L, 0L, 0L, null, null);
        }

        long totalCount = allFeedbacks.stream()
                .count();

        long publishedCount = allFeedbacks.stream()
                .filter(Feedback::isPublished)
                .count();

        long closedCount = allFeedbacks.stream()
                .filter(f -> f.getStatus() == com.example.cityfeedback.feedbackmanagement.domain.valueobjects.Status.CLOSED)
                .count();

        long openCount = allFeedbacks.stream()
                .filter(f -> f.getStatus() == com.example.cityfeedback.feedbackmanagement.domain.valueobjects.Status.OPEN)
                .count();

        LocalDate oldestDate = allFeedbacks.stream()
                .map(Feedback::getFeedbackDate)
                .min(LocalDate::compareTo)
                .orElse(null);

        LocalDate newestDate = allFeedbacks.stream()
                .map(Feedback::getFeedbackDate)
                .max(LocalDate::compareTo)
                .orElse(null);

        return new FeedbackStatisticsDTO(totalCount, publishedCount, closedCount, openCount, oldestDate, newestDate);
    }

    /**
     * DTO für Feedback-Zusammenfassung (für Transformation).
     */
    public static class FeedbackSummaryDTO {
        private final Long id;
        private final String title;
        private final String category;
        private final String status;
        private final LocalDate feedbackDate;

        public FeedbackSummaryDTO(Long id, String title, String category, String status, LocalDate feedbackDate) {
            this.id = id;
            this.title = title;
            this.category = category;
            this.status = status;
            this.feedbackDate = feedbackDate;
        }

        public Long getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public String getCategory() {
            return category;
        }

        public String getStatus() {
            return status;
        }

        public LocalDate getFeedbackDate() {
            return feedbackDate;
        }
    }

    /**
     * DTO für Feedback-Statistiken (für Aggregation/Reduktion).
     */
    public static class FeedbackStatisticsDTO {
        private final Long totalCount;
        private final Long publishedCount;
        private final Long closedCount;
        private final Long openCount;
        private final LocalDate oldestDate;
        private final LocalDate newestDate;

        public FeedbackStatisticsDTO(Long totalCount, Long publishedCount, Long closedCount, Long openCount,
                                     LocalDate oldestDate, LocalDate newestDate) {
            this.totalCount = totalCount;
            this.publishedCount = publishedCount;
            this.closedCount = closedCount;
            this.openCount = openCount;
            this.oldestDate = oldestDate;
            this.newestDate = newestDate;
        }

        public Long getTotalCount() {
            return totalCount;
        }

        public Long getPublishedCount() {
            return publishedCount;
        }

        public Long getClosedCount() {
            return closedCount;
        }

        public Long getOpenCount() {
            return openCount;
        }

        public LocalDate getOldestDate() {
            return oldestDate;
        }

        public LocalDate getNewestDate() {
            return newestDate;
        }
    }
}
