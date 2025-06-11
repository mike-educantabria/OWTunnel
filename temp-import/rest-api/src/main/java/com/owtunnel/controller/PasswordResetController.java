package com.owtunnel.controller;

import com.owtunnel.dto.request.PasswordResetRequest;
import com.owtunnel.dto.response.PasswordResetResponse;
import com.owtunnel.service.PasswordResetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Tag(name = "Password Reset")
@RestController
@RequestMapping("/password-reset")
public class PasswordResetController {

    private final PasswordResetService passwordResetService;

    @Operation(summary = "(get all password resets)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Retrieved all password resets successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<List<PasswordResetResponse>> getAll() {
        return ResponseEntity.ok(passwordResetService.getAll());
    }

    @Operation(summary = "(get a password reset by ID)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Retrieved password reset successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Password reset not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}")
    public ResponseEntity<PasswordResetResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(passwordResetService.getById(id));
    }

    @Operation(summary = "(create a new password reset)")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Password reset created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "409", description = "Conflict occurred"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<PasswordResetResponse> create(@Valid @RequestBody PasswordResetRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(passwordResetService.create(request));
    }

    @Operation(summary = "(update an existing password reset)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Password reset updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Password reset not found"),
            @ApiResponse(responseCode = "409", description = "Conflict occurred"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{id}")
    public ResponseEntity<PasswordResetResponse> update(@PathVariable Long id, @Valid @RequestBody PasswordResetRequest request) {
        return ResponseEntity.ok(passwordResetService.update(id, request));
    }

    @Operation(summary = "(delete a password reset by ID)")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Password reset deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Password reset not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        passwordResetService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
