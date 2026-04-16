package com.erp.assurance.tunisie.policy.entity;

import com.erp.assurance.tunisie.shared.entity.AuditableEntity;
import com.erp.assurance.tunisie.shared.enums.EndorsementType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "policy_endorsements")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PolicyEndorsement extends AuditableEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "policy_id", nullable = false)
    private Policy policy;

    @Column(name = "endorsement_number", nullable = false, unique = true)
    private String endorsementNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "endorsement_type", nullable = false)
    private EndorsementType endorsementType;

    @Column(name = "effective_date", nullable = false)
    private LocalDate effectiveDate;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "premium_adjustment", precision = 15, scale = 3)
    private BigDecimal premiumAdjustment;

    @Column(name = "approved")
    @Builder.Default
    private boolean approved = false;

    @Column(name = "approved_by")
    private String approvedBy;
}
