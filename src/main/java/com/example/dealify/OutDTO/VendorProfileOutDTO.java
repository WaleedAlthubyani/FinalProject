package com.example.dealify.OutDTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class VendorProfileOutDTO { //Ebtehal
    private String name;
    private String companyName;
    private String phoneNumber;
    private String city;
    private String fundamentalFile;
    private List<VendorReviewOutDTO> vendorReviews;
    private List<ProductOutDTO> products;
}