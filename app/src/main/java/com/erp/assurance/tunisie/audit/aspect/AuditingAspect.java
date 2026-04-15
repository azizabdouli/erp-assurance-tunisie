package com.erp.assurance.tunisie.audit.aspect;

import com.erp.assurance.tunisie.audit.entity.AuditLog;
import com.erp.assurance.tunisie.audit.service.AuditService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class AuditingAspect {

    private final AuditService auditService;

    @AfterReturning(pointcut = "execution(* com.erp.assurance.tunisie..service..*Service.create*(..))", returning = "result")
    public void auditCreate(JoinPoint joinPoint, Object result) {
        String entityType = extractEntityType(joinPoint);
        String entityId = extractEntityId(result);
        auditService.logAction(entityType, entityId, AuditLog.AuditAction.CREATE,
                "Created via " + joinPoint.getSignature().getName());
    }

    @AfterReturning(pointcut = "execution(* com.erp.assurance.tunisie..service..*Service.update*(..))", returning = "result")
    public void auditUpdate(JoinPoint joinPoint, Object result) {
        String entityType = extractEntityType(joinPoint);
        String entityId = extractEntityId(result);
        auditService.logAction(entityType, entityId, AuditLog.AuditAction.UPDATE,
                "Updated via " + joinPoint.getSignature().getName());
    }

    @AfterReturning(pointcut = "execution(* com.erp.assurance.tunisie..service..*Service.delete*(..))")
    public void auditDelete(JoinPoint joinPoint) {
        String entityType = extractEntityType(joinPoint);
        Object[] args = joinPoint.getArgs();
        String entityId = args.length > 0 ? args[0].toString() : "unknown";
        auditService.logAction(entityType, entityId, AuditLog.AuditAction.DELETE,
                "Deleted via " + joinPoint.getSignature().getName());
    }

    private String extractEntityType(JoinPoint joinPoint) {
        String className = joinPoint.getTarget().getClass().getSimpleName();
        return className.replace("ServiceImpl", "").replace("Service", "");
    }

    private String extractEntityId(Object result) {
        if (result == null) return "unknown";
        try {
            java.lang.reflect.Method getId = result.getClass().getMethod("getId");
            Object id = getId.invoke(result);
            return id != null ? id.toString() : "unknown";
        } catch (Exception e) {
            return result.toString();
        }
    }
}
