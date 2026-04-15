package com.erp.assurance.tunisie.accounting.service;

import com.erp.assurance.tunisie.accounting.entity.GeneralLedger;
import com.erp.assurance.tunisie.accounting.repository.GeneralLedgerRepository;
import com.erp.assurance.tunisie.accounting.repository.JournalEntryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AccountingEngine {

    private final JournalEntryRepository journalEntryRepository;
    private final GeneralLedgerRepository generalLedgerRepository;

    public GeneralLedger computeLedgerEntry(String accountCode, LocalDate periodStart, LocalDate periodEnd) {
        BigDecimal totalDebit = journalEntryRepository.sumDebitByAccountAndPeriod(accountCode, periodStart, periodEnd);
        BigDecimal totalCredit = journalEntryRepository.sumCreditByAccountAndPeriod(accountCode, periodStart, periodEnd);
        BigDecimal balance = totalDebit.subtract(totalCredit);

        GeneralLedger ledger = GeneralLedger.builder()
                .accountCode(accountCode)
                .periodDate(periodEnd)
                .totalDebit(totalDebit)
                .totalCredit(totalCredit)
                .balance(balance)
                .build();

        GeneralLedger saved = generalLedgerRepository.save(ledger);
        log.info("Ledger entry computed for account {} period {}-{}: balance={}", accountCode, periodStart, periodEnd, balance);
        return saved;
    }
}
