package com.erp.assurance.tunisie.accounting.repository;

import com.erp.assurance.tunisie.accounting.entity.JournalEntry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Repository
public interface JournalEntryRepository extends JpaRepository<JournalEntry, UUID> {
    Page<JournalEntry> findByJournalId(UUID journalId, Pageable pageable);
    Page<JournalEntry> findByAccountCode(String accountCode, Pageable pageable);
    Page<JournalEntry> findByEntryDateBetween(LocalDate start, LocalDate end, Pageable pageable);

    @Query("SELECT COALESCE(SUM(je.debitAmount), 0) FROM JournalEntry je WHERE je.accountCode = :accountCode AND je.entryDate BETWEEN :start AND :end")
    BigDecimal sumDebitByAccountAndPeriod(@Param("accountCode") String accountCode, @Param("start") LocalDate start, @Param("end") LocalDate end);

    @Query("SELECT COALESCE(SUM(je.creditAmount), 0) FROM JournalEntry je WHERE je.accountCode = :accountCode AND je.entryDate BETWEEN :start AND :end")
    BigDecimal sumCreditByAccountAndPeriod(@Param("accountCode") String accountCode, @Param("start") LocalDate start, @Param("end") LocalDate end);
}
