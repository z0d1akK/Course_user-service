package com.zodiakk.userservice.user.controller;

import com.zodiakk.userservice.common.AbstractIntegrationTest;
import com.zodiakk.userservice.user.dto.request.CreateUserRequestDto;
import com.zodiakk.userservice.user.dto.request.UpdateUserRequestDto;
import com.zodiakk.userservice.user.entity.User;
import com.zodiakk.userservice.user.repository.UserRepository;
import com.zodiakk.userservice.user.testclasses.UserTestDataFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserControllerTest extends AbstractIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("create should create user successfully")
    void create_shouldCreateUserSuccessfully() throws Exception {
        CreateUserRequestDto request = UserTestDataFactory.createUserRequest();

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value(request.getEmail()))
                .andExpect(jsonPath("$.name").value(request.getName()))
                .andExpect(jsonPath("$.surname").value(request.getSurname()));
    }

    @Test
    @DisplayName("getById should return user successfully")
    void getById_shouldReturnUserSuccessfully() throws Exception {
        User user = userRepository.save(UserTestDataFactory.createUser());

        mockMvc.perform(get("/api/users/{id}", user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user.getId().toString()))
                .andExpect(jsonPath("$.email").value(user.getEmail()));
    }

    @Test
    @DisplayName("getById should return 404 when user not found")
    void getById_shouldReturn404WhenUserNotFound() throws Exception {
        mockMvc.perform(get("/api/users/{id}", UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("getShortById should return short user successfully")
    void getShortById_shouldReturnShortUserSuccessfully() throws Exception {
        User user = userRepository.save(UserTestDataFactory.createUser());

        mockMvc.perform(get("/api/users/{id}/short", user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user.getId().toString()))
                .andExpect(jsonPath("$.email").value(user.getEmail()));
    }

    @Test
    @DisplayName("getShortById should return 404 when user not found")
    void getShortById_shouldReturn404WhenUserNotFound() throws Exception {
        mockMvc.perform(get("/api/users/{id}/short", UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("getAll should return users page")
    void getAll_shouldReturnUsersPage() throws Exception {
        userRepository.save(UserTestDataFactory.createUser());

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)));
    }

    @Test
    @DisplayName("getAllShort should return short users page")
    void getAllShort_shouldReturnShortUsersPage() throws Exception {
        userRepository.save(UserTestDataFactory.createUser());

        mockMvc.perform(get("/api/users/short"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)));
    }

    @Test
    @DisplayName("update should update user successfully")
    void update_shouldUpdateUserSuccessfully() throws Exception {
        User user = userRepository.save(UserTestDataFactory.createUser());

        UpdateUserRequestDto request = UserTestDataFactory.updateUserRequest();

        mockMvc.perform(patch("/api/users/{id}", user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(request.getName()))
                .andExpect(jsonPath("$.surname").value(request.getSurname()));

        User updatedUser = userRepository.findById(user.getId()).orElseThrow();

        assertThat(updatedUser.getName()).isEqualTo(request.getName());
        assertThat(updatedUser.getSurname()).isEqualTo(request.getSurname());
    }

    @Test
    @DisplayName("update should return 404 when user not found")
    void update_shouldReturn404WhenUserNotFound() throws Exception {
        UpdateUserRequestDto request = UserTestDataFactory.updateUserRequest();

        mockMvc.perform(patch("/api/users/{id}", UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("update should return 400 when request invalid")
    void update_shouldReturn400WhenRequestInvalid() throws Exception {
        User user = userRepository.save(UserTestDataFactory.createUser());

        UpdateUserRequestDto request = UpdateUserRequestDto.builder()
                .name("")
                .surname("")
                .build();

        mockMvc.perform(patch("/api/users/{id}", user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }


    @Test
    @DisplayName("delete should delete user successfully")
    void delete_shouldDeleteUserSuccessfully() throws Exception {
        User user = userRepository.save(UserTestDataFactory.createUser());

        mockMvc.perform(delete("/api/users/{id}", user.getId()))
                .andExpect(status().isNoContent());

        assertThat(userRepository.existsById(user.getId())).isFalse();
    }

    @Test
    @DisplayName("delete should return 404 when user not found")
    void delete_shouldReturn404WhenUserNotFound() throws Exception {
        mockMvc.perform(delete("/api/users/{id}", UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("activate should activate user")
    void activate_shouldActivateUser() throws Exception {
        User user = UserTestDataFactory.createUser();
        user.setActive(false);

        User savedUser = userRepository.save(user);

        mockMvc.perform(patch("/api/users/{id}/activate", savedUser.getId()))
                .andExpect(status().isNoContent());

        User updatedUser = userRepository.findById(savedUser.getId()).orElseThrow();

        assertThat(updatedUser.getActive()).isTrue();
    }

    @Test
    @DisplayName("activate should return 404 when user not found")
    void activate_shouldReturn404WhenUserNotFound() throws Exception {
        mockMvc.perform(patch("/api/users/{id}/activate", UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("deactivate should deactivate user")
    void deactivate_shouldDeactivateUser() throws Exception {
        User user = UserTestDataFactory.createUser();

        User savedUser = userRepository.save(user);

        mockMvc.perform(patch("/api/users/{id}/deactivate", savedUser.getId()))
                .andExpect(status().isNoContent());

        User updatedUser = userRepository.findById(savedUser.getId()).orElseThrow();

        assertThat(updatedUser.getActive()).isFalse();
    }

    @Test
    @DisplayName("deactivate should return 404 when user not found")
    void deactivate_shouldReturn404WhenUserNotFound() throws Exception {
        mockMvc.perform(patch("/api/users/{id}/deactivate", UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }
}