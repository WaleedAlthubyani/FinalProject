package com.example.dealify.InDTO;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
//Ebtehal
public class CustomerProfileInDTO {

    private Integer id;

    @NotEmpty(message = "User Name cannot be null")
    @Size(min = 2, max = 20, message = " Name can not be less than 2 and more than 20 characters")
    private String name;


    @NotEmpty(message = " city can not be null")
    @Size(min = 3, max = 10, message = " city name can not be less than 3 or more than 10")
    private String city;


    @NotEmpty(message = "street can not be null")
    @Size(min = 3, max = 10, message = "Street name can not be less than 3 or more than 10")
    private String street;

    @NotEmpty(message = "street can not be null")
    @Size(min = 3, max = 10, message = "Street name can not be less than 3 or more than 10")
    private String district;
}