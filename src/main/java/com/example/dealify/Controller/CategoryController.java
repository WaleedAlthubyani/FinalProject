package com.example.dealify.Controller;

import com.example.dealify.Api.ApiResponse;
import com.example.dealify.Model.Category;
import com.example.dealify.Model.MyUser;
import com.example.dealify.OutDTO.CategoryOutDTO;
import com.example.dealify.Service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/category")
@RequiredArgsConstructor
public class CategoryController { //Renad

    // 1. Declare a dependency for CategoryService using Dependency Injection
    private final CategoryService categoryService;

    // 2. CRUD
    // 2.1 Get
    @GetMapping("/get-all-categories")
    public ResponseEntity getAllCategories() {
        return ResponseEntity.status(200).body(categoryService.getAllCategories());
    }

    // 2.2 Post
    @PostMapping("/add-category")
    public ResponseEntity addCategory(@AuthenticationPrincipal MyUser myUser, @RequestBody @Valid Category category) {
        categoryService.addCategory(myUser.getId(), category);
        return ResponseEntity.status(200).body(new ApiResponse("New Category Added."));
    }

    // 2.3 Update
    @PutMapping("/update-category/category-id/{category-id}")
    public ResponseEntity updateCategory(@AuthenticationPrincipal MyUser myUser, @PathVariable(name = "category-id") Integer categoryId, @RequestBody @Valid Category category) {
        categoryService.updateCategory(myUser.getId(), categoryId, category);
        return ResponseEntity.status(200).body(new ApiResponse("Category Updated."));
    }

    // 2.4 Delete
    @DeleteMapping("/delete-category/category-id/{category-id}")
    public ResponseEntity deleteCategory(@AuthenticationPrincipal MyUser myUser, @PathVariable(name = "category-id") Integer categoryId) {
        categoryService.deleteCategory(myUser.getId(),categoryId);
        return ResponseEntity.status(200).body(new ApiResponse("Category Deleted."));
    }

    // 3. Extra endpoints
    // An endpoint to get category by name
    //Ebtehal
    @GetMapping("/get-category-by-name/{name}")
    public ResponseEntity getCategoryByName(@PathVariable String name) {
        CategoryOutDTO categoryOutDTO = categoryService.getCategoryByName(name);
        return ResponseEntity.status(200).body(categoryOutDTO);
    }
}