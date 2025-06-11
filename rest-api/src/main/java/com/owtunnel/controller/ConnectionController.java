package com.owtunnel.controller;

import com.owtunnel.dto.request.ConnectionRequest;
import com.owtunnel.dto.response.ConnectionResponse;
import com.owtunnel.service.ConnectionService;
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
@Tag(name = "Connections")
@RestController
@RequestMapping("/connections")
public class ConnectionController {

    private final ConnectionService connectionService;

    @Operation(summary = "(get all connections)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Retrieved all connections successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<List<ConnectionResponse>> getAll() {
        return ResponseEntity.ok(connectionService.getAll());
    }

    @Operation(summary = "(get a connection by ID)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Retrieved connection successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Connection not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ConnectionResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(connectionService.getById(id));
    }

    @Operation(summary = "(create a new connection)")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Connection created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "409", description = "Conflict occurred"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<ConnectionResponse> create(@Valid @RequestBody ConnectionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(connectionService.create(request));
    }

    @Operation(summary = "(update an existing connection)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Connection updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Connection not found"),
            @ApiResponse(responseCode = "409", description = "Conflict occurred"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ConnectionResponse> update(@PathVariable Long id, @Valid @RequestBody ConnectionRequest request) {
        return ResponseEntity.ok(connectionService.update(id, request));
    }

    @Operation(summary = "(delete a connection by ID)")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Connection deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Connection not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        connectionService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
