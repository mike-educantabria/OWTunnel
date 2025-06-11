package com.owtunnel.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter @Setter
@Schema(description = "Payment Request")
public class PaymentRequest {

    @Schema(description = "User ID", example = "1")
    @NotNull
    private Long userId;

    @Schema(description = "Plan ID", example = "1")
    @NotNull
    private Long planId;

    @Schema(description = "Subscription ID", example = "1")
    @NotNull
    private Long subscriptionId;

    @Schema(description = "Amount", example = "9.99")
    @NotNull
    private BigDecimal amount;

    @Schema(description = "Currency", example = "USD")
    @Pattern(
            regexp = "USD|EUR",
            message = "Currency must be USD or EUR"
    )
    private String currency;

    @Schema(description = "Method", example = "PAYPAL")
    @Pattern(
            regexp = "CREDIT_CARD|DEBIT_CARD|PAYPAL|APPLE_PAY|GOOGLE_PAY",
            message = "Method must be CREDIT_CARD, DEBIT_CARD, PAYPAL, APPLE_PAY, or GOOGLE_PAY"
    )
    private String method;

    @Schema(description = "Status", example = "PAID")
    @Pattern(
            regexp = "PENDING|PAID|FAILED|CANCELLED|REFUNDED",
            message = "Status must be PENDING, PAID, FAILED, CANCELLED, or REFUNDED"
    )
    private String status;

    @Schema(description = "Transaction reference", example = "txn_123456789")
    @NotBlank
    private String transactionReference;
}
