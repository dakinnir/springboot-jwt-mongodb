package com.dakinnir.sprintsecurityjwt.exception;

public class UserNotFoundException extends RuntimeException  {
    public UserNotFoundException(String message) {
        super(message);
    }
}
