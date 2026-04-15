package com.erp.assurance.tunisie.underwriting.entity;

import com.erp.assurance.tunisie.shared.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "coverages")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Coverage extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "mandatory")
    @Builder.Default
    private boolean mandatory = false;

    @Column(name = "base_premium", precision = 15, scale = 3)
    private BigDecimal basePremium;

    @Column(name = "max_coverage_amount", precision = 15, scale = 3)
    private BigDecimal maxCoverageAmount;

    @Column(name = "deductible_amount", precision = 15, scale = 3)
    private BigDecimal deductibleAmount;

    @Column(name = "deductible_percentage", precision = 5, scale = 2)
    private BigDecimal deductiblePercentage;
}
