package com.innowise.userservice.user.service;

import com.innowise.userservice.user.dto.request.CreateUserRequestDto;
import com.innowise.userservice.user.dto.request.UpdateUserRequestDto;
import com.innowise.userservice.user.dto.request.UserFilterRequestDto;
import com.innowise.userservice.user.dto.response.UserResponseDto;
import com.innowise.userservice.user.dto.response.UserShortResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface UserService {

    /**
     * Creates a new user.
     *
     * @param request request object containing user creation data
     * @return created user response
     */
    UserResponseDto create(CreateUserRequestDto request);

    /**
     * Returns full user information by identifier.
     *
     * @param id user identifier
     * @return full user response
     */
    UserResponseDto getById(UUID id);

    /**
     * Returns short user information by identifier.
     * <p>
     * Short response contains reduced set of fields
     * compared to the full user response.
     *
     * @param id user identifier
     * @return short user response
     */
    UserShortResponseDto getShortById(UUID id);

    /**
     * Returns paginated list of users in short representation.
     * <p>
     * Supports filtering and pagination.
     *
     * @param filter filtering parameters
     * @param pageable pagination information
     * @return page of short user responses
     */
    Page<UserShortResponseDto> getAllShort(UserFilterRequestDto filter, Pageable pageable);

    /**
     * Returns paginated list of users in full representation.
     * <p>
     * Supports filtering and pagination.
     *
     * @param filter filtering parameters
     * @param pageable pagination information
     * @return page of full user responses
     */
    Page<UserResponseDto> getAll(UserFilterRequestDto filter, Pageable pageable);

    /**
     * Updates existing user data.
     *
     * @param id user identifier
     * @param request request object containing updated user data
     * @return updated user response
     */
    UserResponseDto update(UUID id, UpdateUserRequestDto request);

    /**
     * Activates user account.
     *
     * @param id user identifier
     */
    void activate(UUID id);

    /**
     * Deactivates user account.
     *
     * @param id user identifier
     */
    void deactivate(UUID id);

    /**
     * Deletes user by identifier.
     *
     * @param id user identifier
     */
    void delete(UUID id);
}