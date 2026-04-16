package com.erp.assurance.tunisie.shared.util;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class ValidationUtilsTest {
    @Test
    void isValidCIN_ValidCIN() {
        assertThat(ValidationUtils.isValidCIN("12345678")).isTrue();
    }

    @Test
    void isValidCIN_InvalidCIN() {
        assertThat(ValidationUtils.isValidCIN("1234567")).isFalse();
        assertThat(ValidationUtils.isValidCIN("123456789")).isFalse();
        assertThat(ValidationUtils.isValidCIN(null)).isFalse();
    }

    @Test
    void isValidEmail_ValidEmail() {
        assertThat(ValidationUtils.isValidEmail("test@example.com")).isTrue();
    }

    @Test
    void isValidEmail_InvalidEmail() {
        assertThat(ValidationUtils.isValidEmail("invalid")).isFalse();
        assertThat(ValidationUtils.isValidEmail(null)).isFalse();
    }

    @Test
    void isValidTunisianPhone_ValidPhone() {
        assertThat(ValidationUtils.isValidTunisianPhone("+21698765432")).isTrue();
        assertThat(ValidationUtils.isValidTunisianPhone("98765432")).isTrue();
    }

    @Test
    void isValidTunisianPhone_InvalidPhone() {
        assertThat(ValidationUtils.isValidTunisianPhone("123")).isFalse();
        assertThat(ValidationUtils.isValidTunisianPhone(null)).isFalse();
    }
}
