package com.erp.assurance.tunisie.crm.repository;

import com.erp.assurance.tunisie.crm.entity.ClientInteraction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ClientInteractionRepository extends JpaRepository<ClientInteraction, UUID> {
    Page<ClientInteraction> findByClientId(UUID clientId, Pageable pageable);
}
