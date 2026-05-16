package com.zodiakk.userservice.user.dto.request;

import com.zodiakk.userservice.common.validation.ValidationMessages;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateUserRequestDto {

    @Size(min = 1, max = 255, message = ValidationMessages.USER_NAME_SIZE)
    private String name;

    @Size(min = 1, max = 255, message = ValidationMessages.USER_SURNAME_SIZE)
    private String surname;

    @PastOrPresent(message = ValidationMessages.USER_BIRTH_DATE_PAST)
    private LocalDate birthDate;
}