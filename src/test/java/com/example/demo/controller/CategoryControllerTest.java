package com.example.demo.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import com.example.demo.TestDataHelper;
import com.example.demo.dto.book.BookDto;
import com.example.demo.dto.category.CategoryRequestDto;
import com.example.demo.dto.category.CategoryResponseDto;
import com.example.demo.security.auth.JwtUtil;
import com.example.demo.service.book.BookService;
import com.example.demo.service.category.CategoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
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

@WebMvcTest(controllers = CategoryController.class)
public class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final TestDataHelper testDataHelper = new TestDataHelper();
    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    @MockitoBean
    private CategoryService categoryService;

    @MockitoBean
    private BookService bookService;

    @MockitoBean
    private JwtUtil jwtUtil;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @Test
    @DisplayName("Create category - returns 201 Created for Admin")
    @WithMockUser(roles = "ADMIN")
    public void createCategory_ValidRequest_ReturnsCreated() throws Exception {
        CategoryRequestDto requestDto = testDataHelper.createCategoryRequestDto();

        CategoryResponseDto responseDto = testDataHelper.createCategoryResponseDto();

        when(categoryService.createCategory(any(CategoryRequestDto.class))).thenReturn(responseDto);

        mockMvc.perform(post("/categories")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Fiction"));
    }

    @Test
    @DisplayName("Create category - returns 400 Bad Request when name is blank")
    @WithMockUser(roles = "ADMIN")
    public void createCategory_InvalidName_ReturnsBadRequest() throws Exception {
        CategoryRequestDto requestDto = new CategoryRequestDto()
                .setName("")
                .setDescription("Description");

        mockMvc.perform(post("/categories")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").exists());
    }

    @Test
    @DisplayName("Get all categories - returns page of categories")
    @WithMockUser
    public void getAllCategories_ValidRequest_ReturnsPage() throws Exception {
        CategoryResponseDto responseDto = testDataHelper.createCategoryResponseDto();

        PageImpl<CategoryResponseDto> categoryPage = new PageImpl<>(
                List.of(responseDto), PageRequest.of(0, 10), 1
        );

        when(categoryService.findAllCategories(any(Pageable.class))).thenReturn(categoryPage);

        mockMvc.perform(get("/categories")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Fiction"));
    }

    @Test
    @DisplayName("Get category by ID - returns 404 Not Found for non-existing ID")
    @WithMockUser
    public void getCategoryById_NonExistingId_ReturnsNotFound() throws Exception {
        Long nonExistingId = 999L;
        when(categoryService.getCategoryById(nonExistingId))
                .thenThrow(new com.example.demo.exception.EntityNotFoundException("Can't find category by id: " + nonExistingId));

        mockMvc.perform(get("/categories/{id}", nonExistingId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()); // Очікуємо 404
    }

    @Test
    @DisplayName("Get books by category ID - returns page of books")
    @WithMockUser
    public void getAllBooksByCategoryId_ValidId_ReturnsBooksPage() throws Exception {
        BookDto bookDto = testDataHelper.createBookDto();

        PageImpl<BookDto> bookPage = new PageImpl<>(
                List.of(bookDto), PageRequest.of(0, 10), 1
        );

        when(bookService.findAllBooksByCategoryId(any(Long.class), any(Pageable.class)))
                .thenReturn(bookPage);

        mockMvc.perform(get("/categories/1/books")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].title").value("New Book"));
    }
}
