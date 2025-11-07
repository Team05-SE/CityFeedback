package com.example.cityfeedback.domain.user;

public interface UserRepository {

    boolean existsByEmail(Email email);

    void save(User user);
}
