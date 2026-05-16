package com.zodiakk.userservice.user.exception;

import com.zodiakk.userservice.common.exception.ErrorMessages;
import com.zodiakk.userservice.common.exception.ResourceNotFoundException;

import java.util.UUID;

public class UserNotFoundException extends ResourceNotFoundException {

    public UserNotFoundException(UUID id) {
        super(ErrorMessages.USER_NOT_FOUND.formatted(id));
    }
}
