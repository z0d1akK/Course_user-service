package com.zodiakk.userservice.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Short user response DTO")
public class UserShortResponseDto {

    @Schema(example = "e73dcc73-e1db-4c3a-9246-0e1c2de79074")
    private UUID id;

    @Schema(example = "name")
    private String name;

    @Schema(example = "surname")
    private String surname;

    @Schema(example = "user@gmail.com")
    private String email;

    @Schema(example = "true")
    private Boolean active;
}