package com.innowise.userservice.user.exception;

import com.innowise.userservice.common.exception.BusinessException;
import com.innowise.userservice.common.exception.ErrorMessages;

public class UserAlreadyExistsException extends BusinessException {

    public UserAlreadyExistsException(String email) {
        super(ErrorMessages.USER_ALREADY_EXISTS.formatted(email));
    }
}
