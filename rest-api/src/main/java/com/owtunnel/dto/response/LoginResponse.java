package com.owtunnel.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter @Setter
@Schema(description = "Auth Response")
public class LoginResponse {

    @Schema(description = "JWT Token", example = "abc123xyz")
    private String token;

    @Schema(description = "User details", example = "UserResponse")
    private UserResponse user;
}
