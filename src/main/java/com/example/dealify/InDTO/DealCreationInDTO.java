package com.example.dealify.InDTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DealCreationInDTO {//Waleed
    @NotNull(message = "Please enter the quantity")
    @Min(value = 1,message = "Quantity can't be less than one")
    private Integer quantity;

    @NotNull(message = "Please enter the participants limit")
    @Pattern(regexp = "^(3|5|10|15|20)$",message = "Participants limit can only be 3,5,10,15 or 20")
    private String participantsLimit;
}