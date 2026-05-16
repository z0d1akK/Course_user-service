package com.zodiakk.userservice.user.mapper;

import com.zodiakk.userservice.config.MapStructConfig;
import com.zodiakk.userservice.user.dto.request.CreateUserRequestDto;
import com.zodiakk.userservice.user.dto.request.UpdateUserRequestDto;
import com.zodiakk.userservice.user.dto.response.UserResponseDto;
import com.zodiakk.userservice.user.dto.response.UserShortResponseDto;
import com.zodiakk.userservice.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapStructConfig.class)
public interface UserMapper {

    @Mapping(target = "active", constant = "true")
    User toEntity(CreateUserRequestDto dto);

    void updateToEntity(UpdateUserRequestDto dto, @MappingTarget User user);

    UserResponseDto toResponse(User user);

    UserShortResponseDto toShortResponse(User user);
}