package com.example.dealify.Controller;

import com.example.dealify.Api.ApiResponse;
import com.example.dealify.Service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/image")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @GetMapping("/get-all-images")//Waleed
    public ResponseEntity getAllImages(){
        return ResponseEntity.status(200).body(imageService.getAllImages());
    }

    @DeleteMapping("/delete-image/{image-id}")//Waleed
    public ResponseEntity deleteImage(@PathVariable(name = "image-id") Integer imageId){
        imageService.deleteImage(imageId);
        return ResponseEntity.status(200).body(new ApiResponse("Image deleted successfully"));
    }
}
