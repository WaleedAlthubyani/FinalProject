package com.example.dealify.OutDTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductReviewOutDTO {//Waleed

    private String customer;

    private Double overallRating;

    private Integer qualityRating;

    private Integer packageRating;

    private String comment;
}
