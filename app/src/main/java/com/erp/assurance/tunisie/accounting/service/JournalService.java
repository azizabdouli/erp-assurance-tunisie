package com.erp.assurance.tunisie.accounting.service;

import com.erp.assurance.tunisie.accounting.entity.Journal;
import com.erp.assurance.tunisie.accounting.entity.JournalEntry;
import com.erp.assurance.tunisie.accounting.repository.JournalEntryRepository;
import com.erp.assurance.tunisie.accounting.repository.JournalRepository;
import com.erp.assurance.tunisie.shared.dto.PageResponse;
import com.erp.assurance.tunisie.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class JournalService {

    private final JournalRepository journalRepository;
    private final JournalEntryRepository journalEntryRepository;

    public JournalEntry createEntry(UUID journalId, String accountCode, String description,
                                     BigDecimal debitAmount, BigDecimal creditAmount,
                                     String reference, String referenceType) {
        Journal journal = journalRepository.findById(journalId)
                .orElseThrow(() -> new ResourceNotFoundException("Journal", "id", journalId));

        String entryNumber = "JE-" + System.currentTimeMillis();

        JournalEntry entry = JournalEntry.builder()
                .journal(journal)
                .entryNumber(entryNumber)
                .entryDate(LocalDate.now())
                .accountCode(accountCode)
                .description(description)
                .debitAmount(debitAmount != null ? debitAmount : BigDecimal.ZERO)
                .creditAmount(creditAmount != null ? creditAmount : BigDecimal.ZERO)
                .reference(reference)
                .referenceType(referenceType)
                .build();

        JournalEntry saved = journalEntryRepository.save(entry);
        log.info("Journal entry created: {} for account: {}", entryNumber, accountCode);
        return saved;
    }

    @Transactional(readOnly = true)
    public PageResponse<JournalEntry> getEntriesByJournal(UUID journalId, Pageable pageable) {
        Page<JournalEntry> page = journalEntryRepository.findByJournalId(journalId, pageable);
        return PageResponse.from(page);
    }

    @Transactional(readOnly = true)
    public PageResponse<JournalEntry> getEntriesByAccount(String accountCode, Pageable pageable) {
        Page<JournalEntry> page = journalEntryRepository.findByAccountCode(accountCode, pageable);
        return PageResponse.from(page);
    }
}
