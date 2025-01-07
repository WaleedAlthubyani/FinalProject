package com.example.dealify.InDTO;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
//Ebtehal
public class VendorReviewInDTO {

    @NotNull(message = "Service rating is required.")
    @Min(value = 1, message = "Service rating must be at least 1.")
    @Max(value = 5, message = "Service rating must be no more than 5.")
    private Integer serviceRating;

    @NotNull(message = "Product quality rating is required.")
    @Min(value = 1, message = "Product quality rating must be at least 1.")
    @Max(value = 5, message = "Product quality rating must be no more than 5.")
    private Integer productQualityRating;


    @NotNull(message = "Delivery Rating is required.")
    @Min(value = 1, message = "Delivery Rating must be at least 1.")
    @Max(value = 5, message = "Delivery Rating must be no more than 5.")
    private Integer deliveryRating;

    @NotNull(message = "Delivery Rating is required.")
    @Min(value = 1, message = "ReturnPolicyRating  must be at least 1.")
    @Max(value = 5, message = "ReturnPolicyRating  must be no more than 5.")
    private Integer returnPolicyRating;


    @Size(max = 255, message = "Comment can't exceed 255 characters.")
    private String comment;

    private LocalDate createdAt;


}

