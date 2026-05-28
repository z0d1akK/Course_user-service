package com.innowise.userservice.security.jwt.service;

import com.innowise.userservice.security.jwt.JwtClaims;

public interface JwtService {

    boolean isTokenValid(String token);

    JwtClaims extractClaims(String token);
}