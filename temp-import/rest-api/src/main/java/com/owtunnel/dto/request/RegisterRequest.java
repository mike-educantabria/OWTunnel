package com.owtunnel.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Schema(description = "Register Request")
public class RegisterRequest {

    @Schema(description = "Email", example = "user@example.com")
    @NotBlank
    private String email;

    @Schema(description = "Plan password", example = "password123")
    @NotBlank @Size(min = 8)
    private String password;
}
