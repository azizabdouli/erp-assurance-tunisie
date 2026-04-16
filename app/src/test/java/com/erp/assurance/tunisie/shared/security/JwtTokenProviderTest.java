package com.erp.assurance.tunisie.shared.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class JwtTokenProviderTest {
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        jwtTokenProvider = new JwtTokenProvider(
                "test-jwt-secret-key-for-testing-purposes-must-be-at-least-256-bits-long", 3600000);
    }

    @Test
    void generateToken_ReturnsNonNullToken() {
        String token = jwtTokenProvider.generateToken("testuser");
        assertThat(token).isNotNull().isNotEmpty();
    }

    @Test
    void getUsernameFromToken_ReturnsCorrectUsername() {
        String token = jwtTokenProvider.generateToken("testuser");
        assertThat(jwtTokenProvider.getUsernameFromToken(token)).isEqualTo("testuser");
    }

    @Test
    void validateToken_ValidToken_ReturnsTrue() {
        String token = jwtTokenProvider.generateToken("testuser");
        assertThat(jwtTokenProvider.validateToken(token)).isTrue();
    }

    @Test
    void validateToken_InvalidToken_ReturnsFalse() {
        assertThat(jwtTokenProvider.validateToken("invalid.token.here")).isFalse();
    }

    @Test
    void validateToken_NullToken_ReturnsFalse() {
        assertThat(jwtTokenProvider.validateToken(null)).isFalse();
    }
}
