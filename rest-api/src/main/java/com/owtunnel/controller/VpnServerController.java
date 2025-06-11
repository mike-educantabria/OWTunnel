package com.owtunnel.controller;

import com.owtunnel.dto.request.VpnServerRequest;
import com.owtunnel.dto.response.VpnServerResponse;
import com.owtunnel.service.VpnServerService;
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
@Tag(name = "VPN Servers")
@RestController
@RequestMapping("/vpn-servers")
public class VpnServerController {

    private final VpnServerService vpnServerService;

    @Operation(summary = "(get all VPN servers)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Retrieved all VPN servers successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<List<VpnServerResponse>> getAll() {
        return ResponseEntity.ok(vpnServerService.getAll());
    }

    @Operation(summary = "(get a VPN server by ID)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Retrieved VPN server successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "VPN server not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}")
    public ResponseEntity<VpnServerResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(vpnServerService.getById(id));
    }

    @Operation(summary = "(create a new VPN server)")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "VPN server created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "409", description = "Conflict occurred"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<VpnServerResponse> create(@Valid @RequestBody VpnServerRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(vpnServerService.create(request));
    }

    @Operation(summary = "(update an existing VPN server)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "VPN server updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "VPN server not found"),
            @ApiResponse(responseCode = "409", description = "Conflict occurred"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{id}")
    public ResponseEntity<VpnServerResponse> update(@PathVariable Long id, @Valid @RequestBody VpnServerRequest request) {
        return ResponseEntity.ok(vpnServerService.update(id, request));
    }

    @Operation(summary = "(delete a VPN server by ID)")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "VPN server deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "VPN server not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        vpnServerService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
