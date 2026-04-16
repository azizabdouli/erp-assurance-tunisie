package com.erp.assurance.tunisie.underwriting.dto;

import com.erp.assurance.tunisie.shared.enums.ProductType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class ProductRequest {
    @NotBlank(message = "Product code is required")
    private String code;
    @NotBlank(message = "Product name is required")
    private String name;
    private String description;
    @NotNull(message = "Product type is required")
    private ProductType productType;
}
