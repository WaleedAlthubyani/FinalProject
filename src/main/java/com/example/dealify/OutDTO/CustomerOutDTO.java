package com.example.dealify.OutDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CustomerOutDTO { //Ebtehal
    private String username;
    private String fullName;
    private String phoneNumber;
    private String city;
    private String district;
    private String street;
}