package com.example.cityfeedback.usermanagement.application;

import com.example.cityfeedback.usermanagement.domain.valueobjects.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class UserDTO {

    @NotBlank(message = "E-Mail darf nicht leer sein")
    @Email(message = "E-Mail-Format ist ungültig")
    public String email;

    @NotBlank(message = "Passwort darf nicht leer sein")
    public String password;

    @NotNull(message = "Rolle darf nicht null sein")
    public UserRole role;
}


