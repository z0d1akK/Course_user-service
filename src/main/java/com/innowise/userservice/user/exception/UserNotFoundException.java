package com.innowise.userservice.user.exception;

import com.innowise.userservice.common.exception.ErrorMessages;
import com.innowise.userservice.common.exception.ResourceNotFoundException;

import java.util.UUID;

public class UserNotFoundException extends ResourceNotFoundException {

    public UserNotFoundException(UUID id) {
        super(ErrorMessages.USER_NOT_FOUND.formatted(id));
    }
}
