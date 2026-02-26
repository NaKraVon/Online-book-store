package com.example.demo.mapper;

import com.example.demo.config.MapperConfig;
import com.example.demo.dto.category.CategoryRequestDto;
import com.example.demo.dto.category.CategoryResponseDto;
import com.example.demo.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface CategoryMapper {
    Category toEntity(CategoryRequestDto categoryRequestDto);

    CategoryResponseDto toResponseDto(Category category);

    void updateCategoryFromDb(CategoryRequestDto categoryRequestDto,
                              @MappingTarget Category category);
}
