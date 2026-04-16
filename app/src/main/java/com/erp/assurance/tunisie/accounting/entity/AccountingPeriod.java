package com.erp.assurance.tunisie.accounting.entity;

import com.erp.assurance.tunisie.shared.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "accounting_periods")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AccountingPeriod extends BaseEntity {

    @Column(name = "period_name", nullable = false)
    private String periodName;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "fiscal_year", nullable = false)
    private int fiscalYear;

    @Column(name = "closed")
    @Builder.Default
    private boolean closed = false;
}
