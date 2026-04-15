package com.erp.assurance.tunisie.accounting.entity;

import com.erp.assurance.tunisie.shared.entity.BaseEntity;
import com.erp.assurance.tunisie.shared.enums.AccountType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "chart_of_accounts")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ChartOfAccounts extends BaseEntity {

    @Column(name = "account_code", nullable = false, unique = true)
    private String accountCode;

    @Column(name = "account_name", nullable = false)
    private String accountName;

    @Enumerated(EnumType.STRING)
    @Column(name = "account_type", nullable = false)
    private AccountType accountType;

    @Column(name = "parent_account_code")
    private String parentAccountCode;

    @Column(name = "description")
    private String description;

    @Column(name = "active")
    @Builder.Default
    private boolean active = true;

    @Column(name = "level")
    private int level;
}
