package com.erp.assurance.tunisie.policy.entity;

import com.erp.assurance.tunisie.crm.entity.Client;
import com.erp.assurance.tunisie.shared.entity.AuditableEntity;
import com.erp.assurance.tunisie.shared.enums.PolicyStatus;
import com.erp.assurance.tunisie.underwriting.entity.Product;
import com.erp.assurance.tunisie.underwriting.entity.Quotation;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "policies")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Policy extends AuditableEntity {

    @Column(name = "policy_number", nullable = false, unique = true)
    private String policyNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quotation_id")
    private Quotation quotation;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @Builder.Default
    private PolicyStatus status = PolicyStatus.DRAFT;

    @Column(name = "effective_date", nullable = false)
    private LocalDate effectiveDate;

    @Column(name = "expiry_date", nullable = false)
    private LocalDate expiryDate;

    @Column(name = "annual_premium", precision = 15, scale = 3)
    private BigDecimal annualPremium;

    @Column(name = "net_premium", precision = 15, scale = 3)
    private BigDecimal netPremium;

    @Column(name = "tax_amount", precision = 15, scale = 3)
    private BigDecimal taxAmount;

    @Column(name = "total_premium", precision = 15, scale = 3)
    private BigDecimal totalPremium;

    @Column(name = "sum_insured", precision = 15, scale = 3)
    private BigDecimal sumInsured;

    @Column(name = "renewal_count")
    @Builder.Default
    private int renewalCount = 0;

    @OneToMany(mappedBy = "policy", cascade = CascadeType.ALL)
    @Builder.Default
    private List<PolicyEndorsement> endorsements = new ArrayList<>();

    @OneToMany(mappedBy = "policy", cascade = CascadeType.ALL)
    @Builder.Default
    private List<PremiumInvoice> invoices = new ArrayList<>();
}
