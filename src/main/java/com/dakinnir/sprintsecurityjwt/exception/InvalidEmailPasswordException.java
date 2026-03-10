package com.dakinnir.sprintsecurityjwt.exception;

public class InvalidEmailPasswordException extends RuntimeException {
    public InvalidEmailPasswordException(String message) {
        super(message);
    }
}
