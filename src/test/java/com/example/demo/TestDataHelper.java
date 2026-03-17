package com.example.demo;

import com.example.demo.dto.book.BookDto;
import com.example.demo.dto.book.CreateBookRequestDto;
import com.example.demo.dto.category.CategoryRequestDto;
import com.example.demo.dto.category.CategoryResponseDto;
import com.example.demo.model.Book;
import java.math.BigDecimal;
import java.util.Set;

public class TestDataHelper {

    public BookDto createBookDto(CreateBookRequestDto requestDto){
        return new BookDto()
                .setId(1L)
                .setTitle(requestDto.getTitle())
                .setAuthor(requestDto.getAuthor())
                .setIsbn(requestDto.getIsbn())
                .setPrice(requestDto.getPrice());
    }

    public BookDto createBookDto(){
        return new BookDto()
                .setId(1L)
                .setTitle("New Book");
    }

    public CreateBookRequestDto createBookRequestDto() {
        return new CreateBookRequestDto()
                .setTitle("New Book")
                .setAuthor("John Doe")
                .setIsbn("999-999-999")
                .setPrice(BigDecimal.valueOf(500))
                .setCategoriesIds(Set.of(1L));
    }

    public CategoryRequestDto createCategoryRequestDto() {
        return new CategoryRequestDto()
                .setName("Fiction")
                .setDescription("Fiction");
    }

    public CategoryResponseDto createCategoryResponseDto() {
        return new CategoryResponseDto()
            .setId(1L)
            .setName("Fiction");
    }

    public Book createBook() {
        return new Book()
                .setTitle("Test Book 1")
                .setAuthor("Author 1")
                .setIsbn("111-111")
                .setPrice(BigDecimal.valueOf(100))
                .setDescription("Description 1")
                .setCoverImage("image1.jpg");
    }
}
