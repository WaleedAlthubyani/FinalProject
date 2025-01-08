package com.example.dealify.Controller;

import com.example.dealify.Service.BlackListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/blacklist")
@RequiredArgsConstructor
public class BlacklistController {

    private final BlackListService blackListService;

    @GetMapping("/get-all-blacklists")//Waleed
    public ResponseEntity getAllBlacklists(){
        return ResponseEntity.status(200).body(blackListService.getAllBlacklists());
    }
}
