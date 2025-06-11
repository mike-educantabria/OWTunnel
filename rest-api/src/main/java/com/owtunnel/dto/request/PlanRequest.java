package com.owtunnel.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter @Setter
@Schema(description = "Plan Request")
public class PlanRequest {

    @Schema(description = "Name", example = "Basic Plan")
    @NotBlank @Size(max = 90)
    private String name;

    @Schema(description = "Description", example = "Basic plan description")
    private String description;

    @Schema(description = "Price", example = "9.99")
    @NotNull
    private BigDecimal price;

    @Schema(description = "Currency code", example = "USD")
    @Pattern(
            regexp = "USD|EUR",
            message = "Currency must be USD or EUR"
    )
    private String currency;

    @Schema(description = "Duration in days", example = "30")
    @NotNull
    private Integer durationDays;

    @Schema(description = "Is active", example = "true")
    @NotNull
    private Boolean isActive;
}
