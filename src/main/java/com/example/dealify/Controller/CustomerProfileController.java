package com.example.dealify.Controller;


import com.example.dealify.Api.ApiResponse;
import com.example.dealify.InDTO.CustomerProfileInDTO;
import com.example.dealify.Model.MyUser;
import com.example.dealify.Service.CustomerProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/customer-profile")
@RequiredArgsConstructor
public class CustomerProfileController { //Ebtehal

    private final CustomerProfileService customerProfileService;

    @PutMapping("/update/customer")
    public ResponseEntity updateCustomerProfile(@AuthenticationPrincipal MyUser myUser, @RequestBody @Valid CustomerProfileInDTO customerProfileInDTO) {
        customerProfileService.updateCustomerProfile(myUser.getId(), customerProfileInDTO);
        return ResponseEntity.status(200).body(new ApiResponse("Customer Profile with ID: " + myUser.getId() + " has been updated successfully."));
    }
}