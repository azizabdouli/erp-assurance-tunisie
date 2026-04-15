package com.erp.assurance.tunisie.underwriting.service;

import com.erp.assurance.tunisie.shared.enums.ProductType;
import com.erp.assurance.tunisie.underwriting.strategy.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class PremiumCalculationEngineTest {
    private PremiumCalculationEngine engine;

    @BeforeEach
    void setUp() {
        engine = new PremiumCalculationEngine(Arrays.asList(
                new AutoPremiumStrategy(), new HealthPremiumStrategy(),
                new PropertyPremiumStrategy(), new LiabilityPremiumStrategy()));
    }

    @Test
    void calculateBasePremium_Auto() {
        BigDecimal premium = engine.calculateBasePremium(ProductType.AUTO, new HashMap<>());
        assertThat(premium).isGreaterThan(BigDecimal.ZERO);
    }

    @Test
    void calculateBasePremium_Auto_YoungDriver() {
        Map<String, Object> factors = new HashMap<>();
        factors.put("driverAge", "22");
        BigDecimal premium = engine.calculateBasePremium(ProductType.AUTO, factors);
        BigDecimal base = engine.calculateBasePremium(ProductType.AUTO, new HashMap<>());
        assertThat(premium).isGreaterThan(base);
    }

    @Test
    void calculateBasePremium_Health() {
        BigDecimal premium = engine.calculateBasePremium(ProductType.HEALTH, new HashMap<>());
        assertThat(premium).isGreaterThan(BigDecimal.ZERO);
    }

    @Test
    void calculateBasePremium_Property() {
        BigDecimal premium = engine.calculateBasePremium(ProductType.PROPERTY, new HashMap<>());
        assertThat(premium).isGreaterThan(BigDecimal.ZERO);
    }

    @Test
    void calculateBasePremium_Liability() {
        BigDecimal premium = engine.calculateBasePremium(ProductType.LIABILITY, new HashMap<>());
        assertThat(premium).isGreaterThan(BigDecimal.ZERO);
    }

    @Test
    void applyDiscount_ReducesPremium() {
        BigDecimal result = engine.applyDiscount(new BigDecimal("1000.000"), new BigDecimal("10"));
        assertThat(result).isEqualByComparingTo(new BigDecimal("900.000"));
    }

    @Test
    void applyLoading_IncreasesPremium() {
        BigDecimal result = engine.applyLoading(new BigDecimal("1000.000"), new BigDecimal("15"));
        assertThat(result).isEqualByComparingTo(new BigDecimal("1150.000"));
    }

    @Test
    void calculateTotalPremium_IncludesTVA() {
        BigDecimal total = engine.calculateTotalPremium(new BigDecimal("1000.000"));
        assertThat(total).isEqualByComparingTo(new BigDecimal("1190.000"));
    }
}
