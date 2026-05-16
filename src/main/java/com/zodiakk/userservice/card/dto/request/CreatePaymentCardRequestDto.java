package com.zodiakk.userservice.card.dto.request;

import com.zodiakk.userservice.common.validation.ValidationMessages;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreatePaymentCardRequestDto {

    @NotBlank(message = ValidationMessages.CARD_NUMBER_REQUIRED)
    @Pattern(regexp = "^\\\\d{16}$", message = ValidationMessages.CARD_NUMBER_PATTERN)
    private String number;

    @NotBlank(message = ValidationMessages.CARD_HOLDER_REQUIRED)
    @Size(min = 1, max = 255, message = ValidationMessages.CARD_HOLDER_SIZE)
    private String holder;

    @NotBlank(message = ValidationMessages.CARD_EXPIRATION_REQUIRED)
    @Pattern(regexp = "^(0[1-9]|1[0-2])/\\d{2}$", message = ValidationMessages.CARD_EXPIRATION_PATTERN)
    private String expirationDate;
}