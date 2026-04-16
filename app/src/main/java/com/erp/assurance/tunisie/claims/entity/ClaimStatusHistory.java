package com.erp.assurance.tunisie.claims.entity;

import com.erp.assurance.tunisie.shared.entity.BaseEntity;
import com.erp.assurance.tunisie.shared.enums.ClaimStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "claim_status_history")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ClaimStatusHistory extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "claim_id", nullable = false)
    private Claim claim;

    @Enumerated(EnumType.STRING)
    @Column(name = "old_status")
    private ClaimStatus oldStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "new_status", nullable = false)
    private ClaimStatus newStatus;

    @Column(name = "changed_by")
    private String changedBy;

    @Column(name = "changed_at", nullable = false)
    private LocalDateTime changedAt;

    @Column(name = "comments", columnDefinition = "TEXT")
    private String comments;
}
