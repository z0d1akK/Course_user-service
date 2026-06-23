package com.innowise.userservice.user.mapper;

import com.innowise.userservice.config.MapStructConfig;
import com.innowise.userservice.user.dto.request.CreateUserRequestDto;
import com.innowise.userservice.user.dto.request.UpdateUserRequestDto;
import com.innowise.userservice.user.dto.response.UserResponseDto;
import com.innowise.userservice.user.dto.response.UserShortResponseDto;
import com.innowise.userservice.user.entity.User;
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