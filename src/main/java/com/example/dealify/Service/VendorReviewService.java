package com.example.dealify.Service;

import com.example.dealify.Api.ApiException;
import com.example.dealify.InDTO.VendorReviewInDTO;
import com.example.dealify.Model.*;
import com.example.dealify.Repository.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor

//Ebtehal
public class VendorReviewService {
    private final VendorProfileRepository vendorProfileRepository;
    private final DealRepository dealRepository;
    private final ProductRepository productRepository;
    private final VendorReviewRepository vendorReviewRepository;
    private final CustomerRepository customerRepository;

    public List<VendorReview> getAllVendorReviews(){//Waleed
        return vendorReviewRepository.findAll();
    }

    public void deleteAVendorReview(Integer vendorReviewId){//Waleed
        VendorReview vendorReview=vendorReviewRepository.findVendorReviewById(vendorReviewId);
        if (vendorReview==null) throw new ApiException("Vendor review not found");

        vendorReviewRepository.delete(vendorReview);
    }

    public void addVendorReview(Integer customerId, Integer productId, VendorReviewInDTO vendorReviewInDTO) {
        Customer customer = customerRepository.findCustomerById(customerId);
        if (customer == null) {
            throw new ApiException("Customer not found");
        }

        Product product = productRepository.findProductById(productId);
        if (product == null) {
            throw new ApiException("Product not found");
        }

        VendorProfile vendorProfile = product.getInventory().getVendorProfile();
        if (vendorProfile == null) {
            throw new ApiException("Vendor not found");
        }

        // تحقق من أن العميل جزء من الصفقة المكتملة وأن المنتج يعود للبائع.
        List<Deal> completedDeals = dealRepository.findDealsByCustomerAndCompleted(customer.getCustomerProfile(), "Completed");
        boolean validDeal = false;
        for (Deal deal : completedDeals) {
            if (deal.getProduct().equals(product) && product.getInventory().getVendorProfile().equals(vendorProfile)) {
                validDeal = true;
                break;
            }
        }

        if (!validDeal) {
            throw new ApiException("The customer must be part of a completed deal to allow rating and the product must belong to the vendor being rated.");
        }

        VendorReview vendorReview = new VendorReview();
        vendorReview.setCustomer(customer.getCustomerProfile().getCustomer());
        vendorReview.setVendor(vendorProfile);
        vendorReview.setServiceRating(vendorReviewInDTO.getServiceRating());
        vendorReview.setDeliveryRating(vendorReviewInDTO.getDeliveryRating());
        vendorReview.setReturnPolicyRating(vendorReviewInDTO.getReturnPolicyRating());
        vendorReview.setProductQualityRating(vendorReviewInDTO.getProductQualityRating());
        vendorReview.setComment(vendorReviewInDTO.getComment());
        vendorReview.setCreatedAt(LocalDate.now());

        // لحساب اجمالي عدد التقييمات
        double overallRating = calculateOverallRating(vendorReview);
        vendorReview.setOverallRating(overallRating);

        // إضافة التقييم للبروفايل وتحديثه
        vendorProfile.getVendorReviews().add(vendorReview);
        vendorProfileRepository.save(vendorProfile);
        vendorReviewRepository.save(vendorReview);
    }


    // helper methode
    private double calculateOverallRating(VendorReview vendorReview) {
        return (vendorReview.getServiceRating().doubleValue() + vendorReview.getProductQualityRating().doubleValue()
                + vendorReview.getDeliveryRating().doubleValue() +
                vendorReview.getReturnPolicyRating().doubleValue()) / 4.0;
    }


    public void updateVendorReview(Integer customerId,Integer reviewId, VendorReviewInDTO vendorReviewInDTO) {
        Customer customer = customerRepository.findCustomerById(customerId);
        if (customer == null) {
            throw new ApiException("Customer not found");
        }

        VendorReview vendorReview = vendorReviewRepository.findVendorReviewById(reviewId);
        if (vendorReview == null) {
            throw new ApiException("Review not found");
        }
        vendorReview.setServiceRating(vendorReviewInDTO.getServiceRating());
        vendorReview.setDeliveryRating(vendorReviewInDTO.getDeliveryRating());
        vendorReview.setReturnPolicyRating(vendorReviewInDTO.getReturnPolicyRating());
        vendorReview.setProductQualityRating(vendorReviewInDTO.getProductQualityRating());
        vendorReview.setComment(vendorReviewInDTO.getComment());
        vendorReview.setUpdatedAt(LocalDate.now());
        double overallRating = calculateOverallRating(vendorReview);
        vendorReview.setOverallRating(overallRating);
        vendorReviewRepository.save(vendorReview);

    }

    public void deleteVendorReview(Integer customerId,Integer reviewId) {

        Customer customer = customerRepository.findCustomerById(customerId);
        if (customer == null) {
            throw new ApiException("Customer not found");
        }
        VendorReview vendorReview = vendorReviewRepository.findVendorReviewById(reviewId);
        if (vendorReview==null) {
            throw new ApiException("Review not found");
        }
        VendorProfile vendorProfile = vendorReview.getVendor();
        if (vendorProfile == null) {
            throw new ApiException("Vendor profile not found for the review");
        }

        vendorProfile.getVendorReviews().remove(vendorReview);
        vendorProfileRepository.save(vendorProfile);
        vendorReviewRepository.delete(vendorReview);
    }
}