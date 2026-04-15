package com.erp.assurance.tunisie.indemnity.service;

import com.erp.assurance.tunisie.shared.util.CurrencyUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class IndemnityCalculationEngine {

    public BigDecimal calculateNetIndemnity(BigDecimal grossAmount, BigDecimal deductible, BigDecimal coInsuranceShare) {
        BigDecimal afterDeductible = grossAmount.subtract(deductible != null ? deductible : BigDecimal.ZERO);
        if (afterDeductible.compareTo(BigDecimal.ZERO) < 0) {
            return BigDecimal.ZERO;
        }

        BigDecimal sharePercentage = coInsuranceShare != null ? coInsuranceShare : new BigDecimal("100.00");
        BigDecimal netAmount = afterDeductible.multiply(sharePercentage).divide(new BigDecimal("100"), 3, RoundingMode.HALF_UP);

        return CurrencyUtils.round(netAmount);
    }
}
