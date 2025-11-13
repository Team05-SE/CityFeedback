package com.example.cityfeedback.usermanagement.application;

import com.example.cityfeedback.usermanagement.domain.model.User;
import com.example.cityfeedback.usermanagement.domain.model.UserClass;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return this.userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return this.userService.getUserById(id);
    }


    @PostMapping
    public User createUser(@RequestBody User user) {
        return this.userService.createUser(user);
    }
}
