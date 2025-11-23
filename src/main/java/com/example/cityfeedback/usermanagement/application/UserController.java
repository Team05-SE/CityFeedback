package com.example.cityfeedback.usermanagement.application;

import com.example.cityfeedback.usermanagement.domain.model.User;
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
    public User create(@RequestBody UserDTO dto) {
        return userService.createUser(dto.email, dto.password, dto.role);
    }

    // LOGIN
    @PostMapping("/login")
    public User login(@RequestBody LoginRequestDTO dto) {
        return userService.login(dto.email, dto.password);
    }
}
