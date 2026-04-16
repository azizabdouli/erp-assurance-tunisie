package com.erp.assurance.tunisie.crm.entity;

import com.erp.assurance.tunisie.shared.entity.AuditableEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "client_interactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientInteraction extends AuditableEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @Enumerated(EnumType.STRING)
    @Column(name = "interaction_type", nullable = false)
    private InteractionType interactionType;

    @Column(name = "subject", nullable = false)
    private String subject;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "interaction_date", nullable = false)
    private LocalDateTime interactionDate;

    @Column(name = "performed_by")
    private String performedBy;

    @Column(name = "outcome")
    private String outcome;

    public enum InteractionType {
        CALL, EMAIL, MEETING, VISIT, SMS, OTHER
    }
}
