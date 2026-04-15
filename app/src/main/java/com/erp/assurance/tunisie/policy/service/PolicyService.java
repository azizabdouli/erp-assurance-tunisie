package com.erp.assurance.tunisie.policy.service;

import com.erp.assurance.tunisie.crm.entity.Client;
import com.erp.assurance.tunisie.crm.repository.ClientRepository;
import com.erp.assurance.tunisie.policy.dto.PolicyCreateRequest;
import com.erp.assurance.tunisie.policy.dto.PolicyResponse;
import com.erp.assurance.tunisie.policy.entity.Policy;
import com.erp.assurance.tunisie.policy.repository.PolicyRepository;
import com.erp.assurance.tunisie.shared.dto.PageResponse;
import com.erp.assurance.tunisie.shared.enums.PolicyStatus;
import com.erp.assurance.tunisie.shared.exception.ResourceNotFoundException;
import com.erp.assurance.tunisie.underwriting.entity.Product;
import com.erp.assurance.tunisie.underwriting.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PolicyService {

    private final PolicyRepository policyRepository;
    private final ClientRepository clientRepository;
    private final ProductRepository productRepository;

    public PolicyResponse createPolicy(PolicyCreateRequest request) {
        Client client = clientRepository.findById(request.getClientId())
                .orElseThrow(() -> new ResourceNotFoundException("Client", "id", request.getClientId()));
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", request.getProductId()));

        String policyNumber = "POL-" + System.currentTimeMillis();

        Policy policy = Policy.builder()
                .policyNumber(policyNumber)
                .client(client)
                .product(product)
                .effectiveDate(request.getEffectiveDate())
                .expiryDate(request.getExpiryDate())
                .sumInsured(request.getSumInsured())
                .status(PolicyStatus.DRAFT)
                .build();

        Policy saved = policyRepository.save(policy);
        log.info("Created policy: {}", saved.getPolicyNumber());
        return mapToResponse(saved);
    }

    public PolicyResponse activatePolicy(UUID id) {
        Policy policy = findPolicyOrThrow(id);
        policy.setStatus(PolicyStatus.ACTIVE);
        Policy updated = policyRepository.save(policy);
        return mapToResponse(updated);
    }

    @Transactional(readOnly = true)
    public PolicyResponse getPolicy(UUID id) {
        return mapToResponse(findPolicyOrThrow(id));
    }

    @Transactional(readOnly = true)
    public PageResponse<PolicyResponse> getPoliciesByClient(UUID clientId, Pageable pageable) {
        Page<Policy> page = policyRepository.findByClientId(clientId, pageable);
        return PageResponse.from(page.map(this::mapToResponse));
    }

    @Transactional(readOnly = true)
    public PageResponse<PolicyResponse> getPoliciesByStatus(PolicyStatus status, Pageable pageable) {
        Page<Policy> page = policyRepository.findByStatus(status, pageable);
        return PageResponse.from(page.map(this::mapToResponse));
    }

    public PolicyResponse cancelPolicy(UUID id) {
        Policy policy = findPolicyOrThrow(id);
        policy.setStatus(PolicyStatus.CANCELLED);
        Policy updated = policyRepository.save(policy);
        log.info("Cancelled policy: {}", updated.getPolicyNumber());
        return mapToResponse(updated);
    }

    private Policy findPolicyOrThrow(UUID id) {
        return policyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Policy", "id", id));
    }

    private PolicyResponse mapToResponse(Policy p) {
        return PolicyResponse.builder()
                .id(p.getId())
                .policyNumber(p.getPolicyNumber())
                .clientId(p.getClient().getId())
                .clientName(p.getClient().getFullName())
                .productId(p.getProduct().getId())
                .productName(p.getProduct().getName())
                .status(p.getStatus())
                .effectiveDate(p.getEffectiveDate())
                .expiryDate(p.getExpiryDate())
                .annualPremium(p.getAnnualPremium())
                .totalPremium(p.getTotalPremium())
                .sumInsured(p.getSumInsured())
                .renewalCount(p.getRenewalCount())
                .createdAt(p.getCreatedAt())
                .build();
    }
}
