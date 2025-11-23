package com.example.cityfeedback.usermanagement.application;

import com.example.cityfeedback.usermanagement.domain.model.User;
import com.example.cityfeedback.usermanagement.domain.valueobjects.Email;
import com.example.cityfeedback.usermanagement.domain.valueobjects.Password;
import com.example.cityfeedback.usermanagement.domain.valueobjects.UserRole;
import com.example.cityfeedback.usermanagement.domain.events.UserRegisteredEvent;
import com.example.cityfeedback.usermanagement.infrastructure.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;

    public UserService(UserRepository userRepository, ApplicationEventPublisher eventPublisher) {
        this.userRepository = userRepository;
        this.eventPublisher = eventPublisher;
    }

    // GET ALL USERS
    public List<User> getAllUsers() {
        return this.userRepository.findAll();
    }

    // GET USER BY ID
    public User getUserById(UUID id) {
        return this.userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    // SIGNUP
    @Transactional
    public User createUser(Email email, Password password, UserRole role) {
        User user = new User(email, password, role);
        user = userRepository.save(user);

        // Domain Event publishen
        UserRegisteredEvent event = new UserRegisteredEvent(user.getId(), email.getValue());
        eventPublisher.publishEvent(event);

        return user;
    }

    // LOGIN (EMAIL + PASSWORD)
    public User login(String email, String rawPassword) {
        Email emailVO = new Email(email);

        User user = userRepository.findByEmail(emailVO)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (!user.getPassword().matches(rawPassword)) {
            throw new EntityNotFoundException("Invalid password");
        }

        return user;
    }
}