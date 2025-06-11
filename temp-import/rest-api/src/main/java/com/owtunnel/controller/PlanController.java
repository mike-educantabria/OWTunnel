package com.owtunnel.controller;

import com.owtunnel.dto.request.PlanRequest;
import com.owtunnel.dto.response.PlanResponse;
import com.owtunnel.service.PlanService;
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
@Tag(name = "Plans")
@RestController
@RequestMapping("/plans")
public class PlanController {

    private final PlanService planService;

    @Operation(summary = "(get all plans)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Retrieved all plans successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<List<PlanResponse>> getAll() {
        return ResponseEntity.ok(planService.getAll());
    }

    @Operation(summary = "(get a plan by ID)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Retrieved plan successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Plan not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}")
    public ResponseEntity<PlanResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(planService.getById(id));
    }

    @Operation(summary = "(create a new plan)")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Plan created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "409", description = "Conflict occurred"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<PlanResponse> create(@Valid @RequestBody PlanRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(planService.create(request));
    }

    @Operation(summary = "(update an existing plan)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Plan updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Plan not found"),
            @ApiResponse(responseCode = "409", description = "Conflict occurred"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{id}")
    public ResponseEntity<PlanResponse> update(@PathVariable Long id, @Valid @RequestBody PlanRequest request) {
        return ResponseEntity.ok(planService.update(id, request));
    }

    @Operation(summary = "(delete a plan by ID)")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Plan deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Plan not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        planService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
