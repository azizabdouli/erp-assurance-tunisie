package com.erp.assurance.tunisie.claims.entity;

import com.erp.assurance.tunisie.shared.entity.AuditableEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "claim_reserves")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ClaimReserve extends AuditableEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "claim_id", nullable = false)
    private Claim claim;

    @Column(name = "reserve_amount", precision = 15, scale = 3, nullable = false)
    private BigDecimal reserveAmount;

    @Column(name = "reserve_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ReserveType reserveType;

    @Column(name = "adjustment_date", nullable = false)
    private LocalDateTime adjustmentDate;

    @Column(name = "adjusted_by")
    private String adjustedBy;

    @Column(name = "reason", columnDefinition = "TEXT")
    private String reason;

    public enum ReserveType {
        INITIAL, ADJUSTMENT, FINAL
    }
}
