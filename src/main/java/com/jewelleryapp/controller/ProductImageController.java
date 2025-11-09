package com.jewelleryapp.controller;

import com.jewelleryapp.dto.request.ProductImageRequestDto;
import com.jewelleryapp.dto.response.ProductImageResponseDto;
import com.jewelleryapp.entity.ProductImage;
import com.jewelleryapp.service.ProductImageService;
import com.jewelleryapp.specification.ProductImageSpecification;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/product-images")
@RequiredArgsConstructor
public class ProductImageController {

    private final ProductImageService productImageService;

    @PostMapping
    public ResponseEntity<ProductImageResponseDto> addImageToProduct(@Valid @RequestBody ProductImageRequestDto requestDto) {
        ProductImageResponseDto createdDto = productImageService.addImageToProduct(requestDto);
        return new ResponseEntity<>(createdDto, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductImageResponseDto> getImageById(@PathVariable UUID id) {
        ProductImageResponseDto dto = productImageService.getImageById(id);
        return ResponseEntity.ok(dto);
    }

    // It's common to get images by *product* instead of all images
    @GetMapping
    public ResponseEntity<Page<ProductImageResponseDto>> getAllImages(
            @RequestParam(required = false) UUID productId,
            @RequestParam(required = false) Boolean isPrimary,
            Pageable pageable) {

        Specification<ProductImage> spec = Specification.where(ProductImageSpecification.hasProductId(productId))
                .and(ProductImageSpecification.isPrimary(isPrimary));

        Page<ProductImageResponseDto> dtos = productImageService.getImagesForProduct(productId, pageable);
        return ResponseEntity.ok(dtos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductImageResponseDto> updateImageDetails(@PathVariable UUID id, @Valid @RequestBody ProductImageRequestDto requestDto) {
        ProductImageResponseDto updatedDto = productImageService.updateImageDetails(id, requestDto);
        return ResponseEntity.ok(updatedDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteImage(@PathVariable UUID id) {
        productImageService.deleteImage(id);
        return ResponseEntity.noContent().build();
    }
}