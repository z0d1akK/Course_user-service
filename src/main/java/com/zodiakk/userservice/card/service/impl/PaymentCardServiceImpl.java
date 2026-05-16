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
import com.zodiakk.userservice.user.entity.User;
import com.zodiakk.userservice.user.exception.UserNotFoundException;
import com.zodiakk.userservice.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
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

    @Override
    @Transactional
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
        if (!userRepository.existsById(userId)) throw new UserNotFoundException(userId);
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
        if (!userRepository.existsById(userId)) throw new UserNotFoundException(userId);
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

        return paymentCardMapper.toResponse(updatedPaymentCard);
    }

    @Override
    @Transactional
    public void activate(UUID id) {
        if (!paymentCardRepository.existsById(id)) throw new PaymentCardNotFoundException(id);

        paymentCardRepository.activate(id);
    }

    @Override
    @Transactional
    public void deactivate(UUID id) {
        if (!paymentCardRepository.existsById(id)) throw new PaymentCardNotFoundException(id);

        paymentCardRepository.deactivate(id);
    }
}
