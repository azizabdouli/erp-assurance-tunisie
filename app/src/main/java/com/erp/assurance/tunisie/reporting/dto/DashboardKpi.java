package com.erp.assurance.tunisie.reporting.dto;

import lombok.*;

import java.math.BigDecimal;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class DashboardKpi {
    private long totalClients;
    private long activePolicies;
    private long openClaims;
    private long pendingQuotations;
    private BigDecimal totalPremiumVolume;
    private BigDecimal totalClaimsPaid;
    private BigDecimal sinistraliteRatio;
    private BigDecimal combinedRatio;
}
