package com.example.dealify.Controller;

import com.example.dealify.Service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/favorite")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;

    @GetMapping("/get-all-favorites")
    public ResponseEntity getAllFavorites(){
        return ResponseEntity.status(200).body(favoriteService.getAllFavorites());
    }
}
