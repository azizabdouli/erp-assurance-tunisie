package com.erp.assurance.tunisie.policy.repository;

import com.erp.assurance.tunisie.policy.entity.Payment;
import com.erp.assurance.tunisie.shared.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, UUID> {
    List<Payment> findByInvoiceId(UUID invoiceId);
    List<Payment> findByPaymentStatus(PaymentStatus status);
}
