package com.erp.assurance.tunisie.policy.entity;

import com.erp.assurance.tunisie.shared.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "policy_renewals")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PolicyRenewal extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "original_policy_id", nullable = false)
    private Policy originalPolicy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "renewed_policy_id")
    private Policy renewedPolicy;

    @Column(name = "renewal_date", nullable = false)
    private LocalDate renewalDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "renewal_status")
    @Builder.Default
    private RenewalStatus renewalStatus = RenewalStatus.PENDING;

    @Column(name = "comments", columnDefinition = "TEXT")
    private String comments;

    public enum RenewalStatus {
        PENDING, RENEWED, DECLINED, EXPIRED
    }
}
