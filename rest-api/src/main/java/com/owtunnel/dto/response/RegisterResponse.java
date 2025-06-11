package com.owtunnel.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Schema(description = "Register Response")
public class RegisterResponse {

    @Schema(description = "JWT Token", example = "abc123xyz")
    private String token;

    @Schema(description = "User details", example = "UserResponse")
    private UserResponse user;
}
