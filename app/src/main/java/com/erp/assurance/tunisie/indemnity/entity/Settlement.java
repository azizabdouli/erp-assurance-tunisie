package com.erp.assurance.tunisie.indemnity.entity;

import com.erp.assurance.tunisie.claims.entity.Claim;
import com.erp.assurance.tunisie.shared.entity.AuditableEntity;
import com.erp.assurance.tunisie.shared.enums.SettlementStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "settlements")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Settlement extends AuditableEntity {

    @Column(name = "settlement_number", nullable = false, unique = true)
    private String settlementNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "claim_id", nullable = false)
    private Claim claim;

    @Column(name = "gross_amount", precision = 15, scale = 3, nullable = false)
    private BigDecimal grossAmount;

    @Column(name = "deductible_amount", precision = 15, scale = 3)
    @Builder.Default
    private BigDecimal deductibleAmount = BigDecimal.ZERO;

    @Column(name = "co_insurance_share", precision = 5, scale = 2)
    @Builder.Default
    private BigDecimal coInsuranceShare = new BigDecimal("100.00");

    @Column(name = "net_amount", precision = 15, scale = 3, nullable = false)
    private BigDecimal netAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @Builder.Default
    private SettlementStatus status = SettlementStatus.CALCULATED;

    @Column(name = "approved_by")
    private String approvedBy;

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    @OneToMany(mappedBy = "settlement", cascade = CascadeType.ALL)
    @Builder.Default
    private List<SettlementPayment> payments = new ArrayList<>();
}
