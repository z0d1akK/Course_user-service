package com.innowise.userservice.card.exception;

import com.innowise.userservice.common.exception.BusinessException;

public class PaymentCardAlreadyExistsException extends BusinessException {

    public PaymentCardAlreadyExistsException() {
        super("Payment card with this number already exists for this user");
    }
}
