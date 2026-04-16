package com.erp.assurance.tunisie.underwriting.service;

import com.erp.assurance.tunisie.shared.enums.ProductType;
import com.erp.assurance.tunisie.shared.exception.BusinessException;
import com.erp.assurance.tunisie.shared.util.CurrencyUtils;
import com.erp.assurance.tunisie.underwriting.strategy.PremiumCalculationStrategy;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class PremiumCalculationEngine {

    private final Map<ProductType, PremiumCalculationStrategy> strategies;

    public PremiumCalculationEngine(List<PremiumCalculationStrategy> strategyList) {
        this.strategies = strategyList.stream()
                .collect(Collectors.toMap(PremiumCalculationStrategy::getProductType, Function.identity()));
    }

    public BigDecimal calculateBasePremium(ProductType productType, Map<String, Object> riskFactors) {
        PremiumCalculationStrategy strategy = strategies.get(productType);
        if (strategy == null) {
            throw new BusinessException("UW_001", "No premium calculation strategy for product type: " + productType);
        }
        return strategy.calculateBasePremium(riskFactors);
    }

    public BigDecimal applyDiscount(BigDecimal premium, BigDecimal discountPercentage) {
        if (discountPercentage == null || discountPercentage.compareTo(BigDecimal.ZERO) <= 0) {
            return premium;
        }
        BigDecimal discount = premium.multiply(discountPercentage).divide(new BigDecimal("100"), 3, RoundingMode.HALF_UP);
        return CurrencyUtils.round(premium.subtract(discount));
    }

    public BigDecimal applyLoading(BigDecimal premium, BigDecimal loadingPercentage) {
        if (loadingPercentage == null || loadingPercentage.compareTo(BigDecimal.ZERO) <= 0) {
            return premium;
        }
        BigDecimal loading = premium.multiply(loadingPercentage).divide(new BigDecimal("100"), 3, RoundingMode.HALF_UP);
        return CurrencyUtils.round(premium.add(loading));
    }

    public BigDecimal calculateTax(BigDecimal netPremium) {
        return CurrencyUtils.calculateTVA(netPremium);
    }

    public BigDecimal calculateTotalPremium(BigDecimal netPremium) {
        return CurrencyUtils.addTVA(netPremium);
    }
}
