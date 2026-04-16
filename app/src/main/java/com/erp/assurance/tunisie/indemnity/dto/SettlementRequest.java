package com.erp.assurance.tunisie.indemnity.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class SettlementRequest {
    @NotNull private UUID claimId;
    @NotNull private BigDecimal grossAmount;
    private BigDecimal deductibleAmount;
    private BigDecimal coInsuranceShare;
}
