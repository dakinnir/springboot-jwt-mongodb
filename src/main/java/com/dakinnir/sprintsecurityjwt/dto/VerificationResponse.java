package com.dakinnir.sprintsecurityjwt.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VerificationResponse {
    private String message;
    private int status;
}
