package com.erp.assurance.tunisie.indemnity.service;

import com.erp.assurance.tunisie.claims.entity.Claim;
import com.erp.assurance.tunisie.claims.repository.ClaimRepository;
import com.erp.assurance.tunisie.indemnity.dto.SettlementRequest;
import com.erp.assurance.tunisie.indemnity.dto.SettlementResponse;
import com.erp.assurance.tunisie.indemnity.entity.Settlement;
import com.erp.assurance.tunisie.indemnity.repository.SettlementRepository;
import com.erp.assurance.tunisie.policy.entity.Policy;
import com.erp.assurance.tunisie.shared.enums.SettlementStatus;
import com.erp.assurance.tunisie.shared.exception.BusinessException;
import com.erp.assurance.tunisie.shared.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SettlementServiceTest {

    @Mock private SettlementRepository settlementRepository;
    @Mock private ClaimRepository claimRepository;
    @Mock private IndemnityCalculationEngine calculationEngine;
    @InjectMocks private SettlementService settlementService;

    private UUID claimId;
    private UUID settlementId;
    private Claim testClaim;
    private Settlement calculatedSettlement;
    private Settlement approvedSettlement;

    @BeforeEach
    void setUp() {
        claimId = UUID.randomUUID();
        settlementId = UUID.randomUUID();

        Policy policy = Policy.builder().policyNumber("POL-001").build();
        policy.setId(UUID.randomUUID());

        testClaim = Claim.builder()
                .claimNumber("CLM-001")
                .policy(policy)
                .build();
        testClaim.setId(claimId);

        calculatedSettlement = Settlement.builder()
                .settlementNumber("STL-001")
                .claim(testClaim)
                .grossAmount(new BigDecimal("10000.000"))
                .deductibleAmount(new BigDecimal("500.000"))
                .coInsuranceShare(new BigDecimal("100.00"))
                .netAmount(new BigDecimal("9500.000"))
                .status(SettlementStatus.CALCULATED)
                .build();
        calculatedSettlement.setId(settlementId);

        approvedSettlement = Settlement.builder()
                .settlementNumber("STL-001")
                .claim(testClaim)
                .grossAmount(new BigDecimal("10000.000"))
                .deductibleAmount(new BigDecimal("500.000"))
                .coInsuranceShare(new BigDecimal("100.00"))
                .netAmount(new BigDecimal("9500.000"))
                .status(SettlementStatus.APPROVED)
                .approvedBy("supervisor@erp.tn")
                .build();
        approvedSettlement.setId(settlementId);
    }

    @Test
    void createSettlement_Success() {
        SettlementRequest request = SettlementRequest.builder()
                .claimId(claimId)
                .grossAmount(new BigDecimal("10000.000"))
                .deductibleAmount(new BigDecimal("500.000"))
                .coInsuranceShare(new BigDecimal("100.00"))
                .build();

        when(claimRepository.findById(claimId)).thenReturn(Optional.of(testClaim));
        when(calculationEngine.calculateNetIndemnity(any(), any(), any())).thenReturn(new BigDecimal("9500.000"));
        when(settlementRepository.save(any(Settlement.class))).thenReturn(calculatedSettlement);

        SettlementResponse response = settlementService.createSettlement(request);

        assertThat(response).isNotNull();
        assertThat(response.getSettlementNumber()).isEqualTo("STL-001");
        assertThat(response.getNetAmount()).isEqualByComparingTo(new BigDecimal("9500.000"));
        verify(settlementRepository).save(any(Settlement.class));
    }

    @Test
    void createSettlement_ClaimNotFound_ThrowsException() {
        SettlementRequest request = SettlementRequest.builder()
                .claimId(claimId)
                .grossAmount(new BigDecimal("10000.000"))
                .build();

        when(claimRepository.findById(claimId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> settlementService.createSettlement(request))
                .isInstanceOf(ResourceNotFoundException.class);
        verify(settlementRepository, never()).save(any());
    }

    @Test
    void approveSettlement_Success() {
        when(settlementRepository.findById(settlementId)).thenReturn(Optional.of(calculatedSettlement));
        when(settlementRepository.save(any(Settlement.class))).thenReturn(approvedSettlement);

        SettlementResponse response = settlementService.approveSettlement(settlementId, "supervisor@erp.tn");

        assertThat(response).isNotNull();
        assertThat(response.getStatus()).isEqualTo(SettlementStatus.APPROVED);
        verify(settlementRepository).save(argThat(s -> s.getStatus() == SettlementStatus.APPROVED));
    }

    @Test
    void approveSettlement_AlreadyApproved_ThrowsException() {
        when(settlementRepository.findById(settlementId)).thenReturn(Optional.of(approvedSettlement));

        assertThatThrownBy(() -> settlementService.approveSettlement(settlementId, "supervisor@erp.tn"))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("CALCULATED");
    }

    @Test
    void approveSettlement_NotFound_ThrowsException() {
        when(settlementRepository.findById(settlementId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> settlementService.approveSettlement(settlementId, "supervisor@erp.tn"))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void getSettlement_Success() {
        when(settlementRepository.findById(settlementId)).thenReturn(Optional.of(calculatedSettlement));

        SettlementResponse response = settlementService.getSettlement(settlementId);

        assertThat(response.getId()).isEqualTo(settlementId);
        assertThat(response.getClaimNumber()).isEqualTo("CLM-001");
    }

    @Test
    void getSettlement_NotFound_ThrowsException() {
        when(settlementRepository.findById(settlementId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> settlementService.getSettlement(settlementId))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}
