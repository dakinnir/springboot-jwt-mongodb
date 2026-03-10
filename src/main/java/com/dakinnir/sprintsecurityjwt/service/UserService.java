package com.dakinnir.sprintsecurityjwt.service;

import org.springframework.stereotype.Service;

import com.dakinnir.sprintsecurityjwt.dto.UserResponse;
import com.dakinnir.sprintsecurityjwt.exception.UserNotFoundException;
import com.dakinnir.sprintsecurityjwt.model.User;
import com.dakinnir.sprintsecurityjwt.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserResponse getCurrentUser(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        return UserResponse.fromUser(user);
    }

}
