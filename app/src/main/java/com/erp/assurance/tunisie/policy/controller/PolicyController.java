package com.erp.assurance.tunisie.policy.controller;

import com.erp.assurance.tunisie.policy.dto.EndorsementRequest;
import com.erp.assurance.tunisie.policy.dto.PolicyCreateRequest;
import com.erp.assurance.tunisie.policy.dto.PolicyResponse;
import com.erp.assurance.tunisie.policy.service.EndorsementService;
import com.erp.assurance.tunisie.policy.service.PolicyService;
import com.erp.assurance.tunisie.shared.dto.ApiResponse;
import com.erp.assurance.tunisie.shared.dto.PageResponse;
import com.erp.assurance.tunisie.shared.enums.PolicyStatus;
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
@RequestMapping("/policies")
@RequiredArgsConstructor
@Tag(name = "Policy Management", description = "Insurance policy management")
public class PolicyController {

    private final PolicyService policyService;
    private final EndorsementService endorsementService;

    @PostMapping
    @Operation(summary = "Create policy")
    public ResponseEntity<ApiResponse<PolicyResponse>> createPolicy(@Valid @RequestBody PolicyCreateRequest request) {
        PolicyResponse response = policyService.createPolicy(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Policy created", response));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get policy by ID")
    public ResponseEntity<ApiResponse<PolicyResponse>> getPolicy(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(policyService.getPolicy(id)));
    }

    @PostMapping("/{id}/activate")
    @Operation(summary = "Activate policy")
    public ResponseEntity<ApiResponse<PolicyResponse>> activatePolicy(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success("Policy activated", policyService.activatePolicy(id)));
    }

    @PostMapping("/{id}/cancel")
    @Operation(summary = "Cancel policy")
    public ResponseEntity<ApiResponse<PolicyResponse>> cancelPolicy(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success("Policy cancelled", policyService.cancelPolicy(id)));
    }

    @GetMapping("/client/{clientId}")
    @Operation(summary = "Get policies by client")
    public ResponseEntity<ApiResponse<PageResponse<PolicyResponse>>> getPoliciesByClient(
            @PathVariable UUID clientId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(ApiResponse.success(policyService.getPoliciesByClient(clientId, PageRequest.of(page, size))));
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get policies by status")
    public ResponseEntity<ApiResponse<PageResponse<PolicyResponse>>> getPoliciesByStatus(
            @PathVariable PolicyStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(ApiResponse.success(policyService.getPoliciesByStatus(status, PageRequest.of(page, size))));
    }

    @PostMapping("/{id}/endorsements")
    @Operation(summary = "Create endorsement")
    public ResponseEntity<ApiResponse<Void>> createEndorsement(@PathVariable UUID id, @Valid @RequestBody EndorsementRequest request) {
        endorsementService.createEndorsement(id, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Endorsement created", null));
    }
}
