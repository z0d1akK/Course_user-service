package com.innowise.userservice.user.controller;

import com.innowise.userservice.common.AbstractIntegrationTest;
import com.innowise.userservice.user.dto.request.CreateUserRequestDto;
import com.innowise.userservice.user.entity.User;
import com.innowise.userservice.user.repository.UserRepository;
import com.innowise.userservice.user.testclasses.UserTestDataFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserInternalControllerTest extends AbstractIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Value("${internal.api-key}")
    private String internalApiKey;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("create should create user successfully with valid internal key")
    void create_shouldCreateUserSuccessfullyWithValidInternalKey() throws Exception {
        CreateUserRequestDto request = UserTestDataFactory.createUserRequest();

        mockMvc.perform(post("/internal/users")
                        .header("X-Internal-Key", internalApiKey)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value(request.getEmail()))
                .andExpect(jsonPath("$.name").value(request.getName()))
                .andExpect(jsonPath("$.surname").value(request.getSurname()));
    }

    @Test
    @DisplayName("create should return 401 when internal key is missing")
    void create_shouldReturn401WhenInternalKeyMissing() throws Exception {
        CreateUserRequestDto request = UserTestDataFactory.createUserRequest();

        mockMvc.perform(post("/internal/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("Invalid or missing internal API key"));
    }

    @Test
    @DisplayName("create should return 401 when internal key is invalid")
    void create_shouldReturn401WhenInternalKeyInvalid() throws Exception {
        CreateUserRequestDto request = UserTestDataFactory.createUserRequest();

        mockMvc.perform(post("/internal/users")
                        .header("X-Internal-Key", "wrong-key")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("Invalid or missing internal API key"));
    }

    @Test
    @DisplayName("delete should delete user successfully with valid internal key")
    void delete_shouldDeleteUserSuccessfullyWithValidInternalKey() throws Exception {
        User user = userRepository.save(UserTestDataFactory.createUser());

        mockMvc.perform(delete("/internal/users/{id}", user.getId())
                        .header("X-Internal-Key", internalApiKey))
                .andExpect(status().isNoContent());

        assertThat(userRepository.existsById(user.getId())).isFalse();
    }

    @Test
    @DisplayName("delete should return 401 when internal key is missing")
    void delete_shouldReturn401WhenInternalKeyMissing() throws Exception {
        User user = userRepository.save(UserTestDataFactory.createUser());

        mockMvc.perform(delete("/internal/users/{id}", user.getId()))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("Invalid or missing internal API key"));
    }

    @Test
    @DisplayName("delete should return 404 when user not found")
    void delete_shouldReturn404WhenUserNotFound() throws Exception {
        mockMvc.perform(delete("/internal/users/{id}", UUID.randomUUID())
                        .header("X-Internal-Key", internalApiKey))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("create should return 400 when request is invalid")
    void create_shouldReturn400WhenRequestInvalid() throws Exception {
        CreateUserRequestDto request = CreateUserRequestDto.builder()
                .email("invalid-email")
                .name("")
                .surname("")
                .build();

        mockMvc.perform(post("/internal/users")
                        .header("X-Internal-Key", internalApiKey)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}