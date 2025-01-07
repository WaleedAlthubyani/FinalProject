package com.example.dealify.Controller;

import com.example.dealify.Api.ApiResponse;
import com.example.dealify.InDTO.ProductInDTO;
import com.example.dealify.Model.MyUser;
import com.example.dealify.Service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/product")
@RequiredArgsConstructor
public class ProductController {

    // 1. Declare a dependency for ProductService using Dependency Injection
    private final ProductService productService;

    // 2. CRUD
    // 2.1 Get
    @GetMapping("/get-all-products") //Renad
    public ResponseEntity getAllProducts() {
        return ResponseEntity.status(200).body(productService.getAllProducts());
    }

    // 2.2 Post
    @PostMapping("/add-product") //Renad
    public ResponseEntity addProduct(@AuthenticationPrincipal MyUser myUser, @RequestBody @Valid ProductInDTO productInDTO) {
        productService.addProduct(myUser.getId(), productInDTO);
        return ResponseEntity.status(200).body(new ApiResponse("New Product Added."));
    }

    // 2.3 Update
    @PutMapping("/update/product-id/{product-id}") //Renad
    public ResponseEntity updateProduct(@AuthenticationPrincipal MyUser myUser, @PathVariable(name = "product-id") Integer productId, @RequestBody @Valid ProductInDTO productInDTO) {
        productService.updateProduct(myUser.getId(), productId, productInDTO);
        return ResponseEntity.status(200).body(new ApiResponse("Product Updated."));
    }

    // 2.4 Delete
    @DeleteMapping("/delete/product-id/{product-id}") //Renad
    public ResponseEntity deleteCategory(@AuthenticationPrincipal MyUser myUser, @PathVariable(name = "product-id") Integer productId) {
        productService.deleteProduct(myUser.getId(), productId);
        return ResponseEntity.status(200).body(new ApiResponse("Product Deleted."));
    }

    // 3. Extra endpoint
    // Get all products by category
    @GetMapping("/get-products-by-category/category-name/{category-name}") //Ebtehal
    public ResponseEntity getProductsByCategory(@PathVariable(name = "category-name") String categoryName) {
        return ResponseEntity.status(200).body(productService.getProductsByCategory(categoryName));
    }

    @GetMapping("/get-product-open-deals/{product-id}") //Waleed
    public ResponseEntity getProductOpenDeals(@PathVariable(name = "product-id") Integer productId) {
        return ResponseEntity.status(200).body(productService.viewProductOpenDeals(productId));
    }

    @GetMapping("/low-stock/{stock-limit}")  //Ebtehal
    public ResponseEntity getLowStock(@PathVariable(name = "stock-limit") Integer stockLimit) {
        return ResponseEntity.status(200).body(productService.getLowStockProducts(stockLimit));
    }

    @GetMapping("/get-products-by-price-range/{min}/{max}")  //Ebtehal
    public ResponseEntity getProductsByPriceRange(@PathVariable Double min, @PathVariable Double max) {
        return ResponseEntity.status(200).body(productService.getProductsByPriceRange(min, max));
    }
}