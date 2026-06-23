package com.innowise.userservice.user.repository;

import com.innowise.userservice.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID>, JpaSpecificationExecutor<User> {

    boolean existsByEmail(String email);

    @Query("""
            SELECT u FROM User u
            LEFT JOIN FETCH u.paymentCards
            WHERE u.id = :id
            """)
    Optional<User> findByIdWithCards(UUID id);

    @Query(value = """
            SELECT DISTINCT u FROM User u
            LEFT JOIN FETCH u.paymentCards
            """,
            countQuery = "SELECT COUNT(u) FROM User u")
    Page<User> findAllWithCards(Pageable pageable);
}