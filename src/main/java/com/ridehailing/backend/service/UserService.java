package com.ridehailing.backend.service;

import com.ridehailing.backend.domain.User;
import com.ridehailing.backend.exception.RideHailingException;
import com.ridehailing.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User registerUser(String name, String email) {
        if (name == null || name.trim().isEmpty()) {
            throw new RideHailingException("Name cannot be empty");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new RideHailingException("Email cannot be empty");
        }
        User user = new User(null, name, email);
        return userRepository.save(user);
    }

    public User getUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RideHailingException("User not found"));
    }

    public void deleteUser(Long id) {
        // Optional: check if user exists first
        userRepository.findById(id).orElseThrow(() -> new RideHailingException("User not found"));
        userRepository.deleteById(id);
    }
}
