package com.example.dealify.Service;

import com.example.dealify.Api.ApiException;
import com.example.dealify.InDTO.VendorProfileInDTO;
import com.example.dealify.Model.*;
import com.example.dealify.OutDTO.VendorProfileOutDt;
import com.example.dealify.OutDTO.VendorReviewOutDTO;
import com.example.dealify.Repository.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor

public class VendorProfileService {  //Ebtehal
    private final VendorRepository vendorRepository;
    private final AuthRepository authRepository;
    private final VendorProfileRepository vendorProfileRepository;
    private final VendorReviewRepository vendorReviewRepository;

    public void updateVendorProfile(Integer vendorId, VendorProfileInDTO vendorProfileInDTO) {
        Vendor vendor = vendorRepository.findVendorById(vendorId);
        if (vendor == null) {
            throw new ApiException("Vendor Not Found.");
        }

        VendorProfile vendorProfile = vendorProfileRepository.findVendorProfileById(vendorId);

        vendorProfile.setCompanyName(vendorProfileInDTO.getCompanyName());
        vendorProfile.setCity(vendorProfileInDTO.getCity());

        vendorProfileRepository.save(vendorProfile);
    }

    // اشوف لتقييمات  على بائع محدد
    //for customer to see vendor
    public List<VendorReviewOutDTO> getVendorReviews(Integer vendorId) {// Ebtehal
        VendorProfile vendorProfile = vendorProfileRepository.findVendorProfileById(vendorId);
        if (vendorProfile == null) {
            throw new ApiException("Vendor not found");
        }
        List<VendorReviewOutDTO> reviews = new ArrayList<>();
        List<VendorReview> vendorReviews = vendorReviewRepository.findVendorReviewsByVendor(vendorProfile);

        for (VendorReview review : vendorReviews) {
            MyUser customer = authRepository.findMyUserById(review.getCustomer().getId());
            if (customer == null) {
                throw new ApiException("Customer not found for review");
            }

            reviews.add(new VendorReviewOutDTO(
                    review.getOverallRating(),
                    review.getServiceRating(),
                    review.getProductQualityRating(),
                    review.getDeliveryRating(),
                    review.getReturnPolicyRating(),
                    review.getComment(),
                    customer.getFullName()
            ));
        }
        return reviews;
    }

    //endPoint get vendor by city
    //Ebtehal
    public List<VendorProfileOutDt> getVendorByCity(String city) {
        List<VendorProfile> vendorProfiles = vendorProfileRepository.findVendorProfileByCity(city);
        if (vendorProfiles == null || vendorProfiles.isEmpty()) {
            throw new ApiException("There is no vendor in that city");
        }
        List<VendorProfileOutDt> vendorProfileOutDTOS = new ArrayList<>();
        for (VendorProfile vendorProfile : vendorProfiles) {
            VendorProfileOutDt vendorProfileOutDTO = new VendorProfileOutDt();
            vendorProfileOutDTO.setFullName(vendorProfile.getVendor().getMyUser().getFullName());
            vendorProfileOutDTO.setPhoneNumber(vendorProfile.getVendor().getMyUser().getPhoneNumber());
            vendorProfileOutDTOS.add(vendorProfileOutDTO);
        }
        return vendorProfileOutDTOS;
    }
}