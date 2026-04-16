package com.erp.assurance.tunisie.claims.entity;

import com.erp.assurance.tunisie.shared.entity.AuditableEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "expert_assignments")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ExpertAssignment extends AuditableEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "claim_id", nullable = false)
    private Claim claim;

    @Column(name = "expert_name", nullable = false)
    private String expertName;

    @Column(name = "expert_speciality")
    private String expertSpeciality;

    @Column(name = "assignment_date", nullable = false)
    private LocalDate assignmentDate;

    @Column(name = "report_date")
    private LocalDate reportDate;

    @Column(name = "estimated_damage", precision = 15, scale = 3)
    private BigDecimal estimatedDamage;

    @Column(name = "report_summary", columnDefinition = "TEXT")
    private String reportSummary;

    @Enumerated(EnumType.STRING)
    @Column(name = "assignment_status")
    @Builder.Default
    private AssignmentStatus assignmentStatus = AssignmentStatus.ASSIGNED;

    public enum AssignmentStatus {
        ASSIGNED, IN_PROGRESS, COMPLETED, CANCELLED
    }
}
