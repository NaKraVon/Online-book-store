package com.example.demo.service;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.example.demo.dto.category.CategoryRequestDto;
import com.example.demo.dto.category.CategoryResponseDto;
import com.example.demo.exception.EntityNotFoundException;
import com.example.demo.mapper.CategoryMapper;
import com.example.demo.model.Category;
import com.example.demo.repository.category.CategoryRepository;
import java.util.Optional;

import com.example.demo.service.category.impl.CategoryServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    @DisplayName("Verify findById() returns a category when ID exists")
    void findById_WithValidId_ShouldReturnCategoryDto() {
        Long categoryId = 1L;
        Category category = new Category().setId(categoryId).setName("Test Category");
        CategoryResponseDto categoryResponseDto = new CategoryResponseDto().setId(categoryId).setName("Test Category");

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(categoryMapper.toResponseDto(category)).thenReturn(categoryResponseDto);

        CategoryResponseDto actual = categoryService.findCategoryById(categoryId);

        assertThat(actual).isEqualTo(categoryResponseDto);

        verify(categoryRepository, times(1)).findById(categoryId);
        verify(categoryMapper, times(1)).toResponseDto(category);
    }

    @Test
    @DisplayName("Verify findById() throws exception when ID does not exist")
    void findById_WithInvalidId_ShouldThrowException() {
        Long categoryId = 100L;

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> categoryService.findCategoryById(categoryId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Category was not found by id: " + categoryId);

        verify(categoryRepository, times(1)).findById(categoryId);
        verifyNoInteractions(categoryMapper);
    }

    @Test
    @DisplayName("Verify createCategory() creates a category and returns CategoryDto")
    void save_ValidCreateRequest_ReturnsCategoryDto() {
        CategoryRequestDto requestDto = new CategoryRequestDto();
        Category categoryToSave = new Category();
        Category savedCategory = new Category().setId(1L);
        CategoryResponseDto expectedDto = new CategoryResponseDto().setId(1L);

        when(categoryMapper.toEntity(requestDto)).thenReturn(categoryToSave);
        when(categoryRepository.save(categoryToSave)).thenReturn(savedCategory);
        when(categoryMapper.toResponseDto(savedCategory)).thenReturn(expectedDto);

        CategoryResponseDto actual = categoryService.createCategory(requestDto);

        assertThat(actual).isEqualTo(expectedDto);
        verify(categoryRepository, times(1)).save(categoryToSave);
    }

    @Test
    @DisplayName("Verify updateCategory() updates existing category and returns CategoryDto")
    void update_WithValidId_ReturnsUpdatedBookDto() {
        Long categoryId = 1L;
        CategoryRequestDto requestDto = new CategoryRequestDto();
        Category existingCategory = new Category().setId(categoryId);
        Category updatedCategory = new Category().setId(categoryId);
        CategoryResponseDto expectedDto = new CategoryResponseDto().setId(categoryId);

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(existingCategory));
        when(categoryRepository.save(existingCategory)).thenReturn(updatedCategory);
        when(categoryMapper.toResponseDto(updatedCategory)).thenReturn(expectedDto);

        CategoryResponseDto actual = categoryService.updateCategory(categoryId, requestDto);

        assertThat(actual).isEqualTo(expectedDto);
        verify(categoryRepository).findById(categoryId);
        verify(categoryRepository).save(existingCategory);
    }

    @Test
    @DisplayName("Verify deleteCategory() calls repository deleteById when category exists")
    void delete_ExistingId_DeletesBook() {
        Long categoryId = 1L;
        Category category = new Category().setId(categoryId);

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

        categoryService.deleteCategory(categoryId);

        verify(categoryRepository, times(1)).deleteById(categoryId);
    }

    @Test
    @DisplayName("Verify deleteCategory() does nothing when category does not exist")
    void delete_NonExistingId_DoesNothing() {
        Long categoryId = 100L;
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> categoryService.deleteCategory(categoryId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Category was not found by id: " + categoryId);

        verify(categoryRepository, times(0)).deleteById(categoryId);
    }
}
