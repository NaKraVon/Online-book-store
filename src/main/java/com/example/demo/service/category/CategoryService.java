package com.example.demo.service.category;

import com.example.demo.dto.category.CategoryRequestDto;
import com.example.demo.dto.category.CategoryResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CategoryService {
    CategoryResponseDto createCategory(CategoryRequestDto categoryRequestDto);

    CategoryResponseDto findCategoryById(Long id);

    CategoryResponseDto updateCategory(Long id, CategoryRequestDto categoryRequestDto);

    void deleteCategory(Long id);

    Page<CategoryResponseDto> findAllCategories(Pageable pageable);

    CategoryResponseDto getCategoryById(Long id);
}
