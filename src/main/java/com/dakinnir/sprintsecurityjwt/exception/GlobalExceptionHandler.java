package com.dakinnir.sprintsecurityjwt.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {


    private static final Map<Class<? extends Exception>, HttpStatus> EXCEPTION_STATUS_MAP = Map.of(
            EmailAlreadyExistsException.class, HttpStatus.CONFLICT,
            InvalidEmailPasswordException.class, HttpStatus.BAD_REQUEST,

            InvalidTokenException.class, HttpStatus.BAD_REQUEST,
            EmailNotVerifiedException.class, HttpStatus.BAD_REQUEST
    );

    private ResponseEntity<ErrorResponse> buildResponse(String message, HttpStatus status) {
        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                message,
                status.value()
        );

        return new ResponseEntity<>(error, status);
    }

    // Handle validation errors from @Valid
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        // Combine all field errors into a single message
        String errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining("; "));


        return buildResponse(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAllExceptions(Exception ex) {
        // Return 404 Not Found for unhandled routes
        if (ex instanceof NoHandlerFoundException) {
            return buildResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
        }
        // If status is not defined for the exception, default to 500 Internal Server Error
        HttpStatus status = EXCEPTION_STATUS_MAP.getOrDefault(ex.getClass(), HttpStatus.INTERNAL_SERVER_ERROR);
        return buildResponse(ex.getMessage(), status);
    }
}