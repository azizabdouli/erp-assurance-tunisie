package com.erp.assurance.tunisie.underwriting.entity;

import com.erp.assurance.tunisie.shared.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "premium_factors")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PremiumFactor extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "factor_name", nullable = false)
    private String factorName;

    @Column(name = "factor_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private FactorType factorType;

    @Column(name = "factor_value", precision = 10, scale = 4)
    private BigDecimal factorValue;

    @Column(name = "min_value", precision = 10, scale = 4)
    private BigDecimal minValue;

    @Column(name = "max_value", precision = 10, scale = 4)
    private BigDecimal maxValue;

    @Column(name = "description")
    private String description;

    public enum FactorType {
        MULTIPLIER, PERCENTAGE, FIXED_AMOUNT, DISCOUNT, LOADING
    }
}
