package com.erp.assurance.tunisie.accounting.service;

import com.erp.assurance.tunisie.accounting.entity.GeneralLedger;
import com.erp.assurance.tunisie.accounting.repository.GeneralLedgerRepository;
import com.erp.assurance.tunisie.accounting.repository.JournalEntryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountingEngineTest {

    @Mock private JournalEntryRepository journalEntryRepository;
    @Mock private GeneralLedgerRepository generalLedgerRepository;
    @InjectMocks private AccountingEngine accountingEngine;

    private LocalDate periodStart;
    private LocalDate periodEnd;

    @BeforeEach
    void setUp() {
        periodStart = LocalDate.of(2024, 1, 1);
        periodEnd = LocalDate.of(2024, 1, 31);
    }

    @Test
    void computeLedgerEntry_PositiveBalance() {
        String accountCode = "7001";
        when(journalEntryRepository.sumDebitByAccountAndPeriod(eq(accountCode), eq(periodStart), eq(periodEnd)))
                .thenReturn(new BigDecimal("10000.000"));
        when(journalEntryRepository.sumCreditByAccountAndPeriod(eq(accountCode), eq(periodStart), eq(periodEnd)))
                .thenReturn(new BigDecimal("3000.000"));

        GeneralLedger ledger = GeneralLedger.builder()
                .accountCode(accountCode)
                .periodDate(periodEnd)
                .totalDebit(new BigDecimal("10000.000"))
                .totalCredit(new BigDecimal("3000.000"))
                .balance(new BigDecimal("7000.000"))
                .build();
        when(generalLedgerRepository.save(any())).thenReturn(ledger);

        GeneralLedger result = accountingEngine.computeLedgerEntry(accountCode, periodStart, periodEnd);

        assertThat(result).isNotNull();
        assertThat(result.getBalance()).isEqualByComparingTo(new BigDecimal("7000.000"));
        assertThat(result.getTotalDebit()).isEqualByComparingTo(new BigDecimal("10000.000"));
        assertThat(result.getTotalCredit()).isEqualByComparingTo(new BigDecimal("3000.000"));
        verify(generalLedgerRepository).save(any());
    }

    @Test
    void computeLedgerEntry_NegativeBalance() {
        String accountCode = "4001";
        when(journalEntryRepository.sumDebitByAccountAndPeriod(eq(accountCode), eq(periodStart), eq(periodEnd)))
                .thenReturn(new BigDecimal("1000.000"));
        when(journalEntryRepository.sumCreditByAccountAndPeriod(eq(accountCode), eq(periodStart), eq(periodEnd)))
                .thenReturn(new BigDecimal("5000.000"));

        GeneralLedger ledger = GeneralLedger.builder()
                .accountCode(accountCode)
                .periodDate(periodEnd)
                .totalDebit(new BigDecimal("1000.000"))
                .totalCredit(new BigDecimal("5000.000"))
                .balance(new BigDecimal("-4000.000"))
                .build();
        when(generalLedgerRepository.save(any())).thenReturn(ledger);

        GeneralLedger result = accountingEngine.computeLedgerEntry(accountCode, periodStart, periodEnd);

        assertThat(result.getBalance()).isEqualByComparingTo(new BigDecimal("-4000.000"));
    }

    @Test
    void computeLedgerEntry_ZeroBalance() {
        String accountCode = "6001";
        when(journalEntryRepository.sumDebitByAccountAndPeriod(eq(accountCode), eq(periodStart), eq(periodEnd)))
                .thenReturn(new BigDecimal("5000.000"));
        when(journalEntryRepository.sumCreditByAccountAndPeriod(eq(accountCode), eq(periodStart), eq(periodEnd)))
                .thenReturn(new BigDecimal("5000.000"));

        GeneralLedger ledger = GeneralLedger.builder()
                .accountCode(accountCode)
                .periodDate(periodEnd)
                .totalDebit(new BigDecimal("5000.000"))
                .totalCredit(new BigDecimal("5000.000"))
                .balance(BigDecimal.ZERO)
                .build();
        when(generalLedgerRepository.save(any())).thenReturn(ledger);

        GeneralLedger result = accountingEngine.computeLedgerEntry(accountCode, periodStart, periodEnd);

        assertThat(result.getBalance()).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    void computeLedgerEntry_PersistsToRepository() {
        String accountCode = "7002";
        when(journalEntryRepository.sumDebitByAccountAndPeriod(any(), any(), any()))
                .thenReturn(BigDecimal.ZERO);
        when(journalEntryRepository.sumCreditByAccountAndPeriod(any(), any(), any()))
                .thenReturn(BigDecimal.ZERO);
        when(generalLedgerRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        GeneralLedger result = accountingEngine.computeLedgerEntry(accountCode, periodStart, periodEnd);

        assertThat(result.getAccountCode()).isEqualTo(accountCode);
        assertThat(result.getPeriodDate()).isEqualTo(periodEnd);
        verify(generalLedgerRepository).save(any());
    }
}
