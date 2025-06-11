package com.owtunnel.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
@Schema(description = "Connection Response")
public class ConnectionResponse {

    @Schema(description = "ID", example = "1")
    private Long id;

    @Schema(description = "User ID", example = "1")
    @JsonProperty("user_id")
    private Long userId;

    @Schema(description = "VPN server ID", example = "1")
    @JsonProperty("vpn_server_id")
    private Long vpnServerId;

    @Schema(description = "Device information", example = "iPhone 16 Pro, iOS 18.0")
    @JsonProperty("device_info")
    private String deviceInfo;

    @Schema(description = "Status", example = "CONNECTED")
    private String status;

    @Schema(description = "Creation timestamp", example = "2025-01-01T12:00:00")
    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @Schema(description = "Update timestamp", example = "2025-01-01T12:00:00")
    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;
}
