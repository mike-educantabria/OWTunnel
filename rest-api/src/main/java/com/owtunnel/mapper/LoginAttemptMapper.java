package com.owtunnel.mapper;

import com.owtunnel.dto.request.LoginAttemptRequest;
import com.owtunnel.dto.response.LoginAttemptResponse;
import com.owtunnel.model.entity.LoginAttempt;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LoginAttemptMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "blockedUntil", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    LoginAttempt toEntity(LoginAttemptRequest loginAttemptRequest);

    @Mapping(target = "blockedUntil", ignore = true)
    LoginAttemptResponse toResponse(LoginAttempt loginAttempt);
}
