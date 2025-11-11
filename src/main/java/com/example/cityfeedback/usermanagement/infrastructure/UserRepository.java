package com.example.cityfeedback.usermanagement.infrastructure;

import com.example.cityfeedback.usermanagement.domain.Email;
import com.example.cityfeedback.usermanagement.domain.model.User;

public interface UserRepository {

    boolean existsByEmail(Email email);

    void save(User user);
}
