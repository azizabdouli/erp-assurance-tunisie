package com.erp.assurance.tunisie.accounting.entity;

import com.erp.assurance.tunisie.shared.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "general_ledger")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class GeneralLedger extends BaseEntity {

    @Column(name = "account_code", nullable = false)
    private String accountCode;

    @Column(name = "period_date", nullable = false)
    private LocalDate periodDate;

    @Column(name = "total_debit", precision = 15, scale = 3)
    @Builder.Default
    private BigDecimal totalDebit = BigDecimal.ZERO;

    @Column(name = "total_credit", precision = 15, scale = 3)
    @Builder.Default
    private BigDecimal totalCredit = BigDecimal.ZERO;

    @Column(name = "balance", precision = 15, scale = 3)
    @Builder.Default
    private BigDecimal balance = BigDecimal.ZERO;
}
