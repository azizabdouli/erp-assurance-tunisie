package com.erp.assurance.tunisie.crm.repository;

import com.erp.assurance.tunisie.crm.entity.AmlScreening;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AmlScreeningRepository extends JpaRepository<AmlScreening, UUID> {
    List<AmlScreening> findByClientIdOrderByScreeningDateDesc(UUID clientId);
}
