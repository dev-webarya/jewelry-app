package com.jewelleryapp.service;

import com.jewelleryapp.dto.request.CategoryRequestDto;
import com.jewelleryapp.dto.response.CategoryResponseDto;
import com.jewelleryapp.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface CategoryService {

    CategoryResponseDto createCategory(CategoryRequestDto categoryRequestDto);

    Page<CategoryResponseDto> getAllCategories(Specification<Category> spec, Pageable pageable);

    CategoryResponseDto getCategoryById(Integer id);

    CategoryResponseDto updateCategory(Integer id, CategoryRequestDto categoryRequestDto);

    void deleteCategory(Integer id);
}