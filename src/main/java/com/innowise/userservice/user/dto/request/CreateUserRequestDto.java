package com.innowise.userservice.user.dto.request;

import com.innowise.userservice.common.validation.ValidationMessages;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO for creating a new user")
public class CreateUserRequestDto {

    @Schema(description = "User first name", example = "name")
    @NotBlank(message = ValidationMessages.USER_NAME_REQUIRED)
    @Size(min = 1, max = 255, message = ValidationMessages.USER_NAME_SIZE)
    private String name;

    @Schema(description = "User surname", example = "surname")
    @NotBlank(message = ValidationMessages.USER_SURNAME_REQUIRED)
    @Size(min = 1, max = 255, message = ValidationMessages.USER_SURNAME_SIZE)
    private String surname;

    @Schema(description = "User birth date", example = "2005-05-05")
    @NotNull(message = ValidationMessages.USER_BIRTH_DATE_REQUIRED)
    @PastOrPresent(message = ValidationMessages.USER_BIRTH_DATE_PAST)
    private LocalDate birthDate;

    @Schema(description = "User email", example = "user@gmail.com")
    @NotBlank(message = ValidationMessages.USER_EMAIL_REQUIRED)
    @Email(message = ValidationMessages.USER_EMAIL_VALID)
    @Size(max = 255, message = ValidationMessages.USER_EMAIL_SIZE)
    private String email;
}