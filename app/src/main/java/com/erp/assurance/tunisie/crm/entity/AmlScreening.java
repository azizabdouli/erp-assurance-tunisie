package com.erp.assurance.tunisie.crm.entity;

import javax.persistence.*;
import java.util.UUID;
import java.util.Date;

@Entity
public class AmlScreening extends AuditableEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private UUID clientId;

    @Column(nullable = false)
    private Date screeningDate;

    @Column(nullable = false)
    private String screeningProvider;

    @Column(nullable = false)
    private boolean pepMatch;

    @Column(nullable = false)
    private boolean sanctionMatch;

    @ElementCollection
    private String[] riskKeywords;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ScreeningStatus screeningStatus;

    private String notes;

    private String reviewedBy;

    private Date reviewDate;

    @Enumerated(EnumType.STRING)
    private ApprovalStatus approvalStatus;

    // Getters and Setters
}

public enum ScreeningStatus {
    PASSED,
    FLAGGED,
    REJECTED
}

public enum ApprovalStatus {
    APPROVED,
    PENDING,
    REJECTED
}