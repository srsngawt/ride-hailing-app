package com.ridehailing.backend.controller;

import com.ridehailing.backend.domain.User;
import com.ridehailing.backend.service.UserService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public User registerUser(@RequestBody RegisterUserRequest request) {
        return userService.registerUser(request.getName(), request.getEmail());
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        return userService.getUser(id);
    }

    @Data
    public static class RegisterUserRequest {
        private String name;
        private String email;
    }
}
