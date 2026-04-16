package com.erp.assurance.tunisie.claims.service;

import com.erp.assurance.tunisie.claims.entity.Claim;
import com.erp.assurance.tunisie.claims.entity.FraudIndicator;
import com.erp.assurance.tunisie.claims.repository.ClaimRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class FraudDetectionEngine {

    private static final int FRAUD_THRESHOLD = 50;
    private final ClaimRepository claimRepository;

    public int analyzeClaim(Claim claim) {
        List<FraudIndicator> indicators = new ArrayList<>();
        int totalScore = 0;

        // Check multiple claims on same policy
        long claimCount = claimRepository.countByPolicyId(claim.getPolicy().getId());
        if (claimCount > 3) {
            FraudIndicator indicator = FraudIndicator.builder()
                    .claim(claim)
                    .indicatorType("MULTIPLE_CLAIMS")
                    .description("Policy has " + claimCount + " claims")
                    .score(20)
                    .detectedBy("SYSTEM")
                    .build();
            indicators.add(indicator);
            totalScore += 20;
        }

        // Check claim amount vs sum insured
        if (claim.getEstimatedAmount() != null && claim.getPolicy().getSumInsured() != null) {
            BigDecimal ratio = claim.getEstimatedAmount().divide(claim.getPolicy().getSumInsured(), 4, java.math.RoundingMode.HALF_UP);
            if (ratio.compareTo(new BigDecimal("0.80")) > 0) {
                FraudIndicator indicator = FraudIndicator.builder()
                        .claim(claim)
                        .indicatorType("HIGH_AMOUNT_RATIO")
                        .description("Claim amount is " + ratio.multiply(new BigDecimal("100")) + "% of sum insured")
                        .score(25)
                        .detectedBy("SYSTEM")
                        .build();
                indicators.add(indicator);
                totalScore += 25;
            }
        }

        // Check if claim is declared too late
        if (claim.getIncidentDate() != null && claim.getDeclarationDate() != null) {
            long daysDiff = ChronoUnit.DAYS.between(claim.getIncidentDate(), claim.getDeclarationDate().toLocalDate());
            if (daysDiff > 30) {
                FraudIndicator indicator = FraudIndicator.builder()
                        .claim(claim)
                        .indicatorType("LATE_DECLARATION")
                        .description("Claim declared " + daysDiff + " days after incident")
                        .score(15)
                        .detectedBy("SYSTEM")
                        .build();
                indicators.add(indicator);
                totalScore += 15;
            }
        }

        // Check if claim is near policy expiry
        if (claim.getIncidentDate() != null && claim.getPolicy().getExpiryDate() != null) {
            long daysToExpiry = ChronoUnit.DAYS.between(claim.getIncidentDate(), claim.getPolicy().getExpiryDate());
            if (daysToExpiry < 15) {
                FraudIndicator indicator = FraudIndicator.builder()
                        .claim(claim)
                        .indicatorType("NEAR_EXPIRY")
                        .description("Incident occurred " + daysToExpiry + " days before policy expiry")
                        .score(15)
                        .detectedBy("SYSTEM")
                        .build();
                indicators.add(indicator);
                totalScore += 15;
            }
        }

        claim.setFraudScore(totalScore);
        claim.setFraudFlagged(totalScore >= FRAUD_THRESHOLD);

        log.info("Fraud analysis for claim {}: score={}, flagged={}", claim.getClaimNumber(), totalScore, claim.isFraudFlagged());
        return totalScore;
    }
}
