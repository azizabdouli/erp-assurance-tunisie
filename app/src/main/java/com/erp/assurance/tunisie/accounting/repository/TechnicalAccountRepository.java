package com.erp.assurance.tunisie.accounting.repository;

import com.erp.assurance.tunisie.accounting.entity.TechnicalAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TechnicalAccountRepository extends JpaRepository<TechnicalAccount, UUID> {
    Optional<TechnicalAccount> findByAccountCode(String accountCode);
    List<TechnicalAccount> findByProductCode(String productCode);
}
