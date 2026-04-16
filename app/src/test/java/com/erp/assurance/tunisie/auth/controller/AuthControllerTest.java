package com.erp.assurance.tunisie.auth.controller;

import com.erp.assurance.tunisie.auth.dto.AuthResponse;
import com.erp.assurance.tunisie.auth.dto.UserResponse;
import com.erp.assurance.tunisie.auth.service.AuthService;
import com.erp.assurance.tunisie.config.SecurityConfig;
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

import java.util.Set;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@Import(SecurityConfig.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    private JpaMetamodelMappingContext jpaMetamodelMappingContext;

    // ── Login ────────────────────────────────────────────────────────────────

    @Test
    void login_ValidCredentials_Returns200WithToken() throws Exception {
        AuthResponse authResponse = AuthResponse.builder()
                .accessToken("test.jwt.token")
                .tokenType("Bearer")
                .expiresIn(86400L)
                .username("admin")
                .email("admin@erp.tn")
                .roles(Set.of("ADMIN"))
                .build();

        when(authService.login(any())).thenReturn(authResponse);

        String body = """
                {
                  "username": "admin",
                  "password": "Admin@2024"
                }
                """;

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.accessToken").value("test.jwt.token"))
                .andExpect(jsonPath("$.data.tokenType").value("Bearer"))
                .andExpect(jsonPath("$.data.username").value("admin"));
    }

    @Test
    void login_MissingUsername_Returns400() throws Exception {
        String body = """
                {
                  "password": "Admin@2024"
                }
                """;

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void login_MissingPassword_Returns400() throws Exception {
        String body = """
                {
                  "username": "admin"
                }
                """;

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void login_EmptyBody_Returns400() throws Exception {
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    // ── Register ─────────────────────────────────────────────────────────────

    @Test
    @WithMockUser(roles = "ADMIN")
    void register_ValidRequest_AsAdmin_Returns201() throws Exception {
        UserResponse userResponse = UserResponse.builder()
                .id(UUID.randomUUID())
                .username("newuser")
                .email("newuser@erp.tn")
                .firstName("New")
                .lastName("User")
                .active(true)
                .roles(Set.of("AGENT"))
                .build();

        when(authService.register(any())).thenReturn(userResponse);

        String body = """
                {
                  "username": "newuser",
                  "email": "newuser@erp.tn",
                  "password": "Password123!",
                  "firstName": "New",
                  "lastName": "User"
                }
                """;

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.username").value("newuser"))
                .andExpect(jsonPath("$.data.email").value("newuser@erp.tn"));
    }

    @Test
    void register_Unauthenticated_Returns401() throws Exception {
        String body = """
                {
                  "username": "newuser",
                  "email": "newuser@erp.tn",
                  "password": "Password123!"
                }
                """;

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "AGENT")
    void register_AsNonAdmin_Returns403() throws Exception {
        String body = """
                {
                  "username": "newuser",
                  "email": "newuser@erp.tn",
                  "password": "Password123!"
                }
                """;

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void register_InvalidEmail_Returns400() throws Exception {
        String body = """
                {
                  "username": "newuser",
                  "email": "not-an-email",
                  "password": "Password123!"
                }
                """;

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void register_ShortPassword_Returns400() throws Exception {
        String body = """
                {
                  "username": "newuser",
                  "email": "newuser@erp.tn",
                  "password": "short"
                }
                """;

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    // ── Get current user ──────────────────────────────────────────────────────

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void getCurrentUser_Authenticated_Returns200() throws Exception {
        UserResponse userResponse = UserResponse.builder()
                .id(UUID.randomUUID())
                .username("admin")
                .email("admin@erp.tn")
                .active(true)
                .roles(Set.of("ADMIN"))
                .build();

        when(authService.getCurrentUser("admin")).thenReturn(userResponse);

        mockMvc.perform(get("/auth/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.username").value("admin"));
    }

    @Test
    void getCurrentUser_Unauthenticated_Returns401() throws Exception {
        mockMvc.perform(get("/auth/me"))
                .andExpect(status().isUnauthorized());
    }
}
