package com.zodiakk.userservice.user.repository.specification;

import com.zodiakk.userservice.user.entity.User;
import org.springframework.data.jpa.domain.Specification;

public final class UserSpecification {

    private UserSpecification() { }

    public static Specification<User> hasName(String name) {
        return (root, query, criteriaBuilder) -> {
            if (name == null || name.isBlank()) return criteriaBuilder.conjunction();
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("name")),
                    "%" + name.toLowerCase() + "%"
            );
        };
    }

    public static Specification<User> hasSurname(String surname) {
        return (root, query, criteriaBuilder) -> {
            if (surname == null || surname.isBlank()) return criteriaBuilder.conjunction();
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("surname")),
                    "%" + surname.toLowerCase() + "%"
            );
        };
    }

    public static Specification<User> hasActive(Boolean active) {
        return (root, query, criteriaBuilder) -> {
            if (active == null) return criteriaBuilder.conjunction();
            return criteriaBuilder.equal(root.get("active"), active);
        };
    }
}