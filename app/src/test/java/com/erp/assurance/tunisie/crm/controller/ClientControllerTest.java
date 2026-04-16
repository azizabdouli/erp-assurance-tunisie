package com.erp.assurance.tunisie.crm.controller;

import com.erp.assurance.tunisie.config.SecurityConfig;
import com.erp.assurance.tunisie.crm.dto.ClientResponse;
import com.erp.assurance.tunisie.crm.service.ClientService;
import com.erp.assurance.tunisie.shared.dto.PageResponse;
import com.erp.assurance.tunisie.shared.enums.AmlRiskLevel;
import com.erp.assurance.tunisie.shared.enums.ClientType;
import com.erp.assurance.tunisie.shared.enums.KycStatus;
import com.erp.assurance.tunisie.shared.security.JwtTokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ClientController.class)
@Import(SecurityConfig.class)
class ClientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ClientService clientService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    private JpaMetamodelMappingContext jpaMetamodelMappingContext;

    // ── Create client ─────────────────────────────────────────────────────────

    @Test
    void createClient_Unauthenticated_Returns401() throws Exception {
        mockMvc.perform(post("/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validClientRequestJson()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "AGENT")
    void createClient_Authenticated_Returns201() throws Exception {
        when(clientService.createClient(any())).thenReturn(buildClientResponse());

        mockMvc.perform(post("/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validClientRequestJson()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.cin").value("12345678"))
                .andExpect(jsonPath("$.data.email").value("ali.ben.ali@example.tn"));
    }

    @Test
    @WithMockUser(roles = "AGENT")
    void createClient_MissingCin_Returns400() throws Exception {
        String body = """
                {
                  "clientType": "INDIVIDUAL",
                  "firstName": "Ali",
                  "lastName": "Ben Ali",
                  "email": "ali@example.tn"
                }
                """;

        mockMvc.perform(post("/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "AGENT")
    void createClient_InvalidEmail_Returns400() throws Exception {
        String body = """
                {
                  "clientType": "INDIVIDUAL",
                  "cin": "12345678",
                  "email": "not-an-email"
                }
                """;

        mockMvc.perform(post("/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "AGENT")
    void createClient_MissingClientType_Returns400() throws Exception {
        String body = """
                {
                  "cin": "12345678",
                  "firstName": "Ali",
                  "lastName": "Ben Ali"
                }
                """;

        mockMvc.perform(post("/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    // ── Get client ────────────────────────────────────────────────────────────

    @Test
    void getClient_Unauthenticated_Returns401() throws Exception {
        mockMvc.perform(get("/clients/{id}", UUID.randomUUID()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "AGENT")
    void getClient_Authenticated_Returns200() throws Exception {
        when(clientService.getClientById(any())).thenReturn(buildClientResponse());

        mockMvc.perform(get("/clients/{id}", UUID.randomUUID()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.cin").value("12345678"));
    }

    // ── Update client ─────────────────────────────────────────────────────────

    @Test
    void updateClient_Unauthenticated_Returns401() throws Exception {
        mockMvc.perform(put("/clients/{id}", UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "AGENT")
    void updateClient_Authenticated_Returns200() throws Exception {
        when(clientService.updateClient(any(), any())).thenReturn(buildClientResponse());

        String body = """
                {
                  "firstName": "Ali",
                  "city": "Tunis"
                }
                """;

        mockMvc.perform(put("/clients/{id}", UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    // ── Delete client ─────────────────────────────────────────────────────────

    @Test
    void deleteClient_Unauthenticated_Returns401() throws Exception {
        mockMvc.perform(delete("/clients/{id}", UUID.randomUUID()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    void deleteClient_Authenticated_Returns200() throws Exception {
        doNothing().when(clientService).deleteClient(any());

        mockMvc.perform(delete("/clients/{id}", UUID.randomUUID()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    // ── Search clients ────────────────────────────────────────────────────────

    @Test
    void searchClients_Unauthenticated_Returns401() throws Exception {
        mockMvc.perform(get("/clients/search").param("city", "Tunis"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "AGENT")
    void searchClients_Authenticated_Returns200() throws Exception {
        PageResponse<ClientResponse> page = PageResponse.from(
                new PageImpl<>(List.of(buildClientResponse())));
        when(clientService.searchClients(any(), any())).thenReturn(page);

        mockMvc.perform(get("/clients/search").param("city", "Tunis"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.content[0].cin").value("12345678"));
    }

    // ── KYC validation ────────────────────────────────────────────────────────

    @Test
    void validateKyc_Unauthenticated_Returns401() throws Exception {
        mockMvc.perform(post("/clients/{id}/kyc/validate", UUID.randomUUID()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    void validateKyc_Authenticated_Returns200() throws Exception {
        ClientResponse response = buildClientResponse();
        response.setKycStatus(KycStatus.VERIFIED);
        when(clientService.validateKyc(any())).thenReturn(response);

        mockMvc.perform(post("/clients/{id}/kyc/validate", UUID.randomUUID()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.kycStatus").value("VERIFIED"));
    }

    // ── AML screening ─────────────────────────────────────────────────────────

    @Test
    void performAmlScreening_Unauthenticated_Returns401() throws Exception {
        mockMvc.perform(post("/clients/{id}/aml/screening", UUID.randomUUID()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    void performAmlScreening_Authenticated_Returns200() throws Exception {
        ClientResponse response = buildClientResponse();
        response.setAmlRiskLevel(AmlRiskLevel.LOW);
        when(clientService.performAmlScreening(any())).thenReturn(response);

        mockMvc.perform(post("/clients/{id}/aml/screening", UUID.randomUUID()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.amlRiskLevel").value("LOW"));
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private String validClientRequestJson() {
        return """
                {
                  "clientType": "INDIVIDUAL",
                  "firstName": "Ali",
                  "lastName": "Ben Ali",
                  "email": "ali.ben.ali@example.tn",
                  "phone": "+21698765432",
                  "cin": "12345678",
                  "city": "Tunis",
                  "governorate": "Tunis"
                }
                """;
    }

    private ClientResponse buildClientResponse() {
        return ClientResponse.builder()
                .id(UUID.randomUUID())
                .clientType(ClientType.INDIVIDUAL)
                .firstName("Ali")
                .lastName("Ben Ali")
                .fullName("Ali Ben Ali")
                .email("ali.ben.ali@example.tn")
                .phone("+21698765432")
                .cin("12345678")
                .kycStatus(KycStatus.PENDING)
                .amlRiskLevel(AmlRiskLevel.LOW)
                .city("Tunis")
                .governorate("Tunis")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }
}
