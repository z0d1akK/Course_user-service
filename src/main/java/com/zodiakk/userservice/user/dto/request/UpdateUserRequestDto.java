package com.zodiakk.userservice.user.dto.request;

import com.zodiakk.userservice.common.validation.ValidationMessages;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO for updating user information")
public class UpdateUserRequestDto {

    @Schema(description = "User first name", example = "updatedName")
    @Size(min = 1, max = 255, message = ValidationMessages.USER_NAME_SIZE)
    private String name;

    @Schema(description = "User surname", example = "updatedSurname")
    @Size(min = 1, max = 255, message = ValidationMessages.USER_SURNAME_SIZE)
    private String surname;

    @Schema(description = "User birth date", example = "2006-06-06")
    @PastOrPresent(message = ValidationMessages.USER_BIRTH_DATE_PAST)
    private LocalDate birthDate;
}