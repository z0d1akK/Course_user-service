package com.zodiakk.userservice.card.entity;

import com.zodiakk.userservice.common.entity.BaseEntity;
import com.zodiakk.userservice.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "payment_cards")
public class PaymentCard extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, unique = true, length = 16)
    private String number;

    @Column(nullable = false)
    private String holder;

    @Column(nullable = false, length = 5)
    private String expirationDate;

    @Column(nullable = false)
    private Boolean active;
}