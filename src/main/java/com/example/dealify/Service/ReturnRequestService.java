package com.example.dealify.Service;

import com.example.dealify.Api.ApiException;
import com.example.dealify.InDTO.ReturnRequestCustomerInDTO;
import com.example.dealify.Model.*;
import com.example.dealify.OutDTO.ReturnRequestOutDTO;
import com.example.dealify.Repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequestMapping("/api/v1/returnRequest")
@RequiredArgsConstructor
public class ReturnRequestService { //Renad

    private final ReturnRequestRepository returnRequestRepository;
    private final DealRepository dealRepository;
    private final ProductRepository productRepository;
    private final CustomerProfileRepository customerProfileRepository;
    private final VendorProfileRepository vendorProfileRepository;
    private final AuthRepository authRepository;

    // 1. CRUD
    // 1.1 Get
    // For Admin
    public List<ReturnRequestOutDTO> getAllReturnRequests(Integer authId) {

        MyUser auth = authRepository.findMyUserById(authId);
        if (auth == null) {
            throw new ApiException("Admin was not found");
        }

        if (!auth.getRole().equalsIgnoreCase("Admin")) {
            throw new ApiException("You don't have the permission to access this endpoint");
        }
        List<ReturnRequest> returnRequests = returnRequestRepository.findAll();
        List<ReturnRequestOutDTO> returnRequestOutDTOS = new ArrayList<>();
        for (ReturnRequest returnRequest : returnRequests) {
            ReturnRequestOutDTO returnRequestOutDTO = new ReturnRequestOutDTO(returnRequest.getReason(),
                    returnRequest.getResponse(),
                    returnRequest.getStatus(),
                    returnRequest.getRequestDate(),
                    returnRequest.getProduct().getBrand(),
                    returnRequest.getProduct().getName(),
                    returnRequest.getProduct().getPrice(),
                    returnRequest.getVendorProfile().getVendor().getMyUser().getFullName());

            returnRequestOutDTOS.add(returnRequestOutDTO);
        }
        return returnRequestOutDTOS;
    }

    // 1.2 Post
    public void creatReturnRequest(Integer customerId, Integer productId, Integer dealId, ReturnRequestCustomerInDTO returnRequestCustomerInDTO) {
        // Validate the customer
        CustomerProfile customer = customerProfileRepository.findCustomerProfileById(customerId);
        if (customer == null) {
            throw new ApiException("Customer not found.");
        }

        // Validate the product
        Product product = productRepository.findProductById(productId);
        if (product == null) {
            throw new ApiException("Product not found.");
        }

        // Validate the deal
        Deal deal = dealRepository.findDealById(dealId);
        if (deal == null) {
            throw new ApiException("Deal not found.");
        }

        // Validate the vendor
        VendorProfile vendor = vendorProfileRepository.findVendorProfileById(product.getInventory().getVendorProfile().getId());
        if (vendor == null) {
            throw new ApiException("Vendor not found.");
        }

        // Validate if the customer has completed the deal
        List<Deal> completedDeals = dealRepository.findDealsByCustomerAndCompleted(customer, "Completed");

        boolean customerCompletedDeal = false;
        for (Deal completedDeal : completedDeals) {
            if (completedDeal.getProduct().getId().equals(productId)) {
                customerCompletedDeal = true;
                break;
            }
        }

        if (!customerCompletedDeal) {
            throw new ApiException("The customer did not complete this deal.");
        }

        // Create and populate the ReturnRequest entity
        ReturnRequest returnRequest = new ReturnRequest();
        returnRequest.setReason(returnRequestCustomerInDTO.getReason());
        returnRequest.setStatus("Pending");
        returnRequest.setRequestDate(LocalDateTime.now());
        returnRequest.setDeal(deal);
        returnRequest.setProduct(product);
        returnRequest.setCustomer(customer);
        returnRequest.setVendorProfile(vendor);

        // Save the return request
        returnRequestRepository.save(returnRequest);
    }

    // 3. Extra endpoints
    // 3.1 Get customer return requests
    public List<ReturnRequestOutDTO> getCustomerReturnRequests(Integer customerId) {
        // Validate the customer
        CustomerProfile customer = customerProfileRepository.findCustomerProfileById(customerId);

        if (customer == null) {
            throw new ApiException("Customer not found.");
        }

        List<ReturnRequest> customerReturnRequests = returnRequestRepository.findReturnRequestByCustomer(customer);
        List<ReturnRequestOutDTO> customerReturnRequestOutDTOS = new ArrayList<>();

        for (ReturnRequest customerReturnRequest : customerReturnRequests) {
            ReturnRequestOutDTO customerReturnRequestOutDTO = new ReturnRequestOutDTO(customerReturnRequest.getReason(),
                    customerReturnRequest.getResponse(),
                    customerReturnRequest.getStatus(),
                    customerReturnRequest.getRequestDate(),
                    customerReturnRequest.getProduct().getBrand(),
                    customerReturnRequest.getProduct().getName(),
                    customerReturnRequest.getProduct().getPrice(),
                    customerReturnRequest.getVendorProfile().getVendor().getMyUser().getFullName());

            customerReturnRequestOutDTOS.add(customerReturnRequestOutDTO);
        }
        return customerReturnRequestOutDTOS;
    }

    // 3.2 Accept return request
    public void acceptReturnRequest(Integer vendorId, Integer returnRequestId) { //Renad
        // Validate the vendor
        VendorProfile vendor = vendorProfileRepository.findVendorProfileById(vendorId);

        if (vendor == null) {
            throw new ApiException("Vendor not found.");
        }

        // Validate the return request
        ReturnRequest returnRequest = returnRequestRepository.findReturnRequestById(returnRequestId);
        if (returnRequest == null) {
            throw new ApiException("Return request not found.");
        }

        // Validate that this return request belong to this vendor
        if (!returnRequest.getVendorProfile().getId().equals(vendorId)) {
            throw new ApiException("Return request does not belong to this vendor.");
        }

        // Update the status of the return request to 'Accepted'
        returnRequest.setStatus("Accepted");

        // Save the updated return request back to the repository
        returnRequestRepository.save(returnRequest);
    }

    // 3.3 Reject return request
    public void rejectReturnRequest(Integer vendorId, Integer returnRequestId) { // Renad
        // Validate the vendor
        VendorProfile vendor = vendorProfileRepository.findVendorProfileById(vendorId);

        if (vendor == null) {
            throw new ApiException("Vendor not found.");
        }

        // Validate the return request
        ReturnRequest returnRequest = returnRequestRepository.findReturnRequestById(returnRequestId);
        if (returnRequest == null) {
            throw new ApiException("Return request not found.");
        }

        // Validate that this return request belong to this vendor
        if (!returnRequest.getVendorProfile().getId().equals(vendorId)) {
            throw new ApiException("Return request does not belong to this vendor.");
        }

        // Update the status of the return request to 'Rejected'
        returnRequest.setStatus("Rejected");

        // Save the updated return request back to the repository
        returnRequestRepository.save(returnRequest);
    }
}