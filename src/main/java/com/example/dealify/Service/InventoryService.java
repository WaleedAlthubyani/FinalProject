package com.example.dealify.Service;

import com.example.dealify.Api.ApiException;
import com.example.dealify.Model.*;
import com.example.dealify.OutDTO.*;
import com.example.dealify.Repository.AuthRepository;
import com.example.dealify.Repository.DealRepository;
import com.example.dealify.Repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor 
public class InventoryService { //Renad

    // 1. Declare a dependency for InventoryRepository using Dependency Injection
    private final InventoryRepository inventoryRepository;
    private final AuthRepository authRepository;
    private final DealRepository dealRepository;

    // 2. CRUD
    // 2.1 Get
    public List<InventoryOutDTO> getAllInventories(Integer adminId) {
        MyUser auth = authRepository.findMyUserById(adminId);
        if (auth == null) {
            throw new ApiException("Admin was not found");
        }

        // Retrieve all inventories
        List<Inventory> inventories = inventoryRepository.findAll();
        List<InventoryOutDTO> inventoryOutDTOS = new ArrayList<>();

        for (Inventory inventory : inventories) {
            List<ProductOutDTO> productOutDTOS = new ArrayList<>();

            // Loop through products in the inventory
            for (Product product : inventory.getProducts()) {
                // Construct CategoryOutDTO
                Category category = product.getCategory();
                CategoryOutDTO categoryOutDTO = new CategoryOutDTO(category.getName());

                // Construct List of ImageOutDTO
                Set<Image> images = product.getImages();
                List<ImageOutDTO> imageOutDTOS = new ArrayList<>();
                for (Image image : images) {
                    ImageOutDTO imageOutDTO = new ImageOutDTO(image.getImageUrl());
                    imageOutDTOS.add(imageOutDTO);
                }

                // Construct List of DealOutDTO
                List<DealOutDTO> dealOutDTOS = new ArrayList<>();
                List<Deal> deals = dealRepository.findDealsByProduct(product);
                for (Deal deal : deals) {
                    DealOutDTO dealOutDTO = new DealOutDTO(
                            deal.getCurrentParticipants(),
                            deal.getParticipantsLimit(),
                            deal.getStatus(),
                            deal.getQuantity(),
                            deal.getStartedAt(),
                            deal.getEndsAt()
                    );
                    dealOutDTOS.add(dealOutDTO);
                }

                // Construct ProductOutDTO and add to the list
                ProductOutDTO productOutDTO = new ProductOutDTO(
                        product.getName(),
                        product.getDescription(),
                        product.getPrice(),
                        product.getStock(),
                        product.getSKU(),
                        product.getPrimaryImage(),
                        categoryOutDTO,
                        imageOutDTOS,
                        dealOutDTOS
                );

                productOutDTOS.add(productOutDTO);
            }

            // Construct InventoryOutDTO and add to the list
            InventoryOutDTO inventoryOutDTO = new InventoryOutDTO(
                    inventory.getAvailableQuantity(),
                    inventory.getSoldQuantity(),
                    inventory.getUpdatedAt(),
                    productOutDTOS
            );

            inventoryOutDTOS.add(inventoryOutDTO);
        }

        return inventoryOutDTOS;
    }
}