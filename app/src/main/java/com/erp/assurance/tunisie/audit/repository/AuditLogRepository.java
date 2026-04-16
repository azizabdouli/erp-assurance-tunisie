package com.erp.assurance.tunisie.audit.repository;

import com.erp.assurance.tunisie.audit.entity.AuditLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.UUID;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, UUID> {
    Page<AuditLog> findByEntityTypeAndEntityId(String entityType, String entityId, Pageable pageable);
    Page<AuditLog> findByPerformedBy(String performedBy, Pageable pageable);
    Page<AuditLog> findByPerformedAtBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);
    Page<AuditLog> findByAction(AuditLog.AuditAction action, Pageable pageable);
}
