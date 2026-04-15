package com.erp.assurance.tunisie.claims.entity;

import com.erp.assurance.tunisie.shared.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "fraud_indicators")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class FraudIndicator extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "claim_id", nullable = false)
    private Claim claim;

    @Column(name = "indicator_type", nullable = false)
    private String indicatorType;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "score")
    private int score;

    @Column(name = "detected_by")
    private String detectedBy;
}
