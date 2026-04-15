package com.erp.assurance.tunisie.claims.service;

import com.erp.assurance.tunisie.claims.entity.Claim;
import com.erp.assurance.tunisie.claims.repository.ClaimRepository;
import com.erp.assurance.tunisie.policy.entity.Policy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FraudDetectionEngineTest {

    @Mock private ClaimRepository claimRepository;
    @InjectMocks private FraudDetectionEngine fraudDetectionEngine;

    private Policy policy;
    private Claim claim;

    @BeforeEach
    void setUp() {
        policy = Policy.builder()
                .policyNumber("POL-001")
                .sumInsured(new BigDecimal("50000.000"))
                .expiryDate(LocalDate.now().plusMonths(6))
                .build();
        policy.setId(UUID.randomUUID());

        claim = Claim.builder()
                .claimNumber("CLM-001")
                .policy(policy)
                .incidentDate(LocalDate.now().minusDays(3))
                .declarationDate(LocalDateTime.now())
                .estimatedAmount(new BigDecimal("5000.000"))
                .build();
        claim.setId(UUID.randomUUID());
    }

    @Test
    void analyzeClaim_NoIndicators_ScoreIsZero() {
        when(claimRepository.countByPolicyId(policy.getId())).thenReturn(1L);

        int score = fraudDetectionEngine.analyzeClaim(claim);

        assertThat(score).isZero();
        assertThat(claim.isFraudFlagged()).isFalse();
    }

    @Test
    void analyzeClaim_MultipleClaimsOnPolicy_AddsScore() {
        when(claimRepository.countByPolicyId(policy.getId())).thenReturn(5L);

        int score = fraudDetectionEngine.analyzeClaim(claim);

        assertThat(score).isGreaterThanOrEqualTo(20);
    }

    @Test
    void analyzeClaim_HighAmountRatio_AddsScore() {
        claim.setEstimatedAmount(new BigDecimal("45000.000")); // 90% of sum insured
        when(claimRepository.countByPolicyId(policy.getId())).thenReturn(1L);

        int score = fraudDetectionEngine.analyzeClaim(claim);

        assertThat(score).isGreaterThanOrEqualTo(25);
    }

    @Test
    void analyzeClaim_LateDeclaration_AddsScore() {
        claim.setIncidentDate(LocalDate.now().minusDays(40));
        claim.setDeclarationDate(LocalDateTime.now());
        when(claimRepository.countByPolicyId(policy.getId())).thenReturn(1L);

        int score = fraudDetectionEngine.analyzeClaim(claim);

        assertThat(score).isGreaterThanOrEqualTo(15);
    }

    @Test
    void analyzeClaim_NearPolicyExpiry_AddsScore() {
        policy.setExpiryDate(LocalDate.now().plusDays(7));
        claim.setIncidentDate(LocalDate.now().minusDays(1));
        when(claimRepository.countByPolicyId(policy.getId())).thenReturn(1L);

        int score = fraudDetectionEngine.analyzeClaim(claim);

        assertThat(score).isGreaterThanOrEqualTo(15);
    }

    @Test
    void analyzeClaim_MultipleIndicators_FlagsAsFraud() {
        // Multiple claims (score 20) + high amount ratio (score 25) + late declaration (score 15) = 60 >= 50
        claim.setEstimatedAmount(new BigDecimal("45000.000"));
        claim.setIncidentDate(LocalDate.now().minusDays(40));
        claim.setDeclarationDate(LocalDateTime.now());
        when(claimRepository.countByPolicyId(policy.getId())).thenReturn(5L);

        int score = fraudDetectionEngine.analyzeClaim(claim);

        assertThat(score).isGreaterThanOrEqualTo(50);
        assertThat(claim.isFraudFlagged()).isTrue();
    }

    @Test
    void analyzeClaim_LowAmountRatio_NoAmountIndicator() {
        claim.setEstimatedAmount(new BigDecimal("5000.000")); // 10% of sum insured - below threshold
        when(claimRepository.countByPolicyId(policy.getId())).thenReturn(1L);

        int score = fraudDetectionEngine.analyzeClaim(claim);

        assertThat(score).isZero();
        assertThat(claim.getFraudScore()).isZero();
    }
}
