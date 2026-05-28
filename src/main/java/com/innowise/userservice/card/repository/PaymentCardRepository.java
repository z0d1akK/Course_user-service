package com.innowise.userservice.card.repository;

import com.innowise.userservice.card.entity.PaymentCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PaymentCardRepository extends JpaRepository<PaymentCard, UUID> {

    List<PaymentCard> findAllByUserId(UUID userId);

    long countByUserId(UUID userId);

    boolean existsByIdAndUserId(UUID id, UUID userId );
}