package com.dakinnir.sprintsecurityjwt;

import com.dakinnir.sprintsecurityjwt.dto.UserRegistrationRequest;
import com.dakinnir.sprintsecurityjwt.dto.UserResponse;
import com.dakinnir.sprintsecurityjwt.exception.EmailAlreadyExistsException;
import com.dakinnir.sprintsecurityjwt.model.User;
import com.dakinnir.sprintsecurityjwt.repository.UserRepository;
import com.dakinnir.sprintsecurityjwt.service.AuthService;
import com.dakinnir.sprintsecurityjwt.service.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerUser_shouldReturnUserResponse_whenEmailIsUnique() {
        // Arrange
        UserRegistrationRequest request = UserRegistrationRequest.builder()
                .name("John Doe")
                .email("john@example.com")
                .password("password123")
                .build();

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
        // Use a fixed hash "hashedPassword" for testing
        when(passwordEncoder.encode(request.getPassword())).thenReturn("hashedPassword");

        // Mock saving user
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        when(userRepository.save(userCaptor.capture())).thenAnswer(i -> i.getArguments()[0]);

        UserResponse response = authService.registerUser(request);

        assertNotNull(response);
        // Verify response contains correct user details
        assertEquals(request.getName(), response.getName());
        assertEquals(request.getEmail(), response.getEmail());

        // Verify password was hashed
        assertEquals("hashedPassword", userCaptor.getValue().getPassword());

        // Verify a verification token was generated
        assertNotNull(userCaptor.getValue().getVerificationToken());

        // Verify emailService called
        verify(emailService, times(1)).sendVerificationEmail(request.getEmail(), userCaptor.getValue().getVerificationToken());
    }

    @Test
    void registerUser_shouldThrowException_whenEmailAlreadyExists() {
        // Arrange
        UserRegistrationRequest request = UserRegistrationRequest.builder()
                .name("Jane Doe")
                .email("jane@example.com")
                .password("password123")
                .build();

        when(userRepository.findByEmail(request.getEmail()))
                .thenReturn(Optional.of(new User()));

        // Act & Assert
        assertThrows(EmailAlreadyExistsException.class,
                () -> authService.registerUser(request));

        // Ensure email service and save are never called
        verify(userRepository, never()).save(any());
        verify(emailService, never()).sendVerificationEmail(anyString(), anyString());
    }
}