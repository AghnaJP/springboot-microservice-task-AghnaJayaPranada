package com.aghna.bookmanagement.exception;

// ResourceNotFoundException -> 404
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
