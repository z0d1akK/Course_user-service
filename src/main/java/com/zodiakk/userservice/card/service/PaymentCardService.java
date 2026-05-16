package com.zodiakk.userservice.card.service;

import com.zodiakk.userservice.card.dto.request.CreatePaymentCardRequestDto;
import com.zodiakk.userservice.card.dto.request.UpdatePaymentCardRequestDto;
import com.zodiakk.userservice.card.dto.response.PaymentCardResponseDto;
import com.zodiakk.userservice.card.dto.response.PaymentCardShortResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface PaymentCardService {

    PaymentCardResponseDto create(UUID userId, CreatePaymentCardRequestDto request);

    PaymentCardResponseDto getById(UUID id);

    PaymentCardShortResponseDto getShortById(UUID id);

    Page<PaymentCardShortResponseDto> getAllShort(Pageable pageable);

    List<PaymentCardShortResponseDto> getAllShortByUserId(UUID userId);

    Page<PaymentCardResponseDto> getAll(Pageable pageable);

    List<PaymentCardResponseDto> getAllByUserId(UUID userId);

    PaymentCardResponseDto update(UUID id, UpdatePaymentCardRequestDto request);

    void activate(UUID id);

    void deactivate(UUID id);
}