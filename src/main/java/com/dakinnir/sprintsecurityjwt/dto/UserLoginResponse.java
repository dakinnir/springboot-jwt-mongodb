package com.dakinnir.sprintsecurityjwt.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.transaction.annotation.Transactional;

@Data
@Builder
@Transactional
public class UserLoginResponse {
    private String token;
    private UserResponse user;
    private int status;

}
