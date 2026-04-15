package com.erp.assurance.tunisie.reporting.dto;

import com.erp.assurance.tunisie.shared.enums.ProductType;
import lombok.*;

import java.math.BigDecimal;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class ProductAnalytics {
    private ProductType productType;
    private long policyCount;
    private BigDecimal premiumVolume;
    private long claimCount;
    private BigDecimal claimAmount;
    private BigDecimal lossRatio;
}
