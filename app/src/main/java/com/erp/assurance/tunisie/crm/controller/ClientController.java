package com.erp.assurance.tunisie.crm.controller;

import com.erp.assurance.tunisie.crm.dto.*;
import com.erp.assurance.tunisie.crm.service.ClientService;
import com.erp.assurance.tunisie.shared.dto.ApiResponse;
import com.erp.assurance.tunisie.shared.dto.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/clients")
@RequiredArgsConstructor
@Tag(name = "Client Management", description = "CRM Client CRUD and management operations")
public class ClientController {

    private final ClientService clientService;

    @PostMapping
    @Operation(summary = "Create a new client")
    public ResponseEntity<ApiResponse<ClientResponse>> createClient(@Valid @RequestBody ClientCreateRequest request) {
        ClientResponse response = clientService.createClient(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Client created successfully", response));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get client by ID")
    public ResponseEntity<ApiResponse<ClientResponse>> getClient(@PathVariable UUID id) {
        ClientResponse response = clientService.getClientById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update client")
    public ResponseEntity<ApiResponse<ClientResponse>> updateClient(@PathVariable UUID id, @Valid @RequestBody ClientUpdateRequest request) {
        ClientResponse response = clientService.updateClient(id, request);
        return ResponseEntity.ok(ApiResponse.success("Client updated successfully", response));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete client (soft delete)")
    public ResponseEntity<ApiResponse<Void>> deleteClient(@PathVariable UUID id) {
        clientService.deleteClient(id);
        return ResponseEntity.ok(ApiResponse.success("Client deleted successfully", null));
    }

    @GetMapping
    @Operation(summary = "Get all clients")
    public ResponseEntity<ApiResponse<PageResponse<ClientResponse>>> getAllClients(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        PageResponse<ClientResponse> response = clientService.getAllClients(pageable);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/search")
    @Operation(summary = "Search clients")
    public ResponseEntity<ApiResponse<PageResponse<ClientResponse>>> searchClients(
            @ModelAttribute ClientSearchCriteria criteria,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        PageResponse<ClientResponse> response = clientService.searchClients(criteria, pageable);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping("/{id}/kyc/validate")
    @Operation(summary = "Validate client KYC")
    public ResponseEntity<ApiResponse<ClientResponse>> validateKyc(@PathVariable UUID id) {
        ClientResponse response = clientService.validateKyc(id);
        return ResponseEntity.ok(ApiResponse.success("KYC validated successfully", response));
    }

    @PostMapping("/{id}/aml/screening")
    @Operation(summary = "Perform AML screening")
    public ResponseEntity<ApiResponse<ClientResponse>> performAmlScreening(@PathVariable UUID id) {
        ClientResponse response = clientService.performAmlScreening(id);
        return ResponseEntity.ok(ApiResponse.success("AML screening completed", response));
    }
}
