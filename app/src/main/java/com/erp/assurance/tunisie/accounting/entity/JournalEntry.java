package com.erp.assurance.tunisie.accounting.entity;

import com.erp.assurance.tunisie.shared.entity.AuditableEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "journal_entries")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class JournalEntry extends AuditableEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "journal_id", nullable = false)
    private Journal journal;

    @Column(name = "entry_number", nullable = false)
    private String entryNumber;

    @Column(name = "entry_date", nullable = false)
    private LocalDate entryDate;

    @Column(name = "account_code", nullable = false)
    private String accountCode;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "debit_amount", precision = 15, scale = 3)
    @Builder.Default
    private BigDecimal debitAmount = BigDecimal.ZERO;

    @Column(name = "credit_amount", precision = 15, scale = 3)
    @Builder.Default
    private BigDecimal creditAmount = BigDecimal.ZERO;

    @Column(name = "reference")
    private String reference;

    @Column(name = "reference_type")
    private String referenceType;

    @Column(name = "posted")
    @Builder.Default
    private boolean posted = false;
}
