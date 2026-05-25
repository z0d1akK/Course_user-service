package com.innowise.userservice.card.repository;

import com.innowise.userservice.card.entity.PaymentCard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PaymentCardRepository extends JpaRepository<PaymentCard, UUID> {

    List<PaymentCard> findAllByUserId(UUID userId);

    long countByUserId(UUID userId);

    @Query("""
            SELECT c FROM PaymentCard c
            WHERE c.user.id = :userId
            AND c.active = true
            """)
    List<PaymentCard> findActiveCardsByUserId(UUID userId);

    @Query(
            value = """
                    SELECT * FROM payment_cards
                    WHERE active = true
                    """,
            nativeQuery = true
    )
    Page<PaymentCard> findAllActiveCards(Pageable pageable);
}