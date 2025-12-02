package com.example.cityfeedback.usermanagement.application;

import com.example.cityfeedback.usermanagement.domain.model.User;
import com.example.cityfeedback.usermanagement.domain.repositories.UserRepository;
import com.example.cityfeedback.usermanagement.domain.services.UserRegistrationService;
import com.example.cityfeedback.usermanagement.domain.valueobjects.Email;
import com.example.cityfeedback.usermanagement.domain.valueobjects.Password;
import com.example.cityfeedback.usermanagement.domain.valueobjects.UserRole;
import com.example.cityfeedback.usermanagement.domain.events.UserRegisteredEvent;
import com.example.cityfeedback.usermanagement.domain.exceptions.InvalidCredentialsException;
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

    public UserService(UserRepository userRepository, 
                      UserRegistrationService registrationService,
                      ApplicationEventPublisher eventPublisher) {
        this.userRepository = userRepository;
        this.registrationService = registrationService;
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

        // Domain Events aus dem Aggregat holen und publishen
        List<Object> domainEvents = user.getDomainEvents();
        for (Object event : domainEvents) {
            if (event instanceof UserRegisteredEvent) {
                eventPublisher.publishEvent(event);
            }
        }
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
}