package com.example.dealify.Controller;

import com.example.dealify.Api.ApiResponse;
import com.example.dealify.InDTO.BlackListInDTO;
import com.example.dealify.InDTO.BlackListPardonRequestVendorInDTO;
import com.example.dealify.InDTO.ImageInDTO;
import com.example.dealify.InDTO.VendorInDTO;
import com.example.dealify.Model.Image;
import com.example.dealify.Model.MyUser;
import com.example.dealify.Service.VendorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/vendor")
@RequiredArgsConstructor

public class VendorController {

    private final VendorService vendorService;

    @PostMapping("/register") //Renad
    public ResponseEntity register(@RequestBody @Valid VendorInDTO vendorDto) {
        vendorService.register(vendorDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("Vendor registered successfully."));
    }

    @PutMapping("/update-vendor") //Renad
    public ResponseEntity updateVendor(@AuthenticationPrincipal MyUser myUser, @RequestBody @Valid VendorInDTO vendorInDto) {
        vendorService.updateVendor(myUser.getId(), vendorInDto);
        return ResponseEntity.status(200).body(new ApiResponse("Vendor with ID: " + myUser.getId() + " has been updated successfully."));
    }

    @DeleteMapping("/delete-vendor") //Renad
    public ResponseEntity deleteAccount(@AuthenticationPrincipal MyUser myUser) {
        vendorService.deleteMyAccount(myUser.getId());
        return ResponseEntity.status(200).body(new ApiResponse("Vendor deleted successfully."));
    }

    // Extra endpoints
    // 1. Activate vendor (admin)
    @PutMapping("/activate/vendor-id/{vendor-id}") //Renad
    public ResponseEntity activateVendor(@AuthenticationPrincipal MyUser myUser, @PathVariable(name = "vendor-id") Integer vendorId) {
        vendorService.activateVendor(myUser.getId(), vendorId);
        return ResponseEntity.status(200).body(new ApiResponse("Vendor with ID: " + vendorId + " has been activated successfully."));
    }

    @GetMapping("/get-vendor-open-deals/vendor-id/{vendor-id}") //Waleed
    public ResponseEntity viewVendorOpenDeals(@PathVariable(name = "vendor-id") Integer vendorId) {
        return ResponseEntity.status(200).body(vendorService.viewVendorsOpenDeals(vendorId));
    }

    @GetMapping("/get-vendor-profile/vendor-id/{vendor-id}")//Renad
    public ResponseEntity getVendorProfileById(@PathVariable(name = "vendor-id") Integer vendorId) {
        return ResponseEntity.status(200).body(vendorService.getVendorProfileById(vendorId));
    }

    @GetMapping("/get-my-inventory")//Renad
    public ResponseEntity getVendorInventory(@AuthenticationPrincipal MyUser myUser) {
        return ResponseEntity.status(200).body(vendorService.getVendorInventory(myUser.getId()));
    }

    @GetMapping("/get-vendor-return-requests") //Renad
    public ResponseEntity getVendorReturnRequests(@AuthenticationPrincipal MyUser myUser) {
        return ResponseEntity.status(200).body(vendorService.getVendorReturnRequests(myUser.getId()));
    }

    @GetMapping("/get-vendor-black-list")//Waleed
    public ResponseEntity getVendorBlackList(@AuthenticationPrincipal MyUser myUser) {
        return ResponseEntity.status(200).body(vendorService.getVendorBlackList(myUser.getId()));
    }

    @PostMapping("/add-customer-to-blacklist/customer-id/{customer-id}")//Waleed
    public ResponseEntity addCustomerToBlacklist(@AuthenticationPrincipal MyUser myUser, @PathVariable(name = "customer-id") Integer customerId, @RequestBody @Valid BlackListInDTO blackListInDTO) {
        vendorService.addCustomerToBlacklist(myUser.getId(), customerId, blackListInDTO);
        return ResponseEntity.status(201).body(new ApiResponse("Customer added to blacklist successfully"));
    }

    @DeleteMapping("/remove-customer-from-blacklist/{customer-id}")//Waleed
    public ResponseEntity removeCustomerFromBlackList(@AuthenticationPrincipal MyUser myUser, @PathVariable(name = "customer-id") Integer customerId) {
        vendorService.removeCustomerFromBlackList(myUser.getId(), customerId);
        return ResponseEntity.status(200).body(new ApiResponse("Customer removed from blacklist successfully"));
    }

    @GetMapping("/get-vendor-blacklist-pardon-request")//Waleed
    public ResponseEntity getVendorBlackListPardonRequests(@AuthenticationPrincipal MyUser myUser) {
        return ResponseEntity.status(200).body(vendorService.getVendorBlackListPardonRequests(myUser.getId()));
    }

    @PutMapping("/approve-pardon-request/{pardon-request-id}")//Waleed
    public ResponseEntity approvePardonRequest(@AuthenticationPrincipal MyUser myUser, @PathVariable(name = "pardon-request-id") Integer pardonRequestId, @RequestBody @Valid BlackListPardonRequestVendorInDTO blackListPardonRequestVendorInDTO) {
        vendorService.approvePardonRequest(myUser.getId(), pardonRequestId, blackListPardonRequestVendorInDTO);
        return ResponseEntity.status(200).body(new ApiResponse("Pardon request approved successfully"));
    }

    @PutMapping("/reject-pardon-request/{pardon-request-id}") //Waleed
    public ResponseEntity rejectPardonRequest(@AuthenticationPrincipal MyUser myUser, @PathVariable(name = "pardon-request-id") Integer pardonRequestId, @RequestBody @Valid BlackListPardonRequestVendorInDTO blackListPardonRequestVendorInDTO) {
        vendorService.rejectPardonRequest(myUser.getId(), pardonRequestId, blackListPardonRequestVendorInDTO);
        return ResponseEntity.status(200).body(new ApiResponse("Pardon request rejected successfully"));
    }

    @PutMapping("/update-product-images/product-id/{product-id}") //Renad
    public ResponseEntity updateProductImages(@AuthenticationPrincipal MyUser myUser, @PathVariable(name = "product-id") Integer productId, @RequestBody @Valid ImageInDTO imageInDTO) {
        vendorService.updateProductImages(myUser.getId(), productId, imageInDTO);
        return ResponseEntity.status(200).body(new ApiResponse("Product images has been updated successfully."));
    }

    @PutMapping("/accept-return-request/{return-request-id}") //Renad
    public ResponseEntity acceptReturnRequest(@AuthenticationPrincipal MyUser myUser, @PathVariable(name = "return-request-id") Integer returnRequestId) {
        vendorService.acceptReturnRequest(myUser.getId(), returnRequestId);
        return ResponseEntity.status(200).body(new ApiResponse("Accepted successfully"));
    }

    @PutMapping("/reject-return-request/{return-request-id}") //Renad
    public ResponseEntity rejectReturnRequest(@AuthenticationPrincipal MyUser myUser, @PathVariable(name = "return-request-id") Integer returnRequestId) {
        vendorService.rejectReturnRequest(myUser.getId(), returnRequestId);
        return ResponseEntity.status(200).body(new ApiResponse("Rejected successfully"));
    }
}