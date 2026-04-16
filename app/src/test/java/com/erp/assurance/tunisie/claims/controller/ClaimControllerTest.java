package com.erp.assurance.tunisie.claims.controller;

import com.erp.assurance.tunisie.claims.dto.ClaimResponse;
import com.erp.assurance.tunisie.claims.service.ClaimService;
import com.erp.assurance.tunisie.claims.service.ExpertAssignmentService;
import com.erp.assurance.tunisie.config.SecurityConfig;
import com.erp.assurance.tunisie.shared.enums.ClaimStatus;
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

@WebMvcTest(ClaimController.class)
@Import(SecurityConfig.class)
class ClaimControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ClaimService claimService;

    @MockBean
    private ExpertAssignmentService expertAssignmentService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    private JpaMetamodelMappingContext jpaMetamodelMappingContext;

    // ── Declare claim ─────────────────────────────────────────────────────────

    @Test
    void declareClaim_Unauthenticated_Returns401() throws Exception {
        mockMvc.perform(post("/claims")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validClaimRequestJson()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "AGENT")
    void declareClaim_Authenticated_Returns201() throws Exception {
        when(claimService.declareClaim(any())).thenReturn(buildClaimResponse(ClaimStatus.DECLARED));

        mockMvc.perform(post("/claims")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validClaimRequestJson()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.claimNumber").value("CLM-001"))
                .andExpect(jsonPath("$.data.status").value("DECLARED"));
    }

    @Test
    @WithMockUser(roles = "AGENT")
    void declareClaim_MissingPolicyId_Returns400() throws Exception {
        String body = """
                {
                  "incidentDate": "2025-03-15",
                  "incidentDescription": "Vehicle collision"
                }
                """;

        mockMvc.perform(post("/claims")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "AGENT")
    void declareClaim_MissingDescription_Returns400() throws Exception {
        String body = """
                {
                  "policyId": "%s",
                  "incidentDate": "2025-03-15"
                }
                """.formatted(UUID.randomUUID());

        mockMvc.perform(post("/claims")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    // ── Get claim ─────────────────────────────────────────────────────────────

    @Test
    void getClaim_Unauthenticated_Returns401() throws Exception {
        mockMvc.perform(get("/claims/{id}", UUID.randomUUID()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "AGENT")
    void getClaim_Authenticated_Returns200() throws Exception {
        when(claimService.getClaim(any())).thenReturn(buildClaimResponse(ClaimStatus.UNDER_INVESTIGATION));

        mockMvc.perform(get("/claims/{id}", UUID.randomUUID()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.claimNumber").value("CLM-001"))
                .andExpect(jsonPath("$.data.status").value("UNDER_INVESTIGATION"));
    }

    // ── Update claim status ───────────────────────────────────────────────────

    @Test
    void updateClaimStatus_Unauthenticated_Returns401() throws Exception {
        mockMvc.perform(patch("/claims/{id}/status", UUID.randomUUID())
                        .param("status", "APPROVED"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    void updateClaimStatus_Authenticated_Returns200() throws Exception {
        when(claimService.updateClaimStatus(any(), any(), any())).thenReturn(buildClaimResponse(ClaimStatus.APPROVED));

        mockMvc.perform(patch("/claims/{id}/status", UUID.randomUUID())
                        .param("status", "APPROVED")
                        .param("comments", "All documents verified"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.status").value("APPROVED"));
    }

    // ── Assign expert ─────────────────────────────────────────────────────────

    @Test
    void assignExpert_Unauthenticated_Returns401() throws Exception {
        mockMvc.perform(post("/claims/{id}/experts", UUID.randomUUID())
                        .param("expertName", "Dr. Smith"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    void assignExpert_Authenticated_Returns201() throws Exception {
        when(expertAssignmentService.assignExpert(any(), any(), any())).thenReturn(null);

        mockMvc.perform(post("/claims/{id}/experts", UUID.randomUUID())
                        .param("expertName", "Dr. Smith")
                        .param("speciality", "Auto"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true));
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private String validClaimRequestJson() {
        return """
                {
                  "policyId": "%s",
                  "incidentDate": "2025-03-15",
                  "incidentDescription": "Vehicle collision at intersection",
                  "incidentLocation": "Tunis",
                  "estimatedAmount": 5000.00
                }
                """.formatted(UUID.randomUUID());
    }

    private ClaimResponse buildClaimResponse(ClaimStatus status) {
        return ClaimResponse.builder()
                .id(UUID.randomUUID())
                .claimNumber("CLM-001")
                .policyId(UUID.randomUUID())
                .policyNumber("POL-001")
                .status(status)
                .incidentDate(LocalDate.of(2025, 3, 15))
                .declarationDate(LocalDateTime.now())
                .incidentDescription("Vehicle collision at intersection")
                .incidentLocation("Tunis")
                .estimatedAmount(BigDecimal.valueOf(5000))
                .fraudFlagged(false)
                .fraudScore(10)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
