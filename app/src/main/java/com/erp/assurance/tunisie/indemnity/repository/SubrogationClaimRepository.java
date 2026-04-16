package com.erp.assurance.tunisie.indemnity.repository;

import com.erp.assurance.tunisie.indemnity.entity.SubrogationClaim;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SubrogationClaimRepository extends JpaRepository<SubrogationClaim, UUID> {
    List<SubrogationClaim> findByClaimId(UUID claimId);
    List<SubrogationClaim> findBySubrogationStatus(SubrogationClaim.SubrogationStatus status);
}
