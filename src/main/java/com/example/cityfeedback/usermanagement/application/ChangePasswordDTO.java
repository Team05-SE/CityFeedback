package com.example.cityfeedback.usermanagement.application;

import jakarta.validation.constraints.NotBlank;

public class ChangePasswordDTO {

    @NotBlank(message = "Passwort darf nicht leer sein")
    public String password;
}

