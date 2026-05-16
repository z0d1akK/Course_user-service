package com.zodiakk.userservice.common.validation;

public final class ValidationMessages {

    private ValidationMessages() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static final String FIELD_REQUIRED = "Field is required";
    public static final String SIZE_BETWEEN = "Must be between {min} and {max} characters";
    public static final String SIZE_MAX = "Must be less than {max} characters";

    public static final String USER_NAME_REQUIRED = "Name is required";
    public static final String USER_NAME_SIZE = "Name must be between 1 and 255 characters";
    public static final String USER_SURNAME_REQUIRED = "Surname is required";
    public static final String USER_SURNAME_SIZE = "Surname must be between 1 and 255 characters";
    public static final String USER_BIRTH_DATE_REQUIRED = "Birth date is required";
    public static final String USER_BIRTH_DATE_PAST = "Birth date must be in the past or current day";
    public static final String USER_EMAIL_REQUIRED = "Email is required";
    public static final String USER_EMAIL_VALID = "Email should be valid";
    public static final String USER_EMAIL_SIZE = "Email must be less than 255 characters";
    public static final String USER_NAME_FILTER_SIZE = "Name filter must be less than 255 characters";
    public static final String USER_SURNAME_FILTER_SIZE = "Surname filter must be less than 255 characters";

    public static final String CARD_NUMBER_REQUIRED = "Card number is required";
    public static final String CARD_NUMBER_PATTERN = "Card number must be exactly 16 digits";
    public static final String CARD_HOLDER_REQUIRED = "Card holder name is required";
    public static final String CARD_HOLDER_SIZE = "Card holder name must be between 1 and 255 characters";
    public static final String CARD_EXPIRATION_REQUIRED = "Expiration date is required";
    public static final String CARD_EXPIRATION_PATTERN = "Expiration date must be in format MM/YY";
}