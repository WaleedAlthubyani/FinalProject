package com.example.dealify.Controller;

import com.example.dealify.Service.BlackListPardonRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/blacklist-pardon-request")
@RequiredArgsConstructor
public class BlacklistPardonRequestController {

    private final BlackListPardonRequestService blackListPardonRequestService;

    @GetMapping("/get-all-blacklist-pardon-requests")//Waleed
    public ResponseEntity getAllBlacklistPardonRequests(){
        return ResponseEntity.status(200).body(blackListPardonRequestService.getAllBlacklistPardonRequests());
    }
}
