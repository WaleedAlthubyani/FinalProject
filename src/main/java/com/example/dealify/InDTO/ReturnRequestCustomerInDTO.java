package com.example.dealify.InDTO;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ReturnRequestCustomerInDTO { // Renad
    @NotEmpty(message = "Reason is required.")
    @Size(max = 300, message = "Reason can't exceed 300 characters.")
    private String reason;
}