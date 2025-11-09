package com.jewelleryapp.service.impl;

import com.jewelleryapp.dto.request.CategoryRequestDto;
import com.jewelleryapp.dto.response.CategoryResponseDto;
import com.jewelleryapp.entity.Category;
import com.jewelleryapp.exception.ResourceNotFoundException;
import com.jewelleryapp.mapper.CategoryMapper;
import com.jewelleryapp.repository.CategoryRepository;
import com.jewelleryapp.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    @Transactional
    public CategoryResponseDto createCategory(CategoryRequestDto categoryRequestDto) {
        Category category = categoryMapper.toEntity(categoryRequestDto);

        // Handle setting the parent category
        if (categoryRequestDto.getParentId() != null) {
            Category parent = findCategoryById(categoryRequestDto.getParentId());
            category.setParent(parent);
        }

        Category savedCategory = categoryRepository.save(category);
        return categoryMapper.toDto(savedCategory);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CategoryResponseDto> getAllCategories(Specification<Category> spec, Pageable pageable) {
        return categoryRepository.findAll(spec, pageable)
                .map(categoryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryResponseDto getCategoryById(Integer id) {
        return categoryRepository.findById(id)
                .map(categoryMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));
    }

    @Override
    @Transactional
    public CategoryResponseDto updateCategory(Integer id, CategoryRequestDto categoryRequestDto) {
        Category existingCategory = findCategoryById(id);

        // Update basic fields from DTO
        categoryMapper.updateEntityFromDto(categoryRequestDto, existingCategory);

        // Handle parent update
        if (categoryRequestDto.getParentId() != null) {
            // Prevent setting self as parent
            if (categoryRequestDto.getParentId().equals(id)) {
                throw new IllegalArgumentException("Category cannot be its own parent.");
            }
            Category parent = findCategoryById(categoryRequestDto.getParentId());
            existingCategory.setParent(parent);
        } else {
            existingCategory.setParent(null); // Set as root category
        }

        Category updatedCategory = categoryRepository.save(existingCategory);
        return categoryMapper.toDto(updatedCategory);
    }

    @Override
    @Transactional
    public void deleteCategory(Integer id) {
        Category category = findCategoryById(id);
        // Note: DataIntegrityViolationException will be thrown by DB if products
        // or subcategories are linked. The GlobalExceptionHandler will catch this.
        // For a softer delete, you'd check category.getProducts().isEmpty() etc.
        categoryRepository.delete(category);
    }

    // Helper method to find category or throw
    private Category findCategoryById(Integer id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));
    }
}