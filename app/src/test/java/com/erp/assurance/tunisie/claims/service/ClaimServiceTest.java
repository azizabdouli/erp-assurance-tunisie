package com.erp.assurance.tunisie.claims.service;

import com.erp.assurance.tunisie.claims.dto.ClaimDeclarationRequest;
import com.erp.assurance.tunisie.claims.dto.ClaimResponse;
import com.erp.assurance.tunisie.claims.entity.Claim;
import com.erp.assurance.tunisie.claims.repository.ClaimRepository;
import com.erp.assurance.tunisie.policy.entity.Policy;
import com.erp.assurance.tunisie.policy.repository.PolicyRepository;
import com.erp.assurance.tunisie.shared.enums.ClaimStatus;
import com.erp.assurance.tunisie.shared.enums.PolicyStatus;
import com.erp.assurance.tunisie.shared.exception.BusinessException;
import com.erp.assurance.tunisie.shared.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClaimServiceTest {

    @Mock private ClaimRepository claimRepository;
    @Mock private PolicyRepository policyRepository;
    @InjectMocks private ClaimService claimService;

    private UUID policyId;
    private UUID claimId;
    private Policy activePolicy;
    private Policy cancelledPolicy;
    private Claim testClaim;

    @BeforeEach
    void setUp() {
        policyId = UUID.randomUUID();
        claimId = UUID.randomUUID();

        activePolicy = Policy.builder()
                .policyNumber("POL-001")
                .status(PolicyStatus.ACTIVE)
                .sumInsured(new BigDecimal("50000.000"))
                .expiryDate(LocalDate.now().plusMonths(6))
                .build();
        activePolicy.setId(policyId);

        cancelledPolicy = Policy.builder()
                .policyNumber("POL-002")
                .status(PolicyStatus.CANCELLED)
                .build();
        cancelledPolicy.setId(UUID.randomUUID());

        testClaim = Claim.builder()
                .claimNumber("CLM-001")
                .policy(activePolicy)
                .status(ClaimStatus.DECLARED)
                .incidentDate(LocalDate.now().minusDays(5))
                .declarationDate(LocalDateTime.now())
                .estimatedAmount(new BigDecimal("5000.000"))
                .build();
        testClaim.setId(claimId);
    }

    @Test
    void declareClaim_Success() {
        ClaimDeclarationRequest request = new ClaimDeclarationRequest();
        request.setPolicyId(policyId);
        request.setIncidentDate(LocalDate.now().minusDays(5));
        request.setIncidentDescription("Car accident");
        request.setEstimatedAmount(new BigDecimal("5000.000"));

        when(policyRepository.findById(policyId)).thenReturn(Optional.of(activePolicy));
        when(claimRepository.save(any(Claim.class))).thenReturn(testClaim);

        ClaimResponse response = claimService.declareClaim(request);

        assertThat(response).isNotNull();
        assertThat(response.getClaimNumber()).isEqualTo("CLM-001");
        verify(claimRepository).save(any(Claim.class));
    }

    @Test
    void declareClaim_PolicyNotActive_ThrowsException() {
        ClaimDeclarationRequest request = new ClaimDeclarationRequest();
        request.setPolicyId(cancelledPolicy.getId());

        when(policyRepository.findById(cancelledPolicy.getId())).thenReturn(Optional.of(cancelledPolicy));

        assertThatThrownBy(() -> claimService.declareClaim(request))
                .isInstanceOf(BusinessException.class);
        verify(claimRepository, never()).save(any());
    }

    @Test
    void declareClaim_PolicyNotFound_ThrowsException() {
        ClaimDeclarationRequest request = new ClaimDeclarationRequest();
        request.setPolicyId(policyId);

        when(policyRepository.findById(policyId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> claimService.declareClaim(request))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void updateClaimStatus_Success() {
        when(claimRepository.findById(claimId)).thenReturn(Optional.of(testClaim));
        when(claimRepository.save(any(Claim.class))).thenReturn(testClaim);

        ClaimResponse response = claimService.updateClaimStatus(claimId, ClaimStatus.UNDER_INVESTIGATION, "Starting investigation");

        assertThat(response).isNotNull();
        verify(claimRepository).save(argThat(c -> c.getStatus() == ClaimStatus.UNDER_INVESTIGATION));
    }

    @Test
    void updateClaimStatus_ClaimNotFound_ThrowsException() {
        when(claimRepository.findById(claimId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> claimService.updateClaimStatus(claimId, ClaimStatus.UNDER_INVESTIGATION, ""))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void getClaim_Success() {
        when(claimRepository.findById(claimId)).thenReturn(Optional.of(testClaim));

        ClaimResponse response = claimService.getClaim(claimId);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(claimId);
    }

    @Test
    void getClaim_NotFound_ThrowsException() {
        when(claimRepository.findById(claimId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> claimService.getClaim(claimId))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}
