package com.example.dealify.InDTO;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BlackListPardonRequestCustomerInDTO {//Waleed

    @NotEmpty(message = "varchar(255) not null")
    private String reason;
}
