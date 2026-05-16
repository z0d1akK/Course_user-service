package com.zodiakk.userservice.card.exception;

import com.zodiakk.userservice.common.exception.ErrorMessages;
import com.zodiakk.userservice.common.exception.ResourceNotFoundException;

import java.util.UUID;

public class PaymentCardNotFoundException extends ResourceNotFoundException {

    public PaymentCardNotFoundException(UUID id) {
        super(ErrorMessages.PAYMENT_CARD_NOT_FOUND.formatted(id));
    }
}