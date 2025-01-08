package com.example.dealify.Controller;

import com.example.dealify.Api.ApiResponse;
import com.example.dealify.Service.ProductReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/product-review")
@RequiredArgsConstructor
public class ProductReviewController {

    private final ProductReviewService productReviewService;

    @GetMapping("/get-products-reviews")//Waleed
    public ResponseEntity getProductsReviews(){
        return ResponseEntity.status(200).body(productReviewService.getProductReviews());
    }

    @DeleteMapping("/delete-a-product-review/{product-review-id}")//Waleed
    public ResponseEntity deleteAProductReview(@PathVariable(name = "product-review-id") Integer productReviewId){
        productReviewService.deleteAProductReview(productReviewId);
        return ResponseEntity.status(200).body(new ApiResponse("Review deleted successfully"));
    }
}
