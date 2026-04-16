package com.erp.assurance.tunisie.accounting.repository;

import com.erp.assurance.tunisie.accounting.entity.GeneralLedger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface GeneralLedgerRepository extends JpaRepository<GeneralLedger, UUID> {
    List<GeneralLedger> findByAccountCode(String accountCode);
    List<GeneralLedger> findByPeriodDateBetween(LocalDate start, LocalDate end);
}
