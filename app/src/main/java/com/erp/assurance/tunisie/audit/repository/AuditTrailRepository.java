package com.erp.assurance.tunisie.audit.repository;

import com.erp.assurance.tunisie.audit.entity.AuditTrail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AuditTrailRepository extends JpaRepository<AuditTrail, UUID> {
    List<AuditTrail> findByTableNameAndRecordIdOrderByChangedAtDesc(String tableName, String recordId);
}
