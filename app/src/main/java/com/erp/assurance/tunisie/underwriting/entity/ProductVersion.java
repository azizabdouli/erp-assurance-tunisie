package com.erp.assurance.tunisie.underwriting.entity;

import com.erp.assurance.tunisie.shared.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "product_versions")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ProductVersion extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "version_number", nullable = false)
    private String versionNumber;

    @Column(name = "effective_date", nullable = false)
    private LocalDate effectiveDate;

    @Column(name = "expiry_date")
    private LocalDate expiryDate;

    @Column(name = "changes_description", columnDefinition = "TEXT")
    private String changesDescription;

    @Column(name = "active")
    @Builder.Default
    private boolean active = true;
}
