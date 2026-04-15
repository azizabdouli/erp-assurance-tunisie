package com.erp.assurance.tunisie.policy.service;

import com.erp.assurance.tunisie.policy.entity.Payment;
import com.erp.assurance.tunisie.policy.entity.PremiumInvoice;
import com.erp.assurance.tunisie.policy.repository.PaymentRepository;
import com.erp.assurance.tunisie.policy.repository.PremiumInvoiceRepository;
import com.erp.assurance.tunisie.shared.enums.PaymentStatus;
import com.erp.assurance.tunisie.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PremiumCollectionService {

    private final PremiumInvoiceRepository invoiceRepository;
    private final PaymentRepository paymentRepository;

    @Transactional(readOnly = true)
    public List<PremiumInvoice> getInvoicesByPolicy(UUID policyId) {
        return invoiceRepository.findByPolicyId(policyId);
    }

    public Payment recordPayment(UUID invoiceId, Payment payment) {
        PremiumInvoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice", "id", invoiceId));

        payment.setInvoice(invoice);
        payment.setPaymentStatus(PaymentStatus.COMPLETED);
        payment.setPaymentDate(LocalDateTime.now());
        Payment saved = paymentRepository.save(payment);

        invoice.setPaid(true);
        invoice.setPaidDate(LocalDate.now());
        invoiceRepository.save(invoice);

        log.info("Payment recorded for invoice: {}", invoice.getInvoiceNumber());
        return saved;
    }
}
