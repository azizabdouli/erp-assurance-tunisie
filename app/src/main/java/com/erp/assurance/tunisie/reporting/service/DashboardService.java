package com.erp.assurance.tunisie.reporting.service;

import com.erp.assurance.tunisie.claims.repository.ClaimRepository;
import com.erp.assurance.tunisie.crm.repository.ClientRepository;
import com.erp.assurance.tunisie.policy.repository.PolicyRepository;
import com.erp.assurance.tunisie.reporting.dto.DashboardKpi;
import com.erp.assurance.tunisie.shared.enums.ClaimStatus;
import com.erp.assurance.tunisie.shared.enums.PolicyStatus;
import com.erp.assurance.tunisie.underwriting.repository.QuotationRepository;
import com.erp.assurance.tunisie.shared.enums.QuotationStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DashboardService {

    private final ClientRepository clientRepository;
    private final PolicyRepository policyRepository;
    private final ClaimRepository claimRepository;
    private final QuotationRepository quotationRepository;

    public DashboardKpi getDashboardKpis() {
        long totalClients = clientRepository.count();
        long activePolicies = policyRepository.findByStatus(PolicyStatus.ACTIVE, PageRequest.of(0, 1)).getTotalElements();
        long openClaims = claimRepository.findByStatus(ClaimStatus.DECLARED, PageRequest.of(0, 1)).getTotalElements();
        long pendingQuotations = quotationRepository.countByStatus(QuotationStatus.PENDING_APPROVAL);

        return DashboardKpi.builder()
                .totalClients(totalClients)
                .activePolicies(activePolicies)
                .openClaims(openClaims)
                .pendingQuotations(pendingQuotations)
                .totalPremiumVolume(BigDecimal.ZERO)
                .totalClaimsPaid(BigDecimal.ZERO)
                .sinistraliteRatio(BigDecimal.ZERO)
                .combinedRatio(BigDecimal.ZERO)
                .build();
    }
}
