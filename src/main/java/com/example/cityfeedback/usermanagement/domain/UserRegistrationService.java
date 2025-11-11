package com.example.cityfeedback.usermanagement.domain;

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

