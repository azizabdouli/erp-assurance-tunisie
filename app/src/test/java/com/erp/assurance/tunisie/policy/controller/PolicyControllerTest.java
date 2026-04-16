package com.erp.assurance.tunisie.policy.controller;

import com.erp.assurance.tunisie.config.SecurityConfig;
import com.erp.assurance.tunisie.policy.dto.PolicyResponse;
import com.erp.assurance.tunisie.policy.service.EndorsementService;
import com.erp.assurance.tunisie.policy.service.PolicyService;
import com.erp.assurance.tunisie.shared.enums.PolicyStatus;
import com.erp.assurance.tunisie.shared.security.JwtTokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PolicyController.class)
@Import(SecurityConfig.class)
class PolicyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PolicyService policyService;

    @MockBean
    private EndorsementService endorsementService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    private JpaMetamodelMappingContext jpaMetamodelMappingContext;

    // ── Create policy ─────────────────────────────────────────────────────────

    @Test
    void createPolicy_Unauthenticated_Returns401() throws Exception {
        mockMvc.perform(post("/policies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validPolicyRequestJson()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "AGENT")
    void createPolicy_Authenticated_Returns201() throws Exception {
        when(policyService.createPolicy(any())).thenReturn(buildPolicyResponse(PolicyStatus.DRAFT));

        mockMvc.perform(post("/policies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validPolicyRequestJson()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.policyNumber").value("POL-001"))
                .andExpect(jsonPath("$.data.status").value("DRAFT"));
    }

    @Test
    @WithMockUser(roles = "AGENT")
    void createPolicy_MissingClientId_Returns400() throws Exception {
        String body = """
                {
                  "productId": "%s",
                  "effectiveDate": "2025-01-01",
                  "expiryDate": "2026-01-01"
                }
                """.formatted(UUID.randomUUID());

        mockMvc.perform(post("/policies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    // ── Get policy ────────────────────────────────────────────────────────────

    @Test
    void getPolicy_Unauthenticated_Returns401() throws Exception {
        mockMvc.perform(get("/policies/{id}", UUID.randomUUID()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "AGENT")
    void getPolicy_Authenticated_Returns200() throws Exception {
        when(policyService.getPolicy(any())).thenReturn(buildPolicyResponse(PolicyStatus.ACTIVE));

        mockMvc.perform(get("/policies/{id}", UUID.randomUUID()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.policyNumber").value("POL-001"))
                .andExpect(jsonPath("$.data.status").value("ACTIVE"));
    }

    // ── Activate policy ───────────────────────────────────────────────────────

    @Test
    void activatePolicy_Unauthenticated_Returns401() throws Exception {
        mockMvc.perform(post("/policies/{id}/activate", UUID.randomUUID()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "AGENT")
    void activatePolicy_Authenticated_Returns200() throws Exception {
        when(policyService.activatePolicy(any())).thenReturn(buildPolicyResponse(PolicyStatus.ACTIVE));

        mockMvc.perform(post("/policies/{id}/activate", UUID.randomUUID()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.status").value("ACTIVE"));
    }

    // ── Cancel policy ─────────────────────────────────────────────────────────

    @Test
    void cancelPolicy_Unauthenticated_Returns401() throws Exception {
        mockMvc.perform(post("/policies/{id}/cancel", UUID.randomUUID()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "AGENT")
    void cancelPolicy_Authenticated_Returns200() throws Exception {
        when(policyService.cancelPolicy(any())).thenReturn(buildPolicyResponse(PolicyStatus.CANCELLED));

        mockMvc.perform(post("/policies/{id}/cancel", UUID.randomUUID()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("CANCELLED"));
    }

    // ── Endorsement ───────────────────────────────────────────────────────────

    @Test
    void createEndorsement_Unauthenticated_Returns401() throws Exception {
        mockMvc.perform(post("/policies/{id}/endorsements", UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "AGENT")
    void createEndorsement_Authenticated_Returns201() throws Exception {
        when(endorsementService.createEndorsement(any(), any())).thenReturn(null);

        String body = """
                {
                  "endorsementType": "MODIFICATION",
                  "effectiveDate": "2025-06-01",
                  "description": "Annual premium revision"
                }
                """;

        mockMvc.perform(post("/policies/{id}/endorsements", UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true));
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private String validPolicyRequestJson() {
        return """
                {
                  "clientId": "%s",
                  "productId": "%s",
                  "effectiveDate": "2025-01-01",
                  "expiryDate": "2026-01-01",
                  "sumInsured": 50000.00
                }
                """.formatted(UUID.randomUUID(), UUID.randomUUID());
    }

    private PolicyResponse buildPolicyResponse(PolicyStatus status) {
        return PolicyResponse.builder()
                .id(UUID.randomUUID())
                .policyNumber("POL-001")
                .clientId(UUID.randomUUID())
                .clientName("John Doe")
                .productId(UUID.randomUUID())
                .productName("Auto Insurance")
                .status(status)
                .effectiveDate(LocalDate.of(2025, 1, 1))
                .expiryDate(LocalDate.of(2026, 1, 1))
                .annualPremium(BigDecimal.valueOf(1200))
                .sumInsured(BigDecimal.valueOf(50000))
                .renewalCount(0)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
