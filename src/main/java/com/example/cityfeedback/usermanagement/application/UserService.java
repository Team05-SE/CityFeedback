package com.example.cityfeedback.usermanagement.application;

import com.example.cityfeedback.usermanagement.domain.model.User;
import com.example.cityfeedback.usermanagement.domain.repositories.UserRepository;
import com.example.cityfeedback.usermanagement.domain.services.UserRegistrationService;
import com.example.cityfeedback.usermanagement.domain.valueobjects.Email;
import com.example.cityfeedback.usermanagement.domain.valueobjects.Password;
import com.example.cityfeedback.usermanagement.domain.valueobjects.UserRole;
import com.example.cityfeedback.usermanagement.domain.events.UserRegisteredEvent;
import com.example.cityfeedback.feedbackmanagement.application.FeedbackService;
import com.example.cityfeedback.usermanagement.domain.exceptions.InvalidCredentialsException;
import com.example.cityfeedback.usermanagement.domain.exceptions.UnauthorizedException;
import com.example.cityfeedback.usermanagement.domain.exceptions.UserNotFoundException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserRegistrationService registrationService;
    private final ApplicationEventPublisher eventPublisher;
    private final FeedbackService feedbackService;

    public UserService(UserRepository userRepository, 
                      UserRegistrationService registrationService,
                      ApplicationEventPublisher eventPublisher,
                      FeedbackService feedbackService) {
        this.userRepository = userRepository;
        this.registrationService = registrationService;
        this.eventPublisher = eventPublisher;
        this.feedbackService = feedbackService;
    }

    // GET ALL USERS
    public List<User> getAllUsers() {
        return this.userRepository.findAll();
    }

    // GET USER BY ID
    public User getUserById(UUID id) {
        return this.userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    // SIGNUP
    @Transactional
    public User createUser(Email email, Password password, UserRole role) {
        User user;
        
        if (role == UserRole.CITIZEN) {
            // Domain Service für Registrierung verwenden (prüft E-Mail-Eindeutigkeit)
            user = registrationService.registerUser(email, password);
        } else {
            // Für andere Rollen (STAFF, ADMIN) direkt erstellen
            // E-Mail-Eindeutigkeit prüfen
            if (userRepository.existsByEmail(email)) {
                throw new com.example.cityfeedback.usermanagement.domain.exceptions.EmailAlreadyExistsException(email.getValue());
            }
            user = new User(email, password, role);
            user = userRepository.save(user);
        }

        // Domain Events aus dem Aggregat holen und publishen (funktional mit Stream API)
        user.getDomainEvents().stream()
                .filter(UserRegisteredEvent.class::isInstance)
                .forEach(eventPublisher::publishEvent);
        user.clearDomainEvents();

        return user;
    }

    // LOGIN (EMAIL + PASSWORD)
    public User login(String email, String rawPassword) {
        Email emailVO = new Email(email);

        User user = userRepository.findByEmail(emailVO)
                .orElseThrow(() -> new InvalidCredentialsException("Benutzer nicht gefunden oder Passwort ungültig."));

        if (!user.getPassword().matches(rawPassword)) {
            throw new InvalidCredentialsException("Benutzer nicht gefunden oder Passwort ungültig.");
        }

        return user;
    }

    /**
     * Erstellt einen neuen User durch einen Admin.
     * 
     * @param adminId Die ID des ausführenden Admins
     * @param email E-Mail des neuen Users
     * @param password Passwort des neuen Users
     * @param role Rolle des neuen Users
     * @return Der erstellte User
     * @throws UnauthorizedException wenn der ausführende User kein Admin ist
     */
    @Transactional
    public User createUserByAdmin(UUID adminId, Email email, Password password, UserRole role) {
        User admin = getUserById(adminId);
        
        if (admin.getRole() != UserRole.ADMIN) {
            throw new UnauthorizedException("Nur Administratoren können neue Benutzer erstellen.");
        }

        return createUser(email, password, role);
    }

    /**
     * Ändert das Passwort eines Users.
     * 
     * @param userId Die ID des Users, dessen Passwort geändert werden soll
     * @param newPassword Das neue Passwort
     * @return Der aktualisierte User
     * @throws UserNotFoundException wenn der User nicht gefunden wird
     */
    @Transactional
    public User updatePassword(UUID userId, Password newPassword) {
        User user = getUserById(userId);
        user.changePassword(newPassword);
        return userRepository.save(user);
    }

    /**
     * Ändert die Rolle eines Users (nur durch Admin).
     * 
     * @param adminId Die ID des ausführenden Admins
     * @param userId Die ID des Users, dessen Rolle geändert werden soll
     * @param newRole Die neue Rolle
     * @return Der aktualisierte User
     * @throws UnauthorizedException wenn der ausführende User kein Admin ist
     * @throws UserNotFoundException wenn der User nicht gefunden wird
     */
    @Transactional
    public User updateRole(UUID adminId, UUID userId, UserRole newRole) {
        User admin = getUserById(adminId);
        
        if (admin.getRole() != UserRole.ADMIN) {
            throw new UnauthorizedException("Nur Administratoren können Rollen ändern.");
        }

        User user = getUserById(userId);
        user.changeRole(newRole);
        return userRepository.save(user);
    }

    /**
     * Löscht einen User und alle zugehörigen Feedbacks (nur durch Admin).
     * 
     * @param adminId Die ID des ausführenden Admins
     * @param userId Die ID des zu löschenden Users
     * @throws UnauthorizedException wenn der ausführende User kein Admin ist
     * @throws UserNotFoundException wenn der User nicht gefunden wird
     */
    @Transactional
    public void deleteUser(UUID adminId, UUID userId) {
        User admin = getUserById(adminId);
        
        if (admin.getRole() != UserRole.ADMIN) {
            throw new UnauthorizedException("Nur Administratoren können Benutzer löschen.");
        }

        User user = getUserById(userId);
        
        // Alle Feedbacks des Users löschen
        feedbackService.deleteFeedbacksByUserId(userId);
        
        // User löschen
        userRepository.delete(user);
    }
}