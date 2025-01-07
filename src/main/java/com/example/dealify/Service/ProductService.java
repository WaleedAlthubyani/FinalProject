package com.example.dealify.Service;

import com.example.dealify.Api.ApiException;
import com.example.dealify.InDTO.ImageInDTO;
import com.example.dealify.InDTO.ProductInDTO;
import com.example.dealify.Model.*;
import com.example.dealify.OutDTO.*;
import com.example.dealify.Repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ProductService {

    // 1. Declare dependencies using Dependency Injection
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final InventoryRepository inventoryRepository;
    private final VendorProfileRepository vendorProfileRepository;
    private final DealService dealService;
    private final ProductReviewService productReviewService;
    private final VendorRepository vendorRepository;
    private final ImageRepository imageRepository;
    private final DealRepository dealRepository;

    // 2. CRUD
    // 2.1 Get
    public List<ProductOutDTO> getAllProducts() { //Renad
        List<Product> products = productRepository.findAll();
        List<ProductOutDTO> productOutDTOS = new ArrayList<>();

        for (Product product : products) {
            // Construct CategoryOutDTO
            Category category = product.getCategory();
            CategoryOutDTO categoryOutDTO = new CategoryOutDTO(category.getName());

            // Construct List of ImageOutDTO from the product images
            Set<Image> images = product.getImages();
            List<ImageOutDTO> imageOutDTOS = new ArrayList<>();
            for (Image image : images) {
                ImageOutDTO imageOutDTO = new ImageOutDTO(image.getImageUrl());
                imageOutDTOS.add(imageOutDTO);
            }

            // Construct the List of DealOutDTO for the current product
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

            // Construct ProductOutDTO and add it to the list
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

        return productOutDTOS;
    }

    // 2.2 Post
    public void addProduct(Integer vendorId, ProductInDTO productInDTO) { //Renad
        // Step 1: Validate category & vendor existence
        Category category = categoryRepository.findCategoryById(productInDTO.getCategoryId());
        if (category == null) {
            throw new ApiException("Category Not Found.");
        }

        Vendor vendor = vendorRepository.findVendorById(vendorId);
        if (vendor == null) {
            throw new ApiException("Vendor Not Found.");
        }

        if (vendor.getStatus().equalsIgnoreCase("Inactive")) {
            throw new ApiException("Vendor Inactive. Can't Add Products To Inventory.");
        }

        VendorProfile vendorProfile = vendorProfileRepository.findVendorProfileById(vendorId);

        Inventory inventory = vendorProfile.getInventory();

        // Step 2: Create and save the product without SKU
        Product product = new Product(null, productInDTO.getName(), productInDTO.getBrand(), productInDTO.getDescription(), productInDTO.getPrimaryImage(), productInDTO.getPrice(), productInDTO.getStock(), null, true, category, inventory, null, null, null, null);
        productRepository.save(product);

        // Step 3: Update inventory available quantity
        inventory.setAvailableQuantity(inventory.getAvailableQuantity() + productInDTO.getStock());
        inventory.setUpdatedAt(LocalDateTime.now());
        inventoryRepository.save(inventory);

        // Step 4: Generate SKU with product ID
        String generatedSKU = generateSKU(product.getCategory().getName(), product.getName());

        // Step 5: Update product with SKU and save product
        product.setSKU(generatedSKU);
        productRepository.save(product);

    }

    // 2.3 Update
    public void updateProduct(Integer vendorId, Integer productId, ProductInDTO productInDTO) { //Renad
        // Step 1: Find the existing product by ID
        Product product = productRepository.findProductById(productId);

        if (product == null) {
            throw new ApiException("Product Not Found.");
        }

        // Step 2: Find the existing vendor by ID
        VendorProfile vendor = vendorProfileRepository.findVendorProfileById(vendorId);
        if (vendor == null) {
            throw new ApiException("Vendor Not Found.");
        }

        // Step 3: Validate that the vendor owns this product
        if (!product.getInventory().getVendorProfile().getId().equals(vendorId)) {
            throw new ApiException("Vendor Doesn't Own This Product.");
        }

        // Step 4: Validate category existence
        Category category = product.getCategory(); // Default to the current category
        if (productInDTO.getCategoryId() != null) {
            category = categoryRepository.findCategoryById(productInDTO.getCategoryId());
            if (category == null) {
                throw new ApiException("Category Not Found.");
            }
        }

        // Step 5: Update product fields
        product.setName(productInDTO.getName());
        product.setDescription(productInDTO.getDescription());
        product.setPrice(productInDTO.getPrice());
        product.setStock(productInDTO.getStock());
        // Update inventory available quantity
        Inventory inventory = product.getInventory();
        if (inventory != null) {
            inventory.setAvailableQuantity(inventory.getAvailableQuantity() + productInDTO.getStock());
            inventoryRepository.save(inventory);
        }
        product.setCategory(category);

        // Step 6: Update the SKU if category or name changed
        boolean isCategoryChanged = !category.equals(product.getCategory());
        boolean isNameChanged = productInDTO.getName() != null && !productInDTO.getName().equals(product.getName());
        if (isCategoryChanged || isNameChanged) {
            String updatedSKU = generateSKU(product.getCategory().getName(), product.getName());
            product.setSKU(updatedSKU);
        }

        // Step 7: Save the updated product
        productRepository.save(product);
    }

    // 2.4 Delete
    public void deleteProduct(Integer vendorId, Integer productId) { //Renad
        // Step 1: Find the existing product by ID
        Product product = productRepository.findProductById(productId);

        if (product == null) {
            throw new ApiException("Product Not Found.");
        }

        // Step 2: Find the existing vendor by ID
        VendorProfile vendor = vendorProfileRepository.findVendorProfileById(vendorId);
        if (vendor == null) {
            throw new ApiException("Vendor Not Found.");
        }

        // Step 3: Validate that the vendor owns this product
        if (!product.getInventory().getVendorProfile().getId().equals(vendorId)) {
            throw new ApiException("Vendor Doesn't Own This Product.");
        }

        // Step 4: Delete the product
        productRepository.delete(product);
    }

    // Helper method to generate SKU for the product
    public String generateSKU(String categoryName, String productName) { //Renad
        // Abbreviate category and product name (first 3 uppercase characters)
        String categoryCode = categoryName.length() >= 3
                ? categoryName.substring(0, 3).toUpperCase()
                : categoryName.toUpperCase();
        String productCode = productName.length() >= 3
                ? productName.substring(0, 3).toUpperCase()
                : productName.toUpperCase();

        // Add a random 4-digit number for uniqueness
        int randomNumber = new Random().nextInt(9000) + 1000; // Range: 1000-9999

        // Combine into SKU
        return categoryCode + "-" + productCode + "-" + randomNumber;
    }

    // 3. Extra end points
    // 3.1 Get product by id
    public ProductOutDTO getProductById(Integer productId) { //Renad
        Product product = productRepository.findProductById(productId);
        if (product == null) {
            throw new ApiException("Product Not Found.");
        }
        Category category = product.getCategory();
        CategoryOutDTO categoryOutDTO = new CategoryOutDTO(category.getName());
        // Convert Images to List<ImageOutDTO>
        Set<Image> images = product.getImages();
        List<ImageOutDTO> imageOutDTOS = new ArrayList<>();
        if (images != null) {
            for (Image image : images) {
                ImageOutDTO imageOutDTO = new ImageOutDTO(image.getImageUrl());
                imageOutDTOS.add(imageOutDTO);
            }
        }

        // Convert Deals to List<DealOutDTO>
        Set<Deal> deals = product.getDeals();
        List<DealOutDTO> dealOutDTOS = new ArrayList<>();
        if (deals != null) {
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
        }

        // Construct and return the ProductOutDTO
        return new ProductOutDTO(
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStock(),
                product.getSKU(),
                product.getPrimaryImage(),
                categoryOutDTO, // Converted CategoryOutDTO
                imageOutDTOS,   // List of ImageOutDTO
                dealOutDTOS     // List of DealOutDTO
        );
    }

    // 3.2 Get product images
    public List<ImageOutDTO> getProductImages(Integer productId) { //Renad
        Product product = productRepository.findProductById(productId);
        if (product == null) {
            throw new ApiException("Product Not Found.");
        }
        Set<Image> images = product.getImages();
        List<ImageOutDTO> imageOutDTOS = new ArrayList<>();
        for (Image image : images) {
            ImageOutDTO imageOutDTO = new ImageOutDTO(image.getImageUrl());
            imageOutDTOS.add(imageOutDTO);
        }
        return imageOutDTOS;
    }

    // 3.3 Update product images
    public void updateProductImages(Integer vendorId, Integer productId, ImageInDTO imageInDTO) { //Renad
        Product product = productRepository.findProductById(productId);
        if (product == null) {
            throw new ApiException("Product Not Found.");
        }

        VendorProfile vendor = vendorProfileRepository.findVendorProfileById(vendorId);
        if (vendor == null) {
            throw new ApiException("Vendor Not Found.");
        }

        if (!vendor.getInventory().getProducts().contains(product)) {
            throw new ApiException("Product does not belong to this vendor.");
        }

        // Clear existing images from the product
        product.getImages().clear(); // Assuming getImages() returns a collection with the product's current images

        // Convert input images to Image entities and associate them with the product
        Set<Image> updatedImages = new HashSet<>();
        for (Image inputImage : imageInDTO.getImages()) {
            Image image = new Image();
            image.setImageUrl(inputImage.getImageUrl());
            image.setDescription(inputImage.getDescription());
            image.setProduct(product); // Link image to product
            updatedImages.add(image);
        }

        // Update the product's image collection
        product.setImages(updatedImages);

        // Save the product and its associated images
        productRepository.save(product);

        for (Image image : updatedImages) {
            imageRepository.save(image);  // Assuming an imageRepository exists for saving images
        }
    }

    // 3.4 Get all products by category
    public List<ProductOutDTO> getProductsByCategory(String categoryName) { //Ebtehal
        List<Product> products = productRepository.findProductsByCategoryName(categoryName);

        if (products.isEmpty()) {
            throw new ApiException("No Products Has Been Found For Category: " + categoryName);
        }

        List<ProductOutDTO> productOutDTOS = new ArrayList<>();
        for (Product product : products) {
            // Construct CategoryOutDTO
            Category category = product.getCategory();
            CategoryOutDTO categoryOutDTO = new CategoryOutDTO(category.getName());

            // Construct List of ImageOutDTO from the product images
            Set<Image> images = product.getImages();
            List<ImageOutDTO> imageOutDTOS = new ArrayList<>();
            for (Image image : images) {
                ImageOutDTO imageOutDTO = new ImageOutDTO(image.getImageUrl());
                imageOutDTOS.add(imageOutDTO);
            }

            // Construct the List of DealOutDTO for the current product
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

            // Construct ProductOutDTO and add it to the list
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

        return productOutDTOS;
    }

    // 3.5 Get the open deals for a product
    public List<DealOutDTO> viewProductOpenDeals(Integer productId) { //Waleed
        return dealService.viewProductDeals(productId);
    }

    // 3.6 Get products of a specific vendor
    public List<ProductOutDTO> getProductsByVendor(Integer vendorId) {  // Ebtehal
        VendorProfile vendorProfile = vendorProfileRepository.findVendorProfileById(vendorId);
        if (vendorProfile == null) {
            throw new ApiException("Vendor not found");
        }
        List<Product> vendorProducts = productRepository.findProductByInventory(vendorProfile.getInventory());
        List<ProductOutDTO> productList = new ArrayList<>();

        for (Product product : vendorProducts) {
            // Construct CategoryOutDTO
            Category category = product.getCategory();
            CategoryOutDTO categoryOutDTO = new CategoryOutDTO(category.getName());

            // Construct List of ImageOutDTO from the product images
            Set<Image> images = product.getImages();
            List<ImageOutDTO> imageOutDTOS = new ArrayList<>();
            for (Image image : images) {
                ImageOutDTO imageOutDTO = new ImageOutDTO(image.getImageUrl());
                imageOutDTOS.add(imageOutDTO);
            }

            // Construct the List of DealOutDTO for the current product
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

            // Construct ProductOutDTO and add it to the list
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

            productList.add(productOutDTO);
        }
        return productList;
    }

    // 3.7 get product that has less stock
    public List<ProductOutDTO> getLowStockProducts(int stockLimit) { // Ebtehal
        List<Product> products = productRepository.findByStockLessThan(stockLimit);
        List<ProductOutDTO> productOutDTOs = new ArrayList<>();

        for (Product product : products) {
            // Construct CategoryOutDTO
            Category category = product.getCategory();
            CategoryOutDTO categoryOutDTO = new CategoryOutDTO(category.getName());

            // Construct List of ImageOutDTO from the product images
            Set<Image> images = product.getImages();
            List<ImageOutDTO> imageOutDTOS = new ArrayList<>();
            for (Image image : images) {
                ImageOutDTO imageOutDTO = new ImageOutDTO(image.getImageUrl());
                imageOutDTOS.add(imageOutDTO);
            }

            // Construct the List of DealOutDTO for the current product
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

            // Construct ProductOutDTO and add it to the list
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

            productOutDTOs.add(productOutDTO);
        }

        return productOutDTOs;
    }

    public List<ProductOutDTO> getProductsByPriceRange(Double minPrice, Double maxPrice) { // Ebtehal
        List<Product> products = productRepository.findByPriceBetween(minPrice, maxPrice);
        List<ProductOutDTO> productOutDTOs = new ArrayList<>();

        for (Product product : products) {
            // Construct CategoryOutDTO
            Category category = product.getCategory();
            CategoryOutDTO categoryOutDTO = new CategoryOutDTO(category.getName());

            // Construct List of ImageOutDTO from the product images
            Set<Image> images = product.getImages();
            List<ImageOutDTO> imageOutDTOS = new ArrayList<>();
            for (Image image : images) {
                ImageOutDTO imageOutDTO = new ImageOutDTO(image.getImageUrl());
                imageOutDTOS.add(imageOutDTO);
            }

            // Construct the List of DealOutDTO for the current product
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

            // Construct ProductOutDTO and add it to the list
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

            productOutDTOs.add(productOutDTO);
        }

        return productOutDTOs;
    }

    // 3.9 get product reviews
    public List<ProductReviewOutDTO> getProductReviews(Integer productId) { //Waleed
        Product product = productRepository.findProductById(productId);
        if (product == null) throw new ApiException("Product not found");

        return productReviewService.getAllProductReviews(product);
    }
}