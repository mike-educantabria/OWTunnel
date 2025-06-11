package com.owtunnel.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Schema(description = "Vpn Server Request")
public class VpnServerRequest {

    @Schema(description = "Country", example = "Germany")
    @NotBlank @Size(max = 45)
    private String country;

    @Schema(description = "City", example = "Frankfurt")
    @NotBlank @Size(max = 45)
    private String city;

    @Schema(description = "Hostname", example = "vpn.example.com")
    @NotBlank @Size(max = 45)
    private String hostname;

    @Schema(description = "IP address", example = "192.168.1.1")
    @NotBlank @Size(max = 45)
    private String ipAddress;

    @Schema(description = "Configuration file URL", example = "https://example.com/config.ovpn")
    @NotBlank
    private String configFileUrl;

    @JsonProperty("is_free")
    @JsonSetter(nulls = Nulls.SKIP)
    @Schema(description = "Is free", example = "false")
    private Boolean isFree;

    @JsonProperty("is_active")
    @JsonSetter(nulls = Nulls.SKIP)
    @Schema(description = "Is active", example = "true")
    private Boolean isActive;
}
