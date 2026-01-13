package com.realestate.management.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.realestate.management.entity.User;
import com.realestate.management.exception.ResourceNotFoundException;
import com.realestate.management.repository.UserRepository;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;

    // ---- Constructor Injection (replaces @RequiredArgsConstructor) ---- //
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // ---------------- Service Methods (unchanged) ---------------- //

    public User createUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
        // Store password as plain text (as per requirement)
        return userRepository.save(user);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User updateUser(Long id, User userDetails) {
        User user = getUserById(id);
        user.setName(userDetails.getName());
        user.setEmail(userDetails.getEmail());
        
        if (userDetails.getPassword() != null && !userDetails.getPassword().isEmpty()) {
            user.setPassword(userDetails.getPassword());
        }
        
        user.setRole(userDetails.getRole());
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        User user = getUserById(id);
        userRepository.delete(user);
    }

    public boolean authenticateUser(String email, String password) {
        User user = userRepository.findByEmail(email).orElse(null);
        return user != null && user.getPassword().equals(password);
    }
}
