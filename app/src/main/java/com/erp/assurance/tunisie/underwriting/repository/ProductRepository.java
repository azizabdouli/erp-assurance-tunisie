package com.erp.assurance.tunisie.underwriting.repository;

import com.erp.assurance.tunisie.shared.enums.ProductType;
import com.erp.assurance.tunisie.underwriting.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
    Optional<Product> findByCode(String code);

    Page<Product> findByActiveTrue(Pageable pageable);

    Page<Product> findByProductTypeAndActiveTrue(ProductType productType, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.active = true " +
            "AND (:name IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))) " +
            "AND (:type IS NULL OR p.productType = :type)")
    Page<Product> searchProducts(@Param("name") String name, @Param("type") ProductType type, Pageable pageable);

    boolean existsByCode(String code);
}
