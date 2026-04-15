package com.erp.assurance.tunisie.policy.dto;

import com.erp.assurance.tunisie.shared.enums.EndorsementType;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class EndorsementRequest {
    @NotNull private EndorsementType endorsementType;
    @NotNull private LocalDate effectiveDate;
    private String description;
    private BigDecimal premiumAdjustment;
}
