package com.jewelleryapp.controller;

import com.jewelleryapp.dto.request.CategoryRequestDto;
import com.jewelleryapp.dto.response.CategoryResponseDto;
import com.jewelleryapp.entity.Category;
import com.jewelleryapp.service.CategoryService;
import com.jewelleryapp.specification.CategorySpecification;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryResponseDto> createCategory(@Valid @RequestBody CategoryRequestDto categoryRequestDto) {
        CategoryResponseDto createdCategory = categoryService.createCategory(categoryRequestDto);
        return new ResponseEntity<>(createdCategory, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDto> getCategoryById(@PathVariable Integer id) {
        CategoryResponseDto category = categoryService.getCategoryById(id);
        return ResponseEntity.ok(category);
    }

    @GetMapping
    public ResponseEntity<Page<CategoryResponseDto>> getAllCategories(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer parentId,
            @RequestParam(required = false) Boolean isRoot,
            Pageable pageable) {

        // Build dynamic specification based on query params
        Specification<Category> spec = Specification.where(null);

        if (name != null) {
            spec = spec.and(CategorySpecification.hasName(name));
        }
        if (parentId != null) {
            spec = spec.and(CategorySpecification.hasParentId(parentId));
        }
        if (isRoot != null && isRoot) {
            spec = spec.and(CategorySpecification.isRootCategory());
        }

        Page<CategoryResponseDto> categories = categoryService.getAllCategories(spec, pageable);
        return ResponseEntity.ok(categories);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponseDto> updateCategory(@PathVariable Integer id, @Valid @RequestBody CategoryRequestDto categoryRequestDto) {
        CategoryResponseDto updatedCategory = categoryService.updateCategory(id, categoryRequestDto);
        return ResponseEntity.ok(updatedCategory);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Integer id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}