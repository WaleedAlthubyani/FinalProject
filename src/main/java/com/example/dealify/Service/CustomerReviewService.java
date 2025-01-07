package com.example.dealify.Service;

import com.example.dealify.Api.ApiException;
import com.example.dealify.InDTO.CustomerReviewInDTO;
import com.example.dealify.Model.*;
import com.example.dealify.OutDTO.CustomerReviewOutDTO;
import com.example.dealify.Repository.CustomerReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerReviewService {

    // 1. Declare a dependency for CustomerReviewRepository using Dependency Injection
    private final CustomerReviewRepository customerReviewRepository;


    // 2. CRUD
    // 2.1 Get
    public List<CustomerReviewOutDTO> getAllCustomerReviews() { //Renad
        List<CustomerReview> customerReviews = customerReviewRepository.findAll();
        List<CustomerReviewOutDTO> customerReviewOutDTOS = new ArrayList<>();
        for (CustomerReview customerReview : customerReviews) {
            CustomerReviewOutDTO customerReviewOutDTO = new CustomerReviewOutDTO(customerReview.getSender().getName(), customerReview.getRating(), customerReview.getComment(), customerReview.getCreatedAt());
            customerReviewOutDTOS.add(customerReviewOutDTO);
        }
        return customerReviewOutDTOS;
    }

    // 2.2 Post
    public void reviewACustomer(MyUser sender, CustomerProfile receiver, CustomerReviewInDTO customerReviewInDTO) {//Waleed
        if (sender == null) throw new ApiException("Sender Not Found.");
        if (receiver == null) throw new ApiException("Receiver Not Found.");

        CustomerReview customerReview = new CustomerReview();
        customerReview.setCustomer(receiver);
        customerReview.setSender(sender);
        customerReview.setRating(customerReviewInDTO.getRating());
        customerReview.setComment(customerReviewInDTO.getComment());
        customerReview.setCreatedAt(LocalDate.now());

        customerReviewRepository.save(customerReview);
    }

    // 2.3 Update
    public void updateCustomerReview(MyUser myUser, Integer customerReviewId, CustomerReviewInDTO customerReviewInDTO) {//Waleed
        CustomerReview customerReview = customerReviewRepository.findCustomerReviewById(customerReviewId);
        if (customerReview == null) throw new ApiException("Customer review not found");

        if (!customerReview.getSender().equals(myUser)) {
            throw new ApiException("Can't update someone else's review");
        }

        customerReview.setRating(customerReviewInDTO.getRating());
        customerReview.setComment(customerReviewInDTO.getComment());
        customerReview.setUpdatedAt(LocalDate.now());

        customerReviewRepository.save(customerReview);
    }

    // 2.4 Delete
    public void deleteCustomerReview(MyUser myUser, Integer id) { //Renad
        CustomerReview oldCustomerReview = customerReviewRepository.findCustomerReviewById(id);
        if (oldCustomerReview == null) {
            throw new ApiException("Customer Review Not Found.");
        }

        if (!oldCustomerReview.getSender().equals(myUser))
            throw new ApiException("You can't delete someone else's review");

        customerReviewRepository.delete(oldCustomerReview);
    }

    // 3. Extra endpoints
    // 3.1
    public List<CustomerReviewOutDTO> getCustomerReviews(CustomerProfile customerProfile) { //Waleed
        List<CustomerReview> customerReviews = customerReviewRepository.findCustomerReviewsByCustomer(customerProfile);
        if (customerReviews == null) throw new ApiException("Reviews not found");
        return convertCustomerReviewsToDTO(customerReviews);
    }

    // Helper method
    public List<CustomerReviewOutDTO> convertCustomerReviewsToDTO(Collection<CustomerReview> customerReviews) { //Waleed
        List<CustomerReviewOutDTO> customerReviewOutDTOS = new ArrayList<>();

        for (CustomerReview cr : customerReviews) {
            customerReviewOutDTOS.add(new CustomerReviewOutDTO(cr.getSender().getName(), cr.getRating(), cr.getComment(), cr.getCreatedAt()));
        }

        return customerReviewOutDTOS;
    }
}