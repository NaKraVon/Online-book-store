package com.example.demo.controller.integration;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.demo.TestDataHelper;
import com.example.demo.dto.book.CreateBookRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
public class BookControllerIntegrationTest {

    private static MockMvc mockMvc;

    private final TestDataHelper testDataHelper = new TestDataHelper();
    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();


    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext applicationContext) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
    }

    @Test
    @DisplayName("Get book by ID - returns existing book")
    @WithMockUser(roles = "USER")
    @Sql(scripts = "classpath:database/book-category/add-books-and-categories.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/book-category/remove-all.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getBookById_ValidId_ReturnsBook() throws Exception {
        Long bookId = 1L;
        String expectedTitle = "Test Book 1";

        mockMvc.perform(get("/books/{id}", bookId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(expectedTitle));
    }

    @Test
    @DisplayName("Given valid book request, create a new book and return 201 Created")
    @WithMockUser(roles = "ADMIN")
    @Sql(scripts = "classpath:database/book-category/add-category.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/book-category/remove-all.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void createBook_ValidRequest_ReturnsCreatedBook() throws Exception {
        CreateBookRequestDto requestDto = testDataHelper.createBookRequestDto();

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(post("/books")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.title").exists())
                .andExpect(jsonPath("$.author").exists());
    }


    @Test
    @DisplayName("Get all books - returns page of books")
    @WithMockUser(roles = "USER")
    @Sql(scripts = "classpath:database/book-category/add-books-and-categories.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/book-category/remove-all.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getAll_ValidRequest_ReturnsPage() throws Exception {

        mockMvc.perform(get("/books")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content[0].title").value("Test Book 1"));;

    }

    @Test
    @DisplayName("Delete book - returns 204 No Content for Admin")
    @WithMockUser(roles = "ADMIN")
    @Sql(scripts = "classpath:database/book-category/add-books-and-categories.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/book-category/remove-all.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void deleteBook_ValidId_ReturnsNoContent() throws Exception {
        Long bookId = 1L;

        mockMvc.perform(delete("/books/{id}", bookId))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Delete book - returns 204 No Content for Admin")
    @WithMockUser(roles = "ADMIN")
    @Sql(scripts = "classpath:database/book-category/add-books-and-categories.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/book-category/remove-all.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void updateBook_ValidId_ReturnsOk() throws Exception {
        Long bookId = 1L;
        CreateBookRequestDto requestDto = testDataHelper.createBookRequestDto();

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(put("/books/{id}", bookId)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookId))
                .andExpect(jsonPath("$.title").value("New Book"))
                .andExpect(jsonPath("$.author").value("John Doe"));
    }
}
