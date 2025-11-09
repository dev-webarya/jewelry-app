package com.jewelleryapp.service.impl;

import com.jewelleryapp.dto.request.ProductImageRequestDto;
import com.jewelleryapp.dto.response.ProductImageResponseDto;
import com.jewelleryapp.entity.Product;
import com.jewelleryapp.entity.ProductImage;
import com.jewelleryapp.exception.ResourceNotFoundException;
import com.jewelleryapp.mapper.ProductImageMapper;
import com.jewelleryapp.repository.ProductImageRepository;
import com.jewelleryapp.repository.ProductRepository;
import com.jewelleryapp.service.ProductImageService;
import com.jewelleryapp.specification.ProductImageSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductImageServiceImpl implements ProductImageService {

    private final ProductImageRepository productImageRepository;
    private final ProductRepository productRepository;
    private final ProductImageMapper productImageMapper;

    @Override
    @Transactional
    public ProductImageResponseDto addImageToProduct(ProductImageRequestDto requestDto) {
        Product product = productRepository.findById(requestDto.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", requestDto.getProductId()));

        // If this new image is set as primary, reset all others for this product
        if (requestDto.isPrimary()) {
            productImageRepository.resetAllPrimaryImagesForProduct(product.getId());
        }

        ProductImage productImage = productImageMapper.toEntity(requestDto);
        productImage.setProduct(product);

        ProductImage savedImage = productImageRepository.save(productImage);
        return productImageMapper.toDto(savedImage);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductImageResponseDto> getImagesForProduct(UUID productId, Pageable pageable) {
        Specification<ProductImage> spec = Specification.where(ProductImageSpecification.hasProductId(productId));
        return productImageRepository.findAll(spec, pageable)
                .map(productImageMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductImageResponseDto getImageById(UUID id) {
        return productImageRepository.findById(id)
                .map(productImageMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("ProductImage", "id", id));
    }

    @Override
    @Transactional
    public ProductImageResponseDto updateImageDetails(UUID id, ProductImageRequestDto requestDto) {
        ProductImage existingImage = productImageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ProductImage", "id", id));

        // Check if product ID is changing (not usually allowed, but good to check)
        if (!existingImage.getProduct().getId().equals(requestDto.getProductId())) {
            throw new IllegalArgumentException("Cannot change the product of an existing image.");
        }

        // If this image is being set to primary, reset others.
        if (requestDto.isPrimary() && !existingImage.isPrimary()) {
            productImageRepository.resetAllPrimaryImagesForProduct(requestDto.getProductId());
        }

        productImageMapper.updateEntityFromDto(requestDto, existingImage);

        ProductImage updatedImage = productImageRepository.save(existingImage);
        return productImageMapper.toDto(updatedImage);
    }

    @Override
    @Transactional
    public void deleteImage(UUID id) {
        ProductImage image = productImageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ProductImage", "id", id));
        productImageRepository.delete(image);
    }
}