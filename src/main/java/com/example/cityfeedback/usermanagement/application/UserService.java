package com.example.cityfeedback.usermanagement.application;

import com.example.cityfeedback.usermanagement.domain.exceptions.InvalidPasswordException;
import com.example.cityfeedback.usermanagement.domain.exceptions.UserNotFoundException;
import com.example.cityfeedback.usermanagement.domain.model.User;
import com.example.cityfeedback.usermanagement.domain.repositories.UserRepository;
import com.example.cityfeedback.usermanagement.domain.services.PasswordHasher;
import com.example.cityfeedback.usermanagement.domain.services.UserRegistrationService;
import com.example.cityfeedback.usermanagement.domain.valueobjects.Email;
import com.example.cityfeedback.usermanagement.domain.valueobjects.UserRole;
import com.example.cityfeedback.usermanagement.domain.events.UserRegisteredEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserRegistrationService userRegistrationService;
    private final PasswordHasher passwordHasher;
    private final ApplicationEventPublisher eventPublisher;

    public UserService(
            UserRepository userRepository,
            UserRegistrationService userRegistrationService,
            PasswordHasher passwordHasher,
            ApplicationEventPublisher eventPublisher) {
        this.userRepository = userRepository;
        this.userRegistrationService = userRegistrationService;
        this.passwordHasher = passwordHasher;
        this.eventPublisher = eventPublisher;
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

    // SIGNUP - verwendet UserRegistrationService
    @Transactional
    public User createUser(String email, String rawPassword, UserRole role) {
        Email emailVO = new Email(email);
        
        // Für CITIZEN: Verwende UserRegistrationService (mit E-Mail-Eindeutigkeitsprüfung)
        if (role == UserRole.CITIZEN) {
            User user = userRegistrationService.registerUser(emailVO, rawPassword);
            
            // Domain Events publishen (mit echter User-ID)
            List<Object> domainEvents = user.getDomainEvents();
            for (Object event : domainEvents) {
                if (event instanceof UserRegisteredEvent) {
                    // Event mit echter User-ID erstellen
                    UserRegisteredEvent realEvent = new UserRegisteredEvent(user.getId(), user.getEmail().getValue());
                    eventPublisher.publishEvent(realEvent);
                }
            }
            user.clearDomainEvents();
            
            return user;
        } else {
            // Für STAFF/ADMIN: Direkte Erstellung (ohne E-Mail-Eindeutigkeitsprüfung)
            // TODO: Sollte auch über einen Service laufen
            throw new UnsupportedOperationException("Registrierung für " + role + " noch nicht implementiert");
        }
    }

    // LOGIN (EMAIL + PASSWORD)
    public User login(String email, String rawPassword) {
        Email emailVO = new Email(email);

        User user = userRepository.findByEmail(emailVO)
                .orElseThrow(() -> new UserNotFoundException(email));

        if (!user.getPassword().matches(rawPassword, passwordHasher)) {
            throw new InvalidPasswordException();
        }

        return user;
    }
}