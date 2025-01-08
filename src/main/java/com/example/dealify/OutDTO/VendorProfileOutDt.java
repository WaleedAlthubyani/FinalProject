package com.example.dealify.OutDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
//for customer to get vendor by city
public class VendorProfileOutDt { //ebtehal
    private String fullName;
    private String phoneNumber;

    public VendorProfileOutDt(){

    }
}