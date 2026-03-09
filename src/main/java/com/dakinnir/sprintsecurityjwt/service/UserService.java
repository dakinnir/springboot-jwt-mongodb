package com.dakinnir.sprintsecurityjwt.service;

import com.dakinnir.sprintsecurityjwt.exception.EmailAlreadyExistsException;
import com.dakinnir.sprintsecurityjwt.model.User;
import com.dakinnir.sprintsecurityjwt.dto.UserRegistrationRequest;
import com.dakinnir.sprintsecurityjwt.dto.UserResponse;
import com.dakinnir.sprintsecurityjwt.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserResponse registerUser(UserRegistrationRequest request) {
        // Check if user with the same email already exists
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException("Email already exists");
        }

        // Create a new user object
        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .enabled(false)
                .build();

        // Save the user to the database collection
        User savedUser = userRepository.save(user);
        System.out.println(savedUser.getPassword());
        // Convert the saved user to a response DTO and return it
        return UserResponse.fromUser(savedUser);
    }
}
