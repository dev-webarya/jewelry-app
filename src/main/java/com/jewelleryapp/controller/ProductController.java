package com.jewelleryapp.controller;

import com.jewelleryapp.dto.request.ProductRequestDto;
import com.jewelleryapp.dto.response.ProductResponseDto;
import com.jewelleryapp.entity.Product;
import com.jewelleryapp.service.ProductService;
import com.jewelleryapp.specification.ProductSpecification;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductResponseDto> createProduct(@Valid @RequestBody ProductRequestDto requestDto) {
        ProductResponseDto createdDto = productService.createProduct(requestDto);
        return new ResponseEntity<>(createdDto, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> getProductById(@PathVariable UUID id) {
        ProductResponseDto dto = productService.getProductById(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    public ResponseEntity<Page<ProductResponseDto>> getAllProducts(
            // Search Params
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String sku,
            @RequestParam(required = false) Boolean isActive,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            // Relationship Params
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) Integer collectionId,
            @RequestParam(required = false) Integer attributeValueId,
            Pageable pageable) {

        // Start with fetch-all specification to avoid N+1
        Specification<Product> spec = Specification.where(ProductSpecification.fetchAllRelations())
                .and(ProductSpecification.hasName(name))
                .and(ProductSpecification.hasSku(sku))
                .and(ProductSpecification.isActive(isActive))
                .and(ProductSpecification.priceBetween(minPrice, maxPrice))
                .and(ProductSpecification.inCategory(categoryId))
                .and(ProductSpecification.inCollection(collectionId))
                .and(ProductSpecification.hasAttribute(attributeValueId));

        Page<ProductResponseDto> dtos = productService.getAllProducts(spec, pageable);
        return ResponseEntity.ok(dtos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDto> updateProduct(@PathVariable UUID id, @Valid @RequestBody ProductRequestDto requestDto) {
        ProductResponseDto updatedDto = productService.updateProduct(id, requestDto);
        return ResponseEntity.ok(updatedDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable UUID id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}