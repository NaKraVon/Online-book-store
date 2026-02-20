package com.example.demo.mapper;

import com.example.demo.config.MapperConfig;
import com.example.demo.dto.book.BookDto;
import com.example.demo.dto.book.BookDtoWithoutCategoriesIds;
import com.example.demo.dto.book.CreateBookRequestDto;
import com.example.demo.model.Book;
import com.example.demo.model.Category;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(config = MapperConfig.class,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface BookMapper {
    BookDto toDto(Book book);

    Book toModel(CreateBookRequestDto createBookRequestDto);

    void updateModel(
            CreateBookRequestDto createBookRequestDto,
            @MappingTarget Book book);

    @AfterMapping
    default void setCategoryIds(@MappingTarget BookDto bookDto, Book book) {
        if (book.getCategories() != null) {
            Set<Long> categoryIds = book.getCategories().stream()
                    .map(Category::getId)
                    .collect(Collectors.toSet());
            bookDto.setCategoryIds(categoryIds);
        }
    }

    @AfterMapping
    default void setCategories(CreateBookRequestDto bookDto, @MappingTarget Book book) {
        Set<Category> categories = bookDto.getCategoriesIds().stream()
                .map(Category::new)
                .collect(Collectors.toUnmodifiableSet());
        book.setCategories(categories);
    }

    BookDtoWithoutCategoriesIds toDtoWithoutCategories(Book book);
}
