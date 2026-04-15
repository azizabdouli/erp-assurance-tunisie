package com.erp.assurance.tunisie.underwriting.service;

import com.erp.assurance.tunisie.shared.dto.PageResponse;
import com.erp.assurance.tunisie.shared.enums.ProductType;
import com.erp.assurance.tunisie.shared.exception.BusinessException;
import com.erp.assurance.tunisie.shared.exception.ResourceNotFoundException;
import com.erp.assurance.tunisie.underwriting.dto.ProductRequest;
import com.erp.assurance.tunisie.underwriting.dto.ProductResponse;
import com.erp.assurance.tunisie.underwriting.entity.Product;
import com.erp.assurance.tunisie.underwriting.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ProductService {

    private final ProductRepository productRepository;

    public ProductResponse createProduct(ProductRequest request) {
        if (productRepository.existsByCode(request.getCode())) {
            throw new BusinessException("UW_002", "Product with code " + request.getCode() + " already exists");
        }

        Product product = Product.builder()
                .code(request.getCode())
                .name(request.getName())
                .description(request.getDescription())
                .productType(request.getProductType())
                .build();

        Product saved = productRepository.save(product);
        log.info("Created product: {} ({})", saved.getName(), saved.getCode());
        return mapToResponse(saved);
    }

    @Transactional(readOnly = true)
    public ProductResponse getProductById(UUID id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));
        return mapToResponse(product);
    }

    @Transactional(readOnly = true)
    public PageResponse<ProductResponse> getActiveProducts(Pageable pageable) {
        Page<Product> page = productRepository.findByActiveTrue(pageable);
        return PageResponse.from(page.map(this::mapToResponse));
    }

    @Transactional(readOnly = true)
    public PageResponse<ProductResponse> searchProducts(String name, ProductType type, Pageable pageable) {
        Page<Product> page = productRepository.searchProducts(name, type, pageable);
        return PageResponse.from(page.map(this::mapToResponse));
    }

    private ProductResponse mapToResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .code(product.getCode())
                .name(product.getName())
                .description(product.getDescription())
                .productType(product.getProductType())
                .active(product.isActive())
                .createdAt(product.getCreatedAt())
                .build();
    }
}
