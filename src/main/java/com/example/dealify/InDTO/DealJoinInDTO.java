package com.example.dealify.InDTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DealJoinInDTO { //Waleed
    @NotNull(message = "Please enter the quantity")
    @Min(value = 1,message = "Quantity can't be less than one")
    private Integer quantity;
}