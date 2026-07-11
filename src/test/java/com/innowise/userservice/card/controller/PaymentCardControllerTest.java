package com.innowise.userservice.card.controller;

import com.innowise.userservice.card.dto.request.CreatePaymentCardRequestDto;
import com.innowise.userservice.card.dto.request.UpdatePaymentCardRequestDto;
import com.innowise.userservice.card.entity.PaymentCard;
import com.innowise.userservice.card.repository.PaymentCardRepository;
import com.innowise.userservice.card.testclasses.PaymentCardTestDataFactory;
import com.innowise.userservice.common.AbstractIntegrationTest;
import com.innowise.userservice.common.annotation.WithMockCustomUser;
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
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class PaymentCardControllerTest extends AbstractIntegrationTest {

    @Autowired
    private PaymentCardRepository paymentCardRepository;

    @Autowired
    private UserRepository userRepository;

    @Value("${gateway.api-key}")
    private String gatewayApiKey;

    @BeforeEach
    void setUp() {
        paymentCardRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("create should create payment card successfully")
    @WithMockCustomUser(role = "ROLE_ADMIN")
    void create_shouldCreatePaymentCardSuccessfully() throws Exception {
        User user = userRepository.save(UserTestDataFactory.createUser());

        CreatePaymentCardRequestDto request = PaymentCardTestDataFactory.createPaymentCardRequest();

        mockMvc.perform(post("/api/users/{userId}/payment-cards", user.getId())
                        .header("X-Gateway-Key", gatewayApiKey)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.number").value(request.getNumber()))
                .andExpect(jsonPath("$.holder").value(request.getHolder()))
                .andExpect(jsonPath("$.expirationDate").value(request.getExpirationDate()));
    }

    @Test
    @DisplayName("create should return 404 when user not found")
    @WithMockCustomUser(role = "ROLE_ADMIN")
    void create_shouldReturn404WhenUserNotFound() throws Exception {
        CreatePaymentCardRequestDto request = PaymentCardTestDataFactory.createPaymentCardRequest();

        mockMvc.perform(post("/api/users/{userId}/payment-cards", UUID.randomUUID())
                        .header("X-Gateway-Key", gatewayApiKey)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("create should return 400 when request invalid")
    @WithMockCustomUser
    void create_shouldReturn400WhenRequestInvalid() throws Exception {
        User user = userRepository.save(UserTestDataFactory.createUser());

        CreatePaymentCardRequestDto request = CreatePaymentCardRequestDto.builder()
                        .number("invalid")
                        .holder("")
                        .expirationDate("99/99")
                        .build();

        mockMvc.perform(post("/api/users/{userId}/payment-cards", user.getId())
                        .header("X-Gateway-Key", gatewayApiKey)
                        .param("userId", user.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("getById should return payment card successfully")
    @WithMockCustomUser(role = "ROLE_ADMIN")
    void getById_shouldReturnPaymentCardSuccessfully() throws Exception {
        User user = userRepository.save(UserTestDataFactory.createUser());

        PaymentCard card = PaymentCardTestDataFactory.createPaymentCard(user);

        PaymentCard savedCard = paymentCardRepository.save(card);

        mockMvc.perform(get("/api/payment-cards/{id}", savedCard.getId())
                        .header("X-Gateway-Key", gatewayApiKey))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedCard.getId().toString()))
                .andExpect(jsonPath("$.number").value(savedCard.getNumber()));
    }

    @Test
    @DisplayName("getById should return 404 when payment card not found")
    @WithMockCustomUser(role = "ROLE_ADMIN")
    void getById_shouldReturn404WhenPaymentCardNotFound() throws Exception {
        mockMvc.perform(get("/api/payment-cards/{id}", UUID.randomUUID())
                        .header("X-Gateway-Key", gatewayApiKey))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("getShortById should return short payment card successfully")
    @WithMockCustomUser(role = "ROLE_ADMIN")
    void getShortById_shouldReturnShortPaymentCardSuccessfully() throws Exception {
        User user = userRepository.save(UserTestDataFactory.createUser());

        PaymentCard card = PaymentCardTestDataFactory.createPaymentCard(user);

        PaymentCard savedCard = paymentCardRepository.save(card);

        mockMvc.perform(get("/api/payment-cards/{id}/short", savedCard.getId())
                        .header("X-Gateway-Key", gatewayApiKey))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedCard.getId().toString()))
                .andExpect(jsonPath("$.number").value(savedCard.getNumber()));
    }

    @Test
    @DisplayName("getShortById should return 404 when payment card not found")
    @WithMockCustomUser(role = "ROLE_ADMIN")
    void getShortById_shouldReturn404WhenPaymentCardNotFound() throws Exception {
        mockMvc.perform(get("/api/payment-cards/{id}/short", UUID.randomUUID())
                        .header("X-Gateway-Key", gatewayApiKey))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("getAll should return payment cards page")
    @WithMockCustomUser(role = "ROLE_ADMIN")
    void getAll_shouldReturnPaymentCardsPage() throws Exception {
        User user = userRepository.save(UserTestDataFactory.createUser());

        paymentCardRepository.save(PaymentCardTestDataFactory.createPaymentCard(user));

        mockMvc.perform(get("/api/payment-cards")
                        .header("X-Gateway-Key", gatewayApiKey))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)));
    }

    @Test
    @DisplayName("getAllShort should return short payment cards page")
    @WithMockCustomUser(role = "ROLE_ADMIN")
    void getAllShort_shouldReturnShortPaymentCardsPage() throws Exception {
        User user = userRepository.save(UserTestDataFactory.createUser());

        paymentCardRepository.save(PaymentCardTestDataFactory.createPaymentCard(user));

        mockMvc.perform(get("/api/payment-cards/short")
                        .header("X-Gateway-Key", gatewayApiKey))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)));
    }

    @Test
    @DisplayName("getAllByUserId should return payment cards successfully")
    @WithMockCustomUser(role = "ROLE_ADMIN")
    void getAllByUserId_shouldReturnPaymentCardsSuccessfully() throws Exception {
        User user = userRepository.save(UserTestDataFactory.createUser());

        paymentCardRepository.save(PaymentCardTestDataFactory.createPaymentCard(user));

        mockMvc.perform(get("/api/users/{userId}/payment-cards", user.getId())
                        .header("X-Gateway-Key", gatewayApiKey))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    @DisplayName("getAllByUserId should return 404 when user not found")
    @WithMockCustomUser(role = "ROLE_ADMIN")
    void getAllByUserId_shouldReturn404WhenUserNotFound() throws Exception {
        mockMvc.perform(get("/api/users/{userId}/payment-cards", UUID.randomUUID())
                        .header("X-Gateway-Key", gatewayApiKey))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("getAllShortByUserId should return short payment cards successfully")
    @WithMockCustomUser(role = "ROLE_ADMIN")
    void getAllShortByUserId_shouldReturnShortPaymentCardsSuccessfully() throws Exception {
        User user = userRepository.save(UserTestDataFactory.createUser());

        paymentCardRepository.save(PaymentCardTestDataFactory.createPaymentCard(user));

        mockMvc.perform(get("/api/users/{userId}/payment-cards/short", user.getId())
                        .header("X-Gateway-Key", gatewayApiKey))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    @DisplayName("getAllShortByUserId should return 404 when user not found")
    @WithMockCustomUser(role = "ROLE_ADMIN")
    void getAllShortByUserId_shouldReturn404WhenUserNotFound() throws Exception {
        mockMvc.perform(get("/api/users/{userId}/payment-cards/short", UUID.randomUUID())
                        .header("X-Gateway-Key", gatewayApiKey))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("update should update payment card successfully")
    @WithMockCustomUser(role = "ROLE_ADMIN")
    void update_shouldUpdatePaymentCardSuccessfully() throws Exception {
        User user = userRepository.save(UserTestDataFactory.createUser());

        PaymentCard savedCard = paymentCardRepository.save(PaymentCardTestDataFactory.createPaymentCard(user));

        UpdatePaymentCardRequestDto request = PaymentCardTestDataFactory.updatePaymentCardRequest();

        mockMvc.perform(patch("/api/payment-cards/{id}", savedCard.getId())
                        .header("X-Gateway-Key", gatewayApiKey)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.number").value(request.getNumber()))
                .andExpect(jsonPath("$.holder").value(request.getHolder()));
    }

    @Test
    @DisplayName("update should return 404 when payment card not found")
    @WithMockCustomUser(role = "ROLE_ADMIN")
    void update_shouldReturn404WhenPaymentCardNotFound() throws Exception {
        UpdatePaymentCardRequestDto request = PaymentCardTestDataFactory.updatePaymentCardRequest();

        mockMvc.perform(patch("/api/payment-cards/{id}", UUID.randomUUID())
                        .header("X-Gateway-Key", gatewayApiKey)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("update should return 400 when request invalid")
    @WithMockCustomUser
    void update_shouldReturn400WhenRequestInvalid() throws Exception {
        User user = userRepository.save(UserTestDataFactory.createUser());

        PaymentCard savedCard = paymentCardRepository.save(PaymentCardTestDataFactory.createPaymentCard(user));

        UpdatePaymentCardRequestDto request = UpdatePaymentCardRequestDto.builder()
                .number("invalid")
                .holder("")
                .expirationDate("99/99")
                .build();

        mockMvc.perform(patch("/api/payment-cards/{id}", savedCard.getId())
                        .header("X-Gateway-Key", gatewayApiKey)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }


    @Test
    @DisplayName("activate should activate payment card")
    @WithMockCustomUser(role = "ROLE_ADMIN")
    void activate_shouldActivatePaymentCard() throws Exception {
        User user = userRepository.save(UserTestDataFactory.createUser());

        PaymentCard card = PaymentCardTestDataFactory.createPaymentCard(user);
        card.setActive(false);

        PaymentCard savedCard = paymentCardRepository.save(card);

        mockMvc.perform(patch("/api/payment-cards/{id}/activate", savedCard.getId())
                        .header("X-Gateway-Key", gatewayApiKey))
                .andExpect(status().isNoContent());

        PaymentCard updatedCard = paymentCardRepository.findById(savedCard.getId()).orElseThrow();

        assertThat(updatedCard.getActive()).isTrue();
    }

    @Test
    @DisplayName("activate should return 404 when payment card not found")
    @WithMockCustomUser(role = "ROLE_ADMIN")
    void activate_shouldReturn404WhenPaymentCardNotFound() throws Exception {
        mockMvc.perform(patch("/api/payment-cards/{id}/activate", UUID.randomUUID())
                        .header("X-Gateway-Key", gatewayApiKey))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("deactivate should deactivate payment card")
    @WithMockCustomUser(role = "ROLE_ADMIN")
    void deactivate_shouldDeactivatePaymentCard() throws Exception {
        User user = userRepository.save(UserTestDataFactory.createUser());

        PaymentCard savedCard = paymentCardRepository.save(PaymentCardTestDataFactory.createPaymentCard(user));

        mockMvc.perform(patch("/api/payment-cards/{id}/deactivate", savedCard.getId())
                        .header("X-Gateway-Key", gatewayApiKey))
                .andExpect(status().isNoContent());

        PaymentCard updatedCard = paymentCardRepository.findById(savedCard.getId()).orElseThrow();

        assertThat(updatedCard.getActive()).isFalse();
    }

    @Test
    @DisplayName("deactivate should return 404 when payment card not found")
    @WithMockCustomUser(role = "ROLE_ADMIN")
    void deactivate_shouldReturn404WhenPaymentCardNotFound() throws Exception {
        mockMvc.perform(patch("/api/payment-cards/{id}/deactivate", UUID.randomUUID())
                        .header("X-Gateway-Key", gatewayApiKey))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("delete should delete payment card successfully")
    @WithMockCustomUser(role = "ROLE_ADMIN")
    void delete_shouldDeletePaymentCardSuccessfully() throws Exception {
        User user = userRepository.save(UserTestDataFactory.createUser());

        PaymentCard savedCard = paymentCardRepository.save(PaymentCardTestDataFactory.createPaymentCard(user));

        mockMvc.perform(delete("/api/payment-cards/{id}", savedCard.getId())
                        .header("X-Gateway-Key", gatewayApiKey))
                .andExpect(status().isNoContent());

        assertThat(paymentCardRepository.existsById(savedCard.getId())).isFalse();
    }

    @Test
    @DisplayName("delete should return 404 when payment card not found")
    @WithMockCustomUser(role = "ROLE_ADMIN")
    void delete_shouldReturn404WhenPaymentCardNotFound() throws Exception {
        mockMvc.perform(delete("/api/payment-cards/{id}", UUID.randomUUID())
                        .header("X-Gateway-Key", gatewayApiKey))
                .andExpect(status().isNotFound());
    }
}