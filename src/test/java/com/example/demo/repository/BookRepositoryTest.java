package com.example.demo.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.demo.TestDataHelper;
import com.example.demo.dto.book.BookDto;
import com.example.demo.model.Book;
import com.example.demo.model.Category;
import com.example.demo.repository.book.BookRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    private final TestDataHelper testDataHelper = new TestDataHelper();

    @Test
    @DisplayName("Save new book - assigns ID and saves to DB")
    public void saveBook_ValidBook_ShouldPersist() {
        Book savedBook = bookRepository.save(testDataHelper.createBook());

        assertThat(savedBook).isNotNull();
        assertThat(savedBook.getId()).isNotNull();
        assertThat(savedBook.getTitle()).isEqualTo("Test Book 1");
    }

    @Test
    @DisplayName("Find book by ID - returns correct book")
    @Sql(scripts = "/database/book-category/add-books-and-categories.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/database/book-category/remove-all.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findById_ExistingId_ShouldReturnBook() {
        Long bookId = 1L;

        Optional<Book> actualBook = bookRepository.findById(bookId);

        assertThat(actualBook).isPresent();
        assertThat(actualBook.get().getId()).isEqualTo(bookId);
        assertThat(actualBook.get().getTitle()).isEqualTo("Test Book 1");
    }

    @Test
    @DisplayName("Find all books by category ID - Corrected Version")
    @Sql(scripts = "/database/book-category/add-books-and-categories.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/database/book-category/remove-all.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findAllBooks_ByCategoryId_ShouldReturnCorrectBooks() {
        Long categoryId = 1L;
        Pageable pageable = PageRequest.of(0, 10);

        Page<Book> actualPage = bookRepository.findAllBooksByCategories_Id(categoryId, pageable);

        assertThat(actualPage.getContent()).hasSize(2);

        Book actualBook = actualPage.getContent().get(0);

        assertThat(actualBook)
                .usingRecursiveComparison()
                .ignoringFields("categories", "price", "id")
                .isEqualTo(testDataHelper.createBook());

        assertThat(actualBook.getPrice()).isEqualByComparingTo(BigDecimal.valueOf(199.99));

        assertThat(actualBook.getCategories())
                .extracting(Category::getId)
                .contains(categoryId);
    }

    @Test
    @DisplayName("Update existing book - reflects changes in DB")
    @Sql(scripts = "/database/book-category/add-books-and-categories.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/database/book-category/remove-all.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void updateBook_ExistingBook_ShouldSaveUpdates() {
        Long bookId = 1L;
        Book bookToUpdate = bookRepository.findById(bookId).orElseThrow();
        bookToUpdate.setTitle("Updated Title");
        bookToUpdate.setPrice(BigDecimal.valueOf(999.99));

        bookRepository.save(bookToUpdate);

        Book updatedBook = bookRepository.findById(bookId).orElseThrow();
        assertThat(updatedBook.getTitle()).isEqualTo("Updated Title");
        assertThat(updatedBook.getPrice()).isEqualByComparingTo(BigDecimal.valueOf(999.99));
    }

    @Test
    @DisplayName("Delete book by ID - removes from DB")
    @Sql(scripts = "/database/book-category/add-books-and-categories.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/database/book-category/remove-all.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void deleteById_ExistingId_ShouldDeleteBook() {
        Long bookId = 1L;

        bookRepository.deleteById(bookId);

        var deletedBook = bookRepository.findById(bookId);
        assertThat(deletedBook).isEmpty();
    }
}
