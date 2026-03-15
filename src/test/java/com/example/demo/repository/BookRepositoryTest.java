package com.example.demo.repository;

import static org.assertj.core.api.Assertions.assertThat;

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

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

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
                .isEqualTo(expectedBookStub());

        assertThat(actualBook.getPrice()).isEqualByComparingTo(BigDecimal.valueOf(199.99));

        assertThat(actualBook.getCategories())
                .extracting(Category::getId)
                .contains(categoryId);

    }

    private Book expectedBookStub() {
        return new Book()
                .setTitle("Test Book 1")
                .setAuthor("Author 1")
                .setIsbn("111-111")
                .setDescription("Description 1")
                .setCoverImage("image1.jpg");
    }

}


