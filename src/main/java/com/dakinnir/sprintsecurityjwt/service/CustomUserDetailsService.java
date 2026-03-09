package com.dakinnir.sprintsecurityjwt.service;

import com.dakinnir.sprintsecurityjwt.repository.UserRepository;
import com.dakinnir.sprintsecurityjwt.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * CustomUserDetailsService is a service that implements UserDetailsService to load user-specific data during authentication with email field.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository repository;

    public CustomUserDetailsService(UserRepository repository) {
        this.repository = repository;
    }

    /**
     * Loads the user by email instead of username for authentication.
     * @throws UsernameNotFoundException if the user is not found in the database.
     * @return UserDetails object containing the user's email and password for authentication.
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Using email as the username for authentication
        User user = repository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return org.springframework.security.core.userdetails.User
                .builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .build();
    }
}
