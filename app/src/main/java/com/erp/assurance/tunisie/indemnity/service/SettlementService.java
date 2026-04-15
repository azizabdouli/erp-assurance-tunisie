package com.erp.assurance.tunisie.indemnity.service;

import com.erp.assurance.tunisie.claims.entity.Claim;
import com.erp.assurance.tunisie.claims.repository.ClaimRepository;
import com.erp.assurance.tunisie.indemnity.dto.SettlementRequest;
import com.erp.assurance.tunisie.indemnity.dto.SettlementResponse;
import com.erp.assurance.tunisie.indemnity.entity.Settlement;
import com.erp.assurance.tunisie.indemnity.repository.SettlementRepository;
import com.erp.assurance.tunisie.shared.dto.PageResponse;
import com.erp.assurance.tunisie.shared.enums.SettlementStatus;
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
public class SettlementService {

    private final SettlementRepository settlementRepository;
    private final ClaimRepository claimRepository;
    private final IndemnityCalculationEngine calculationEngine;

    public SettlementResponse createSettlement(SettlementRequest request) {
        Claim claim = claimRepository.findById(request.getClaimId())
                .orElseThrow(() -> new ResourceNotFoundException("Claim", "id", request.getClaimId()));

        java.math.BigDecimal netAmount = calculationEngine.calculateNetIndemnity(
                request.getGrossAmount(), request.getDeductibleAmount(), request.getCoInsuranceShare());

        String settlementNumber = "STL-" + System.currentTimeMillis();

        Settlement settlement = Settlement.builder()
                .settlementNumber(settlementNumber)
                .claim(claim)
                .grossAmount(request.getGrossAmount())
                .deductibleAmount(request.getDeductibleAmount())
                .coInsuranceShare(request.getCoInsuranceShare())
                .netAmount(netAmount)
                .build();

        Settlement saved = settlementRepository.save(settlement);
        log.info("Settlement created: {} for claim: {}", settlementNumber, claim.getClaimNumber());
        return mapToResponse(saved);
    }

    public SettlementResponse approveSettlement(UUID id, String approvedBy) {
        Settlement settlement = settlementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Settlement", "id", id));
        if (settlement.getStatus() != SettlementStatus.CALCULATED) {
            throw new BusinessException("IND_001", "Settlement must be in CALCULATED status to approve");
        }
        settlement.setStatus(SettlementStatus.APPROVED);
        settlement.setApprovedBy(approvedBy);
        settlement.setApprovedAt(LocalDateTime.now());
        Settlement updated = settlementRepository.save(settlement);
        return mapToResponse(updated);
    }

    @Transactional(readOnly = true)
    public SettlementResponse getSettlement(UUID id) {
        Settlement settlement = settlementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Settlement", "id", id));
        return mapToResponse(settlement);
    }

    @Transactional(readOnly = true)
    public PageResponse<SettlementResponse> getSettlementsByClaim(UUID claimId, Pageable pageable) {
        Page<Settlement> page = settlementRepository.findByClaimId(claimId, pageable);
        return PageResponse.from(page.map(this::mapToResponse));
    }

    private SettlementResponse mapToResponse(Settlement s) {
        return SettlementResponse.builder()
                .id(s.getId())
                .settlementNumber(s.getSettlementNumber())
                .claimId(s.getClaim().getId())
                .claimNumber(s.getClaim().getClaimNumber())
                .grossAmount(s.getGrossAmount())
                .deductibleAmount(s.getDeductibleAmount())
                .coInsuranceShare(s.getCoInsuranceShare())
                .netAmount(s.getNetAmount())
                .status(s.getStatus())
                .createdAt(s.getCreatedAt())
                .build();
    }
}
