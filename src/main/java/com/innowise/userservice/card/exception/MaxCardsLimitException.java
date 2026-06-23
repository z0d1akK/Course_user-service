package com.innowise.userservice.card.exception;

import com.innowise.userservice.common.exception.BusinessException;
import com.innowise.userservice.common.exception.ErrorMessages;

import java.util.UUID;

public class MaxCardsLimitException extends BusinessException {

    public MaxCardsLimitException(UUID userId) {
        super(ErrorMessages.MAX_CARDS_LIMIT.formatted(userId));
    }
}