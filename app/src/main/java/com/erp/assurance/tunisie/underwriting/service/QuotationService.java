package com.erp.assurance.tunisie.underwriting.service;

import com.erp.assurance.tunisie.crm.entity.Client;
import com.erp.assurance.tunisie.crm.repository.ClientRepository;
import com.erp.assurance.tunisie.shared.dto.PageResponse;
import com.erp.assurance.tunisie.shared.enums.QuotationStatus;
import com.erp.assurance.tunisie.shared.exception.BusinessException;
import com.erp.assurance.tunisie.shared.exception.ResourceNotFoundException;
import com.erp.assurance.tunisie.underwriting.dto.QuotationRequest;
import com.erp.assurance.tunisie.underwriting.dto.QuotationResponse;
import com.erp.assurance.tunisie.underwriting.entity.Product;
import com.erp.assurance.tunisie.underwriting.entity.Quotation;
import com.erp.assurance.tunisie.underwriting.repository.ProductRepository;
import com.erp.assurance.tunisie.underwriting.repository.QuotationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class QuotationService {

    private final QuotationRepository quotationRepository;
    private final ClientRepository clientRepository;
    private final ProductRepository productRepository;
    private final PremiumCalculationEngine premiumEngine;

    public QuotationResponse createQuotation(QuotationRequest request) {
        Client client = clientRepository.findById(request.getClientId())
                .orElseThrow(() -> new ResourceNotFoundException("Client", "id", request.getClientId()));
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", request.getProductId()));

        BigDecimal basePremium = premiumEngine.calculateBasePremium(product.getProductType(), new HashMap<>());
        BigDecimal netPremium = premiumEngine.applyDiscount(basePremium, request.getDiscountPercentage());
        BigDecimal taxAmount = premiumEngine.calculateTax(netPremium);
        BigDecimal totalPremium = premiumEngine.calculateTotalPremium(netPremium);

        String quotationNumber = "QT-" + System.currentTimeMillis();

        Quotation quotation = Quotation.builder()
                .quotationNumber(quotationNumber)
                .client(client)
                .product(product)
                .effectiveDate(request.getEffectiveDate())
                .expiryDate(request.getExpiryDate())
                .basePremium(basePremium)
                .netPremium(netPremium)
                .taxAmount(taxAmount)
                .totalPremium(totalPremium)
                .discountPercentage(request.getDiscountPercentage())
                .riskData(request.getRiskData())
                .validUntil(LocalDateTime.now().plusDays(30))
                .build();

        Quotation saved = quotationRepository.save(quotation);
        log.info("Created quotation: {}", saved.getQuotationNumber());
        return mapToResponse(saved);
    }

    @Transactional(readOnly = true)
    public QuotationResponse getQuotation(UUID id) {
        Quotation quotation = quotationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Quotation", "id", id));
        return mapToResponse(quotation);
    }

    @Transactional(readOnly = true)
    public PageResponse<QuotationResponse> getQuotationsByClient(UUID clientId, Pageable pageable) {
        Page<Quotation> page = quotationRepository.findByClientId(clientId, pageable);
        return PageResponse.from(page.map(this::mapToResponse));
    }

    public QuotationResponse approveQuotation(UUID id, String approvedBy) {
        Quotation quotation = quotationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Quotation", "id", id));
        if (quotation.getStatus() != QuotationStatus.PENDING_APPROVAL) {
            throw new BusinessException("UW_003", "Quotation must be in PENDING_APPROVAL status to be approved");
        }
        quotation.setStatus(QuotationStatus.APPROVED);
        quotation.setApprovedBy(approvedBy);
        quotation.setApprovedAt(LocalDateTime.now());
        Quotation updated = quotationRepository.save(quotation);
        log.info("Approved quotation: {}", updated.getQuotationNumber());
        return mapToResponse(updated);
    }

    private QuotationResponse mapToResponse(Quotation q) {
        return QuotationResponse.builder()
                .id(q.getId())
                .quotationNumber(q.getQuotationNumber())
                .clientId(q.getClient().getId())
                .clientName(q.getClient().getFullName())
                .productId(q.getProduct().getId())
                .productName(q.getProduct().getName())
                .status(q.getStatus())
                .effectiveDate(q.getEffectiveDate())
                .expiryDate(q.getExpiryDate())
                .basePremium(q.getBasePremium())
                .netPremium(q.getNetPremium())
                .taxAmount(q.getTaxAmount())
                .totalPremium(q.getTotalPremium())
                .discountPercentage(q.getDiscountPercentage())
                .loadingPercentage(q.getLoadingPercentage())
                .validUntil(q.getValidUntil())
                .createdAt(q.getCreatedAt())
                .build();
    }
}
