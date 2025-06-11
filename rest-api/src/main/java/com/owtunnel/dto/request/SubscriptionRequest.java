package com.owtunnel.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
@Schema(description = "Subscription Request")
public class SubscriptionRequest {

    @Schema(description = "User ID", example = "1")
    @NotNull
    private Long userId;

    @Schema(description = "Plan ID", example = "1")
    @NotNull
    private Long planId;

    @Schema(description = "Status", example = "ACTIVE")
    @Pattern(
            regexp = "PENDING|ACTIVE|CANCELLED|EXPIRED",
            message = "Status must be one of PENDING, ACTIVE, CANCELLED, EXPIRED"
    )
    private String status;

    @Schema(description = "Auto-renew", example = "true")
    @NotNull
    private Boolean autoRenew;

    @Schema(description = "Expiration timestamp", example = "2025-01-01T12:00:00")
    @NotNull
    private LocalDateTime expiresAt;
}
