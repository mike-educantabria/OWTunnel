package com.owtunnel.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
@Schema(description = "Login Attempt Response")
public class LoginAttemptResponse {

    @Schema(description = "ID", example = "1")
    private Long id;

    @Schema(description = "IP address", example = "192.168.1.1")
    @JsonProperty("ip_address")
    private String ipAddress;

    @Schema(description = "Attempt count", example = "1")
    @JsonProperty("attempt_count")
    private Integer attemptCount;

    @Schema(description = "Blocked until", example = "2025-01-01T12:00:00")
    @JsonProperty("blocked_until")
    private String blockedUntil;

    @Schema(description = "Creation timestamp", example = "2025-01-01T12:00:00")
    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @Schema(description = "Update timestamp", example = "2025-01-01T12:00:00")
    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;
}
