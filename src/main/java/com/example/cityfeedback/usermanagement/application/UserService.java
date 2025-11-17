package com.example.cityfeedback.usermanagement.application;

import com.example.cityfeedback.usermanagement.domain.model.User;
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

    public User createUser(User user) {
        return this.userRepository.save(user);
    }
}

