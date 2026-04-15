package com.erp.assurance.tunisie.claims.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class ClaimDeclarationRequest {
    @NotNull(message = "Policy ID is required")
    private UUID policyId;
    @NotNull(message = "Incident date is required")
    private LocalDate incidentDate;
    @NotBlank(message = "Incident description is required")
    private String incidentDescription;
    private String incidentLocation;
    private BigDecimal estimatedAmount;
}
