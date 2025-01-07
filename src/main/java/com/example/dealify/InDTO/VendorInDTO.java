package com.example.dealify.InDTO;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Check;

@Getter
@Setter
public class VendorInDTO {//Ebtehal
    private Integer Id;

    @NotEmpty(message = "User Name cannot be null")
    @Size(min = 2, max = 20, message = " Name can not be less than 2 and more than 20 characters")
    private String name;

    @NotEmpty(message = "Username cannot be empty")
    @Size(min = 3, max = 10, message = "Username can not be less than 3 and  more than 10 characters")
    private String username;

    @NotEmpty(message = "User Email cannot be null")
    @Email(message = "Invalid email format")
    @Size(max = 30, message = "Email character can not be more than 30")
    private String email;

    @NotEmpty(message = "Password can't be empty.")
    @Size(min = 8, max = 20, message = "Password length must be between 8-20 characters.")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,20}$", message = "Password must contain at least one uppercase letter, one lowercase letter, one digit, and one special character.")
    @Check(constraints = "length(password) >= 8")
    private String password;

    @NotEmpty(message = "Phone Number can not be null")
    @Pattern(regexp = "^05\\d{8}$", message = "Phone number must start with '05' and be followed by 8 digits")
    private String phoneNumber;

    @NotEmpty(message = "Commercial registration is required")
    @Pattern(regexp = "^[0-9]{10}$", message = "Commercial registration must be a 10-digit number")
    private String commercialRegistration;
}