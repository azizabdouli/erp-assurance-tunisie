package com.erp.assurance.tunisie.auth.repository;

import com.erp.assurance.tunisie.auth.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private User savedUser;

    @BeforeEach
    void setUp() {
        savedUser = userRepository.save(User.builder()
                .username("testuser")
                .email("testuser@erp.tn")
                .passwordHash("$2a$10$hashedPassword")
                .firstName("Test")
                .lastName("User")
                .active(true)
                .build());
    }

    @Test
    void findByUsername_ExistingUser_ReturnsUser() {
        Optional<User> result = userRepository.findByUsername("testuser");

        assertThat(result).isPresent();
        assertThat(result.get().getUsername()).isEqualTo("testuser");
        assertThat(result.get().getEmail()).isEqualTo("testuser@erp.tn");
    }

    @Test
    void findByUsername_UnknownUser_ReturnsEmpty() {
        Optional<User> result = userRepository.findByUsername("nonexistent");

        assertThat(result).isEmpty();
    }

    @Test
    void findByEmail_ExistingEmail_ReturnsUser() {
        Optional<User> result = userRepository.findByEmail("testuser@erp.tn");

        assertThat(result).isPresent();
        assertThat(result.get().getUsername()).isEqualTo("testuser");
    }

    @Test
    void findByEmail_UnknownEmail_ReturnsEmpty() {
        Optional<User> result = userRepository.findByEmail("unknown@erp.tn");

        assertThat(result).isEmpty();
    }

    @Test
    void existsByUsername_ExistingUsername_ReturnsTrue() {
        assertThat(userRepository.existsByUsername("testuser")).isTrue();
    }

    @Test
    void existsByUsername_UnknownUsername_ReturnsFalse() {
        assertThat(userRepository.existsByUsername("nobody")).isFalse();
    }

    @Test
    void existsByEmail_ExistingEmail_ReturnsTrue() {
        assertThat(userRepository.existsByEmail("testuser@erp.tn")).isTrue();
    }

    @Test
    void existsByEmail_UnknownEmail_ReturnsFalse() {
        assertThat(userRepository.existsByEmail("nobody@erp.tn")).isFalse();
    }

    @Test
    void save_PersistsUser_AndAssignsId() {
        User user = User.builder()
                .username("another")
                .email("another@erp.tn")
                .passwordHash("$2a$10$hash")
                .active(true)
                .build();

        User persisted = userRepository.save(user);

        assertThat(persisted.getId()).isNotNull();
        assertThat(persisted.getCreatedAt()).isNotNull();
    }
}
