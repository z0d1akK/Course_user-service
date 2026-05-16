package com.zodiakk.userservice.card.dto.response;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentCardShortResponseDto {

    private UUID id;

    private String number;

    private String expirationDate;

    private Boolean active;
}