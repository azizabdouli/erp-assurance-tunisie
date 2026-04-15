package com.erp.assurance.tunisie.indemnity.repository;

import com.erp.assurance.tunisie.indemnity.entity.Settlement;
import com.erp.assurance.tunisie.shared.enums.SettlementStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SettlementRepository extends JpaRepository<Settlement, UUID> {
    Optional<Settlement> findBySettlementNumber(String settlementNumber);
    Page<Settlement> findByClaimId(UUID claimId, Pageable pageable);
    Page<Settlement> findByStatus(SettlementStatus status, Pageable pageable);
}
