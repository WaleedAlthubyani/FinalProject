package com.example.dealify.Controller;

import com.example.dealify.Api.ApiResponse;
import com.example.dealify.InDTO.*;
import com.example.dealify.Model.MyUser;
import com.example.dealify.Service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/customer")
@RestController
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    @PostMapping("/register") //Ebtehal
    public ResponseEntity register(@RequestBody @Valid CustomerInDTO customerInDTO) {
        customerService.register(customerInDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("Customer register successful"));
    }

    @PutMapping("/update-customer") //Ebtehal
    public ResponseEntity updateCustomer(@AuthenticationPrincipal MyUser myUser, @RequestBody @Valid CustomerInDTO customerInDTO) {
        customerService.updateCustomer(myUser.getId(), customerInDTO);
        return ResponseEntity.status(200).body(new ApiResponse("Customer with ID: " + myUser.getId() + " has been updated successfully"));
    }

    @DeleteMapping("/delete-customer") //Ebtehal
    public ResponseEntity deleteCustomer(@AuthenticationPrincipal MyUser myUser) {
        customerService.deleteMyAccount(myUser.getId());
        return ResponseEntity.status(200).body(new ApiResponse("Customer deleted successful"));
    }

    @GetMapping("/get-customer-by-id/user-id/{id}") //Ebtehal
    public ResponseEntity getCustomerById(@PathVariable Integer id) {
        return ResponseEntity.status(200).body(customerService.getCustomerById(id));
    }

    @PostMapping("/create-a-deal/product-id/{product-id}") //Waleed
    public ResponseEntity createADeal(@AuthenticationPrincipal MyUser myUser,@PathVariable(name = "product-id") Integer productId, @RequestBody @Valid DealCreationInDTO dealCreationInDTO) {
        customerService.createADeal(myUser.getId(), productId, dealCreationInDTO);
        return ResponseEntity.status(201).body(new ApiResponse("Deal created successfully"));
    }

    @GetMapping("/get-customer-open-deals") //Waleed
    public ResponseEntity viewCustomerOpenedDeals(@AuthenticationPrincipal MyUser myUser) {
        return ResponseEntity.status(200).body(customerService.viewCustomerOpenedDeals(myUser.getId()));
    }

    @GetMapping("/get-customer-completed-deals") //Waleed
    public ResponseEntity viewCustomerCompletedDeals(@AuthenticationPrincipal MyUser myUser) {
        return ResponseEntity.status(200).body(customerService.viewCustomerCompletedDeals(myUser.getId()));
    }

    @GetMapping("/get-deals-by-product-category/category-name/{category-name}") //Renad
    public ResponseEntity viewDealsByProductCategory(@PathVariable(name = "category-name") String categoryName) {
        return ResponseEntity.status(200).body(customerService.viewDealsByProductCategory(categoryName));
    }

    @PostMapping("/join-a-deal/deal-id/{deal-id}") //Waleed
    public ResponseEntity joinADeal(@AuthenticationPrincipal MyUser myUser, @PathVariable(name = "deal-id") Integer dealId, @RequestBody @Valid DealJoinInDTO dealJoinInDTO) {
        customerService.joinADeal(myUser.getId(), dealId, dealJoinInDTO);
        return ResponseEntity.status(201).body(new ApiResponse("Deal Joined successfully"));
    }

    @PutMapping("/update-quantity-to-buy/customer-deal-id/{customer-deal-id}") //Waleed
    public ResponseEntity updateQuantityToBuy(@AuthenticationPrincipal MyUser myUser, @PathVariable(name = "customer-deal-id") Integer customerDealId, @RequestBody @Valid DealJoinInDTO dealJoinInDTO) {
        customerService.updateQuantity(myUser.getId(), customerDealId, dealJoinInDTO);
        return ResponseEntity.status(200).body(new ApiResponse("Quantity updated successfully"));
    }

    @DeleteMapping("/leave-a-deal/customer-deal-id/{customer-deal-id}") //Waleed
    public ResponseEntity leaveADeal(@AuthenticationPrincipal MyUser myUser, @PathVariable(name = "customer-deal-id") Integer customerDealId) {
        customerService.leaveADeal(myUser.getId(), customerDealId);
        return ResponseEntity.status(200).body(new ApiResponse("Left deal successfully"));
    }

    @GetMapping("/get-customer-favorites") //Waleed
    public ResponseEntity getCustomerFavorites(@AuthenticationPrincipal MyUser myUser) {
        return ResponseEntity.status(200).body(customerService.getCustomerFavorites(myUser.getId()));
    }

    @PostMapping("/add-product-to-favorite/product-id/{product-id}") //Waleed
    public ResponseEntity addProductToFavorite(@AuthenticationPrincipal MyUser myUser, @PathVariable(name = "product-id") Integer productId) {
        customerService.addProductToFavorite(myUser.getId(), productId);
        return ResponseEntity.status(201).body(new ApiResponse("Product added to favorites successfully"));
    }

    @DeleteMapping("/remove-product-from-favorite/product-id/{product-id}") //Waleed
    public ResponseEntity removeProductFromFavorite(@AuthenticationPrincipal MyUser myUser, @PathVariable(name = "product-id") Integer productId) {
        customerService.removeProductFromFavorite(myUser.getId(), productId);
        return ResponseEntity.status(200).body(new ApiResponse("Product removed from favorites successfully"));
    }

    @GetMapping("/get-customer-reviews/customer-id/{customer-id}") //Waleed
    public ResponseEntity getCustomerReviews(@PathVariable(name = "customer-id") Integer customerId) {
        return ResponseEntity.status(200).body(customerService.getCustomerReviews(customerId));
    }

    @PostMapping("/invite-to-deal/deal-id/{deal-id}/invitee-customer-id/{invitee-customer-id}") //Ebtehal
    public ResponseEntity inviteToDeal(@AuthenticationPrincipal MyUser myUser, @PathVariable(name = "deal-id") Integer dealId, @PathVariable(name = "invitee-customer-id") Integer inviteeCustomerId) {
        customerService.inviteToDeal(myUser.getId(), dealId, inviteeCustomerId);
        return ResponseEntity.status(200).body(new ApiResponse("Invitation sent successfully."));
    }

    @PostMapping("/review-a-customer/reviewed-id/{reviewed-id}")//Waleed
    public ResponseEntity reviewACustomer(@AuthenticationPrincipal MyUser myUser, @PathVariable(name = "reviewed-id") Integer reviewedId, @RequestBody @Valid CustomerReviewInDTO customerReview) {
        customerService.reviewACustomer(myUser.getId(), reviewedId, customerReview);
        return ResponseEntity.status(201).body(new ApiResponse("Review posted successfully"));
    }

    @PutMapping("/update-a-customer-review/review-id/{review-id}")//Waleed
    public ResponseEntity updateACustomerReview(@AuthenticationPrincipal MyUser myUser, @PathVariable(name = "review-id") Integer ReviewId, CustomerReviewInDTO customerReviewInDTO) {
        customerService.updateACustomerReview(myUser.getId(), ReviewId, customerReviewInDTO);
        return ResponseEntity.status(200).body(new ApiResponse("Review updated successfully"));
    }

    @DeleteMapping("/delete-a-customer-review/review-id/{review-id}") //Renad
    public ResponseEntity deleteACustomerReview(@AuthenticationPrincipal MyUser myUser, @PathVariable(name = "review-id") Integer ReviewId) {
        customerService.deleteACustomerReview(myUser.getId(), ReviewId);
        return ResponseEntity.status(200).body(new ApiResponse("Customer Review Deleted."));
    }

    @GetMapping("/get-product-by-id/product-id/{product-id}")//Renad
    public ResponseEntity viewProductById(@PathVariable(name = "product-id") Integer productId) {
        return ResponseEntity.status(200).body(customerService.viewProductById(productId));
    }

    @GetMapping("/get-product-images/product-id/{product-id}")//Renad
    public ResponseEntity viewProductImages(@PathVariable(name = "product-id") Integer productId) {
        return ResponseEntity.status(200).body(customerService.viewProductImages(productId));
    }

    @GetMapping("/get-products-by-vendor/vendor-id/{vendor-id}")//Renad
    public ResponseEntity viewProductsByVendor(@PathVariable(name = "vendor-id") Integer vendorId) {
        return ResponseEntity.status(200).body(customerService.viewProductsByVendor(vendorId));
    }

    @GetMapping("/get-product-reviews/product-id/{product-id}")//Waleed
    public ResponseEntity viewProductReviews(@PathVariable(name = "product-id") Integer productId) {
        return ResponseEntity.status(200).body(customerService.viewProductReviews(productId));
    }

    @PostMapping("/create-return-request/product-id/{productId}/deal-id/{dealId}")//Renad
    public ResponseEntity creatReturnRequest(@AuthenticationPrincipal MyUser myUser, @PathVariable Integer productId, @PathVariable Integer dealId, @RequestBody @Valid ReturnRequestCustomerInDTO returnRequestCustomerInDTO) {
        customerService.creatReturnRequest(myUser.getId(), productId, dealId, returnRequestCustomerInDTO);
        return ResponseEntity.status(200).body(new ApiResponse("Return request created successfully"));
    }

    @GetMapping("/get-customer-return-requests")//Renad
    public ResponseEntity viewCustomerReturnRequests(@AuthenticationPrincipal MyUser myUser) {
        return ResponseEntity.status(200).body(customerService.viewCustomerReturnRequests(myUser.getId()));
    }

    @PostMapping("/review-a-product/product-id/{product-id}")//Waleed
    public ResponseEntity reviewAProduct(@AuthenticationPrincipal MyUser myUser, @PathVariable(name = "product-id") Integer productId, @RequestBody @Valid ProductReviewInDTO productReviewInDTO) {
        customerService.reviewAProduct(myUser.getId(), productId, productReviewInDTO);
        return ResponseEntity.status(201).body(new ApiResponse("Review added successfully"));
    }

    @PutMapping("/update-a-product-review/product-review-id/{product-review-id}")//Waleed
    public ResponseEntity updateAProductReview(@AuthenticationPrincipal MyUser myUser, @PathVariable(name = "product-review-id") Integer productReviewId, @RequestBody @Valid ProductReviewInDTO productReviewInDTO) {
        customerService.updateAProductReview(myUser.getId(), productReviewId, productReviewInDTO);
        return ResponseEntity.status(200).body(new ApiResponse("Review updated successfully"));
    }

    //Admin
    @GetMapping("/get-all-users") //Ebtehal
    public ResponseEntity getAllUser(@AuthenticationPrincipal MyUser myUser) {//Admin
        return ResponseEntity.status(200).body(customerService.getAllCustomers(myUser.getId()));
    }

    @GetMapping("/get-customer-pardon-requests") //Waleed
    public ResponseEntity getCustomerBlackListPardonRequests(@AuthenticationPrincipal MyUser myUser) {
        return ResponseEntity.status(200).body(customerService.getCustomerBlackListPardonRequests(myUser.getId()));
    }

    @PostMapping("/request-a-pardon/vendor-id/{vendor-id}") //Waleed
    public ResponseEntity requestAPardon(@AuthenticationPrincipal MyUser myUser, @PathVariable(name = "vendor-id") Integer vendorId, @RequestBody @Valid BlackListPardonRequestCustomerInDTO blackListPardonRequestCustomerInDTO) {
        customerService.requestAPardon(myUser.getId(), vendorId, blackListPardonRequestCustomerInDTO);
        return ResponseEntity.status(201).body(new ApiResponse("Pardon requested successfully"));
    }

    @PutMapping("/update-a-pardon-request/pardon-request-id/{pardon-request-id}") //Waleed
    public ResponseEntity updateAPardonRequest(@AuthenticationPrincipal MyUser myUser, @PathVariable(name = "pardon-request-id") Integer pardonId, @RequestBody @Valid BlackListPardonRequestCustomerInDTO blackListPardonRequestCustomerInDTO) {
        customerService.updateAPardonRequest(myUser.getId(), pardonId, blackListPardonRequestCustomerInDTO);
        return ResponseEntity.status(200).body(new ApiResponse("Pardon request updated successfully"));
    }

    @PutMapping("/pay/deal-id/{deal-id}")//Waleed
    public ResponseEntity pay(@AuthenticationPrincipal MyUser myUser, @PathVariable(name = "deal-id") Integer dealId) {
        customerService.pay(myUser.getId(), dealId);
        return ResponseEntity.status(200).body(new ApiResponse("Payment method registered successfully"));
    }

    @PutMapping("/pay-with-points/deal-id/{deal-id}") //Waleed
    public ResponseEntity payWithPoints(@AuthenticationPrincipal MyUser myUser, @PathVariable(name = "deal-id") Integer dealId) {
        customerService.payWithPoints(myUser.getId(), dealId);
        return ResponseEntity.status(200).body(new ApiResponse("Payment method registered successfully"));
    }

    @DeleteMapping("/decline-to-pay/deal-id/{deal-id}")//Waleed
    public ResponseEntity declineToPay(@AuthenticationPrincipal MyUser myUser, @PathVariable(name = "deal-id") Integer dealId) {
        customerService.declineToPay(myUser.getId(), dealId);
        return ResponseEntity.status(200).body(new ApiResponse("Rejection registered successfully"));
    }
}