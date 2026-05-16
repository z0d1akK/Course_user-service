package com.zodiakk.userservice.user.exception;

import com.zodiakk.userservice.common.exception.BusinessException;
import com.zodiakk.userservice.common.exception.ErrorMessages;

public class UserAlreadyExistsException extends BusinessException {

    public UserAlreadyExistsException(String email) {
        super(ErrorMessages.USER_ALREADY_EXISTS.formatted(email));
    }
}
