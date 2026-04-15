package com.erp.assurance.tunisie.underwriting.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class QuotationRequest {
    @NotNull(message = "Client ID is required")
    private UUID clientId;
    @NotNull(message = "Product ID is required")
    private UUID productId;
    private LocalDate effectiveDate;
    private LocalDate expiryDate;
    private BigDecimal discountPercentage;
    private String riskData;
}
