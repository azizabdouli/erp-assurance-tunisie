package com.erp.assurance.tunisie.indemnity.entity;

import com.erp.assurance.tunisie.shared.entity.AuditableEntity;
import com.erp.assurance.tunisie.shared.enums.PaymentMethod;
import com.erp.assurance.tunisie.shared.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "settlement_payments")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class SettlementPayment extends AuditableEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "settlement_id", nullable = false)
    private Settlement settlement;

    @Column(name = "payment_reference", nullable = false, unique = true)
    private String paymentReference;

    @Column(name = "amount", precision = 15, scale = 3, nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", nullable = false)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false)
    @Builder.Default
    private PaymentStatus paymentStatus = PaymentStatus.PENDING;

    @Column(name = "payment_date")
    private LocalDateTime paymentDate;

    @Column(name = "beneficiary_name")
    private String beneficiaryName;

    @Column(name = "beneficiary_iban")
    private String beneficiaryIban;
}
