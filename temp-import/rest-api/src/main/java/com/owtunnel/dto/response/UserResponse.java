package com.owtunnel.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
@Schema(description = "User Response")
public class UserResponse {

    @Schema(description = "ID", example = "1")
    private Long id;

    @Schema(description = "Email", example = "user@example.com")
    private String email;

    @Schema(description = "First name", example = "John")
    @JsonProperty("first_name")
    private String firstName;

    @Schema(description = "Last name", example = "Doe")
    @JsonProperty("last_name")
    private String lastName;

    @Schema(description = "Locale", example = "en-US")
    private String locale;

    @Schema(description = "Role", example = "USER")
    private String role;

    @Schema(description = "Creation timestamp", example = "2025-01-01T12:00:00")
    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @Schema(description = "Update timestamp", example = "2025-01-01T12:00:00")
    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;
}
