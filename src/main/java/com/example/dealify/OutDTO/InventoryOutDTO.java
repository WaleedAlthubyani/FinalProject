package com.example.dealify.OutDTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class InventoryOutDTO { //Ebtehal
    private Integer availableQuantity;

    private Integer soldQuantity;

    private LocalDateTime updatedAt;

    private List<ProductOutDTO> products;
}