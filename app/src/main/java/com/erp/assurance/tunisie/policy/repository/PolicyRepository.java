package com.erp.assurance.tunisie.policy.repository;

import com.erp.assurance.tunisie.policy.entity.Policy;
import com.erp.assurance.tunisie.shared.enums.PolicyStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PolicyRepository extends JpaRepository<Policy, UUID> {
    Optional<Policy> findByPolicyNumber(String policyNumber);
    Page<Policy> findByClientId(UUID clientId, Pageable pageable);
    Page<Policy> findByStatus(PolicyStatus status, Pageable pageable);
    List<Policy> findByStatusAndExpiryDateBefore(PolicyStatus status, LocalDate date);

    @Query("SELECT p FROM Policy p WHERE p.status = :status AND p.expiryDate BETWEEN :start AND :end")
    List<Policy> findPoliciesForRenewal(@Param("status") PolicyStatus status,
                                        @Param("start") LocalDate start,
                                        @Param("end") LocalDate end);
}
