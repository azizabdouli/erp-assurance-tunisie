package com.erp.assurance.tunisie.underwriting.strategy;

import com.erp.assurance.tunisie.shared.enums.ProductType;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

@Component
public class AutoPremiumStrategy implements PremiumCalculationStrategy {

    private static final BigDecimal BASE_RATE = new BigDecimal("500.000");

    @Override
    public BigDecimal calculateBasePremium(Map<String, Object> riskFactors) {
        BigDecimal premium = BASE_RATE;

        Object vehicleValue = riskFactors.get("vehicleValue");
        if (vehicleValue != null) {
            BigDecimal value = new BigDecimal(vehicleValue.toString());
            premium = premium.add(value.multiply(new BigDecimal("0.035")));
        }

        Object driverAge = riskFactors.get("driverAge");
        if (driverAge != null) {
            int age = Integer.parseInt(driverAge.toString());
            if (age < 25) {
                premium = premium.multiply(new BigDecimal("1.25"));
            } else if (age > 65) {
                premium = premium.multiply(new BigDecimal("1.15"));
            }
        }

        Object claimsHistory = riskFactors.get("claimsCount");
        if (claimsHistory != null) {
            int claims = Integer.parseInt(claimsHistory.toString());
            if (claims > 0) {
                BigDecimal loading = new BigDecimal("1").add(new BigDecimal(claims).multiply(new BigDecimal("0.10")));
                premium = premium.multiply(loading);
            }
        }

        return premium.setScale(3, RoundingMode.HALF_UP);
    }

    @Override
    public ProductType getProductType() {
        return ProductType.AUTO;
    }
}
