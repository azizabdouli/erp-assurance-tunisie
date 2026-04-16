package com.erp.assurance.tunisie.underwriting.strategy;

import com.erp.assurance.tunisie.shared.enums.ProductType;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

@Component
public class PropertyPremiumStrategy implements PremiumCalculationStrategy {

    @Override
    public BigDecimal calculateBasePremium(Map<String, Object> riskFactors) {
        BigDecimal premium = new BigDecimal("200.000");

        Object propertyValue = riskFactors.get("propertyValue");
        if (propertyValue != null) {
            BigDecimal value = new BigDecimal(propertyValue.toString());
            premium = premium.add(value.multiply(new BigDecimal("0.002")));
        }

        Object propertyType = riskFactors.get("propertyType");
        if ("COMMERCIAL".equals(propertyType)) {
            premium = premium.multiply(new BigDecimal("1.30"));
        }

        return premium.setScale(3, RoundingMode.HALF_UP);
    }

    @Override
    public ProductType getProductType() {
        return ProductType.PROPERTY;
    }
}
