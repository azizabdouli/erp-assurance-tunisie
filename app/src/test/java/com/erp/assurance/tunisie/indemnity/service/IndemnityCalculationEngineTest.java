package com.erp.assurance.tunisie.indemnity.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.assertj.core.api.Assertions.assertThat;

class IndemnityCalculationEngineTest {
    private IndemnityCalculationEngine engine;

    @BeforeEach
    void setUp() {
        engine = new IndemnityCalculationEngine();
    }

    @Test
    void calculateNetIndemnity_NoDeductible() {
        BigDecimal net = engine.calculateNetIndemnity(new BigDecimal("10000.000"), BigDecimal.ZERO, new BigDecimal("100.00"));
        assertThat(net).isEqualByComparingTo(new BigDecimal("10000.000"));
    }

    @Test
    void calculateNetIndemnity_WithDeductible() {
        BigDecimal net = engine.calculateNetIndemnity(new BigDecimal("10000.000"), new BigDecimal("1000.000"), new BigDecimal("100.00"));
        assertThat(net).isEqualByComparingTo(new BigDecimal("9000.000"));
    }

    @Test
    void calculateNetIndemnity_WithCoInsurance() {
        BigDecimal net = engine.calculateNetIndemnity(new BigDecimal("10000.000"), BigDecimal.ZERO, new BigDecimal("70.00"));
        assertThat(net).isEqualByComparingTo(new BigDecimal("7000.000"));
    }

    @Test
    void calculateNetIndemnity_DeductibleExceedsAmount() {
        BigDecimal net = engine.calculateNetIndemnity(new BigDecimal("500.000"), new BigDecimal("1000.000"), new BigDecimal("100.00"));
        assertThat(net).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    void calculateNetIndemnity_NullInputs() {
        BigDecimal net = engine.calculateNetIndemnity(new BigDecimal("10000.000"), null, null);
        assertThat(net).isEqualByComparingTo(new BigDecimal("10000.000"));
    }
}
