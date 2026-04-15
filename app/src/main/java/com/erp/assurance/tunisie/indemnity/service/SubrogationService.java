package com.erp.assurance.tunisie.indemnity.service;

import com.erp.assurance.tunisie.claims.entity.Claim;
import com.erp.assurance.tunisie.claims.repository.ClaimRepository;
import com.erp.assurance.tunisie.indemnity.entity.SubrogationClaim;
import com.erp.assurance.tunisie.indemnity.repository.SubrogationClaimRepository;
import com.erp.assurance.tunisie.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class SubrogationService {

    private final SubrogationClaimRepository subrogationRepository;
    private final ClaimRepository claimRepository;

    public SubrogationClaim initiateSubrogation(UUID claimId, String thirdPartyName, String thirdPartyInsurer, BigDecimal amount) {
        Claim claim = claimRepository.findById(claimId)
                .orElseThrow(() -> new ResourceNotFoundException("Claim", "id", claimId));

        SubrogationClaim subrogation = SubrogationClaim.builder()
                .claim(claim)
                .thirdPartyName(thirdPartyName)
                .thirdPartyInsurer(thirdPartyInsurer)
                .claimedAmount(amount)
                .initiatedDate(LocalDate.now())
                .build();

        SubrogationClaim saved = subrogationRepository.save(subrogation);
        log.info("Subrogation initiated for claim: {} against: {}", claim.getClaimNumber(), thirdPartyName);
        return saved;
    }

    @Transactional(readOnly = true)
    public List<SubrogationClaim> getSubrogationsByClaim(UUID claimId) {
        return subrogationRepository.findByClaimId(claimId);
    }
}
