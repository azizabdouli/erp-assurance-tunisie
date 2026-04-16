package com.erp.assurance.tunisie.policy.repository;

import com.erp.assurance.tunisie.policy.entity.PremiumInvoice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface PremiumInvoiceRepository extends JpaRepository<PremiumInvoice, UUID> {
    List<PremiumInvoice> findByPolicyId(UUID policyId);
    Page<PremiumInvoice> findByPaidFalseAndDueDateBefore(LocalDate date, Pageable pageable);
}
