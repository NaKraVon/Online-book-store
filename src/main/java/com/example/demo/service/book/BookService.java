package com.example.demo.service.book;

import com.example.demo.dto.book.BookDto;
import com.example.demo.dto.book.BookSearchParametersDto;
import com.example.demo.dto.book.CreateBookRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookDto save(CreateBookRequestDto book);

    BookDto findById(Long id);

    Page<BookDto> findAll(Pageable pageable);

    BookDto update(Long id, CreateBookRequestDto requestDto);

    void delete(Long id);

    Page<BookDto> search(BookSearchParametersDto searchParameters, Pageable pageable);
}
