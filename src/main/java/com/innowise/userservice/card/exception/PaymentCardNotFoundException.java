package com.innowise.userservice.card.exception;

import com.innowise.userservice.common.exception.ErrorMessages;
import com.innowise.userservice.common.exception.ResourceNotFoundException;

import java.util.UUID;

public class PaymentCardNotFoundException extends ResourceNotFoundException {

    public PaymentCardNotFoundException(UUID id) {
        super(ErrorMessages.PAYMENT_CARD_NOT_FOUND.formatted(id));
    }
}