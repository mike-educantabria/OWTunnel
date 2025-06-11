package com.owtunnel.controller;

import com.owtunnel.dto.request.LoginAttemptRequest;
import com.owtunnel.dto.response.LoginAttemptResponse;
import com.owtunnel.service.LoginAttemptService;
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
@Tag(name = "Login Attempts")
@RestController
@RequestMapping("/login-attempts")
public class LoginAttemptController {

    private final LoginAttemptService loginAttemptService;

    @Operation(summary = "(get all login attempts)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Retrieved all login attempts successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<List<LoginAttemptResponse>> getAll() {
        return ResponseEntity.ok(loginAttemptService.getAll());
    }

    @Operation(summary = "(get a login attempt by ID)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Retrieved login attempt successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Login attempt not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}")
    public ResponseEntity<LoginAttemptResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(loginAttemptService.getById(id));
    }

    @Operation(summary = "(create a new login attempt)")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Login attempt created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "409", description = "Conflict occurred"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<LoginAttemptResponse> create(@Valid @RequestBody LoginAttemptRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(loginAttemptService.create(request));
    }

    @Operation(summary = "(update an existing login attempt)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login attempt updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Login attempt not found"),
            @ApiResponse(responseCode = "409", description = "Conflict occurred"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{id}")
    public ResponseEntity<LoginAttemptResponse> update(@PathVariable Long id, @Valid @RequestBody LoginAttemptRequest request) {
        return ResponseEntity.ok(loginAttemptService.update(id, request));
    }

    @Operation(summary = "(delete a login attempt by ID)")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Login attempt deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Login attempt not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        loginAttemptService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
