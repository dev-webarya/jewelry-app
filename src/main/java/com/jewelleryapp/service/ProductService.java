package com.jewelleryapp.service;

import com.jewelleryapp.dto.request.ProductRequestDto;
import com.jewelleryapp.dto.response.ProductResponseDto;
import com.jewelleryapp.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import java.util.UUID;

public interface ProductService {

    ProductResponseDto createProduct(ProductRequestDto requestDto);

    Page<ProductResponseDto> getAllProducts(Specification<Product> spec, Pageable pageable);

    ProductResponseDto getProductById(UUID id);

    ProductResponseDto updateProduct(UUID id, ProductRequestDto requestDto);

    void deleteProduct(UUID id);
}