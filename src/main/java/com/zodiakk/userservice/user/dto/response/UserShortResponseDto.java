package com.zodiakk.userservice.user.dto.response;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserShortResponseDto {

    private UUID id;

    private String name;

    private String surname;

    private String email;

    private Boolean active;
}