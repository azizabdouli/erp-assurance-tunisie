package com.erp.assurance.tunisie.claims.controller;

import com.erp.assurance.tunisie.claims.dto.ClaimDeclarationRequest;
import com.erp.assurance.tunisie.claims.dto.ClaimResponse;
import com.erp.assurance.tunisie.claims.service.ClaimService;
import com.erp.assurance.tunisie.claims.service.ExpertAssignmentService;
import com.erp.assurance.tunisie.shared.dto.ApiResponse;
import com.erp.assurance.tunisie.shared.dto.PageResponse;
import com.erp.assurance.tunisie.shared.enums.ClaimStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/claims")
@RequiredArgsConstructor
@Tag(name = "Claims Management", description = "Insurance claims management")
@SecurityRequirement(name = "Bearer Authentication")
public class ClaimController {

    private final ClaimService claimService;
    private final ExpertAssignmentService expertAssignmentService;

    @PostMapping
    @Operation(summary = "Declare a claim")
    public ResponseEntity<ApiResponse<ClaimResponse>> declareClaim(@Valid @RequestBody ClaimDeclarationRequest request) {
        ClaimResponse response = claimService.declareClaim(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Claim declared", response));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get claim by ID")
    public ResponseEntity<ApiResponse<ClaimResponse>> getClaim(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(claimService.getClaim(id)));
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Update claim status")
    public ResponseEntity<ApiResponse<ClaimResponse>> updateStatus(
            @PathVariable UUID id,
            @RequestParam ClaimStatus status,
            @RequestParam(required = false) String comments) {
        return ResponseEntity.ok(ApiResponse.success("Status updated", claimService.updateClaimStatus(id, status, comments)));
    }

    @GetMapping("/policy/{policyId}")
    @Operation(summary = "Get claims by policy")
    public ResponseEntity<ApiResponse<PageResponse<ClaimResponse>>> getClaimsByPolicy(
            @PathVariable UUID policyId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(ApiResponse.success(claimService.getClaimsByPolicy(policyId, PageRequest.of(page, size))));
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get claims by status")
    public ResponseEntity<ApiResponse<PageResponse<ClaimResponse>>> getClaimsByStatus(
            @PathVariable ClaimStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(ApiResponse.success(claimService.getClaimsByStatus(status, PageRequest.of(page, size))));
    }

    @GetMapping("/fraud-flagged")
    @Operation(summary = "Get fraud-flagged claims")
    public ResponseEntity<ApiResponse<PageResponse<ClaimResponse>>> getFraudFlaggedClaims(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(ApiResponse.success(claimService.getFraudFlaggedClaims(PageRequest.of(page, size))));
    }

    @PostMapping("/{id}/experts")
    @Operation(summary = "Assign expert to claim")
    public ResponseEntity<ApiResponse<Void>> assignExpert(
            @PathVariable UUID id,
            @RequestParam String expertName,
            @RequestParam(required = false) String speciality) {
        expertAssignmentService.assignExpert(id, expertName, speciality);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Expert assigned", null));
    }
}
