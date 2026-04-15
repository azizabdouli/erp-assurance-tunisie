package com.erp.assurance.tunisie.underwriting.repository;

import com.erp.assurance.tunisie.shared.enums.QuotationStatus;
import com.erp.assurance.tunisie.underwriting.entity.Quotation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface QuotationRepository extends JpaRepository<Quotation, UUID> {
    Optional<Quotation> findByQuotationNumber(String quotationNumber);

    Page<Quotation> findByClientId(UUID clientId, Pageable pageable);

    Page<Quotation> findByStatus(QuotationStatus status, Pageable pageable);

    long countByStatus(QuotationStatus status);
}
