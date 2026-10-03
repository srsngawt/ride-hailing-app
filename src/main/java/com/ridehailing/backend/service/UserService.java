package com.ridehailing.backend.service;

import com.ridehailing.backend.domain.User;
import com.ridehailing.backend.exception.RideHailingException;
import com.ridehailing.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User registerUser(String name, String email) {
        if (name == null || name.trim().isEmpty()) {
            throw new RideHailingException("Name cannot be empty");
        }
        User user = User.builder().name(name).email(email).build();
        return userRepository.save(user);
    }

    public User getUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RideHailingException("User not found"));
    }
}
