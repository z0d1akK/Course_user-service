package com.zodiakk.userservice.user.entity;

import com.zodiakk.userservice.card.entity.PaymentCard;
import com.zodiakk.userservice.common.entity.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class User extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String surname;

    @Column(nullable = false)
    private LocalDate birthDate;

    @Column(nullable = false, unique = true, updatable = false)
    private String email;

    @Column(nullable = false)
    private Boolean active;

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PaymentCard> paymentCards = new ArrayList<>();

    public void addPaymentCard(PaymentCard card) {
        paymentCards.add(card);
        card.setUser(this);
    }

    public void removePaymentCard(PaymentCard card) {
        paymentCards.remove(card);
        card.setUser(null);
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (getId() != null && user.getId() != null) {
            return Objects.equals(getId(), user.getId());
        }

        return Objects.equals(email, user.email);
    }

    @Override
    public final int hashCode() {
        return getId() != null ? Objects.hash(getId()) : Objects.hash(email);
    }
}