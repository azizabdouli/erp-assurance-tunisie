package com.erp.assurance.tunisie.policy.repository;

import com.erp.assurance.tunisie.policy.entity.PolicyEndorsement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PolicyEndorsementRepository extends JpaRepository<PolicyEndorsement, UUID> {
    List<PolicyEndorsement> findByPolicyId(UUID policyId);
}
