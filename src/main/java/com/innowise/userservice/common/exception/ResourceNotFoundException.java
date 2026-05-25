package com.innowise.userservice.common.exception;

public abstract class ResourceNotFoundException extends RuntimeException {

    protected ResourceNotFoundException(String message) {
        super(message);
    }
}
