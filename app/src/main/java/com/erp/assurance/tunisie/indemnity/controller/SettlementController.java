package com.erp.assurance.tunisie.indemnity.controller;

import com.erp.assurance.tunisie.indemnity.dto.SettlementRequest;
import com.erp.assurance.tunisie.indemnity.dto.SettlementResponse;
import com.erp.assurance.tunisie.indemnity.service.SettlementService;
import com.erp.assurance.tunisie.shared.dto.ApiResponse;
import com.erp.assurance.tunisie.shared.dto.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/settlements")
@RequiredArgsConstructor
@Tag(name = "Settlement Management", description = "Indemnity settlement management")
public class SettlementController {

    private final SettlementService settlementService;

    @PostMapping
    @Operation(summary = "Create settlement")
    public ResponseEntity<ApiResponse<SettlementResponse>> createSettlement(@Valid @RequestBody SettlementRequest request) {
        SettlementResponse response = settlementService.createSettlement(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Settlement created", response));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get settlement")
    public ResponseEntity<ApiResponse<SettlementResponse>> getSettlement(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(settlementService.getSettlement(id)));
    }

    @PostMapping("/{id}/approve")
    @Operation(summary = "Approve settlement")
    public ResponseEntity<ApiResponse<SettlementResponse>> approveSettlement(
            @PathVariable UUID id, @RequestParam String approvedBy) {
        return ResponseEntity.ok(ApiResponse.success("Settlement approved", settlementService.approveSettlement(id, approvedBy)));
    }

    @GetMapping("/claim/{claimId}")
    @Operation(summary = "Get settlements by claim")
    public ResponseEntity<ApiResponse<PageResponse<SettlementResponse>>> getSettlementsByClaim(
            @PathVariable UUID claimId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(ApiResponse.success(settlementService.getSettlementsByClaim(claimId, PageRequest.of(page, size))));
    }
}
