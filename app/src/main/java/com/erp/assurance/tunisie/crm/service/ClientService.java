package com.erp.assurance.tunisie.crm.service;

import com.erp.assurance.tunisie.crm.dto.*;
import com.erp.assurance.tunisie.shared.dto.PageResponse;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ClientService {
    ClientResponse createClient(ClientCreateRequest request);
    ClientResponse updateClient(UUID id, ClientUpdateRequest request);
    ClientResponse getClientById(UUID id);
    PageResponse<ClientResponse> searchClients(ClientSearchCriteria criteria, Pageable pageable);
    PageResponse<ClientResponse> getAllClients(Pageable pageable);
    void deleteClient(UUID id);
    ClientResponse validateKyc(UUID clientId);
    ClientResponse performAmlScreening(UUID clientId);
}
