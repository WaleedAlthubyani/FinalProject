package com.example.dealify.Repository;

import com.example.dealify.Model.VendorProfile;
import com.example.dealify.Model.VendorReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VendorReviewRepository extends JpaRepository<VendorReview, Integer> {//Waleed

    VendorReview findVendorReviewById(Integer id);

    List<VendorReview> findVendorReviewsByVendor(VendorProfile vendorProfile);
}