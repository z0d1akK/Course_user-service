package com.innowise.userservice.card.testclasses;

import com.innowise.userservice.card.dto.request.CreatePaymentCardRequestDto;
import com.innowise.userservice.card.dto.request.UpdatePaymentCardRequestDto;
import com.innowise.userservice.card.dto.response.PaymentCardResponseDto;
import com.innowise.userservice.card.dto.response.PaymentCardShortResponseDto;
import com.innowise.userservice.card.entity.PaymentCard;
import com.innowise.userservice.user.entity.User;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.innowise.userservice.user.testclasses.UserTestDataFactory.createUserShortResponse;

public final class PaymentCardTestDataFactory {

    private PaymentCardTestDataFactory() {
    }

    public static CreatePaymentCardRequestDto createPaymentCardRequest() {
        return CreatePaymentCardRequestDto.builder()
                .number("1111222233334444")
                .holder("test test")
                .expirationDate("11/11")
                .build();
    }

    public static UpdatePaymentCardRequestDto updatePaymentCardRequest() {
        return UpdatePaymentCardRequestDto.builder()
                .number("5555666677778888")
                .holder("updated test")
                .expirationDate("12/12")
                .build();
    }

    public static PaymentCard createPaymentCard(User user) {
        return PaymentCard.builder()
                .user(user)
                .number("1111222233334444")
                .holder("test test")
                .expirationDate("11/11")
                .active(true)
                .build();
    }

    public static PaymentCard createUpdatedPaymentCard(User user) {
        return PaymentCard.builder()
                .user(user)
                .number("5555666677778888")
                .holder("updated test")
                .expirationDate("12/12")
                .active(true)
                .build();
    }

    public static PaymentCardResponseDto createPaymentCardResponse() {
        return createPaymentCardResponse(UUID.randomUUID());
    }

    public static PaymentCardResponseDto createPaymentCardResponse(UUID id) {
        return PaymentCardResponseDto.builder()
                .id(id)
                .number("1111222233334444")
                .holder("test test")
                .expirationDate("11/11")
                .active(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .user(createUserShortResponse())
                .build();
    }

    public static PaymentCardResponseDto createUpdatedPaymentCardResponse() {
        return PaymentCardResponseDto.builder()
                .id(UUID.randomUUID())
                .number("5555666677778888")
                .holder("updated test")
                .expirationDate("12/12")
                .active(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .user(createUserShortResponse())
                .build();
    }

    public static PaymentCardShortResponseDto createPaymentCardShortResponse() {
        return createPaymentCardShortResponse(UUID.randomUUID());
    }

    public static PaymentCardShortResponseDto createPaymentCardShortResponse(UUID id) {
        return PaymentCardShortResponseDto.builder()
                .id(id)
                .number("1111222233334444")
                .expirationDate("11/11")
                .active(true)
                .build();
    }
}