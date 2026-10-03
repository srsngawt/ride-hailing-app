package com.ridehailing.backend.controller;

import com.ridehailing.backend.domain.User;
import com.ridehailing.backend.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public User registerUser(@RequestBody RegisterUserRequest request) {
        return userService.registerUser(request.name, request.email);
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        return userService.getUser(id);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }

        public static class RegisterUserRequest {
        public String name;
        public String email;
    }
}
