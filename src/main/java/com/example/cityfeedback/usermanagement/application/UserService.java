package com.example.cityfeedback.usermanagement.application;

import com.example.cityfeedback.usermanagement.domain.model.User;
import com.example.cityfeedback.usermanagement.domain.valueobjects.Email;
import com.example.cityfeedback.usermanagement.domain.valueobjects.Password;
import com.example.cityfeedback.usermanagement.domain.valueobjects.UserRole;
import com.example.cityfeedback.usermanagement.infrastructure.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return this.userRepository.findAll();
    }

    public User getUserById(UUID id) {
        if(!this.userRepository.existsById(id)) {
            throw new EntityNotFoundException("no user with given id found");
        }
        return this.userRepository.findById(id).get();
    }

    public User createUser(Email email, Password password, UserRole role) {
        User user = new User(email, password, role);
        return userRepository.save(user);
    }

}

