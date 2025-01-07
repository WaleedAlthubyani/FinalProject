package com.example.dealify.InDTO;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class VendorProfileInDTO {//Ebtehal
    private Integer id;

    @NotEmpty(message = "Company Name can Is required")
    @Size(min = 3, max = 20, message = "Company name can not be less than 3 or more than 20 ")
    private String companyName;

    @NotEmpty(message = "Fundamental file is required")
    @Pattern(regexp = "^[0-9]{10}$", message = "Fundamental file must be exactly 10 digits")
    private String fundamentalFile;

    @NotEmpty(message = " city can not be null")
    @Size(min = 3, max = 10, message = " city name can not be less than 3 or more than 10")
    private String city;
}