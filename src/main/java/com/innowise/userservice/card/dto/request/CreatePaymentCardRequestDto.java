package com.innowise.userservice.card.dto.request;

import com.innowise.userservice.common.validation.ValidationMessages;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO for creating a payment card")
public class CreatePaymentCardRequestDto {

    @Schema(description = "Card number", example = "1111222233334444")
    @NotBlank(message = ValidationMessages.CARD_NUMBER_REQUIRED)
    @Pattern(regexp = "^\\d{16}$", message = ValidationMessages.CARD_NUMBER_PATTERN)
    private String number;

    @Schema(description = "Card holder full name", example = "name surname")
    @NotBlank(message = ValidationMessages.CARD_HOLDER_REQUIRED)
    @Size(min = 1, max = 255, message = ValidationMessages.CARD_HOLDER_SIZE)
    private String holder;

    @Schema(description = "Card expiration date in MM/YY format", example = "00/00")
    @NotBlank(message = ValidationMessages.CARD_EXPIRATION_REQUIRED)
    @Pattern(regexp = "^(0[1-9]|1[0-2])/\\d{2}$", message = ValidationMessages.CARD_EXPIRATION_PATTERN)
    private String expirationDate;
}