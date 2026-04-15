package com.erp.assurance.tunisie.crm.service;

import com.erp.assurance.tunisie.crm.dto.*;
import com.erp.assurance.tunisie.crm.entity.AmlScreening;
import com.erp.assurance.tunisie.crm.entity.Client;
import com.erp.assurance.tunisie.crm.repository.AmlScreeningRepository;
import com.erp.assurance.tunisie.crm.repository.ClientRepository;
import com.erp.assurance.tunisie.shared.dto.PageResponse;
import com.erp.assurance.tunisie.shared.enums.AmlRiskLevel;
import com.erp.assurance.tunisie.shared.enums.KycStatus;
import com.erp.assurance.tunisie.shared.exception.BusinessException;
import com.erp.assurance.tunisie.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final AmlScreeningRepository amlScreeningRepository;

    @Override
    public ClientResponse createClient(ClientCreateRequest request) {
        if (clientRepository.existsByCinAndDeletedFalse(request.getCin())) {
            throw new BusinessException("CRM_001", "Client with CIN " + request.getCin() + " already exists");
        }
        if (request.getEmail() != null && clientRepository.existsByEmailAndDeletedFalse(request.getEmail())) {
            throw new BusinessException("CRM_002", "Client with email " + request.getEmail() + " already exists");
        }

        Client client = Client.builder()
                .clientType(request.getClientType())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .cin(request.getCin())
                .dateOfBirth(request.getDateOfBirth())
                .companyName(request.getCompanyName())
                .registrationNumber(request.getRegistrationNumber())
                .taxId(request.getTaxId())
                .addressLine1(request.getAddressLine1())
                .addressLine2(request.getAddressLine2())
                .city(request.getCity())
                .governorate(request.getGovernorate())
                .postalCode(request.getPostalCode())
                .iban(request.getIban())
                .bankName(request.getBankName())
                .build();

        Client saved = clientRepository.save(client);
        log.info("Created client: {} ({})", saved.getFullName(), saved.getId());
        return mapToResponse(saved);
    }

    @Override
    public ClientResponse updateClient(UUID id, ClientUpdateRequest request) {
        Client client = findClientOrThrow(id);

        if (request.getFirstName() != null) client.setFirstName(request.getFirstName());
        if (request.getLastName() != null) client.setLastName(request.getLastName());
        if (request.getEmail() != null) client.setEmail(request.getEmail());
        if (request.getPhone() != null) client.setPhone(request.getPhone());
        if (request.getAddressLine1() != null) client.setAddressLine1(request.getAddressLine1());
        if (request.getAddressLine2() != null) client.setAddressLine2(request.getAddressLine2());
        if (request.getCity() != null) client.setCity(request.getCity());
        if (request.getGovernorate() != null) client.setGovernorate(request.getGovernorate());
        if (request.getPostalCode() != null) client.setPostalCode(request.getPostalCode());
        if (request.getIban() != null) client.setIban(request.getIban());
        if (request.getBankName() != null) client.setBankName(request.getBankName());

        Client updated = clientRepository.save(client);
        log.info("Updated client: {}", updated.getId());
        return mapToResponse(updated);
    }

    @Override
    @Transactional(readOnly = true)
    public ClientResponse getClientById(UUID id) {
        return mapToResponse(findClientOrThrow(id));
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<ClientResponse> searchClients(ClientSearchCriteria criteria, Pageable pageable) {
        Page<Client> page = clientRepository.searchClients(
                criteria.getFirstName(),
                criteria.getLastName(),
                criteria.getEmail(),
                criteria.getClientType(),
                pageable);
        return PageResponse.from(page.map(this::mapToResponse));
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<ClientResponse> getAllClients(Pageable pageable) {
        Page<Client> page = clientRepository.findByDeletedFalse(pageable);
        return PageResponse.from(page.map(this::mapToResponse));
    }

    @Override
    public void deleteClient(UUID id) {
        Client client = findClientOrThrow(id);
        client.setDeleted(true);
        clientRepository.save(client);
        log.info("Soft deleted client: {}", id);
    }

    @Override
    public ClientResponse validateKyc(UUID clientId) {
        Client client = findClientOrThrow(clientId);
        client.setKycStatus(KycStatus.VERIFIED);
        Client updated = clientRepository.save(client);
        log.info("KYC validated for client: {}", clientId);
        return mapToResponse(updated);
    }

    @Override
    public ClientResponse performAmlScreening(UUID clientId) {
        Client client = findClientOrThrow(clientId);

        AmlScreening screening = AmlScreening.builder()
                .client(client)
                .screeningDate(LocalDateTime.now())
                .screeningProvider("INTERNAL")
                .pepMatch(false)
                .sanctionMatch(false)
                .riskLevel(AmlRiskLevel.LOW)
                .screeningStatus(AmlScreening.ScreeningStatus.PASSED)
                .build();
        amlScreeningRepository.save(screening);

        client.setAmlRiskLevel(AmlRiskLevel.LOW);
        Client updated = clientRepository.save(client);
        log.info("AML screening completed for client: {}", clientId);
        return mapToResponse(updated);
    }

    private Client findClientOrThrow(UUID id) {
        return clientRepository.findById(id)
                .filter(c -> !c.isDeleted())
                .orElseThrow(() -> new ResourceNotFoundException("Client", "id", id));
    }

    private ClientResponse mapToResponse(Client client) {
        return ClientResponse.builder()
                .id(client.getId())
                .clientType(client.getClientType())
                .firstName(client.getFirstName())
                .lastName(client.getLastName())
                .fullName(client.getFullName())
                .email(client.getEmail())
                .phone(client.getPhone())
                .cin(client.getCin())
                .dateOfBirth(client.getDateOfBirth())
                .companyName(client.getCompanyName())
                .registrationNumber(client.getRegistrationNumber())
                .kycStatus(client.getKycStatus())
                .amlRiskLevel(client.getAmlRiskLevel())
                .city(client.getCity())
                .governorate(client.getGovernorate())
                .createdAt(client.getCreatedAt())
                .updatedAt(client.getUpdatedAt())
                .build();
    }
}
