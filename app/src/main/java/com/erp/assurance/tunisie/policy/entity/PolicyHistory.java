package com.erp.assurance.tunisie.policy.entity;

import com.erp.assurance.tunisie.shared.entity.BaseEntity;
import com.erp.assurance.tunisie.shared.enums.PolicyStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "policy_history")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PolicyHistory extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "policy_id", nullable = false)
    private Policy policy;

    @Enumerated(EnumType.STRING)
    @Column(name = "old_status")
    private PolicyStatus oldStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "new_status", nullable = false)
    private PolicyStatus newStatus;

    @Column(name = "changed_by")
    private String changedBy;

    @Column(name = "changed_at", nullable = false)
    private LocalDateTime changedAt;

    @Column(name = "comments", columnDefinition = "TEXT")
    private String comments;
}
