package com.example.cityfeedback.usermanagement.domain;

public interface UserRepository {

    boolean existsByEmail(Email email);

    void save(User user);
}
