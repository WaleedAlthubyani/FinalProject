package com.example.dealify.Controller;

import com.example.dealify.Service.CustomerDealService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/customer-deal")
@RequiredArgsConstructor
public class CustomerDealController {

    private final CustomerDealService customerDealService;

    @GetMapping("/get-all-customer-deals")//Waleed
    public ResponseEntity getAllCustomerDeals(){
        return ResponseEntity.status(200).body(customerDealService.getAllCustomerDeals());
    }
}
