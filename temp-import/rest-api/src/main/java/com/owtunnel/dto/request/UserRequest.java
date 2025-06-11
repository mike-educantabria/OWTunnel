package com.owtunnel.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Schema(description = "User Request")
public class UserRequest {

    @Schema(description = "Email", example = "user@example.com")
    @Email @NotBlank @Size(max = 90)
    private String email;

    @Schema(description = "Plain password", example = "password123")
    @Size(min = 8, max = 100)
    private String password;

    @Schema(description = "First name", example = "John")
    @Size(max = 45)
    private String firstName;

    @Schema(description = "Last name", example = "Doe")
    @Size(max = 45)
    private String lastName;

    @Schema(description = "Locale", example = "en-US")
    @Size(max = 45)
    private String locale;

    @Schema(description = "Role", example = "USER")
    @Pattern(
            regexp = "USER|SUPPORT|ADMINISTRATOR",
            message = "Role must be USER, SUPPORT or ADMINISTRATOR"
    )
    private String role;
}
