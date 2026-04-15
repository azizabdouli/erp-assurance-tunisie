package com.erp.assurance.tunisie.crm.repository;

import com.erp.assurance.tunisie.crm.entity.KycDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface KycDocumentRepository extends JpaRepository<KycDocument, UUID> {
    List<KycDocument> findByClientId(UUID clientId);
}
