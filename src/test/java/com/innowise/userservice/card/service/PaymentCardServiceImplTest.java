package com.innowise.userservice.card.service;

import com.innowise.userservice.card.dto.request.CreatePaymentCardRequestDto;
import com.innowise.userservice.card.dto.request.UpdatePaymentCardRequestDto;
import com.innowise.userservice.card.dto.response.PaymentCardResponseDto;
import com.innowise.userservice.card.dto.response.PaymentCardShortResponseDto;
import com.innowise.userservice.card.entity.PaymentCard;
import com.innowise.userservice.card.exception.MaxCardsLimitException;
import com.innowise.userservice.card.exception.PaymentCardNotFoundException;
import com.innowise.userservice.card.mapper.PaymentCardMapper;
import com.innowise.userservice.card.repository.PaymentCardRepository;
import com.innowise.userservice.card.service.impl.PaymentCardServiceImpl;
import com.innowise.userservice.card.testclasses.PaymentCardTestDataFactory;
import com.innowise.userservice.common.cache.CacheNames;
import com.innowise.userservice.user.entity.User;
import com.innowise.userservice.user.exception.UserNotFoundException;
import com.innowise.userservice.user.repository.UserRepository;
import com.innowise.userservice.user.testclasses.UserTestDataFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PaymentCardServiceImplTest {

    @Mock
    private PaymentCardRepository paymentCardRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PaymentCardMapper paymentCardMapper;

    @Mock
    private CacheManager cacheManager;

    @Mock
    private Cache usersCache;

    @Mock
    private Cache usersShortCache;

    @InjectMocks
    private PaymentCardServiceImpl paymentCardService;

    @Test
    @DisplayName("Should create payment card successfully")
    void create_shouldCreatePaymentCardSuccessfully() {
        UUID userId = UUID.randomUUID();

        CreatePaymentCardRequestDto request = PaymentCardTestDataFactory.createPaymentCardRequest();

        User user = UserTestDataFactory.createUser();
        user.setId(userId);

        PaymentCard paymentCard = PaymentCardTestDataFactory.createPaymentCard(user);

        PaymentCard savedPaymentCard = PaymentCardTestDataFactory.createPaymentCard(user);
        savedPaymentCard.setId(UUID.randomUUID());

        PaymentCardResponseDto response = PaymentCardTestDataFactory.createPaymentCardResponse();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(paymentCardRepository.countByUserId(userId)).thenReturn(2L);
        when(paymentCardMapper.toEntity(request)).thenReturn(paymentCard);
        when(paymentCardRepository.save(paymentCard)).thenReturn(savedPaymentCard);
        when(paymentCardMapper.toResponse(savedPaymentCard)).thenReturn(response);

        PaymentCardResponseDto actual = paymentCardService.create(userId, request);

        assertThat(actual).isNotNull();
        assertThat(actual.getNumber()).isEqualTo(response.getNumber());

        verify(userRepository).findById(userId);
        verify(paymentCardRepository).countByUserId(userId);
        verify(paymentCardMapper).toEntity(request);
        verify(paymentCardRepository).save(paymentCard);
        verify(paymentCardMapper).toResponse(savedPaymentCard);
    }

    @Test
    @DisplayName("Should throw exception when user not found during card creation")
    void create_shouldThrowExceptionWhenUserNotFound() {
        UUID userId = UUID.randomUUID();

        CreatePaymentCardRequestDto request = PaymentCardTestDataFactory.createPaymentCardRequest();

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> paymentCardService.create(userId, request))
                .isInstanceOf(UserNotFoundException.class);

        verify(userRepository).findById(userId);
        verify(paymentCardRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception when max cards limit exceeded")
    void create_shouldThrowExceptionWhenMaxCardsLimitExceeded() {
        UUID userId = UUID.randomUUID();

        CreatePaymentCardRequestDto request = PaymentCardTestDataFactory.createPaymentCardRequest();

        User user = UserTestDataFactory.createUser();
        user.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(paymentCardRepository.countByUserId(userId)).thenReturn(5L);

        assertThatThrownBy(() -> paymentCardService.create(userId, request))
                .isInstanceOf(MaxCardsLimitException.class);

        verify(paymentCardRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should return payment card by id")
    void getById_shouldReturnPaymentCard() {
        UUID cardId = UUID.randomUUID();

        User user = UserTestDataFactory.createUser();

        PaymentCard paymentCard = PaymentCardTestDataFactory.createPaymentCard(user);
        paymentCard.setId(cardId);

        PaymentCardResponseDto response = PaymentCardTestDataFactory.createPaymentCardResponse(cardId);

        when(paymentCardRepository.findById(cardId)).thenReturn(Optional.of(paymentCard));
        when(paymentCardMapper.toResponse(paymentCard)).thenReturn(response);

        PaymentCardResponseDto actual = paymentCardService.getById(cardId);

        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isEqualTo(cardId);

        verify(paymentCardRepository).findById(cardId);
        verify(paymentCardMapper).toResponse(paymentCard);
    }

    @Test
    @DisplayName("Should throw exception when payment card not found by id")
    void getById_shouldThrowExceptionWhenPaymentCardNotFound() {
        UUID cardId = UUID.randomUUID();

        when(paymentCardRepository.findById(cardId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> paymentCardService.getById(cardId))
                .isInstanceOf(PaymentCardNotFoundException.class);

        verify(paymentCardRepository).findById(cardId);
        verify(paymentCardMapper, never()).toResponse(any());
    }

    @Test
    @DisplayName("Should return short payment card by id")
    void getShortById_shouldReturnShortPaymentCard() {
        UUID cardId = UUID.randomUUID();

        User user = UserTestDataFactory.createUser();

        PaymentCard paymentCard = PaymentCardTestDataFactory.createPaymentCard(user);
        paymentCard.setId(cardId);

        PaymentCardShortResponseDto response = PaymentCardTestDataFactory.createPaymentCardShortResponse(cardId);

        when(paymentCardRepository.findById(cardId)).thenReturn(Optional.of(paymentCard));
        when(paymentCardMapper.toShortResponse(paymentCard)).thenReturn(response);

        PaymentCardShortResponseDto actual = paymentCardService.getShortById(cardId);

        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isEqualTo(cardId);

        verify(paymentCardRepository).findById(cardId);
        verify(paymentCardMapper).toShortResponse(paymentCard);
    }

    @Test
    @DisplayName("Should throw exception when short payment card not found")
    void getShortById_shouldThrowExceptionWhenPaymentCardNotFound() {
        UUID cardId = UUID.randomUUID();

        when(paymentCardRepository.findById(cardId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> paymentCardService.getShortById(cardId))
                .isInstanceOf(PaymentCardNotFoundException.class);

        verify(paymentCardRepository).findById(cardId);
        verify(paymentCardMapper, never()).toShortResponse(any());
    }

    @Test
    @DisplayName("Should return all short payment cards")
    void getAllShort_shouldReturnPageOfShortPaymentCards() {
        Pageable pageable = PageRequest.of(0, 10);

        User user = UserTestDataFactory.createUser();

        PaymentCard paymentCard = PaymentCardTestDataFactory.createPaymentCard(user);
        paymentCard.setId(UUID.randomUUID());

        Page<PaymentCard> page = new PageImpl<>(List.of(paymentCard));

        PaymentCardShortResponseDto response = PaymentCardTestDataFactory.createPaymentCardShortResponse();

        when(paymentCardRepository.findAll(pageable)).thenReturn(page);
        when(paymentCardMapper.toShortResponse(paymentCard)).thenReturn(response);

        Page<PaymentCardShortResponseDto> actual = paymentCardService.getAllShort(pageable);

        assertThat(actual).isNotNull();
        assertThat(actual.getContent()).hasSize(1);

        verify(paymentCardRepository).findAll(pageable);
        verify(paymentCardMapper).toShortResponse(paymentCard);
    }

    @Test
    @DisplayName("Should return all payment cards")
    void getAll_shouldReturnPageOfPaymentCards() {
        Pageable pageable = PageRequest.of(0, 10);

        User user = UserTestDataFactory.createUser();

        PaymentCard paymentCard = PaymentCardTestDataFactory.createPaymentCard(user);
        paymentCard.setId(UUID.randomUUID());

        Page<PaymentCard> page = new PageImpl<>(List.of(paymentCard));

        PaymentCardResponseDto response = PaymentCardTestDataFactory.createPaymentCardResponse();

        when(paymentCardRepository.findAll(pageable)).thenReturn(page);
        when(paymentCardMapper.toResponse(paymentCard)).thenReturn(response);

        Page<PaymentCardResponseDto> actual = paymentCardService.getAll(pageable);

        assertThat(actual).isNotNull();
        assertThat(actual.getContent()).hasSize(1);

        verify(paymentCardRepository).findAll(pageable);
        verify(paymentCardMapper).toResponse(paymentCard);
    }

    @Test
    @DisplayName("Should return all short payment cards by user id")
    void getAllShortByUserId_shouldReturnShortCards() {
        UUID userId = UUID.randomUUID();

        User user = UserTestDataFactory.createUser();
        user.setId(userId);

        PaymentCard paymentCard = PaymentCardTestDataFactory.createPaymentCard(user);
        paymentCard.setId(UUID.randomUUID());

        PaymentCardShortResponseDto response = PaymentCardTestDataFactory.createPaymentCardShortResponse();

        when(userRepository.existsById(userId)).thenReturn(true);
        when(paymentCardRepository.findAllByUserId(userId)).thenReturn(List.of(paymentCard));
        when(paymentCardMapper.toShortResponse(paymentCard)).thenReturn(response);

        List<PaymentCardShortResponseDto> actual = paymentCardService.getAllShortByUserId(userId);

        assertThat(actual).hasSize(1);

        verify(userRepository).existsById(userId);
        verify(paymentCardRepository).findAllByUserId(userId);
        verify(paymentCardMapper).toShortResponse(paymentCard);
    }

    @Test
    @DisplayName("Should throw exception when getting short cards for nonexistent user")
    void getAllShortByUserId_shouldThrowExceptionWhenUserNotFound() {
        UUID userId = UUID.randomUUID();

        when(userRepository.existsById(userId)).thenReturn(false);

        assertThatThrownBy(() ->
                paymentCardService.getAllShortByUserId(userId))
                .isInstanceOf(UserNotFoundException.class);

        verify(paymentCardRepository, never()).findAllByUserId(any());
    }

    @Test
    @DisplayName("Should return all payment cards by user id")
    void getAllByUserId_shouldReturnCards() {
        UUID userId = UUID.randomUUID();

        User user = UserTestDataFactory.createUser();
        user.setId(userId);

        PaymentCard paymentCard = PaymentCardTestDataFactory.createPaymentCard(user);
        paymentCard.setId(UUID.randomUUID());

        PaymentCardResponseDto response = PaymentCardTestDataFactory.createPaymentCardResponse();

        when(userRepository.existsById(userId)).thenReturn(true);
        when(paymentCardRepository.findAllByUserId(userId)).thenReturn(List.of(paymentCard));
        when(paymentCardMapper.toResponse(paymentCard)).thenReturn(response);

        List<PaymentCardResponseDto> actual = paymentCardService.getAllByUserId(userId);

        assertThat(actual).hasSize(1);

        verify(userRepository).existsById(userId);
        verify(paymentCardRepository).findAllByUserId(userId);
        verify(paymentCardMapper).toResponse(paymentCard);
    }

    @Test
    @DisplayName("Should throw exception when getting cards for nonexistent user")
    void getAllByUserId_shouldThrowExceptionWhenUserNotFound() {
        UUID userId = UUID.randomUUID();

        when(userRepository.existsById(userId)).thenReturn(false);

        assertThatThrownBy(() ->
                paymentCardService.getAllByUserId(userId))
                .isInstanceOf(UserNotFoundException.class);

        verify(paymentCardRepository, never()).findAllByUserId(any());
    }

    @Test
    @DisplayName("Should update payment card successfully")
    void update_shouldUpdatePaymentCardSuccessfully() {
        UUID cardId = UUID.randomUUID();

        User user = UserTestDataFactory.createUser();

        PaymentCard paymentCard = PaymentCardTestDataFactory.createPaymentCard(user);
        paymentCard.setId(cardId);

        PaymentCard updatedPaymentCard = PaymentCardTestDataFactory.createUpdatedPaymentCard(user);

        UpdatePaymentCardRequestDto request = PaymentCardTestDataFactory.updatePaymentCardRequest();

        PaymentCardResponseDto response = PaymentCardTestDataFactory.createUpdatedPaymentCardResponse();

        when(paymentCardRepository.findById(cardId)).thenReturn(Optional.of(paymentCard));
        when(paymentCardRepository.save(paymentCard)).thenReturn(updatedPaymentCard);
        when(paymentCardMapper.toResponse(updatedPaymentCard)).thenReturn(response);
        when(cacheManager.getCache(CacheNames.USERS)).thenReturn(usersCache);
        when(cacheManager.getCache(CacheNames.USERS_SHORT)).thenReturn(usersShortCache);

        PaymentCardResponseDto actual = paymentCardService.update(cardId, request);

        assertThat(actual).isNotNull();
        assertThat(actual.getHolder()).isEqualTo(response.getHolder());

        verify(paymentCardRepository).findById(cardId);
        verify(paymentCardMapper).updateToEntity(request, paymentCard);
        verify(paymentCardRepository).save(paymentCard);

        verify(usersCache).evict(user.getId());
        verify(usersShortCache).evict(user.getId());
    }

    @Test
    @DisplayName("Should throw exception when updating nonexistent payment card")
    void update_shouldThrowExceptionWhenPaymentCardNotFound() {
        UUID cardId = UUID.randomUUID();

        UpdatePaymentCardRequestDto request = PaymentCardTestDataFactory.updatePaymentCardRequest();

        when(paymentCardRepository.findById(cardId)).thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                paymentCardService.update(cardId, request))
                .isInstanceOf(PaymentCardNotFoundException.class);

        verify(paymentCardRepository).findById(cardId);
        verify(paymentCardRepository, never()).save(any());
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    @DisplayName("Should activate or deactivate payment card")
    void activateDeactivate_shouldWorkCorrectly(boolean activate) {
        UUID cardId = UUID.randomUUID();

        User user = UserTestDataFactory.createUser();
        user.setId(UUID.randomUUID());

        PaymentCard paymentCard = PaymentCardTestDataFactory.createPaymentCard(user);
        paymentCard.setId(cardId);

        when(paymentCardRepository.findById(cardId)).thenReturn(Optional.of(paymentCard));
        when(cacheManager.getCache(CacheNames.USERS)).thenReturn(usersCache);
        when(cacheManager.getCache(CacheNames.USERS_SHORT)).thenReturn(usersShortCache);

        if (activate) {
            paymentCardService.activate(cardId);

            assertThat(paymentCard.getActive()).isTrue();
        } else {
            paymentCardService.deactivate(cardId);

            assertThat(paymentCard.getActive()).isFalse();
        }

        verify(usersCache).evict(user.getId());
        verify(usersShortCache).evict(user.getId());
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    @DisplayName("Should throw exception when payment card not found during activate/deactivate")
    void activateDeactivate_shouldThrowExceptionWhenCardNotFound(boolean activate) {
        UUID cardId = UUID.randomUUID();

        when(paymentCardRepository.findById(cardId)).thenReturn(Optional.empty());

        if (activate) {
            assertThatThrownBy(() ->
                    paymentCardService.activate(cardId))
                    .isInstanceOf(PaymentCardNotFoundException.class);
        } else {
            assertThatThrownBy(() ->
                    paymentCardService.deactivate(cardId))
                    .isInstanceOf(PaymentCardNotFoundException.class);
        }
    }

    @Test
    @DisplayName("Should delete payment card successfully")
    void delete_shouldDeletePaymentCardSuccessfully() {
        UUID cardId = UUID.randomUUID();

        User user = UserTestDataFactory.createUser();
        user.setId(UUID.randomUUID());

        PaymentCard paymentCard = PaymentCardTestDataFactory.createPaymentCard(user);
        paymentCard.setId(cardId);

        when(paymentCardRepository.findById(cardId)).thenReturn(Optional.of(paymentCard));
        when(cacheManager.getCache(CacheNames.USERS)).thenReturn(usersCache);
        when(cacheManager.getCache(CacheNames.USERS_SHORT)).thenReturn(usersShortCache);

        paymentCardService.delete(cardId);

        verify(paymentCardRepository).findById(cardId);
        verify(userRepository).save(user);
        verify(usersCache).evict(user.getId());
        verify(usersShortCache).evict(user.getId());
    }

    @Test
    @DisplayName("Should throw exception when deleting nonexistent payment card")
    void delete_shouldThrowExceptionWhenPaymentCardNotFound() {
        UUID cardId = UUID.randomUUID();

        when(paymentCardRepository.findById(cardId)).thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                paymentCardService.delete(cardId))
                .isInstanceOf(PaymentCardNotFoundException.class);

        verify(userRepository, never()).save(any());
    }
}
