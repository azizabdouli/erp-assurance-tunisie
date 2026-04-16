package com.erp.assurance.tunisie.indemnity.dto;

import com.erp.assurance.tunisie.shared.enums.SettlementStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class SettlementResponse {
    private UUID id;
    private String settlementNumber;
    private UUID claimId;
    private String claimNumber;
    private BigDecimal grossAmount;
    private BigDecimal deductibleAmount;
    private BigDecimal coInsuranceShare;
    private BigDecimal netAmount;
    private SettlementStatus status;
    private LocalDateTime createdAt;
}
