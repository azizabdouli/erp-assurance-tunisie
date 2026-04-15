package com.erp.assurance.tunisie.underwriting.controller;

import com.erp.assurance.tunisie.shared.dto.ApiResponse;
import com.erp.assurance.tunisie.shared.dto.PageResponse;
import com.erp.assurance.tunisie.underwriting.dto.QuotationRequest;
import com.erp.assurance.tunisie.underwriting.dto.QuotationResponse;
import com.erp.assurance.tunisie.underwriting.service.QuotationService;
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
@RequestMapping("/quotations")
@RequiredArgsConstructor
@Tag(name = "Quotation Management", description = "Insurance quotation management")
public class QuotationController {

    private final QuotationService quotationService;

    @PostMapping
    @Operation(summary = "Create quotation")
    public ResponseEntity<ApiResponse<QuotationResponse>> createQuotation(@Valid @RequestBody QuotationRequest request) {
        QuotationResponse response = quotationService.createQuotation(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Quotation created", response));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get quotation by ID")
    public ResponseEntity<ApiResponse<QuotationResponse>> getQuotation(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(quotationService.getQuotation(id)));
    }

    @GetMapping("/client/{clientId}")
    @Operation(summary = "Get quotations by client")
    public ResponseEntity<ApiResponse<PageResponse<QuotationResponse>>> getQuotationsByClient(
            @PathVariable UUID clientId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(ApiResponse.success(quotationService.getQuotationsByClient(clientId, PageRequest.of(page, size))));
    }

    @PostMapping("/{id}/approve")
    @Operation(summary = "Approve quotation")
    public ResponseEntity<ApiResponse<QuotationResponse>> approveQuotation(
            @PathVariable UUID id,
            @RequestParam String approvedBy) {
        return ResponseEntity.ok(ApiResponse.success("Quotation approved", quotationService.approveQuotation(id, approvedBy)));
    }
}
