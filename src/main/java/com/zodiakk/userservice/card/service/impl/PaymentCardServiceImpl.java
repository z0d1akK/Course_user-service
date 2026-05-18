package com.zodiakk.userservice.card.service.impl;

import com.zodiakk.userservice.card.dto.request.CreatePaymentCardRequestDto;
import com.zodiakk.userservice.card.dto.request.UpdatePaymentCardRequestDto;
import com.zodiakk.userservice.card.dto.response.PaymentCardResponseDto;
import com.zodiakk.userservice.card.dto.response.PaymentCardShortResponseDto;
import com.zodiakk.userservice.card.entity.PaymentCard;
import com.zodiakk.userservice.card.exception.MaxCardsLimitException;
import com.zodiakk.userservice.card.exception.PaymentCardNotFoundException;
import com.zodiakk.userservice.card.mapper.PaymentCardMapper;
import com.zodiakk.userservice.card.repository.PaymentCardRepository;
import com.zodiakk.userservice.card.service.PaymentCardService;
import com.zodiakk.userservice.common.cache.CacheNames;
import com.zodiakk.userservice.user.entity.User;
import com.zodiakk.userservice.user.exception.UserNotFoundException;
import com.zodiakk.userservice.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaymentCardServiceImpl implements PaymentCardService {

    private static final long MAX_CARDS_COUNT = 5;

    private final PaymentCardRepository paymentCardRepository;

    private final UserRepository userRepository;

    private final PaymentCardMapper paymentCardMapper;

    private final CacheManager cacheManager;

    @Override
    @Transactional
    @CacheEvict(value = CacheNames.USERS, key = "#userId")
    public PaymentCardResponseDto create(UUID userId, CreatePaymentCardRequestDto request) {

        //TODO Make util with getting current user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        long cardsCount = paymentCardRepository.countByUserId(userId);

        if (cardsCount >= MAX_CARDS_COUNT) throw new MaxCardsLimitException(userId);

        PaymentCard paymentCard = paymentCardMapper.toEntity(request);

        user.addPaymentCard(paymentCard);

        PaymentCard savedPaymentCard = paymentCardRepository.save(paymentCard);

        return paymentCardMapper.toResponse(savedPaymentCard);
    }

    @Override
    public PaymentCardResponseDto getById(UUID id) {
        return paymentCardRepository.findById(id)
                .map(paymentCardMapper::toResponse)
                .orElseThrow(() -> new PaymentCardNotFoundException(id));
    }

    @Override
    public PaymentCardShortResponseDto getShortById(UUID id) {
        return paymentCardRepository.findById(id)
                .map(paymentCardMapper::toShortResponse)
                .orElseThrow(() -> new PaymentCardNotFoundException(id));
    }

    @Override
    public Page<PaymentCardShortResponseDto> getAllShort(Pageable pageable) {
        return paymentCardRepository.findAll(pageable)
                .map(paymentCardMapper::toShortResponse);
    }

    @Override
    public List<PaymentCardShortResponseDto> getAllShortByUserId(UUID userId) {
        validateUserExists(userId);
        return paymentCardRepository.findAllByUserId(userId).stream()
                .map(paymentCardMapper::toShortResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Page<PaymentCardResponseDto> getAll(Pageable pageable) {
        return paymentCardRepository.findAll(pageable)
                .map(paymentCardMapper::toResponse);
    }

    @Override
    public List<PaymentCardResponseDto> getAllByUserId(UUID userId) {
        validateUserExists(userId);
        return paymentCardRepository.findAllByUserId(userId).stream()
                .map(paymentCardMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PaymentCardResponseDto update(UUID id, UpdatePaymentCardRequestDto request) {
        PaymentCard paymentCard = paymentCardRepository.findById(id)
                        .orElseThrow(() -> new PaymentCardNotFoundException(id));

        paymentCardMapper.updateToEntity(request, paymentCard);

        PaymentCard updatedPaymentCard = paymentCardRepository.save(paymentCard);

        evictUserCache(paymentCard.getUser().getId());

        return paymentCardMapper.toResponse(updatedPaymentCard);
    }

    @Override
    @Transactional
    public void activate(UUID id) {
        PaymentCard paymentCard = paymentCardRepository.findById(id)
                .orElseThrow(() -> new PaymentCardNotFoundException(id));

        paymentCardRepository.activate(id);

        evictUserCache(paymentCard.getUser().getId());
    }

    @Override
    @Transactional
    public void deactivate(UUID id) {
        PaymentCard paymentCard = paymentCardRepository.findById(id)
                .orElseThrow(() -> new PaymentCardNotFoundException(id));

        paymentCardRepository.deactivate(id);

        evictUserCache(paymentCard.getUser().getId());
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        PaymentCard paymentCard = paymentCardRepository.findById(id)
                .orElseThrow(() -> new PaymentCardNotFoundException(id));

        User user = paymentCard.getUser();

        UUID userId = user.getId();

        user.removePaymentCard(paymentCard);

        userRepository.save(user);

        evictUserCache(userId);
    }

    private void validateUserExists(UUID userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(userId);
        }
    }

    private void evictUserCache(UUID userId) {
        if (cacheManager.getCache(CacheNames.USERS) != null) {
            cacheManager.getCache(CacheNames.USERS).evict(userId);
        }
        if (cacheManager.getCache(CacheNames.USERS_SHORT) != null) {
            cacheManager.getCache(CacheNames.USERS_SHORT).evict(userId);
        }
    }
}
