package com.jewelleryapp.service.impl;

import com.jewelleryapp.dto.request.ProductRequestDto;
import com.jewelleryapp.dto.response.ProductResponseDto;
import com.jewelleryapp.entity.AttributeValue;
import com.jewelleryapp.entity.Category;
import com.jewelleryapp.entity.Collection;
import com.jewelleryapp.entity.Product;
import com.jewelleryapp.exception.ResourceNotFoundException;
import com.jewelleryapp.mapper.ProductMapper;
import com.jewelleryapp.repository.AttributeValueRepository;
import com.jewelleryapp.repository.CategoryRepository;
import com.jewelleryapp.repository.CollectionRepository;
import com.jewelleryapp.repository.ProductRepository;
import com.jewelleryapp.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final CollectionRepository collectionRepository;
    private final AttributeValueRepository attributeValueRepository;
    private final ProductMapper productMapper;

    @Override
    @Transactional
    public ProductResponseDto createProduct(ProductRequestDto requestDto) {
        Product product = productMapper.toEntity(requestDto);

        // Handle relationships
        updateProductRelationships(product, requestDto);

        Product savedProduct = productRepository.save(product);
        return productMapper.toDto(savedProduct);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponseDto> getAllProducts(Specification<Product> spec, Pageable pageable) {
        // Here we fetch Products, which triggers fetches for related entities
        // This can be N+1. For production, @EntityGraph or fetch joins in the
        // specification are recommended.
        return productRepository.findAll(spec, pageable)
                .map(productMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponseDto getProductById(UUID id) {
        // Fetching a single product is usually fine.
        return productRepository.findById(id)
                .map(productMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));
    }

    @Override
    @Transactional
    public ProductResponseDto updateProduct(UUID id, ProductRequestDto requestDto) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));

        // Update basic fields
        productMapper.updateEntityFromDto(requestDto, existingProduct);

        // Update relationships
        updateProductRelationships(existingProduct, requestDto);

        Product updatedProduct = productRepository.save(existingProduct);
        return productMapper.toDto(updatedProduct);
    }

    @Override
    @Transactional
    public void deleteProduct(UUID id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));
        // This will cascade to ProductImages and StockItems
        productRepository.delete(product);
    }

    /**
     * Helper method to update the relationships of a Product entity based on a DTO.
     */
    private void updateProductRelationships(Product product, ProductRequestDto dto) {
        // 1. Set Category
        if (dto.getCategoryId() != null) {
            Category category = categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category", "id", dto.getCategoryId()));
            product.setCategory(category);
        } else {
            product.setCategory(null);
        }

        // 2. Set Collections
        product.getCollections().clear();
        if (dto.getCollectionIds() != null && !dto.getCollectionIds().isEmpty()) {
            Set<Collection> collections = new HashSet<>(collectionRepository.findAllById(dto.getCollectionIds()));
            if (collections.size() != dto.getCollectionIds().size()) {
                throw new ResourceNotFoundException("One or more Collections not found");
            }
            product.setCollections(collections);
        }

        // 3. Set Attributes
        product.getAttributes().clear();
        if (dto.getAttributeValueIds() != null && !dto.getAttributeValueIds().isEmpty()) {
            Set<AttributeValue> attributes = new HashSet<>(attributeValueRepository.findAllById(dto.getAttributeValueIds()));
            if (attributes.size() != dto.getAttributeValueIds().size()) {
                throw new ResourceNotFoundException("One or more AttributeValues not found");
            }
            product.setAttributes(attributes);
        }
    }
}