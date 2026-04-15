package com.erp.assurance.tunisie.underwriting.strategy;

import com.erp.assurance.tunisie.shared.enums.ProductType;

import java.math.BigDecimal;
import java.util.Map;

public interface PremiumCalculationStrategy {
    BigDecimal calculateBasePremium(Map<String, Object> riskFactors);
    ProductType getProductType();
}
