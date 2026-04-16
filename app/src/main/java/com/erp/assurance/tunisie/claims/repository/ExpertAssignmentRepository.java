package com.erp.assurance.tunisie.claims.repository;

import com.erp.assurance.tunisie.claims.entity.ExpertAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ExpertAssignmentRepository extends JpaRepository<ExpertAssignment, UUID> {
    List<ExpertAssignment> findByClaimId(UUID claimId);
    List<ExpertAssignment> findByAssignmentStatus(ExpertAssignment.AssignmentStatus status);
}
