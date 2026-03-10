package com.dakinnir.sprintsecurityjwt.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Getter
public class ErrorResponse {
    private LocalDateTime timestamp;
    private String error;
    private int status;
}