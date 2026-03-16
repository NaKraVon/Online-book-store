package com.example.demo.controller.integration;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.demo.TestDataHelper;
import com.example.demo.dto.category.CategoryRequestDto;
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
public class CategoryControllerIntegrationTest {

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
    @DisplayName("Given valid category request, create a new category and return 201 Created")
    @WithMockUser(roles = "ADMIN")
    @Sql(scripts = "classpath:database/book-category/remove-all.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void createCategory_ValidRequest_ReturnsCreatedCategory() throws Exception {
        CategoryRequestDto categoryRequestDto = testDataHelper.createCategoryRequestDto();

        String jsonRequest = objectMapper.writeValueAsString(categoryRequestDto);

        mockMvc.perform(post("/categories")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").exists());

    }

    @Test
    @DisplayName("Get category by ID - returns existing category")
    @WithMockUser(roles = "USER")
    @Sql(scripts = "classpath:database/book-category/add-books-and-categories.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/book-category/remove-all.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getCategoryById_ValidId_ReturnsCategory() throws Exception {
        Long categoryId = 1L;

        mockMvc.perform(get("/categories/{id}", categoryId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Fiction"));
    }

    @Test
    @DisplayName("Get all categories - returns page")
    @WithMockUser(roles = "USER")
    @Sql(scripts = "classpath:database/book-category/add-books-and-categories.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/book-category/remove-all.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getAllCategories_ValidId_ReturnsPage() throws Exception {

        mockMvc.perform(get("/categories")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content[0].name").value("Fiction"));
    }

    @Test
    @DisplayName("Update category - returns 200 Ok for Admin")
    @WithMockUser(roles = "ADMIN")
    @Sql(scripts = "classpath:database/book-category/add-books-and-categories.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/book-category/remove-all.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void updateCategory_ValidId_ReturnsOk() throws Exception {
        Long categoryId = 1L;
        CategoryRequestDto categoryRequestDto = testDataHelper.createCategoryRequestDto();

        String jsonRequest = objectMapper.writeValueAsString(categoryRequestDto);

        mockMvc.perform(put("/categories/{id}", categoryId)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(categoryId))
                .andExpect(jsonPath("$.name").value("Fiction"));


    }

    @Test
    @DisplayName("Get all categories - returns page")
    @WithMockUser(roles = "ADMIN")
    @Sql(scripts = "classpath:database/book-category/add-category.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/book-category/remove-all.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void deleteCategory_ValidId_ReturnsNoContent() throws Exception {
        Long categoryId = 1L;

        mockMvc.perform(delete("/categories/{id}", categoryId))
                .andExpect(status().isNoContent());
    }
}
