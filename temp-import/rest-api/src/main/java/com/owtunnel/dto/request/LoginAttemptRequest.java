package com.owtunnel.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Schema(description = "Login Attempt Request")
public class LoginAttemptRequest {

    @Schema(description = "IP address", example = "192.168.1.1")
    @NotBlank @Size(max = 45)
    private String ipAddress;

    @Schema(description = "Attempt count", example = "1")
    @NotNull @Min(value = 1)
    private Integer attemptCount;

    @Schema(description = "Blocked until", example = "2025-01-01T12:00:00")
    private String blockedUntil;
}
