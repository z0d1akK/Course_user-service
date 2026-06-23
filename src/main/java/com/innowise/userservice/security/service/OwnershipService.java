package com.innowise.userservice.security.service;

import com.innowise.userservice.security.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OwnershipService {

    public boolean isOwner(UUID userId) {
        return SecurityUtils.getCurrentUserId().equals(userId);
    }

    public boolean isOwnerOrAdmin(UUID userId) {
        return isOwner(userId) || SecurityUtils.isAdmin();
    }
}