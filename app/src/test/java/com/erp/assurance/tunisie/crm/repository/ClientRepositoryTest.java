package com.erp.assurance.tunisie.crm.repository;

import com.erp.assurance.tunisie.crm.entity.Client;
import com.erp.assurance.tunisie.shared.enums.ClientType;
import com.erp.assurance.tunisie.shared.enums.KycStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class ClientRepositoryTest {

    @Autowired
    private ClientRepository clientRepository;

    private Client individualClient;
    private Client corporateClient;

    @BeforeEach
    void setUp() {
        individualClient = clientRepository.save(Client.builder()
                .clientType(ClientType.INDIVIDUAL)
                .firstName("Ali")
                .lastName("Ben Salah")
                .email("ali.bensalah@erp.tn")
                .cin("12345678")
                .kycStatus(KycStatus.VERIFIED)
                .deleted(false)
                .build());

        corporateClient = clientRepository.save(Client.builder()
                .clientType(ClientType.CORPORATE)
                .companyName("Assurance SA")
                .email("contact@assurance.tn")
                .kycStatus(KycStatus.PENDING)
                .deleted(false)
                .build());
    }

    @Test
    void findByCinAndDeletedFalse_ExistingCin_ReturnsClient() {
        Optional<Client> result = clientRepository.findByCinAndDeletedFalse("12345678");

        assertThat(result).isPresent();
        assertThat(result.get().getFirstName()).isEqualTo("Ali");
    }

    @Test
    void findByCinAndDeletedFalse_UnknownCin_ReturnsEmpty() {
        Optional<Client> result = clientRepository.findByCinAndDeletedFalse("99999999");

        assertThat(result).isEmpty();
    }

    @Test
    void findByEmailAndDeletedFalse_ExistingEmail_ReturnsClient() {
        Optional<Client> result = clientRepository.findByEmailAndDeletedFalse("ali.bensalah@erp.tn");

        assertThat(result).isPresent();
        assertThat(result.get().getCin()).isEqualTo("12345678");
    }

    @Test
    void findByEmailAndDeletedFalse_DeletedClient_ReturnsEmpty() {
        individualClient.setDeleted(true);
        clientRepository.save(individualClient);

        Optional<Client> result = clientRepository.findByEmailAndDeletedFalse("ali.bensalah@erp.tn");

        assertThat(result).isEmpty();
    }

    @Test
    void existsByCinAndDeletedFalse_ExistingCin_ReturnsTrue() {
        assertThat(clientRepository.existsByCinAndDeletedFalse("12345678")).isTrue();
    }

    @Test
    void existsByCinAndDeletedFalse_UnknownCin_ReturnsFalse() {
        assertThat(clientRepository.existsByCinAndDeletedFalse("00000000")).isFalse();
    }

    @Test
    void existsByEmailAndDeletedFalse_ExistingEmail_ReturnsTrue() {
        assertThat(clientRepository.existsByEmailAndDeletedFalse("ali.bensalah@erp.tn")).isTrue();
    }

    @Test
    void findByKycStatusAndDeletedFalse_VerifiedClients_ReturnsSingleRecord() {
        Page<Client> verified = clientRepository.findByKycStatusAndDeletedFalse(
                KycStatus.VERIFIED, PageRequest.of(0, 10));

        assertThat(verified.getTotalElements()).isEqualTo(1);
        assertThat(verified.getContent().get(0).getFirstName()).isEqualTo("Ali");
    }

    @Test
    void findByDeletedFalse_ReturnsAllNonDeletedClients() {
        Page<Client> all = clientRepository.findByDeletedFalse(PageRequest.of(0, 10));

        assertThat(all.getTotalElements()).isEqualTo(2);
    }

    @Test
    void searchClients_ByFirstName_ReturnsMatchingClients() {
        Page<Client> results = clientRepository.searchClients(
                "ali", null, null, null, PageRequest.of(0, 10));

        assertThat(results.getTotalElements()).isEqualTo(1);
        assertThat(results.getContent().get(0).getFirstName()).isEqualTo("Ali");
    }

    @Test
    void searchClients_ByClientType_ReturnsCorporateOnly() {
        Page<Client> results = clientRepository.searchClients(
                null, null, null, ClientType.CORPORATE, PageRequest.of(0, 10));

        assertThat(results.getTotalElements()).isEqualTo(1);
        assertThat(results.getContent().get(0).getCompanyName()).isEqualTo("Assurance SA");
    }

    @Test
    void searchClients_NoFilters_ReturnsAll() {
        Page<Client> results = clientRepository.searchClients(
                null, null, null, null, PageRequest.of(0, 10));

        assertThat(results.getTotalElements()).isEqualTo(2);
    }
}
