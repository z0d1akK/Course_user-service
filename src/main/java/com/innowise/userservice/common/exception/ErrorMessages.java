package com.innowise.userservice.common.exception;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ErrorMessages {

    public static final String USER_NOT_FOUND = "User not found with id: %s";

    public static final String USER_ALREADY_EXISTS = "User already exists with email: %s";

    public static final String PAYMENT_CARD_NOT_FOUND = "Payment card not found with id: %s";

    public static final String MAX_CARDS_LIMIT = "User with id %s already has maximum allowed cards";

    public static final String UNAUTHORIZED = "Unauthorized";

    public static final String FORBIDDEN = "Access denied";

    public static final String INVALID_GATEWAY_KEY = "Invalid Gateway Key";
}