package com.erp.assurance.tunisie.policy.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class PolicyCreateRequest {
    private UUID quotationId;
    @NotNull private UUID clientId;
    @NotNull private UUID productId;
    @NotNull private LocalDate effectiveDate;
    @NotNull private LocalDate expiryDate;
    private BigDecimal sumInsured;
}
