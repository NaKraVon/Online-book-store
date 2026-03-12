package com.example.demo.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


import com.example.demo.dto.book.BookDto;
import com.example.demo.dto.book.CreateBookRequestDto;
import com.example.demo.security.auth.JwtUtil;
import com.example.demo.service.book.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@WebMvcTest(controllers = BookController.class)
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    @MockitoBean
    private BookService bookService;

    @MockitoBean
    private JwtUtil jwtUtil;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @Test
    @DisplayName("Given valid book request, create a new book and return 201 Created")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void createBook_ValidRequest_ReturnsCreatedBook() throws Exception {
        CreateBookRequestDto requestDto = new CreateBookRequestDto()
            .setTitle("New Spring Boot Book")
            .setAuthor("John Doe")
            .setIsbn("999-999-999")
            .setPrice(BigDecimal.valueOf(500))
            .setCategoriesIds(Set.of(1L));

        BookDto expectedResponse = new BookDto()
            .setId(1L)
            .setTitle(requestDto.getTitle())
            .setAuthor(requestDto.getAuthor())
            .setIsbn(requestDto.getIsbn())
            .setPrice(requestDto.getPrice());

        when(bookService.save(any(CreateBookRequestDto.class))).thenReturn(expectedResponse);

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(post("/books")
                        .with(csrf())
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        BookDto actual = objectMapper.readValue(result.getResponse().getContentAsString(), BookDto.class);

        assertNotNull(actual, "Response body should not be null");
        assertEquals(1L, actual.getId());
        assertEquals("New Spring Boot Book", actual.getTitle());
        assertEquals("John Doe", actual.getAuthor());
    }

    @Test
    @DisplayName("Get all books - returns page of books")
    @WithMockUser
    public void getAll_ValidRequest_ReturnsPage() throws Exception {
        BookDto bookDto = new BookDto();
        bookDto.setId(1L);
        bookDto.setTitle("Sample Book");

        List<BookDto> books = List.of(bookDto);
        PageImpl<BookDto> bookPage = new PageImpl<>(books, PageRequest.of(0, 10), 1);

        when(bookService.findAll(any(Pageable.class))).thenReturn(bookPage);

        mockMvc.perform(get("/books")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content[0].title").value("Sample Book"));
    }

    @Test
    @DisplayName("Delete book - returns 204 No Content for Admin")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void deleteBook_ValidId_ReturnsNoContent() throws Exception {
        Long bookId = 1L;
        doNothing().when(bookService).delete(bookId);

        mockMvc.perform(delete("/books/{id}", bookId)
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Create book - returns 201 Created when valid")
    @WithMockUser(roles = "ADMIN")
    public void createBook_ValidRequest_ReturnsCreated() throws Exception {
        CreateBookRequestDto requestDto = new CreateBookRequestDto();
        requestDto.setTitle("New Book");
        requestDto.setAuthor("Author");
        requestDto.setIsbn("123-456-789");
        requestDto.setPrice(BigDecimal.valueOf(100));
        requestDto.setCategoriesIds(Set.of(1L));

        BookDto expectedResponse = new BookDto();
        expectedResponse.setId(1L);
        expectedResponse.setTitle("New Book");

        when(bookService.save(any(CreateBookRequestDto.class))).thenReturn(expectedResponse);

        mockMvc.perform(post("/books")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("New Book"));
    }
}