package com.erp.assurance.tunisie.crm.service;

import com.erp.assurance.tunisie.crm.dto.*;
import com.erp.assurance.tunisie.crm.entity.Client;
import com.erp.assurance.tunisie.crm.repository.AmlScreeningRepository;
import com.erp.assurance.tunisie.crm.repository.ClientRepository;
import com.erp.assurance.tunisie.shared.enums.ClientType;
import com.erp.assurance.tunisie.shared.enums.KycStatus;
import com.erp.assurance.tunisie.shared.exception.BusinessException;
import com.erp.assurance.tunisie.shared.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientServiceImplTest {
    @Mock private ClientRepository clientRepository;
    @Mock private AmlScreeningRepository amlScreeningRepository;
    @InjectMocks private ClientServiceImpl clientService;

    private Client testClient;
    private UUID clientId;

    @BeforeEach
    void setUp() {
        clientId = UUID.randomUUID();
        testClient = Client.builder()
                .clientType(ClientType.INDIVIDUAL)
                .firstName("Ahmed").lastName("Ben Ali")
                .email("ahmed@example.com").cin("12345678")
                .build();
        testClient.setId(clientId);
    }

    @Test
    void createClient_Success() {
        ClientCreateRequest request = ClientCreateRequest.builder()
                .clientType(ClientType.INDIVIDUAL).firstName("Ahmed").lastName("Ben Ali")
                .email("ahmed@example.com").cin("12345678").build();
        when(clientRepository.existsByCinAndDeletedFalse("12345678")).thenReturn(false);
        when(clientRepository.existsByEmailAndDeletedFalse("ahmed@example.com")).thenReturn(false);
        when(clientRepository.save(any(Client.class))).thenReturn(testClient);

        ClientResponse response = clientService.createClient(request);
        assertThat(response).isNotNull();
        assertThat(response.getFirstName()).isEqualTo("Ahmed");
    }

    @Test
    void createClient_DuplicateCIN_ThrowsException() {
        ClientCreateRequest request = ClientCreateRequest.builder().clientType(ClientType.INDIVIDUAL).cin("12345678").build();
        when(clientRepository.existsByCinAndDeletedFalse("12345678")).thenReturn(true);
        assertThatThrownBy(() -> clientService.createClient(request)).isInstanceOf(BusinessException.class);
    }

    @Test
    void getClientById_Success() {
        when(clientRepository.findById(clientId)).thenReturn(Optional.of(testClient));
        ClientResponse response = clientService.getClientById(clientId);
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(clientId);
    }

    @Test
    void getClientById_NotFound() {
        when(clientRepository.findById(clientId)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> clientService.getClientById(clientId)).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void deleteClient_SoftDelete() {
        when(clientRepository.findById(clientId)).thenReturn(Optional.of(testClient));
        when(clientRepository.save(any(Client.class))).thenReturn(testClient);
        clientService.deleteClient(clientId);
        verify(clientRepository).save(argThat(Client::isDeleted));
    }

    @Test
    void validateKyc_Success() {
        when(clientRepository.findById(clientId)).thenReturn(Optional.of(testClient));
        when(clientRepository.save(any(Client.class))).thenReturn(testClient);
        clientService.validateKyc(clientId);
        verify(clientRepository).save(argThat(c -> c.getKycStatus() == KycStatus.VERIFIED));
    }

    @Test
    void getAllClients_ReturnsPage() {
        Page<Client> page = new PageImpl<>(List.of(testClient));
        when(clientRepository.findByDeletedFalse(any())).thenReturn(page);
        var response = clientService.getAllClients(PageRequest.of(0, 20));
        assertThat(response.getContent()).hasSize(1);
    }
}
