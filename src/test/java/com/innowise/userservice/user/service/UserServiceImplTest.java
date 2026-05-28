package com.innowise.userservice.user.service;

import com.innowise.userservice.user.dto.request.CreateUserRequestDto;
import com.innowise.userservice.user.dto.request.UpdateUserRequestDto;
import com.innowise.userservice.user.dto.request.UserFilterRequestDto;
import com.innowise.userservice.user.dto.response.UserResponseDto;
import com.innowise.userservice.user.dto.response.UserShortResponseDto;
import com.innowise.userservice.user.entity.User;
import com.innowise.userservice.user.exception.UserAlreadyExistsException;
import com.innowise.userservice.user.exception.UserNotFoundException;
import com.innowise.userservice.user.mapper.UserMapper;
import com.innowise.userservice.user.repository.UserRepository;
import com.innowise.userservice.user.service.impl.UserServiceImpl;
import com.innowise.userservice.user.testclasses.UserTestDataFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    @DisplayName("Should create user successfully")
    void create_shouldCreateUserSuccessfully() {
        CreateUserRequestDto request = UserTestDataFactory.createUserRequest();
        User user = UserTestDataFactory.createUser();

        User savedUser = UserTestDataFactory.createUser();
        user.setId(UUID.randomUUID());

        UserResponseDto response = UserTestDataFactory.createUserResponse();

        when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(userMapper.toEntity(request)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(savedUser);
        when(userMapper.toResponse(savedUser)).thenReturn(response);

        UserResponseDto actual = userService.create(request);

        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isEqualTo(response.getId());
        assertThat(actual.getEmail()).isEqualTo(response.getEmail());

        verify(userRepository).existsByEmail(request.getEmail());
        verify(userMapper).toEntity(request);
        verify(userRepository).save(user);
        verify(userMapper).toResponse(savedUser);
    }

    @Test
    @DisplayName("Should throw exception when creating user with existing email")
    void create_shouldThrowExceptionWhenEmailAlreadyExists() {
        CreateUserRequestDto request = UserTestDataFactory.createUserRequest();

        when(userRepository.existsByEmail(request.getEmail())).thenReturn(true);

        assertThatThrownBy(() -> userService.create(request))
                .isInstanceOf(UserAlreadyExistsException.class)
                .hasMessageContaining(request.getEmail());

        verify(userRepository).existsByEmail(request.getEmail());
        verify(userMapper, never()).toEntity(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should return user by id")
    void getById_shouldReturnUser() {
        UUID userId = UUID.randomUUID();

        User user = UserTestDataFactory.createUser();
        user.setId(userId);

        UserResponseDto response = UserTestDataFactory.createUserResponse(userId);

        when(userRepository.findByIdWithCards(userId)).thenReturn(Optional.of(user));
        when(userMapper.toResponse(user)).thenReturn(response);

        UserResponseDto actual = userService.getById(userId);

        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isEqualTo(userId);

        verify(userRepository).findByIdWithCards(userId);
        verify(userMapper).toResponse(user);
    }

    @Test
    @DisplayName("Should throw exception when user not found by id")
    void getById_shouldThrowExceptionWhenUserNotFound() {
        UUID userId = UUID.randomUUID();

        when(userRepository.findByIdWithCards(userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getById(userId))
                .isInstanceOf(UserNotFoundException.class);

        verify(userRepository).findByIdWithCards(userId);
        verify(userMapper, never()).toResponse(any());
    }

    @Test
    @DisplayName("Should return short user by id")
    void getShortById_shouldReturnShortUser() {
        UUID userId = UUID.randomUUID();

        User user = UserTestDataFactory.createUser();
        user.setId(userId);

        UserShortResponseDto response = UserTestDataFactory.createUserShortResponse(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userMapper.toShortResponse(user)).thenReturn(response);

        UserShortResponseDto actual = userService.getShortById(userId);

        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isEqualTo(userId);

        verify(userRepository).findById(userId);
        verify(userMapper).toShortResponse(user);
    }

    @Test
    @DisplayName("Should throw exception when short user not found")
    void getShortById_shouldThrowExceptionWhenUserNotFound() {
        UUID userId = UUID.randomUUID();

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getShortById(userId))
                .isInstanceOf(UserNotFoundException.class);

        verify(userRepository).findById(userId);
        verify(userMapper, never()).toShortResponse(any());
    }

    @Test
    @DisplayName("Should return all short users")
    void getAllShort_shouldReturnPageOfShortUsers() {
        UserFilterRequestDto filter = UserTestDataFactory.createUserFilterRequest();

        Pageable pageable = PageRequest.of(0, 10);

        User user = UserTestDataFactory.createUser();
        user.setId(UUID.randomUUID());

        Page<User> usersPage = new PageImpl<>(List.of(user));

        UserShortResponseDto shortResponse = UserTestDataFactory.createUserShortResponse();

        when(userRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(usersPage);
        when(userMapper.toShortResponse(user)).thenReturn(shortResponse);

        Page<UserShortResponseDto> actual = userService.getAllShort(filter, pageable);

        assertThat(actual).isNotNull();
        assertThat(actual.getContent()).hasSize(1);

        verify(userRepository).findAll(any(Specification.class), eq(pageable));
        verify(userMapper).toShortResponse(user);
    }

    @Test
    @DisplayName("Should return all users")
    void getAll_shouldReturnPageOfUsers() {
        UserFilterRequestDto filter = UserTestDataFactory.createUserFilterRequest();

        Pageable pageable = PageRequest.of(0, 10);

        User user = UserTestDataFactory.createUser();
        user.setId(UUID.randomUUID());

        Page<User> usersPage = new PageImpl<>(List.of(user));

        UserResponseDto response = UserTestDataFactory.createUserResponse();

        when(userRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(usersPage);
        when(userMapper.toResponse(user)).thenReturn(response);

        Page<UserResponseDto> actual = userService.getAll(filter, pageable);

        assertThat(actual).isNotNull();
        assertThat(actual.getContent()).hasSize(1);

        verify(userRepository).findAll(any(Specification.class), eq(pageable));
        verify(userMapper).toResponse(user);
    }

    @Test
    @DisplayName("Should update user successfully")
    void update_shouldUpdateUserSuccessfully() {
        UUID userId = UUID.randomUUID();

        UpdateUserRequestDto request = UserTestDataFactory.updateUserRequest();

        User user = UserTestDataFactory.createUser();
        user.setId(userId);

        User updatedUser = UserTestDataFactory.createUpdatedUser(userId);

        UserResponseDto response = UserTestDataFactory.createUpdatedUserResponse(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(updatedUser);
        when(userMapper.toResponse(updatedUser)).thenReturn(response);

        UserResponseDto actual = userService.update(userId, request);

        assertThat(actual).isNotNull();
        assertThat(actual.getName()).isEqualTo(response.getName());

        verify(userRepository).findById(userId);
        verify(userMapper).updateToEntity(request, user);
        verify(userRepository).save(user);
        verify(userMapper).toResponse(updatedUser);
    }

    @Test
    @DisplayName("Should throw exception when updating nonexistent user")
    void update_shouldThrowExceptionWhenUserNotFound() {
        UUID userId = UUID.randomUUID();

        UpdateUserRequestDto request = UserTestDataFactory.updateUserRequest();

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.update(userId, request))
                .isInstanceOf(UserNotFoundException.class);

        verify(userRepository).findById(userId);
        verify(userMapper, never()).updateToEntity(any(), any());
        verify(userRepository, never()).save(any());
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    @DisplayName("Should activate or deactivate user")
    void activateDeactivate_shouldWorkCorrectly(boolean activate) {
        UUID userId = UUID.randomUUID();

        User user = UserTestDataFactory.createUser();
        user.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        if (activate) {
            userService.activate(userId);

            assertThat(user.getActive()).isTrue();
        } else {
            userService.deactivate(userId);

            assertThat(user.getActive()).isFalse();
        }

        verify(userRepository).findById(userId);
        verify(userRepository).save(user);
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    @DisplayName("Should throw exception when activate or deactivate nonexistent user")
    void activateDeactivate_shouldThrowExceptionWhenUserNotFound(boolean activate) {
        UUID userId = UUID.randomUUID();

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        if (activate) {
            assertThatThrownBy(() -> userService.activate(userId))
                    .isInstanceOf(UserNotFoundException.class);
        } else {
            assertThatThrownBy(() -> userService.deactivate(userId))
                    .isInstanceOf(UserNotFoundException.class);
        }

        verify(userRepository).findById(userId);
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should delete user successfully")
    void delete_shouldDeleteUserSuccessfully() {
        UUID userId = UUID.randomUUID();

        User user = UserTestDataFactory.createUser();
        user.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        userService.delete(userId);

        verify(userRepository).findById(userId);
        verify(userRepository).delete(user);
    }

    @Test
    @DisplayName("Should throw exception when deleting nonexistent user")
    void delete_shouldThrowExceptionWhenUserNotFound() {
        UUID userId = UUID.randomUUID();

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.delete(userId))
                .isInstanceOf(UserNotFoundException.class);

        verify(userRepository).findById(userId);
        verify(userRepository, never()).delete((User) any());
    }
}