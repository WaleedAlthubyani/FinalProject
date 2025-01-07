package com.example.dealify.Controller;

import com.example.dealify.Model.MyUser;
import com.example.dealify.Service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/inventory")
@RequiredArgsConstructor
public class InventoryController { //Renad

    // 1. Declare a dependency for InventoryService using Dependency Injection
    private final InventoryService inventoryService;

    // 2. CRUD
    // 2.1 Get
    @GetMapping("/get-all-inventories")
    public ResponseEntity getAllInventories(@AuthenticationPrincipal MyUser myUser) {
        return ResponseEntity.status(200).body(inventoryService.getAllInventories(myUser.getId()));
    }
}