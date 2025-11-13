package com.example.cityfeedback.usermanagement.infrastructure;

import com.example.cityfeedback.usermanagement.domain.model.User;
import com.example.cityfeedback.usermanagement.domain.valueobjects.Email;
import com.example.cityfeedback.usermanagement.domain.model.UserClass;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(Email email);

    //void save(User user);
}
