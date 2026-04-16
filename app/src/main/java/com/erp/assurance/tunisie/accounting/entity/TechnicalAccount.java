package com.erp.assurance.tunisie.accounting.entity;

import com.erp.assurance.tunisie.shared.entity.AuditableEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "technical_accounts")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class TechnicalAccount extends AuditableEntity {

    @Column(name = "account_code", nullable = false, unique = true)
    private String accountCode;

    @Column(name = "account_name", nullable = false)
    private String accountName;

    @Column(name = "product_code")
    private String productCode;

    @Column(name = "premiums_written", precision = 15, scale = 3)
    @Builder.Default
    private BigDecimal premiumsWritten = BigDecimal.ZERO;

    @Column(name = "premiums_earned", precision = 15, scale = 3)
    @Builder.Default
    private BigDecimal premiumsEarned = BigDecimal.ZERO;

    @Column(name = "claims_incurred", precision = 15, scale = 3)
    @Builder.Default
    private BigDecimal claimsIncurred = BigDecimal.ZERO;

    @Column(name = "claims_paid", precision = 15, scale = 3)
    @Builder.Default
    private BigDecimal claimsPaid = BigDecimal.ZERO;

    @Column(name = "reserves", precision = 15, scale = 3)
    @Builder.Default
    private BigDecimal reserves = BigDecimal.ZERO;

    @Column(name = "commissions", precision = 15, scale = 3)
    @Builder.Default
    private BigDecimal commissions = BigDecimal.ZERO;
}
