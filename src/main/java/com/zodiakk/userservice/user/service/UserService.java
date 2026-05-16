package com.zodiakk.userservice.user.service;

import com.zodiakk.userservice.user.dto.request.CreateUserRequestDto;
import com.zodiakk.userservice.user.dto.request.UpdateUserRequestDto;
import com.zodiakk.userservice.user.dto.request.UserFilterRequestDto;
import com.zodiakk.userservice.user.dto.response.UserResponseDto;
import com.zodiakk.userservice.user.dto.response.UserShortResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface UserService {

    UserResponseDto create(CreateUserRequestDto request);

    UserResponseDto getById(UUID id);

    UserShortResponseDto getShortById(UUID id);

    Page<UserShortResponseDto> getAllShort(UserFilterRequestDto filter, Pageable pageable);

    Page<UserResponseDto> getAll(UserFilterRequestDto filter, Pageable pageable);

    UserResponseDto update(UUID id, UpdateUserRequestDto request);

    void activate(UUID id);

    void deactivate(UUID id);
}