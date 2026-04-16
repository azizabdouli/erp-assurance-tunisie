package com.erp.assurance.tunisie.claims.service;

import com.erp.assurance.tunisie.claims.dto.ClaimDeclarationRequest;
import com.erp.assurance.tunisie.claims.dto.ClaimResponse;
import com.erp.assurance.tunisie.claims.entity.Claim;
import com.erp.assurance.tunisie.claims.entity.ClaimStatusHistory;
import com.erp.assurance.tunisie.claims.repository.ClaimRepository;
import com.erp.assurance.tunisie.policy.entity.Policy;
import com.erp.assurance.tunisie.policy.repository.PolicyRepository;
import com.erp.assurance.tunisie.shared.dto.PageResponse;
import com.erp.assurance.tunisie.shared.enums.ClaimStatus;
import com.erp.assurance.tunisie.shared.enums.PolicyStatus;
import com.erp.assurance.tunisie.shared.exception.BusinessException;
import com.erp.assurance.tunisie.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ClaimService {

    private final ClaimRepository claimRepository;
    private final PolicyRepository policyRepository;

    public ClaimResponse declareClaim(ClaimDeclarationRequest request) {
        Policy policy = policyRepository.findById(request.getPolicyId())
                .orElseThrow(() -> new ResourceNotFoundException("Policy", "id", request.getPolicyId()));

        if (policy.getStatus() != PolicyStatus.ACTIVE) {
            throw new BusinessException("CLM_001", "Claims can only be declared for active policies");
        }

        String claimNumber = "CLM-" + System.currentTimeMillis();

        Claim claim = Claim.builder()
                .claimNumber(claimNumber)
                .policy(policy)
                .incidentDate(request.getIncidentDate())
                .declarationDate(LocalDateTime.now())
                .incidentDescription(request.getIncidentDescription())
                .incidentLocation(request.getIncidentLocation())
                .estimatedAmount(request.getEstimatedAmount())
                .build();

        Claim saved = claimRepository.save(claim);
        addStatusHistory(saved, null, ClaimStatus.DECLARED, "Claim declared");
        log.info("Claim declared: {}", saved.getClaimNumber());
        return mapToResponse(saved);
    }

    public ClaimResponse updateClaimStatus(UUID claimId, ClaimStatus newStatus, String comments) {
        Claim claim = findClaimOrThrow(claimId);
        ClaimStatus oldStatus = claim.getStatus();
        claim.setStatus(newStatus);
        Claim updated = claimRepository.save(claim);
        addStatusHistory(updated, oldStatus, newStatus, comments);
        log.info("Claim {} status changed: {} -> {}", claim.getClaimNumber(), oldStatus, newStatus);
        return mapToResponse(updated);
    }

    @Transactional(readOnly = true)
    public ClaimResponse getClaim(UUID id) {
        return mapToResponse(findClaimOrThrow(id));
    }

    @Transactional(readOnly = true)
    public PageResponse<ClaimResponse> getClaimsByPolicy(UUID policyId, Pageable pageable) {
        Page<Claim> page = claimRepository.findByPolicyId(policyId, pageable);
        return PageResponse.from(page.map(this::mapToResponse));
    }

    @Transactional(readOnly = true)
    public PageResponse<ClaimResponse> getClaimsByStatus(ClaimStatus status, Pageable pageable) {
        Page<Claim> page = claimRepository.findByStatus(status, pageable);
        return PageResponse.from(page.map(this::mapToResponse));
    }

    @Transactional(readOnly = true)
    public PageResponse<ClaimResponse> getFraudFlaggedClaims(Pageable pageable) {
        Page<Claim> page = claimRepository.findByFraudFlaggedTrue(pageable);
        return PageResponse.from(page.map(this::mapToResponse));
    }

    private void addStatusHistory(Claim claim, ClaimStatus oldStatus, ClaimStatus newStatus, String comments) {
        ClaimStatusHistory history = ClaimStatusHistory.builder()
                .claim(claim)
                .oldStatus(oldStatus)
                .newStatus(newStatus)
                .changedAt(LocalDateTime.now())
                .comments(comments)
                .build();
        claim.getStatusHistory().add(history);
    }

    private Claim findClaimOrThrow(UUID id) {
        return claimRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Claim", "id", id));
    }

    private ClaimResponse mapToResponse(Claim c) {
        return ClaimResponse.builder()
                .id(c.getId())
                .claimNumber(c.getClaimNumber())
                .policyId(c.getPolicy().getId())
                .policyNumber(c.getPolicy().getPolicyNumber())
                .status(c.getStatus())
                .incidentDate(c.getIncidentDate())
                .declarationDate(c.getDeclarationDate())
                .incidentDescription(c.getIncidentDescription())
                .incidentLocation(c.getIncidentLocation())
                .estimatedAmount(c.getEstimatedAmount())
                .approvedAmount(c.getApprovedAmount())
                .deductibleAmount(c.getDeductibleAmount())
                .fraudFlagged(c.isFraudFlagged())
                .fraudScore(c.getFraudScore())
                .createdAt(c.getCreatedAt())
                .build();
    }
}
