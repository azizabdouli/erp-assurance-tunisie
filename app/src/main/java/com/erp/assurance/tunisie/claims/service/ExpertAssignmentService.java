package com.erp.assurance.tunisie.claims.service;

import com.erp.assurance.tunisie.claims.entity.Claim;
import com.erp.assurance.tunisie.claims.entity.ExpertAssignment;
import com.erp.assurance.tunisie.claims.repository.ClaimRepository;
import com.erp.assurance.tunisie.claims.repository.ExpertAssignmentRepository;
import com.erp.assurance.tunisie.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ExpertAssignmentService {

    private final ExpertAssignmentRepository expertAssignmentRepository;
    private final ClaimRepository claimRepository;

    public ExpertAssignment assignExpert(UUID claimId, String expertName, String speciality) {
        Claim claim = claimRepository.findById(claimId)
                .orElseThrow(() -> new ResourceNotFoundException("Claim", "id", claimId));

        ExpertAssignment assignment = ExpertAssignment.builder()
                .claim(claim)
                .expertName(expertName)
                .expertSpeciality(speciality)
                .assignmentDate(LocalDate.now())
                .build();

        ExpertAssignment saved = expertAssignmentRepository.save(assignment);
        log.info("Expert {} assigned to claim {}", expertName, claim.getClaimNumber());
        return saved;
    }

    @Transactional(readOnly = true)
    public List<ExpertAssignment> getAssignmentsByClaim(UUID claimId) {
        return expertAssignmentRepository.findByClaimId(claimId);
    }
}
