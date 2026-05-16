package com.zodiakk.userservice.user.repository;

import com.zodiakk.userservice.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID>, JpaSpecificationExecutor<User> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    @Query("""
            SELECT u FROM User u
            LEFT JOIN FETCH u.paymentCards
            WHERE u.id = :id
            """)
    Optional<User> findByIdWithCards(UUID id);

    @Query(
            value = """
                    SELECT * FROM users
                    WHERE active = true
                    """, nativeQuery = true)
    Page<User> findAllActiveUsers(Pageable pageable);

    @Modifying
    @Query("UPDATE User u SET u.active = true WHERE u.id = :id")
    void activate(@Param("id") UUID id);

    @Modifying
    @Query("UPDATE User u SET u.active = false WHERE u.id = :id")
    void deactivate(@Param("id") UUID id);
}