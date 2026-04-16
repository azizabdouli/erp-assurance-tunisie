package com.erp.assurance.tunisie.underwriting.strategy;

import com.erp.assurance.tunisie.shared.enums.ProductType;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

@Component
public class LiabilityPremiumStrategy implements PremiumCalculationStrategy {

    @Override
    public BigDecimal calculateBasePremium(Map<String, Object> riskFactors) {
        BigDecimal premium = new BigDecimal("400.000");

        Object revenue = riskFactors.get("annualRevenue");
        if (revenue != null) {
            BigDecimal rev = new BigDecimal(revenue.toString());
            premium = premium.add(rev.multiply(new BigDecimal("0.001")));
        }

        Object employeeCount = riskFactors.get("employeeCount");
        if (employeeCount != null) {
            int count = Integer.parseInt(employeeCount.toString());
            premium = premium.add(new BigDecimal(count).multiply(new BigDecimal("10.000")));
        }

        return premium.setScale(3, RoundingMode.HALF_UP);
    }

    @Override
    public ProductType getProductType() {
        return ProductType.LIABILITY;
    }
}
