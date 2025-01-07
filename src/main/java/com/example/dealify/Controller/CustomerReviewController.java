package com.example.dealify.Controller;

import com.example.dealify.Service.CustomerReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/customer-review")
@RequiredArgsConstructor
public class CustomerReviewController { //Renad

    // 1. Declare a dependency for CustomerReviewService using Dependency Injection
    private final CustomerReviewService customerReviewService;

    // 2. CRUD
    // 2.1 Get
    @GetMapping("/get-All-customers-reviews")
    public ResponseEntity getAllCustomersReviews() {
        return ResponseEntity.status(200).body(customerReviewService.getAllCustomerReviews());
    }
}