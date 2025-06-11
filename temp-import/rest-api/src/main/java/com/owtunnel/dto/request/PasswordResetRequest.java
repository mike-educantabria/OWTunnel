package com.owtunnel.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Schema(description = "Password Reset Request")
public class PasswordResetRequest {

    @Schema(description = "User ID", example = "1")
    @NotNull
    private Long userId;
}
