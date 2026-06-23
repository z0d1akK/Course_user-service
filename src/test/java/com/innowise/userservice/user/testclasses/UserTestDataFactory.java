package com.innowise.userservice.user.testclasses;

import com.innowise.userservice.user.dto.request.CreateUserRequestDto;
import com.innowise.userservice.user.dto.request.UpdateUserRequestDto;
import com.innowise.userservice.user.dto.request.UserFilterRequestDto;
import com.innowise.userservice.user.dto.response.UserResponseDto;
import com.innowise.userservice.user.dto.response.UserShortResponseDto;
import com.innowise.userservice.user.entity.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

public final class UserTestDataFactory {

    private UserTestDataFactory() {
    }

    public static CreateUserRequestDto createUserRequest() {
        return CreateUserRequestDto.builder()
                .name("test")
                .surname("test")
                .birthDate(LocalDate.of(2000, 1, 1))
                .email("test@gmail.com")
                .build();
    }

    public static UpdateUserRequestDto updateUserRequest() {
        return UpdateUserRequestDto.builder()
                .name("updatedTest")
                .surname("updatedTest")
                .birthDate(LocalDate.of(2001, 1, 1))
                .build();
    }

    public static UserFilterRequestDto createUserFilterRequest() {
        return UserFilterRequestDto.builder()
                .name("test")
                .surname("test")
                .active(true)
                .build();
    }

    public static User createUser() {
        return User.builder()
                .name("test")
                .surname("test")
                .birthDate(LocalDate.of(2000, 1, 1))
                .email("test@gmail.com")
                .active(true)
                .paymentCards(new ArrayList<>())
                .build();
    }

    public static User createUpdatedUser(UUID id) {
        return User.builder()
                .id(id)
                .name("updatedTest")
                .surname("updatedTest")
                .birthDate(LocalDate.of(2001, 1, 1))
                .email("test@gmail.com")
                .active(true)
                .paymentCards(new ArrayList<>())
                .build();
    }

    public static UserResponseDto createUserResponse() {
        return createUserResponse(UUID.randomUUID());
    }

    public static UserResponseDto createUserResponse(UUID id) {
        return UserResponseDto.builder()
                .id(id)
                .name("test")
                .surname("test")
                .birthDate(LocalDate.of(2000, 1, 1))
                .email("test@gmail.com")
                .active(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .paymentCards(new ArrayList<>())
                .build();
    }

    public static UserResponseDto createUpdatedUserResponse(UUID id) {
        return UserResponseDto.builder()
                .id(id)
                .name("updatedTest")
                .surname("updatedTest")
                .birthDate(LocalDate.of(2001, 1, 1))
                .email("test@gmail.com")
                .active(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .paymentCards(new ArrayList<>())
                .build();
    }

    public static UserShortResponseDto createUserShortResponse() {
        return createUserShortResponse(UUID.randomUUID());
    }

    public static UserShortResponseDto createUserShortResponse(UUID id) {
        return UserShortResponseDto.builder()
                .id(id)
                .name("test")
                .surname("test")
                .email("test@gmail.com")
                .active(true)
                .build();
    }
}