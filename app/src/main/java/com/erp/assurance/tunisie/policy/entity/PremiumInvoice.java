package com.erp.assurance.tunisie.policy.entity;

import com.erp.assurance.tunisie.shared.entity.AuditableEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "premium_invoices")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PremiumInvoice extends AuditableEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "policy_id", nullable = false)
    private Policy policy;

    @Column(name = "invoice_number", nullable = false, unique = true)
    private String invoiceNumber;

    @Column(name = "amount", precision = 15, scale = 3, nullable = false)
    private BigDecimal amount;

    @Column(name = "tax_amount", precision = 15, scale = 3)
    private BigDecimal taxAmount;

    @Column(name = "total_amount", precision = 15, scale = 3, nullable = false)
    private BigDecimal totalAmount;

    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;

    @Column(name = "paid")
    @Builder.Default
    private boolean paid = false;

    @Column(name = "paid_date")
    private LocalDate paidDate;

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Payment> payments = new ArrayList<>();
}
