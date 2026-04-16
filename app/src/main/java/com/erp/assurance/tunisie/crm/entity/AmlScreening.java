package com.erp.assurance.tunisie.crm.entity;

import com.erp.assurance.tunisie.shared.entity.AuditableEntity;
import com.erp.assurance.tunisie.shared.enums.AmlRiskLevel;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "aml_screenings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AmlScreening extends AuditableEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @Column(name = "screening_date", nullable = false)
    private LocalDateTime screeningDate;

    @Column(name = "screening_provider")
    private String screeningProvider;

    @Column(name = "pep_match")
    @Builder.Default
    private boolean pepMatch = false;

    @Column(name = "sanction_match")
    @Builder.Default
    private boolean sanctionMatch = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "risk_level")
    private AmlRiskLevel riskLevel;

    @Enumerated(EnumType.STRING)
    @Column(name = "screening_status", nullable = false)
    @Builder.Default
    private ScreeningStatus screeningStatus = ScreeningStatus.PENDING;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "reviewed_by")
    private String reviewedBy;

    @Column(name = "review_date")
    private LocalDateTime reviewDate;

    public enum ScreeningStatus {
        PENDING, PASSED, FLAGGED, REJECTED
    }
}
