package com.example.dealify.Controller;

import com.example.dealify.Api.ApiResponse;
import com.example.dealify.Service.DealService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/deal")
@RequiredArgsConstructor
public class DealController {

    private final DealService dealService;

    @GetMapping("/get-all-deals")//Waleed
    public ResponseEntity getAllDeals(){
        return ResponseEntity.status(200).body(dealService.getAllDeals());
    }

    @DeleteMapping("/delete-a-deal/{deal-id}")//Waleed
    public ResponseEntity deleteADeal(@PathVariable(name = "deal-id") Integer dealId){
        dealService.deleteADeal(dealId);
        return ResponseEntity.status(200).body(new ApiResponse("Deal deleted successfully"));
    }
}
