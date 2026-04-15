package com.erp.assurance.tunisie.policy.service;

import com.erp.assurance.tunisie.policy.entity.Policy;
import com.erp.assurance.tunisie.policy.entity.PolicyRenewal;
import com.erp.assurance.tunisie.policy.repository.PolicyRepository;
import com.erp.assurance.tunisie.shared.enums.PolicyStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PolicyRenewalService {

    private final PolicyRepository policyRepository;

    public List<Policy> findPoliciesForRenewal() {
        LocalDate today = LocalDate.now();
        LocalDate thirtyDaysFromNow = today.plusDays(30);
        return policyRepository.findPoliciesForRenewal(PolicyStatus.ACTIVE, today, thirtyDaysFromNow);
    }

    public Policy renewPolicy(Policy originalPolicy) {
        String newPolicyNumber = "POL-R" + System.currentTimeMillis();

        Policy renewedPolicy = Policy.builder()
                .policyNumber(newPolicyNumber)
                .client(originalPolicy.getClient())
                .product(originalPolicy.getProduct())
                .effectiveDate(originalPolicy.getExpiryDate())
                .expiryDate(originalPolicy.getExpiryDate().plusYears(1))
                .annualPremium(originalPolicy.getAnnualPremium())
                .netPremium(originalPolicy.getNetPremium())
                .taxAmount(originalPolicy.getTaxAmount())
                .totalPremium(originalPolicy.getTotalPremium())
                .sumInsured(originalPolicy.getSumInsured())
                .renewalCount(originalPolicy.getRenewalCount() + 1)
                .status(PolicyStatus.ACTIVE)
                .build();

        Policy saved = policyRepository.save(renewedPolicy);

        originalPolicy.setStatus(PolicyStatus.RENEWED);
        policyRepository.save(originalPolicy);

        log.info("Policy {} renewed as {}", originalPolicy.getPolicyNumber(), saved.getPolicyNumber());
        return saved;
    }
}
