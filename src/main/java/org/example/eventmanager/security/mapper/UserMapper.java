package org.example.eventmanager.security.mapper;

import org.example.eventmanager.security.dto.RegisterRequest;
import org.example.eventmanager.security.dto.UserDto;
import org.example.eventmanager.security.entity.UserEntity;
import org.example.eventmanager.security.model.User;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "role", constant = "USER")
    UserEntity toEntity(User user);

    User toUser(UserEntity entity);

    UserDto toDto(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", constant = "USER")
    @Mapping(target = "password", expression = "java(passwordEncoder.encode(registerRequest.getPassword()))")
    UserEntity toEntity(RegisterRequest registerRequest, @Context PasswordEncoder passwordEncoder);
}