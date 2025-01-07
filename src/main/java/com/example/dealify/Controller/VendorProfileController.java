package com.example.dealify.Controller;

import com.example.dealify.Api.ApiResponse;
import com.example.dealify.InDTO.VendorProfileInDTO;
import com.example.dealify.Model.MyUser;
import com.example.dealify.Service.VendorProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/vendor-profile")
@RequiredArgsConstructor
public class VendorProfileController { //Ebtehal

    private final VendorProfileService vendorProfileService;

    @PutMapping("/update-vendor-profile")
    public ResponseEntity updateVendorProfile(@AuthenticationPrincipal MyUser myUser, @RequestBody @Valid VendorProfileInDTO vendorProfileInDto) {
        vendorProfileService.updateVendorProfile(myUser.getId(), vendorProfileInDto);
        return ResponseEntity.status(200).body(new ApiResponse("Vendor profile updated successfully"));
    }

    @GetMapping("/get-vendor-reviews/vendor-id/{vendor-id}")
    public ResponseEntity getVendorReviews(@PathVariable(name = "vendor-id") Integer vendorId) {
        return ResponseEntity.status(200).body(vendorProfileService.getVendorReviews(vendorId));
    }

    @GetMapping("/get-vendors-by-city/{city}")
    public ResponseEntity getVendorByCity(@PathVariable String city) {
        return ResponseEntity.status(200).body(vendorProfileService.getVendorByCity(city));
    }
}