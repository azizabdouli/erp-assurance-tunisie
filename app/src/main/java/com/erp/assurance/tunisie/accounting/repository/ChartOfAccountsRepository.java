package com.erp.assurance.tunisie.accounting.repository;

import com.erp.assurance.tunisie.accounting.entity.ChartOfAccounts;
import com.erp.assurance.tunisie.shared.enums.AccountType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ChartOfAccountsRepository extends JpaRepository<ChartOfAccounts, UUID> {
    Optional<ChartOfAccounts> findByAccountCode(String accountCode);
    List<ChartOfAccounts> findByAccountType(AccountType accountType);
    List<ChartOfAccounts> findByParentAccountCode(String parentAccountCode);
    List<ChartOfAccounts> findByActiveTrue();
}
