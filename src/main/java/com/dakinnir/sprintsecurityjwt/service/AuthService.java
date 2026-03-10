package com.dakinnir.sprintsecurityjwt.service;

import com.dakinnir.sprintsecurityjwt.dto.*;
import com.dakinnir.sprintsecurityjwt.exception.EmailAlreadyExistsException;
import com.dakinnir.sprintsecurityjwt.exception.EmailNotVerifiedException;
import com.dakinnir.sprintsecurityjwt.exception.InvalidEmailPasswordException;
import com.dakinnir.sprintsecurityjwt.exception.InvalidTokenException;
import com.dakinnir.sprintsecurityjwt.model.User;
import com.dakinnir.sprintsecurityjwt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final EmailService emailService;

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
                .verificationToken(UUID.randomUUID().toString())
                .enabled(false)
                .build();
        User savedUser = userRepository.save(user);

        // Send verification email to the user with the verification token
        emailService.sendVerificationEmail(savedUser.getEmail(), savedUser.getVerificationToken());

        // Convert the saved user to a response DTO and return it
        return UserResponse.fromUser(savedUser);
    }

    public UserLoginResponse loginUser(UserLoginRequest loginRequest) {
        User foundUser = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new InvalidEmailPasswordException("Invalid email or password"));

        Authentication authentication = authenticationManager.authenticate(
                new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        UserDetails user = (UserDetails) authentication.getPrincipal();
        if (!foundUser.isEnabled()) {
            throw new EmailNotVerifiedException("Please verify your email before logging in");
        }
        // Generate a JWT token (this is just a placeholder, implement your JWT logic here)
        String generatedToken = jwtService.generateToken(foundUser);

        return UserLoginResponse.builder()
                .token(generatedToken)
                .user(UserResponse.fromUser(foundUser))
                .status(HttpStatus.OK.value())
                .build();
    }

    public VerificationResponse verifyEmail(String token) {
        User user = userRepository.findByVerificationToken(token)
                .orElseThrow(() -> new InvalidTokenException("Invalid verification token"));

        user.setEnabled(true);
        user.setVerificationToken(null); // Clear the token after successful verification
        userRepository.save(user);

        return VerificationResponse.builder()
                .message("Email verified successfully")
                .status(HttpStatus.OK.value())
                .build();
    }
}
