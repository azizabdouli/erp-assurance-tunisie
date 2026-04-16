package com.erp.assurance.tunisie.shared.util;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.assertj.core.api.Assertions.assertThat;

class CurrencyUtilsTest {
    @Test
    void round_ThreeDecimalPlaces() {
        BigDecimal result = CurrencyUtils.round(new BigDecimal("123.45678"));
        assertThat(result).isEqualByComparingTo(new BigDecimal("123.457"));
    }

    @Test
    void calculateTVA_19Percent() {
        BigDecimal tva = CurrencyUtils.calculateTVA(new BigDecimal("1000.000"));
        assertThat(tva).isEqualByComparingTo(new BigDecimal("190.000"));
    }

    @Test
    void addTVA_IncludesTax() {
        BigDecimal total = CurrencyUtils.addTVA(new BigDecimal("1000.000"));
        assertThat(total).isEqualByComparingTo(new BigDecimal("1190.000"));
    }
}
