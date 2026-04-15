package com.erp.assurance.tunisie.accounting.repository;

import com.erp.assurance.tunisie.accounting.entity.Journal;
import com.erp.assurance.tunisie.shared.enums.JournalType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface JournalRepository extends JpaRepository<Journal, UUID> {
    Optional<Journal> findByJournalCode(String journalCode);
    List<Journal> findByJournalType(JournalType journalType);
}
