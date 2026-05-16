package com.zodiakk.userservice.user.dto.request;

import com.zodiakk.userservice.common.validation.ValidationMessages;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateUserRequestDto {

    @NotBlank(message = ValidationMessages.USER_NAME_REQUIRED)
    @Size(min = 1, max = 255, message = ValidationMessages.USER_NAME_SIZE)
    private String name;

    @NotBlank(message = ValidationMessages.USER_SURNAME_REQUIRED)
    @Size(min = 1, max = 255, message = ValidationMessages.USER_SURNAME_SIZE)
    private String surname;

    @NotNull(message = ValidationMessages.USER_BIRTH_DATE_REQUIRED)
    @Past(message = ValidationMessages.USER_BIRTH_DATE_PAST)
    private LocalDate birthDate;

    @NotBlank(message = ValidationMessages.USER_EMAIL_REQUIRED)
    @Email(message = ValidationMessages.USER_EMAIL_VALID)
    @Size(max = 255, message = ValidationMessages.USER_EMAIL_SIZE)
    private String email;
}