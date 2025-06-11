package com.owtunnel.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
@Schema(description = "Password Reset Response")
public class PasswordResetResponse {

    @Schema(description = "ID", example = "1")
    private Long id;

    @Schema(description = "User ID", example = "1")
    @JsonProperty("user_id")
    private Long userId;

    @Schema(description = "Reset token", example = "abc123xyz")
    @JsonProperty("reset_token")
    private String resetToken;

    @Schema(description = "Creation timestamp", example = "2025-01-01T12:00:00")
    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @Schema(description = "Expiration timestamp", example = "2025-01-01T12:00:00")
    @JsonProperty("expires_at")
    private LocalDateTime expiresAt;
}
