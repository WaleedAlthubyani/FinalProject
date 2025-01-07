package com.example.dealify.InDTO;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductReviewInDTO { //Ebtehal

    @NotNull(message = "Please enter quality rating")
    @Min(value = 1,message = "quality Rating can't be less than 1")
    @Max(value = 5,message = "Quality rating can't be more than 5")
    private Integer qualityRating;

    @NotNull(message = "Please enter quality rating")
    @Min(value = 1,message = "Package Rating can't be less than 1")
    @Max(value = 5,message = "Package rating can't be more than 5")
    private Integer packageRating;

    @NotEmpty(message = "Please enter a comment")
    @Size(max = 255,message = "Comment can't be longer than 255 characters")
    private String comment;
}
