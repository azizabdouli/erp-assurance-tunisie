package com.erp.assurance.tunisie.claims.entity;

import com.erp.assurance.tunisie.policy.entity.Policy;
import com.erp.assurance.tunisie.shared.entity.AuditableEntity;
import com.erp.assurance.tunisie.shared.enums.ClaimStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "claims")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Claim extends AuditableEntity {

    @Column(name = "claim_number", nullable = false, unique = true)
    private String claimNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "policy_id", nullable = false)
    private Policy policy;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @Builder.Default
    private ClaimStatus status = ClaimStatus.DECLARED;

    @Column(name = "incident_date", nullable = false)
    private LocalDate incidentDate;

    @Column(name = "declaration_date", nullable = false)
    private LocalDateTime declarationDate;

    @Column(name = "incident_description", columnDefinition = "TEXT")
    private String incidentDescription;

    @Column(name = "incident_location")
    private String incidentLocation;

    @Column(name = "estimated_amount", precision = 15, scale = 3)
    private BigDecimal estimatedAmount;

    @Column(name = "approved_amount", precision = 15, scale = 3)
    private BigDecimal approvedAmount;

    @Column(name = "deductible_amount", precision = 15, scale = 3)
    private BigDecimal deductibleAmount;

    @Column(name = "fraud_score")
    private Integer fraudScore;

    @Column(name = "fraud_flagged")
    @Builder.Default
    private boolean fraudFlagged = false;

    @OneToMany(mappedBy = "claim", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ClaimDocument> documents = new ArrayList<>();

    @OneToMany(mappedBy = "claim", cascade = CascadeType.ALL)
    @Builder.Default
    private List<ClaimStatusHistory> statusHistory = new ArrayList<>();

    @OneToMany(mappedBy = "claim", cascade = CascadeType.ALL)
    @Builder.Default
    private List<ExpertAssignment> expertAssignments = new ArrayList<>();
}
