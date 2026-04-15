package com.erp.assurance.tunisie.underwriting.strategy;

import com.erp.assurance.tunisie.shared.enums.ProductType;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

@Component
public class HealthPremiumStrategy implements PremiumCalculationStrategy {

    private static final BigDecimal BASE_RATE = new BigDecimal("300.000");

    @Override
    public BigDecimal calculateBasePremium(Map<String, Object> riskFactors) {
        BigDecimal premium = BASE_RATE;

        Object age = riskFactors.get("age");
        if (age != null) {
            int ageValue = Integer.parseInt(age.toString());
            if (ageValue > 50) {
                premium = premium.multiply(new BigDecimal("1.50"));
            } else if (ageValue > 35) {
                premium = premium.multiply(new BigDecimal("1.20"));
            }
        }

        Object familySize = riskFactors.get("familySize");
        if (familySize != null) {
            int members = Integer.parseInt(familySize.toString());
            premium = premium.add(new BigDecimal(members - 1).multiply(new BigDecimal("150.000")));
        }

        return premium.setScale(3, RoundingMode.HALF_UP);
    }

    @Override
    public ProductType getProductType() {
        return ProductType.HEALTH;
    }
}
