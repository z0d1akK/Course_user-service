package com.innowise.userservice.user.dto.request;

import com.innowise.userservice.common.validation.ValidationMessages;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserFilterRequestDto {

    @Size(max = 255, message = ValidationMessages.USER_NAME_FILTER_SIZE)
    private String name;

    @Size(max = 255, message = ValidationMessages.USER_SURNAME_FILTER_SIZE)
    private String surname;

    private Boolean active;
}