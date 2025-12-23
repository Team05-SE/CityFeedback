package com.example.cityfeedback.usermanagement.application;

import com.example.cityfeedback.feedbackmanagement.application.FeedbackService;
import com.example.cityfeedback.usermanagement.domain.model.User;
import com.example.cityfeedback.usermanagement.domain.repositories.UserRepository;
import com.example.cityfeedback.usermanagement.domain.valueobjects.UserRole;
import com.example.cityfeedback.usermanagement.domain.exceptions.UnauthorizedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service zum Verwalten von Demo-Daten.
 * Nur für Administratoren.
 */
@Service
public class DemoDataService {

    private final UserRepository userRepository;
    private final FeedbackService feedbackService;

    public DemoDataService(UserRepository userRepository, FeedbackService feedbackService) {
        this.userRepository = userRepository;
        this.feedbackService = feedbackService;
    }

    /**
     * Löscht alle Demo-Daten (Demo-User und deren Feedbacks).
     * 
     * @param adminId Die ID des ausführenden Admins
     * @return Anzahl der gelöschten User
     * @throws UnauthorizedException wenn der ausführende User kein Admin ist
     */
    @Transactional
    public int deleteAllDemoData(UUID adminId) {
        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new com.example.cityfeedback.usermanagement.domain.exceptions.UserNotFoundException(adminId));
        
        if (admin.getRole() != UserRole.ADMIN) {
            throw new UnauthorizedException("Nur Administratoren können Demo-Daten löschen.");
        }

        // Finde alle Demo-User (erkennbar an E-Mail-Domain)
        List<User> allUsers = userRepository.findAll();
        List<User> demoUsers = allUsers.stream()
                .filter(user -> {
                    String email = user.getEmail().getValue();
                    return email.startsWith("demo.") || email.contains("@example.com");
                })
                .filter(user -> user.getRole() != UserRole.ADMIN) // Admin nicht löschen
                .collect(Collectors.toList());

        // Lösche Feedbacks der Demo-User
        for (User demoUser : demoUsers) {
            feedbackService.deleteFeedbacksByUserId(demoUser.getId());
        }

        // Lösche Demo-User
        int deletedCount = 0;
        for (User demoUser : demoUsers) {
            userRepository.delete(demoUser);
            deletedCount++;
        }

        return deletedCount;
    }
}

