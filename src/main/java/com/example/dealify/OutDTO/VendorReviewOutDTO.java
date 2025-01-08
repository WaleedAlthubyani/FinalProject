package com.example.dealify.OutDTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VendorReviewOutDTO { //Ebtehal
    private Double overallRating;
    private Integer serviceRating;
    private Integer productQualityRating;
    private Integer deliveryRating;
    private Integer returnPolicyRating;
    private String comment;
    private String reviewerName;
}