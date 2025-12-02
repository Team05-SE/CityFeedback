package com.example.cityfeedback.usermanagement.application;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class LoginRequestDTO {
    
    @NotBlank(message = "E-Mail darf nicht leer sein")
    @Email(message = "E-Mail-Format ist ungültig")
    public String email;

    @NotBlank(message = "Passwort darf nicht leer sein")
    public String password;
}