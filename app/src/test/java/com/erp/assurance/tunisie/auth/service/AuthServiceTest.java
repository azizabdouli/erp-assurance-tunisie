package com.erp.assurance.tunisie.auth.service;

import com.erp.assurance.tunisie.auth.dto.AuthResponse;
import com.erp.assurance.tunisie.auth.dto.LoginRequest;
import com.erp.assurance.tunisie.auth.dto.RegisterRequest;
import com.erp.assurance.tunisie.auth.dto.UserResponse;
import com.erp.assurance.tunisie.auth.entity.Role;
import com.erp.assurance.tunisie.auth.entity.User;
import com.erp.assurance.tunisie.auth.repository.RoleRepository;
import com.erp.assurance.tunisie.auth.repository.UserRepository;
import com.erp.assurance.tunisie.shared.exception.BusinessException;
import com.erp.assurance.tunisie.shared.security.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private RoleRepository roleRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private JwtTokenProvider jwtTokenProvider;
    @Mock private AuthenticationManager authenticationManager;
    @InjectMocks private AuthService authService;

    private User adminUser;
    private Role adminRole;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(authService, "jwtExpirationMs", 86400000L);

        adminRole = Role.builder().name("ADMIN").description("System administrator").build();
        adminRole.setId(UUID.randomUUID());

        adminUser = User.builder()
                .username("admin")
                .email("admin@erp.tn")
                .passwordHash("$2a$10$encodedPassword")
                .firstName("System").lastName("Admin")
                .active(true)
                .roles(Set.of(adminRole))
                .build();
        adminUser.setId(UUID.randomUUID());
    }

    @Test
    void login_Success() {
        LoginRequest request = new LoginRequest();
        request.setUsername("admin");
        request.setPassword("Admin@2024");

        Authentication auth = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(auth);
        when(jwtTokenProvider.generateToken(auth)).thenReturn("test.jwt.token");
        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(adminUser));

        AuthResponse response = authService.login(request);

        assertThat(response).isNotNull();
        assertThat(response.getAccessToken()).isEqualTo("test.jwt.token");
        assertThat(response.getUsername()).isEqualTo("admin");
        assertThat(response.getRoles()).contains("ADMIN");
        assertThat(response.getTokenType()).isEqualTo("Bearer");
    }

    @Test
    void login_UserNotFound_ThrowsException() {
        LoginRequest request = new LoginRequest();
        request.setUsername("unknown");
        request.setPassword("password");

        Authentication auth = mock(Authentication.class);
        when(authenticationManager.authenticate(any())).thenReturn(auth);
        when(userRepository.findByUsername("unknown")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(BusinessException.class);
    }

    @Test
    void register_Success() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("newuser");
        request.setEmail("newuser@erp.tn");
        request.setPassword("Password123!");
        request.setFirstName("New");
        request.setLastName("User");

        when(userRepository.existsByUsername("newuser")).thenReturn(false);
        when(userRepository.existsByEmail("newuser@erp.tn")).thenReturn(false);
        when(passwordEncoder.encode("Password123!")).thenReturn("$2a$10$encoded");
        when(roleRepository.findByName("AGENT")).thenReturn(Optional.empty());

        User savedUser = User.builder()
                .username("newuser").email("newuser@erp.tn")
                .passwordHash("$2a$10$encoded")
                .firstName("New").lastName("User")
                .active(true).build();
        savedUser.setId(UUID.randomUUID());

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        UserResponse response = authService.register(request);

        assertThat(response).isNotNull();
        assertThat(response.getUsername()).isEqualTo("newuser");
        assertThat(response.getEmail()).isEqualTo("newuser@erp.tn");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void register_DuplicateUsername_ThrowsException() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("admin");
        request.setEmail("another@erp.tn");
        request.setPassword("Password123!");

        when(userRepository.existsByUsername("admin")).thenReturn(true);

        assertThatThrownBy(() -> authService.register(request))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Username already taken");
    }

    @Test
    void register_DuplicateEmail_ThrowsException() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("newuser");
        request.setEmail("admin@erp.tn");
        request.setPassword("Password123!");

        when(userRepository.existsByUsername("newuser")).thenReturn(false);
        when(userRepository.existsByEmail("admin@erp.tn")).thenReturn(true);

        assertThatThrownBy(() -> authService.register(request))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Email already registered");
    }

    @Test
    void getCurrentUser_Success() {
        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(adminUser));

        UserResponse response = authService.getCurrentUser("admin");

        assertThat(response).isNotNull();
        assertThat(response.getUsername()).isEqualTo("admin");
        assertThat(response.getRoles()).contains("ADMIN");
    }

    @Test
    void getCurrentUser_NotFound_ThrowsException() {
        when(userRepository.findByUsername("unknown")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authService.getCurrentUser("unknown"))
                .isInstanceOf(BusinessException.class);
    }
}
