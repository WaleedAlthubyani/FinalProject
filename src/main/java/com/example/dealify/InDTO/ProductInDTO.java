package com.example.dealify.InDTO;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
@AllArgsConstructor
public class ProductInDTO { //Renad
    @NotEmpty(message = "Name is required.")
    @Size(max = 50, message = "Name can't exceed 50 characters.")
    private String name;

    @NotEmpty(message = "Brand is required.")
    @Size(max = 30, message = "Brand can't exceed 30 characters.")
    private String brand;

    @NotEmpty(message = "Description is required.")
    @Size(max = 800, message = "Description can't exceed 800 characters.")
    private String description;

    @NotEmpty(message = "Primary image is required.")
    @Size(max = 800, message = "Primary image can't exceed 800 characters.")
    private String primaryImage;

    @NotNull(message = "Price is required.")
    @Positive(message = "Price must be a positive number.")
    private Double price;

    @NotNull(message = "Stock is required.")
    @Positive(message = "Stock must be a positive number.")
    private Integer stock;

    @NotNull(message = "Category id is required.")
    private Integer categoryId;
}