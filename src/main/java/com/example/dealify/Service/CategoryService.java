package com.example.dealify.Service;

import com.example.dealify.Api.ApiException;
import com.example.dealify.Model.Category;
import com.example.dealify.Model.MyUser;
import com.example.dealify.OutDTO.CategoryOutDTO;
import com.example.dealify.Repository.AuthRepository;
import com.example.dealify.Repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {  //Renad

    // 1. Declare a dependencies using Dependency Injection
    private final CategoryRepository categoryRepository;
    private final AuthRepository authRepository;

    // 2. CRUD
    // 2.1 Get
    public List<CategoryOutDTO> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        List<CategoryOutDTO> categoryOutDTOS = new ArrayList<>();
        for (Category category : categories) {
            CategoryOutDTO categoryOutDTO = new CategoryOutDTO(category.getName());
            categoryOutDTOS.add(categoryOutDTO);
        }
        return categoryOutDTOS;
    }

    // 2.2 Post
    public void addCategory(Integer authId, Category category) {
        MyUser auth = authRepository.findMyUserById(authId);
        if (auth == null) {
            throw new ApiException("Admin was not found");
        }

        if (!auth.getRole().equalsIgnoreCase("Admin")) {
            throw new ApiException("You don't have the permission to access this endpoint");
        }

        categoryRepository.save(category);
    }

    // 2.3 Update
    public void updateCategory(Integer authId, Integer categoryId, Category category) {
        MyUser auth = authRepository.findMyUserById(authId);
        if (auth == null) {
            throw new ApiException("Admin was not found");
        }

        if (!auth.getRole().equalsIgnoreCase("Admin")) {
            throw new ApiException("You don't have the permission to access this endpoint");
        }

        Category oldCategory = categoryRepository.findCategoryById(categoryId);
        if (oldCategory == null) {
            throw new ApiException("Category Not Found.");
        }
        oldCategory.setName(category.getName());
        categoryRepository.save(oldCategory);
    }

    // 2.4 Delete
    public void deleteCategory(Integer authId, Integer categoryId) {
        MyUser auth = authRepository.findMyUserById(authId);
        if (auth == null) {
            throw new ApiException("Admin was not found");
        }

        if (!auth.getRole().equalsIgnoreCase("Admin")) {
            throw new ApiException("You don't have the permission to access this endpoint");
        }

        Category oldCategory = categoryRepository.findCategoryById(categoryId);
        if (oldCategory == null) {
            throw new ApiException("Category Not Found.");
        }
        categoryRepository.delete(oldCategory);
    }

    // 3. Extra endpoint
    // An endpoint to get category by name
    public CategoryOutDTO getCategoryByName(String name) {
        Category category = categoryRepository.findCategoryByName(name);
        if (category == null) {
            throw new ApiException("Category Not Found.");
        }

        return new CategoryOutDTO(category.getName());
    }
}