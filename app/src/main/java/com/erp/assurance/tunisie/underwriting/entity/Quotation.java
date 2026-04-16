package com.erp.assurance.tunisie.underwriting.entity;

import com.erp.assurance.tunisie.crm.entity.Client;
import com.erp.assurance.tunisie.shared.entity.AuditableEntity;
import com.erp.assurance.tunisie.shared.enums.QuotationStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "quotations")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Quotation extends AuditableEntity {

    @Column(name = "quotation_number", nullable = false, unique = true)
    private String quotationNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @Builder.Default
    private QuotationStatus status = QuotationStatus.DRAFT;

    @Column(name = "effective_date")
    private LocalDate effectiveDate;

    @Column(name = "expiry_date")
    private LocalDate expiryDate;

    @Column(name = "base_premium", precision = 15, scale = 3)
    private BigDecimal basePremium;

    @Column(name = "net_premium", precision = 15, scale = 3)
    private BigDecimal netPremium;

    @Column(name = "tax_amount", precision = 15, scale = 3)
    private BigDecimal taxAmount;

    @Column(name = "total_premium", precision = 15, scale = 3)
    private BigDecimal totalPremium;

    @Column(name = "discount_percentage", precision = 5, scale = 2)
    private BigDecimal discountPercentage;

    @Column(name = "loading_percentage", precision = 5, scale = 2)
    private BigDecimal loadingPercentage;

    @Column(name = "risk_data", columnDefinition = "TEXT")
    private String riskData;

    @Column(name = "valid_until")
    private LocalDateTime validUntil;

    @Column(name = "approved_by")
    private String approvedBy;

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;
}
