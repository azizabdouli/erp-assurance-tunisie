package com.erp.assurance.tunisie.underwriting.repository;

import com.erp.assurance.tunisie.underwriting.entity.Coverage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CoverageRepository extends JpaRepository<Coverage, UUID> {
    List<Coverage> findByProductId(UUID productId);
    List<Coverage> findByProductIdAndMandatoryTrue(UUID productId);
}
