package com.erp.assurance.tunisie.policy.service;

import com.erp.assurance.tunisie.crm.entity.Client;
import com.erp.assurance.tunisie.crm.repository.ClientRepository;
import com.erp.assurance.tunisie.policy.dto.PolicyCreateRequest;
import com.erp.assurance.tunisie.policy.dto.PolicyResponse;
import com.erp.assurance.tunisie.policy.entity.Policy;
import com.erp.assurance.tunisie.policy.repository.PolicyRepository;
import com.erp.assurance.tunisie.shared.enums.ClientType;
import com.erp.assurance.tunisie.shared.enums.PolicyStatus;
import com.erp.assurance.tunisie.shared.enums.ProductType;
import com.erp.assurance.tunisie.shared.exception.ResourceNotFoundException;
import com.erp.assurance.tunisie.underwriting.entity.Product;
import com.erp.assurance.tunisie.underwriting.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PolicyServiceTest {

    @Mock private PolicyRepository policyRepository;
    @Mock private ClientRepository clientRepository;
    @Mock private ProductRepository productRepository;
    @InjectMocks private PolicyService policyService;

    private UUID clientId;
    private UUID productId;
    private UUID policyId;
    private Client testClient;
    private Product testProduct;
    private Policy testPolicy;

    @BeforeEach
    void setUp() {
        clientId = UUID.randomUUID();
        productId = UUID.randomUUID();
        policyId = UUID.randomUUID();

        testClient = Client.builder()
                .clientType(ClientType.INDIVIDUAL)
                .firstName("Ahmed").lastName("Ben Ali")
                .email("ahmed@example.com").cin("12345678")
                .build();
        testClient.setId(clientId);

        testProduct = Product.builder()
                .name("Auto Insurance").productType(ProductType.AUTO)
                .build();
        testProduct.setId(productId);

        testPolicy = Policy.builder()
                .policyNumber("POL-12345")
                .client(testClient)
                .product(testProduct)
                .effectiveDate(LocalDate.now())
                .expiryDate(LocalDate.now().plusYears(1))
                .sumInsured(new BigDecimal("100000.000"))
                .status(PolicyStatus.DRAFT)
                .build();
        testPolicy.setId(policyId);
    }

    @Test
    void createPolicy_Success() {
        PolicyCreateRequest request = PolicyCreateRequest.builder()
                .clientId(clientId).productId(productId)
                .effectiveDate(LocalDate.now()).expiryDate(LocalDate.now().plusYears(1))
                .sumInsured(new BigDecimal("100000.000"))
                .build();

        when(clientRepository.findById(clientId)).thenReturn(Optional.of(testClient));
        when(productRepository.findById(productId)).thenReturn(Optional.of(testProduct));
        when(policyRepository.save(any(Policy.class))).thenReturn(testPolicy);

        PolicyResponse response = policyService.createPolicy(request);

        assertThat(response).isNotNull();
        assertThat(response.getPolicyNumber()).isEqualTo("POL-12345");
        assertThat(response.getStatus()).isEqualTo(PolicyStatus.DRAFT);
        verify(policyRepository).save(any(Policy.class));
    }

    @Test
    void createPolicy_ClientNotFound_ThrowsException() {
        PolicyCreateRequest request = PolicyCreateRequest.builder()
                .clientId(clientId).productId(productId)
                .effectiveDate(LocalDate.now()).expiryDate(LocalDate.now().plusYears(1))
                .build();

        when(clientRepository.findById(clientId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> policyService.createPolicy(request))
                .isInstanceOf(ResourceNotFoundException.class);
        verify(policyRepository, never()).save(any());
    }

    @Test
    void createPolicy_ProductNotFound_ThrowsException() {
        PolicyCreateRequest request = PolicyCreateRequest.builder()
                .clientId(clientId).productId(productId)
                .effectiveDate(LocalDate.now()).expiryDate(LocalDate.now().plusYears(1))
                .build();

        when(clientRepository.findById(clientId)).thenReturn(Optional.of(testClient));
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> policyService.createPolicy(request))
                .isInstanceOf(ResourceNotFoundException.class);
        verify(policyRepository, never()).save(any());
    }

    @Test
    void activatePolicy_Success() {
        when(policyRepository.findById(policyId)).thenReturn(Optional.of(testPolicy));
        when(policyRepository.save(any(Policy.class))).thenReturn(testPolicy);

        PolicyResponse response = policyService.activatePolicy(policyId);

        assertThat(response).isNotNull();
        verify(policyRepository).save(argThat(p -> p.getStatus() == PolicyStatus.ACTIVE));
    }

    @Test
    void activatePolicy_NotFound_ThrowsException() {
        when(policyRepository.findById(policyId)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> policyService.activatePolicy(policyId))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void getPolicy_Success() {
        when(policyRepository.findById(policyId)).thenReturn(Optional.of(testPolicy));
        PolicyResponse response = policyService.getPolicy(policyId);
        assertThat(response.getId()).isEqualTo(policyId);
    }

    @Test
    void cancelPolicy_SetsStatusCancelled() {
        when(policyRepository.findById(policyId)).thenReturn(Optional.of(testPolicy));
        when(policyRepository.save(any(Policy.class))).thenReturn(testPolicy);

        policyService.cancelPolicy(policyId);

        verify(policyRepository).save(argThat(p -> p.getStatus() == PolicyStatus.CANCELLED));
    }

    @Test
    void getPoliciesByClient_ReturnsPaginatedResults() {
        Page<Policy> page = new PageImpl<>(List.of(testPolicy));
        when(policyRepository.findByClientId(eq(clientId), any())).thenReturn(page);

        var response = policyService.getPoliciesByClient(clientId, PageRequest.of(0, 20));

        assertThat(response.getContent()).hasSize(1);
        assertThat(response.getContent().get(0).getPolicyNumber()).isEqualTo("POL-12345");
    }

    @Test
    void getPoliciesByStatus_ReturnsPaginatedResults() {
        Page<Policy> page = new PageImpl<>(List.of(testPolicy));
        when(policyRepository.findByStatus(eq(PolicyStatus.DRAFT), any())).thenReturn(page);

        var response = policyService.getPoliciesByStatus(PolicyStatus.DRAFT, PageRequest.of(0, 20));

        assertThat(response.getContent()).hasSize(1);
    }
}
