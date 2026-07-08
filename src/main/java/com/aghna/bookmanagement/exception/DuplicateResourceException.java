package com.aghna.bookmanagement.exception;

// DuplicateResourceException -> 409
public class DuplicateResourceException extends RuntimeException {

    public DuplicateResourceException(String message) {
        super(message);
    }
}
