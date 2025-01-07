package com.example.dealify.Controller;

import com.example.dealify.Model.MyUser;
import com.example.dealify.Service.ReturnRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/return-request")
@RequiredArgsConstructor
public class ReturnRequestController { //Renad
    private final ReturnRequestService returnRequestService;


    @GetMapping("/get-all-return-requests")
    public ResponseEntity getAllReturnRequests(@AuthenticationPrincipal MyUser myUser) {
        return ResponseEntity.status(200).body(returnRequestService.getAllReturnRequests(myUser.getId()));
    }
}