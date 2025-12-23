package com.example.cityfeedback.usermanagement.application;

import com.example.cityfeedback.usermanagement.domain.model.User;
import com.example.cityfeedback.usermanagement.domain.valueobjects.Email;
import com.example.cityfeedback.usermanagement.domain.valueobjects.Password;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // GET ALL USERS
    @GetMapping
    public List<User> getAllUsers() {
        return this.userService.getAllUsers();
    }

    // GET USER BY ID
    @GetMapping("/{id}")
    public User getUserById(@PathVariable UUID id) {
        return this.userService.getUserById(id);
    }

    // SIGNUP
    @PostMapping
    public User create(@Valid @RequestBody UserDTO dto) {
        Email email = new Email(dto.email);
        Password password = new Password(dto.password);
        return userService.createUser(email, password, dto.role);
    }

    // LOGIN
    @PostMapping("/login")
    public User login(@Valid @RequestBody LoginRequestDTO dto) {
        return userService.login(dto.email, dto.password);
    }

    // ADMIN: Passwort ändern
    @PutMapping("/{id}/password")
    public User changePassword(@PathVariable UUID id, @Valid @RequestBody ChangePasswordDTO dto) {
        Password newPassword = new Password(dto.password);
        return userService.updatePassword(id, newPassword);
    }

    // ADMIN: Rolle ändern
    @PutMapping("/{id}/role")
    public User changeRole(
            @RequestHeader(value = "X-Admin-Id", required = true) UUID adminId,
            @PathVariable UUID id,
            @Valid @RequestBody ChangeRoleDTO dto) {
        return userService.updateRole(adminId, id, dto.role);
    }

    // ADMIN: User erstellen
    @PostMapping("/admin/create")
    public User createUserByAdmin(
            @RequestHeader(value = "X-Admin-Id", required = true) UUID adminId,
            @Valid @RequestBody CreateUserByAdminDTO dto) {
        Email email = new Email(dto.email);
        Password password = new Password(dto.password);
        return userService.createUserByAdmin(adminId, email, password, dto.role);
    }

    // ADMIN: User löschen
    @DeleteMapping("/{id}")
    @org.springframework.web.bind.annotation.ResponseStatus(org.springframework.http.HttpStatus.NO_CONTENT)
    public void deleteUser(
            @RequestHeader(value = "X-Admin-Id", required = true) UUID adminId,
            @PathVariable UUID id) {
        userService.deleteUser(adminId, id);
    }
}

