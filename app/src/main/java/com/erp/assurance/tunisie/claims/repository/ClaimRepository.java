package com.erp.assurance.tunisie.claims.repository;

import com.erp.assurance.tunisie.claims.entity.Claim;
import com.erp.assurance.tunisie.shared.enums.ClaimStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClaimRepository extends JpaRepository<Claim, UUID> {
    Optional<Claim> findByClaimNumber(String claimNumber);
    Page<Claim> findByPolicyId(UUID policyId, Pageable pageable);
    Page<Claim> findByStatus(ClaimStatus status, Pageable pageable);
    Page<Claim> findByFraudFlaggedTrue(Pageable pageable);

    @Query("SELECT c FROM Claim c WHERE c.status = :status AND c.incidentDate BETWEEN :start AND :end")
    Page<Claim> findClaimsByStatusAndDateRange(@Param("status") ClaimStatus status,
                                                @Param("start") LocalDate start,
                                                @Param("end") LocalDate end,
                                                Pageable pageable);

    @Query("SELECT COUNT(c) FROM Claim c WHERE c.policy.id = :policyId")
    long countByPolicyId(@Param("policyId") UUID policyId);
}
