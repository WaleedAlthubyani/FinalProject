package com.example.dealify.Repository;

import com.example.dealify.Model.CustomerProfile;
import com.example.dealify.Model.CustomerReview;
import com.example.dealify.Model.MyUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

//Renad
@Repository
public interface CustomerReviewRepository extends JpaRepository<CustomerReview, Integer> {
    CustomerReview findCustomerReviewById(Integer id);

    CustomerReview findCustomerReviewByCustomerAndSender(CustomerProfile customer, MyUser sender);

    List<CustomerReview> findCustomerReviewsByCustomer(CustomerProfile customerProfile);
}