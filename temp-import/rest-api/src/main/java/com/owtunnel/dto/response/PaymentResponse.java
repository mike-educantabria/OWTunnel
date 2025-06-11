package com.owtunnel.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter @Setter
@Schema(description = "Payment Response")
public class PaymentResponse {

    @Schema(description = "ID", example = "1")
    private Long id;

    @Schema(description = "User ID", example = "1")
    @JsonProperty("user_id")
    private Long userId;

    @Schema(description = "Plan ID", example = "1")
    @JsonProperty("plan_id")
    private Long planId;

    @Schema(description = "Subscription ID", example = "1")
    @JsonProperty("subscription_id")
    private Long subscriptionId;

    @Schema(description = "Amount", example = "9.99")
    private BigDecimal amount;

    @Schema(description = "Currency", example = "USD")
    private String currency;

    @Schema(description = "Method", example = "PAYPAL")
    private String method;

    @Schema(description = "Status", example = "PAID")
    private String status;

    @Schema(description = "Transaction reference", example = "txn_123456789")
    @JsonProperty("transaction_reference")
    private String transactionReference;

    @Schema(description = "Creation timestamp", example = "2025-01-01T12:00:00")
    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @Schema(description = "Update timestamp", example = "2025-01-01T12:00:00")
    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;
}
