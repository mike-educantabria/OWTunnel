package com.owtunnel.mapper;

import com.owtunnel.dto.request.PasswordResetRequest;
import com.owtunnel.dto.response.PasswordResetResponse;
import com.owtunnel.model.entity.PasswordReset;
import com.owtunnel.model.entity.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface PasswordResetMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "expiresAt", ignore = true)
    PasswordReset toEntity(PasswordResetRequest passwordResetRequest, @Context User user);

    @Mapping(target = "userId", source = "user.id")
    PasswordResetResponse toResponse(PasswordReset passwordReset);

    @AfterMapping
    default void assignUser(@MappingTarget PasswordReset passwordReset, @Context User user) {
        passwordReset.setUser(user);
    }
}
