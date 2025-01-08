package com.example.dealify.InDTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Check;

@Getter
@Setter
public class CustomerInDTO { //Ebtehal
    private Integer Id;

    @NotEmpty(message = "Full name cannot be null")
    @Size(min = 2, max = 20, message = "Full name can not be less than 2 and more than 20 characters")
    private String fullName;

    @NotEmpty(message = "Username cannot be null")
    @Size(min = 3, max = 10, message = "Username can not be less than 3 and  more than 10 characters")
    private String username;

    @NotEmpty(message = "User Email cannot be null")
    @Email(message = "you should enter Valid email")
    @Size(max = 30,message = "email character can not be more than 30")
    private String email;

    @NotEmpty(message = "Password can't be empty.")
    @Size(min = 8, max = 20, message = "Password length must be between 8-20 characters.")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,20}$", message = "Password must contain at least one uppercase letter, one lowercase letter, one digit, and one special character.")
    @Check(constraints = "length(password) >= 8")
    private String password;

    @NotEmpty(message = "Phone Number can not be null")
    @Pattern(regexp = "^05\\d{8}$", message = "Phone number must start with '05' and be followed by 8 digits")
    private String phoneNumber;
}