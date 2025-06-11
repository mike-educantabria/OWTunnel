package com.owtunnel.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
@Schema(description = "Subscription Response")
public class SubscriptionResponse {

    @Schema(description = "ID", example = "1")
    private Long id;

    @Schema(description = "User ID", example = "1")
    @JsonProperty("user_id")
    private Long userId;

    @Schema(description = "Plan ID", example = "1")
    @JsonProperty("plan_id")
    private Long planId;

    @Schema(description = "Status", example = "ACTIVE")
    private String status;

    @Schema(description = "Auto-renew", example = "true")
    @JsonProperty("auto_renew")
    private Boolean autoRenew;

    @Schema(description = "Creation timestamp", example = "2025-01-01T12:00:00")
    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @Schema(description = "Update timestamp", example = "2025-01-01T12:00:00")
    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

    @Schema(description = "Expiration timestamp", example = "2025-01-01T12:00:00")
    @JsonProperty("expires_at")
    private LocalDateTime expiresAt;
}
