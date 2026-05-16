package com.zodiakk.userservice.user.repository.specification;

import com.zodiakk.userservice.user.entity.User;
import org.springframework.data.jpa.domain.Specification;

public final class UserSpecification {

    private UserSpecification() { }

    public static Specification<User> hasName(String name) {
        return (root, query, criteriaBuilder) ->
                name == null ? null
                        : criteriaBuilder.like(criteriaBuilder.lower(root.get("name")),
                        "%" + name.toLowerCase() + "%"
                );
    }

    public static Specification<User> hasSurname(String surname) {
        return (root, query, criteriaBuilder) ->
                surname == null ? null
                        : criteriaBuilder.like(criteriaBuilder.lower(root.get("surname")),
                        "%" + surname.toLowerCase() + "%"
                );
    }
}