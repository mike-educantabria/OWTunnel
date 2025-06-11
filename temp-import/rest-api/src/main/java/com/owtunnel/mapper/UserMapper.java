package com.owtunnel.mapper;

import com.owtunnel.dto.request.UserRequest;
import com.owtunnel.dto.response.UserResponse;
import com.owtunnel.model.entity.User;
import com.owtunnel.model.enums.UserRole;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "passwordHash", ignore = true)
    @Mapping(target = "role", expression = "java(mapStringToRole(userRequest.getRole()))")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    User toEntity(UserRequest userRequest);

    @Mapping(target = "role", expression = "java(mapRoleToString(user.getRole()))")
    UserResponse toResponse(User user);

    default UserRole mapStringToRole(String roleStr) {
        try {
            return roleStr != null ? UserRole.valueOf(roleStr) : UserRole.USER;
        } catch (IllegalArgumentException e) {
            return UserRole.USER;
        }
    }

    default String mapRoleToString(UserRole role) {
        return role != null ? role.name() : null;
    }
}
