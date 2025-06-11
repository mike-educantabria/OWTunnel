package com.owtunnel.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
@Schema(description = "Vpn Server Response")
public class VpnServerResponse {

    @Schema(description = "ID", example = "1")
    private Long id;

    @Schema(description = "Country", example = "Germany")
    private String country;

    @Schema(description = "City", example = "Frankfurt")
    private String city;

    @Schema(description = "Hostname", example = "vpn.example.com")
    private String hostname;

    @Schema(description = "IP address", example = "192.168.1.1")
    @JsonProperty("ip_address")
    private String ipAddress;

    @Schema(description = "Configuration file URL", example = "https://example.com/config.ovpn")
    @JsonProperty("config_file_url")
    private String configFileUrl;

    @Schema(description = "Is free", example = "false")
    @JsonProperty("is_free")
    private Boolean isFree;

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
