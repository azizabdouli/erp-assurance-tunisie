package com.erp.assurance.tunisie.audit.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "audit_trail")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AuditTrail {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "table_name", nullable = false)
    private String tableName;

    @Column(name = "record_id", nullable = false)
    private String recordId;

    @Column(name = "field_name", nullable = false)
    private String fieldName;

    @Column(name = "old_value", columnDefinition = "TEXT")
    private String oldValue;

    @Column(name = "new_value", columnDefinition = "TEXT")
    private String newValue;

    @Column(name = "changed_by")
    private String changedBy;

    @Column(name = "changed_at", nullable = false)
    private LocalDateTime changedAt;
}
