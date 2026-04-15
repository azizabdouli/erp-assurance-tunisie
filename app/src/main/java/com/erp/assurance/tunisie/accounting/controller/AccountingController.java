package com.erp.assurance.tunisie.accounting.controller;

import com.erp.assurance.tunisie.accounting.entity.JournalEntry;
import com.erp.assurance.tunisie.accounting.service.JournalService;
import com.erp.assurance.tunisie.shared.dto.ApiResponse;
import com.erp.assurance.tunisie.shared.dto.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/accounting")
@RequiredArgsConstructor
@Tag(name = "Accounting", description = "Financial accounting operations")
public class AccountingController {

    private final JournalService journalService;

    @GetMapping("/journals/{journalId}/entries")
    @Operation(summary = "Get journal entries")
    public ResponseEntity<ApiResponse<PageResponse<JournalEntry>>> getJournalEntries(
            @PathVariable UUID journalId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(ApiResponse.success(journalService.getEntriesByJournal(journalId, PageRequest.of(page, size))));
    }

    @GetMapping("/accounts/{accountCode}/entries")
    @Operation(summary = "Get entries by account")
    public ResponseEntity<ApiResponse<PageResponse<JournalEntry>>> getAccountEntries(
            @PathVariable String accountCode,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(ApiResponse.success(journalService.getEntriesByAccount(accountCode, PageRequest.of(page, size))));
    }
}
