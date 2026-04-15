package com.erp.assurance.tunisie.policy.service;

import com.erp.assurance.tunisie.policy.dto.EndorsementRequest;
import com.erp.assurance.tunisie.policy.entity.Policy;
import com.erp.assurance.tunisie.policy.entity.PolicyEndorsement;
import com.erp.assurance.tunisie.policy.repository.PolicyEndorsementRepository;
import com.erp.assurance.tunisie.policy.repository.PolicyRepository;
import com.erp.assurance.tunisie.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class EndorsementService {

    private final PolicyEndorsementRepository endorsementRepository;
    private final PolicyRepository policyRepository;

    public PolicyEndorsement createEndorsement(UUID policyId, EndorsementRequest request) {
        Policy policy = policyRepository.findById(policyId)
                .orElseThrow(() -> new ResourceNotFoundException("Policy", "id", policyId));

        String endorsementNumber = "END-" + System.currentTimeMillis();

        PolicyEndorsement endorsement = PolicyEndorsement.builder()
                .policy(policy)
                .endorsementNumber(endorsementNumber)
                .endorsementType(request.getEndorsementType())
                .effectiveDate(request.getEffectiveDate())
                .description(request.getDescription())
                .premiumAdjustment(request.getPremiumAdjustment())
                .build();

        PolicyEndorsement saved = endorsementRepository.save(endorsement);
        log.info("Created endorsement: {} for policy: {}", endorsementNumber, policy.getPolicyNumber());
        return saved;
    }

    @Transactional(readOnly = true)
    public List<PolicyEndorsement> getEndorsementsByPolicy(UUID policyId) {
        return endorsementRepository.findByPolicyId(policyId);
    }
}
