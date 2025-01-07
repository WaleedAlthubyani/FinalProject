package com.example.dealify.OutDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
public class ProductOutDTO { //Renad
    private String name;

    private String description;

    private Double price;

    private Integer stock;

    private String SKU;

    private String primaryImage;

    private CategoryOutDTO category;

    private List<ImageOutDTO> images;

    private List<DealOutDTO> deals;
}