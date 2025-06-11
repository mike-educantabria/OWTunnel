package com.owtunnel.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Schema(description = "Connection Request")
public class ConnectionRequest {

    @Schema(description = "User ID", example = "1")
    @NotNull
    private Long userId;

    @Schema(description = "VPN server ID", example = "1")
    @NotNull
    private Long vpnServerId;

    @Schema(description = "Device information", example = "iPhone 16 Pro, iOS 18.0")
    @NotBlank
    private String deviceInfo;

    @Schema(description = "Status", example = "CONNECTED")
    @Pattern(
            regexp = "CONNECTED|DISCONNECTED|TIMEOUT|REJECTED|ERROR",
            message = "Status must be one of CONNECTED, DISCONNECTED, TIMEOUT, REJECTED, ERROR"
    )
    private String status;
}
