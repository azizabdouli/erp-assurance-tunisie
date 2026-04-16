package com.erp.assurance.tunisie.audit.service;

import com.erp.assurance.tunisie.audit.entity.AuditLog;
import com.erp.assurance.tunisie.audit.repository.AuditLogRepository;
import com.erp.assurance.tunisie.shared.dto.PageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuditService {

    private final AuditLogRepository auditLogRepository;

    @Async
    @Transactional
    public void logAction(String entityType, String entityId, AuditLog.AuditAction action, String description) {
        String performedBy = getCurrentUser();

        AuditLog auditLog = AuditLog.builder()
                .entityType(entityType)
                .entityId(entityId)
                .action(action)
                .performedBy(performedBy)
                .performedAt(LocalDateTime.now())
                .description(description)
                .build();

        auditLogRepository.save(auditLog);
        log.debug("Audit log: {} {} {} by {}", action, entityType, entityId, performedBy);
    }

    @Async
    @Transactional
    public void logChange(String entityType, String entityId, AuditLog.AuditAction action,
                          String oldValue, String newValue) {
        String performedBy = getCurrentUser();

        AuditLog auditLog = AuditLog.builder()
                .entityType(entityType)
                .entityId(entityId)
                .action(action)
                .performedBy(performedBy)
                .performedAt(LocalDateTime.now())
                .oldValue(oldValue)
                .newValue(newValue)
                .build();

        auditLogRepository.save(auditLog);
    }

    @Transactional(readOnly = true)
    public PageResponse<AuditLog> getAuditLogs(String entityType, String entityId, Pageable pageable) {
        Page<AuditLog> page = auditLogRepository.findByEntityTypeAndEntityId(entityType, entityId, pageable);
        return PageResponse.from(page);
    }

    @Transactional(readOnly = true)
    public PageResponse<AuditLog> getAuditLogsByUser(String username, Pageable pageable) {
        Page<AuditLog> page = auditLogRepository.findByPerformedBy(username, pageable);
        return PageResponse.from(page);
    }

    private String getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null ? auth.getName() : "system";
    }
}
