package com.example.cityfeedback.usermanagement.application;

import com.example.cityfeedback.usermanagement.domain.Email;
import com.example.cityfeedback.usermanagement.domain.Password;
import com.example.cityfeedback.usermanagement.domain.model.User;
import com.example.cityfeedback.usermanagement.infrastructure.UserRepository;

public class UserRegistrationService {

    private final UserRepository userRepository;

    public UserRegistrationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User register(Email email, Password password) {

        if (userRepository.existsByEmail(email)) {
            throw new IllegalStateException("Ein Benutzer mit dieser E-Mail existiert bereits: " + email);
        }

        User user = User.register(email, password);
        userRepository.save(user);
        return user;
    }
}

