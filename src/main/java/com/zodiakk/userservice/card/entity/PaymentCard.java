package com.zodiakk.userservice.card.entity;

import com.zodiakk.userservice.common.entity.BaseEntity;
import com.zodiakk.userservice.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "payment_cards")
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class PaymentCard extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 16, updatable = false)
    private String number;

    @Column(nullable = false)
    private String holder;

    @Column(nullable = false, length = 5)
    private String expirationDate;

    @Column(nullable = false)
    private Boolean active;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        PaymentCard card = (PaymentCard) o;

        return getId() != null && Objects.equals(getId(), card.getId());
    }

    @Override
    public final int hashCode() {
        return Objects.hash(getId());
    }
}