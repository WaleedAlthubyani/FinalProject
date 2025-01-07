package com.example.dealify.OutDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor

public class ReturnRequestOutDTO { //Renad
    private String reason;

    private String response;

    // Pending - Approved - Rejected
    private String status;

    private LocalDateTime RequestDate;

    private String productBrand;

    private String productName;

    private Double productPrice;

    private String vendorName;
}