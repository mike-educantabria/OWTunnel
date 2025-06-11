package com.owtunnel.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Schema(description = "Auth Request")
public class LoginRequest {

    @Schema(description = "Email", example = "user@example.com")
    @NotBlank
    private String email;

    @Schema(description = "Plain password", example = "password123")
    @NotBlank
    private String password;
}
