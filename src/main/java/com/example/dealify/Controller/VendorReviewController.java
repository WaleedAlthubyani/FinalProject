package com.example.dealify.Controller;

import com.example.dealify.Api.ApiResponse;
import com.example.dealify.InDTO.VendorReviewInDTO;
import com.example.dealify.Model.MyUser;
import com.example.dealify.Service.VendorReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/vendor-review")
@RequiredArgsConstructor
public class VendorReviewController { //Ebtehal
    private final VendorReviewService vendorReviewService;


    @PostMapping("/add-review/{product-id}")
    public ResponseEntity addReview(@AuthenticationPrincipal MyUser myUser, @PathVariable(name = "product-id") Integer productId, @RequestBody @Valid VendorReviewInDTO vendorReviewInDTO) {
        vendorReviewService.addVendorReview(myUser.getId(), productId, vendorReviewInDTO);
        return ResponseEntity.status(200).body(new ApiResponse("Review added successfully"));
    }

    @PutMapping("/update-review/{review-id}")
    public ResponseEntity updateVendorReview(@AuthenticationPrincipal MyUser myUser, @PathVariable(name = "review-id") Integer reviewId, @RequestBody @Valid VendorReviewInDTO vendorReviewInDTO) {
        vendorReviewService.updateVendorReview(myUser.getId(), reviewId, vendorReviewInDTO);
        return ResponseEntity.status(200).body(new ApiResponse("Review updated successfully"));
    }

    @DeleteMapping("/delete-review/{review-id}")
    public ResponseEntity deleteVendorReview(@AuthenticationPrincipal MyUser myUser, @PathVariable(name = "review-id") Integer vendorReviewId) {
        vendorReviewService.deleteVendorReview(myUser.getId(), vendorReviewId);
        return ResponseEntity.status(200).body(new ApiResponse("Review deleted successfully"));
    }
}