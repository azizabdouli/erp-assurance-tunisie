package com.erp.assurance.tunisie.claims.dto;

import com.erp.assurance.tunisie.shared.enums.ClaimStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class ClaimResponse {
    private UUID id;
    private String claimNumber;
    private UUID policyId;
    private String policyNumber;
    private ClaimStatus status;
    private LocalDate incidentDate;
    private LocalDateTime declarationDate;
    private String incidentDescription;
    private String incidentLocation;
    private BigDecimal estimatedAmount;
    private BigDecimal approvedAmount;
    private BigDecimal deductibleAmount;
    private boolean fraudFlagged;
    private Integer fraudScore;
    private LocalDateTime createdAt;
}
