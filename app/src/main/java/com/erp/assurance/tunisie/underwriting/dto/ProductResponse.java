package com.erp.assurance.tunisie.underwriting.dto;

import com.erp.assurance.tunisie.shared.enums.ProductType;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class ProductResponse {
    private UUID id;
    private String code;
    private String name;
    private String description;
    private ProductType productType;
    private boolean active;
    private LocalDateTime createdAt;
}
