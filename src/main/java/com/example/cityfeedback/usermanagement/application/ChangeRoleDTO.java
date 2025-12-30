package com.example.cityfeedback.usermanagement.application;

import com.example.cityfeedback.usermanagement.domain.valueobjects.UserRole;
import jakarta.validation.constraints.NotNull;

public class ChangeRoleDTO {

    @NotNull(message = "Rolle darf nicht null sein")
    public UserRole role;
}

