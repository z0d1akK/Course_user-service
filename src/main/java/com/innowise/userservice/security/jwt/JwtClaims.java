package com.innowise.userservice.security.jwt;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class JwtClaims {

    private UUID userId;

    private String role;

    private String email;
}