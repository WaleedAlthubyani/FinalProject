package com.example.dealify;


import com.example.dealify.Controller.CategoryController;
import com.example.dealify.Model.Category;
import com.example.dealify.Service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@WebMvcTest(value = CategoryController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
public class CategoryControllerTest {

    @MockBean
    private CategoryService categoryService;

    @Autowired
    private MockMvc mockMvc;

    private Category category1, category2;
    private List<Category> categories;

    @BeforeEach
    void setUp() {
        category1 = new Category(1, "Electronics", new HashSet<>());
        category2 = new Category(2, "Furniture", new HashSet<>());
        categories = Arrays.asList(category1, category2);
    }

//Renad
    @Test
    public void addCategoryTest() throws Exception {
        Mockito.doNothing().when(categoryService).addCategory(any(Integer.class), any(Category.class));

        mockMvc.perform(post("/api/v1/category/add-category")
                        .contentType("application/json")
                        .content("{\"name\": \"Electronics\", \"description\": \"Description\"}"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("New Category Added."));
    }
//Renad
    @Test
    public void updateCategoryTest() throws Exception {
        Mockito.doNothing().when(categoryService).updateCategory(any(Integer.class), any(Integer.class), any(Category.class));

        mockMvc.perform(put("/api/v1/category/update-category/category-id/1")
                        .contentType("application/json")
                        .content("{\"name\": \"Updated Category\", \"description\": \"Updated Description\"}"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Category Updated."));
    }
    //Ebtehal
    @Test
    public void deleteCategoryTest() throws Exception {
        Mockito.doNothing().when(categoryService).deleteCategory(any(Integer.class), any(Integer.class));

        mockMvc.perform(delete("/api/v1/category/delete-category/category-id/1"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Category Deleted."));
    }

    //Ebtehal

//    @Test
//    public void getCategoryByNameTest() throws Exception {
//        Mockito.when(categoryService.getCategoryByName("Electronics")).thenReturn(category1);
//
//        mockMvc.perform(get("/api/v1/category/getCategoryByName/Electronics"))
//                .andExpect(status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Electronics"));
//    }
}