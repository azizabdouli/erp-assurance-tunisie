package com.erp.assurance.tunisie.indemnity.entity;

import com.erp.assurance.tunisie.claims.entity.Claim;
import com.erp.assurance.tunisie.shared.entity.AuditableEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "subrogation_claims")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class SubrogationClaim extends AuditableEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "claim_id", nullable = false)
    private Claim claim;

    @Column(name = "third_party_name", nullable = false)
    private String thirdPartyName;

    @Column(name = "third_party_insurer")
    private String thirdPartyInsurer;

    @Column(name = "claimed_amount", precision = 15, scale = 3, nullable = false)
    private BigDecimal claimedAmount;

    @Column(name = "recovered_amount", precision = 15, scale = 3)
    @Builder.Default
    private BigDecimal recoveredAmount = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(name = "subrogation_status")
    @Builder.Default
    private SubrogationStatus subrogationStatus = SubrogationStatus.INITIATED;

    @Column(name = "initiated_date")
    private LocalDate initiatedDate;

    @Column(name = "resolved_date")
    private LocalDate resolvedDate;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    public enum SubrogationStatus {
        INITIATED, IN_PROGRESS, PARTIALLY_RECOVERED, FULLY_RECOVERED, ABANDONED
    }
}
