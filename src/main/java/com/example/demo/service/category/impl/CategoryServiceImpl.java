package com.example.demo.service.category.impl;

import com.example.demo.dto.category.CategoryRequestDto;
import com.example.demo.dto.category.CategoryResponseDto;
import com.example.demo.exception.EntityNotFoundException;
import com.example.demo.mapper.CategoryMapper;
import com.example.demo.model.Category;
import com.example.demo.repository.category.CategoryRepository;
import com.example.demo.service.category.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public CategoryResponseDto createCategory(CategoryRequestDto categoryRequestDto) {
        return categoryMapper.toResponseDto(categoryRepository
                .save(categoryMapper.toEntity(categoryRequestDto)));
    }

    @Override
    public CategoryResponseDto findCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found by id: " + id));
        return categoryMapper.toResponseDto(category);
    }

    @Override
    public CategoryResponseDto updateCategory(Long id, CategoryRequestDto categoryRequestDto) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category "
                        + "was not found by id: " + id));
        categoryMapper.updateCategoryFromDb(categoryRequestDto, category);
        return categoryMapper.toResponseDto(categoryRepository.save(category));
    }

    @Override
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category "
                        + "was not found by id: " + id));
        categoryRepository.deleteById(id);
    }

    @Override
    public Page<CategoryResponseDto> findAllCategories(Pageable pageable) {
        return categoryRepository.findAll(pageable).map(categoryMapper::toResponseDto);
    }

    @Override
    public CategoryResponseDto getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category was "
                        + "not found by id: " + id));
        return categoryMapper.toResponseDto(category);
    }
}
