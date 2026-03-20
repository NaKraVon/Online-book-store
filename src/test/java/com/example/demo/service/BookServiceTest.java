package com.example.demo.service;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.example.demo.dto.book.BookDto;
import com.example.demo.dto.book.CreateBookRequestDto;
import com.example.demo.exception.EntityNotFoundException;
import com.example.demo.mapper.BookMapper;
import com.example.demo.model.Book;
import com.example.demo.repository.book.BookRepository;
import com.example.demo.service.book.impl.BookServiceImpl;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    @DisplayName("Verify findById() returns a book when ID exists")
    void findById_WithValidId_ShouldReturnBookDto() {
        Long bookId = 1L;
        Book book = new Book().setId(bookId).setTitle("Test Book");
        BookDto bookDto = new BookDto().setId(bookId).setTitle("Test Book");

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        BookDto actual = bookService.findById(bookId);

        assertThat(actual).isEqualTo(bookDto);

        verify(bookRepository, times(1)).findById(bookId);
        verify(bookMapper, times(1)).toDto(book);
    }

    @Test
    @DisplayName("Verify findById() throws exception when ID does not exist")
    void findById_WithInvalidId_ShouldThrowException() {
        Long bookId = 100L;

        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookService.findById(bookId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Can not find book with id: " + bookId);

        verify(bookRepository, times(1)).findById(bookId);
        verifyNoInteractions(bookMapper);
    }

    @Test
    @DisplayName("Verify save() creates a book and returns BookDto")
    void save_ValidCreateRequest_ReturnsBookDto() {
        CreateBookRequestDto requestDto = new CreateBookRequestDto();
        Book bookToSave = new Book();
        Book savedBook = new Book().setId(1L);
        BookDto expectedDto = new BookDto().setId(1L);

        when(bookMapper.toModel(requestDto)).thenReturn(bookToSave);
        when(bookRepository.save(bookToSave)).thenReturn(savedBook);
        when(bookMapper.toDto(savedBook)).thenReturn(expectedDto);

        BookDto actual = bookService.save(requestDto);

        assertThat(actual).isEqualTo(expectedDto);
        verify(bookRepository, times(1)).save(bookToSave);
    }

    @Test
    @DisplayName("Verify update() updates existing book and returns BookDto")
    void update_WithValidId_ReturnsUpdatedBookDto() {
        Long bookId = 1L;
        CreateBookRequestDto requestDto = new CreateBookRequestDto();
        Book existingBook = new Book().setId(bookId);
        Book updatedBook = new Book().setId(bookId);
        BookDto expectedDto = new BookDto().setId(bookId);

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(existingBook));
        when(bookRepository.save(existingBook)).thenReturn(updatedBook);
        when(bookMapper.toDto(updatedBook)).thenReturn(expectedDto);

        BookDto actual = bookService.update(bookId, requestDto);

        assertThat(actual).isEqualTo(expectedDto);
        verify(bookRepository).findById(bookId);
        verify(bookRepository).save(existingBook);
    }

    @Test
    @DisplayName("Verify delete() calls repository deleteById when book exists")
    void delete_ExistingId_DeletesBook() {
        Long bookId = 1L;
        when(bookRepository.existsById(bookId)).thenReturn(true);

        bookService.delete(bookId);

        verify(bookRepository, times(1)).deleteById(bookId);
    }

    @Test
    @DisplayName("Verify delete() does nothing when book does not exist")
    void delete_NonExistingId_DoesNothing() {
        Long bookId = 100L;
        when(bookRepository.existsById(bookId)).thenReturn(false);

        bookService.delete(bookId);

        verify(bookRepository, times(0)).deleteById(bookId);
    }

}
