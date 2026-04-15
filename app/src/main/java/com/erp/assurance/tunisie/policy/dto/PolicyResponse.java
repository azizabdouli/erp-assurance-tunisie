package com.erp.assurance.tunisie.policy.dto;

import com.erp.assurance.tunisie.shared.enums.PolicyStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class PolicyResponse {
    private UUID id;
    private String policyNumber;
    private UUID clientId;
    private String clientName;
    private UUID productId;
    private String productName;
    private PolicyStatus status;
    private LocalDate effectiveDate;
    private LocalDate expiryDate;
    private BigDecimal annualPremium;
    private BigDecimal totalPremium;
    private BigDecimal sumInsured;
    private int renewalCount;
    private LocalDateTime createdAt;
}
