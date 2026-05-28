package com.innowise.userservice.security.service;

import com.innowise.userservice.card.repository.PaymentCardRepository;
import com.innowise.userservice.security.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CardOwnershipService {

    private final PaymentCardRepository paymentCardRepository;

    public boolean isOwnerOrAdmin(UUID cardId) {
        if (SecurityUtils.isAdmin()) return true;

        return paymentCardRepository.existsByIdAndUserId(cardId, SecurityUtils.getCurrentUserId());
    }
}