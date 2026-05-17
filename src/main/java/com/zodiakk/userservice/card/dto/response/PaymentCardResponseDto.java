package com.zodiakk.userservice.card.dto.response;

import com.zodiakk.userservice.user.dto.response.UserShortResponseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Full payment card response DTO")
public class PaymentCardResponseDto {

    @Schema(example = "e73dcc73-e1db-4c3a-9246-0e1c2de79074")
    private UUID id;

    @Schema(example = "1111222233334444")
    private String number;

    @Schema(example = "name surname")
    private String holder;

    @Schema(example = "00/00")
    private String expirationDate;

    @Schema(example = "true")
    private Boolean active;

    @Schema(example = "2026-05-16T15:00:00")
    private LocalDateTime createdAt;

    @Schema(example = "2026-05-16T15:30:00")
    private LocalDateTime updatedAt;

    private UserShortResponseDto user;
}