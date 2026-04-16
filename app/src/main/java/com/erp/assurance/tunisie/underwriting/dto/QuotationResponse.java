package com.erp.assurance.tunisie.underwriting.dto;

import com.erp.assurance.tunisie.shared.enums.QuotationStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class QuotationResponse {
    private UUID id;
    private String quotationNumber;
    private UUID clientId;
    private String clientName;
    private UUID productId;
    private String productName;
    private QuotationStatus status;
    private LocalDate effectiveDate;
    private LocalDate expiryDate;
    private BigDecimal basePremium;
    private BigDecimal netPremium;
    private BigDecimal taxAmount;
    private BigDecimal totalPremium;
    private BigDecimal discountPercentage;
    private BigDecimal loadingPercentage;
    private LocalDateTime validUntil;
    private LocalDateTime createdAt;
}
