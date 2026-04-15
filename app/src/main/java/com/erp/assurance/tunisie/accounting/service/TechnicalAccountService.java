package com.erp.assurance.tunisie.accounting.service;

import com.erp.assurance.tunisie.accounting.entity.TechnicalAccount;
import com.erp.assurance.tunisie.accounting.repository.TechnicalAccountRepository;
import com.erp.assurance.tunisie.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class TechnicalAccountService {

    private final TechnicalAccountRepository technicalAccountRepository;

    public TechnicalAccount getByCode(String accountCode) {
        return technicalAccountRepository.findByAccountCode(accountCode)
                .orElseThrow(() -> new ResourceNotFoundException("TechnicalAccount", "accountCode", accountCode));
    }

    public List<TechnicalAccount> getByProduct(String productCode) {
        return technicalAccountRepository.findByProductCode(productCode);
    }

    public void recordPremium(String accountCode, BigDecimal amount) {
        TechnicalAccount account = getByCode(accountCode);
        account.setPremiumsWritten(account.getPremiumsWritten().add(amount));
        technicalAccountRepository.save(account);
        log.info("Premium recorded: {} for account: {}", amount, accountCode);
    }

    public void recordClaim(String accountCode, BigDecimal amount) {
        TechnicalAccount account = getByCode(accountCode);
        account.setClaimsIncurred(account.getClaimsIncurred().add(amount));
        technicalAccountRepository.save(account);
        log.info("Claim recorded: {} for account: {}", amount, accountCode);
    }
}
