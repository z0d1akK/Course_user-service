package com.zodiakk.userservice.card.dto.response;

import com.zodiakk.userservice.user.dto.response.UserShortResponseDto;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentCardResponseDto {

    private UUID id;

    private String number;

    private String holder;

    private String expirationDate;

    private Boolean active;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private UserShortResponseDto user;
}