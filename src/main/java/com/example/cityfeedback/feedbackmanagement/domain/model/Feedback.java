package com.example.cityfeedback.feedbackmanagement.domain.model;

import com.example.cityfeedback.feedbackmanagement.domain.valueobjects.Category;
import com.example.cityfeedback.feedbackmanagement.domain.valueobjects.Status;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

/**
 * Aggregate Root: Feedback
 * Repräsentiert ein Feedback-Eintrag im City-Feedback-System.
 * 
 * Diese Klasse ist framework-unabhängig und enthält keine JPA-Annotationen.
 * Die Persistierung wird über FeedbackEntity und FeedbackMapper in der Infrastructure-Schicht gehandhabt.
 */
public class Feedback {

    private Long id;
    private String title;
    private Category category;
    private LocalDate feedbackDate;
    private String content;
    private Status status;
    private boolean isPublished;
    private UUID userId; // Lose Kopplung: Nur User-ID statt User-Entity

    /**
     * No-Args Konstruktor für Mapper in Infrastructure.
     * WARNUNG: Verwende stattdessen die Factory-Methode create().
     */
    public Feedback() {
    }

    /**
     * Privater Konstruktor für interne Verwendung.
     * Verwende stattdessen die Factory-Methode create().
     */
    private Feedback(Long id, String title, Category category, LocalDate feedbackDate,
                     String content, Status status, boolean isPublished, UUID userId) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.feedbackDate = feedbackDate;
        this.content = content;
        this.status = status;
        this.isPublished = isPublished;
        this.userId = userId;
    }

    /**
     * Factory-Methode zum Erstellen eines neuen Feedbacks.
     * 
     * @param title Titel des Feedbacks
     * @param category Kategorie des Feedbacks
     * @param content Inhalt des Feedbacks
     * @param userId ID des Benutzers, der das Feedback erstellt
     * @return Neues Feedback-Objekt mit Status PENDING und nicht veröffentlicht
     * @throws IllegalArgumentException wenn Validierung fehlschlägt
     */
    public static Feedback create(String title, Category category, String content, UUID userId) {
        validateTitle(title);
        validateContent(content);
        validateCategory(category);
        Objects.requireNonNull(userId, "User-ID darf nicht null sein");

        return new Feedback(
            null, // ID wird von der Datenbank generiert
            title,
            category,
            LocalDate.now(),
            content,
            Status.PENDING, // Neues Feedback ist zunächst PENDING (Entwurf)
            false, // Neues Feedback ist zunächst nicht veröffentlicht
            userId
        );
    }

    /**
     * Veröffentlicht das Feedback.
     * 
     * @throws IllegalStateException wenn das Feedback bereits veröffentlicht ist
     */
    public void publish() {
        if (isPublished) {
            throw new IllegalStateException("Feedback ist bereits veröffentlicht.");
        }
        if (status == Status.CLOSED) {
            throw new IllegalStateException("Geschlossenes Feedback kann nicht veröffentlicht werden.");
        }
        if (status == Status.PENDING) {
            throw new IllegalStateException("PENDING Feedbacks müssen erst auf OPEN gesetzt werden, bevor sie veröffentlicht werden können.");
        }
        this.isPublished = true;
    }

    /**
     * Gibt ein PENDING Feedback frei (setzt Status auf OPEN).
     * Kann nur von Mitarbeitern/Admins verwendet werden.
     * 
     * @throws IllegalStateException wenn das Feedback nicht PENDING ist
     */
    public void approve() {
        if (status != Status.PENDING) {
            throw new IllegalStateException("Nur PENDING Feedbacks können freigegeben werden.");
        }
        this.status = Status.OPEN;
    }

    /**
     * Aktualisiert den Status des Feedbacks.
     * 
     * @param newStatus Neuer Status
     * @throws IllegalArgumentException wenn der Status-Übergang ungültig ist
     */
    public void updateStatus(Status newStatus) {
        Objects.requireNonNull(newStatus, "Status darf nicht null sein");
        
        // Validierung von Status-Übergängen
        if (this.status == Status.CLOSED && newStatus != Status.CLOSED) {
            throw new IllegalArgumentException("Ein geschlossenes Feedback kann nicht wieder geöffnet werden.");
        }
        
        this.status = newStatus;
        
        // Wenn Feedback geschlossen wird, automatisch als nicht veröffentlicht markieren
        if (newStatus == Status.CLOSED) {
            this.isPublished = false;
        }
    }

    /**
     * Schließt das Feedback.
     */
    public void close() {
        updateStatus(Status.CLOSED);
    }

    // Validierungsmethoden
    private static void validateTitle(String title) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Titel darf nicht null oder leer sein.");
        }
        if (title.length() > 200) {
            throw new IllegalArgumentException("Titel darf maximal 200 Zeichen lang sein.");
        }
    }

    private static void validateContent(String content) {
        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException("Inhalt darf nicht null oder leer sein.");
        }
        if (content.length() > 5000) {
            throw new IllegalArgumentException("Inhalt darf maximal 5000 Zeichen lang sein.");
        }
    }

    private static void validateCategory(Category category) {
        Objects.requireNonNull(category, "Kategorie darf nicht null sein.");
    }

    // Getter
    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Category getCategory() {
        return category;
    }

    public LocalDate getFeedbackDate() {
        return feedbackDate;
    }

    public String getContent() {
        return content;
    }

    public Status getStatus() {
        return status;
    }

    public boolean isPublished() {
        return isPublished;
    }

    public UUID getUserId() {
        return userId;
    }

    /**
     * Setter für Mapper in Infrastructure.
     * WARNUNG: Diese Methoden sind nur für die Persistierung gedacht.
     * Verwende stattdessen die Business-Methoden (create, publish, updateStatus, etc.).
     */
    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setFeedbackDate(LocalDate feedbackDate) {
        this.feedbackDate = feedbackDate;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setPublished(boolean published) {
        isPublished = published;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Feedback feedback = (Feedback) o;
        return Objects.equals(id, feedback.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Feedback{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", category=" + category +
                ", feedbackDate=" + feedbackDate +
                ", content='" + content + '\'' +
                ", status=" + status +
                ", isPublished=" + isPublished +
                ", userId=" + userId +
                '}';
    }
}


