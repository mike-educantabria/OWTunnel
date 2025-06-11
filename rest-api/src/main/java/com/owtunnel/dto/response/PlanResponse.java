package com.owtunnel.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter @Setter
@Schema(description = "Plan Response")
public class PlanResponse {

    @Schema(description = "ID", example = "1")
    private Long id;

    @Schema(description = "Name", example = "Basic Plan")
    private String name;

    @Schema(description = "Description", example = "Basic plan description")
    private String description;

    @Schema(description = "Price", example = "9.99")
    private BigDecimal price;

    @Schema(description = "Currency code", example = "USD")
    private String currency;

    @Schema(description = "Duration in days", example = "30")
    @JsonProperty("duration_days")
    private Integer durationDays;

    @Schema(description = "Is active", example = "true")
    @JsonProperty("is_active")
    private Boolean isActive;

    @Schema(description = "Creation timestamp", example = "2025-01-01T12:00:00")
    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @Schema(description = "Update timestamp", example = "2025-01-01T12:00:00")
    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;
}
