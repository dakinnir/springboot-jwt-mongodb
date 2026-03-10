package com.dakinnir.sprintsecurityjwt.dto;

import com.dakinnir.sprintsecurityjwt.model.User;
import lombok.Builder;
import lombok.Data;
import org.springframework.transaction.annotation.Transactional;

@Data
@Builder
@Transactional
public class UserResponse {
    private String id;
    private String name;
    private String email;

    public static UserResponse fromUser(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }
}
