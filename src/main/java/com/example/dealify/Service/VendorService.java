package com.example.dealify.Service;

import com.example.dealify.Api.ApiException;
import com.example.dealify.InDTO.BlackListInDTO;
import com.example.dealify.InDTO.BlackListPardonRequestVendorInDTO;
import com.example.dealify.InDTO.ImageInDTO;
import com.example.dealify.InDTO.VendorInDTO;
import com.example.dealify.Model.*;
import com.example.dealify.OutDTO.*;
import com.example.dealify.Repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class VendorService { //Ebtehal
    private final AuthRepository authRepository;
    private final VendorRepository vendorRepository;
    private final InventoryRepository inventoryRepository;
    private final VendorProfileRepository vendorProfileRepository;
    private final DealService dealService;
    private final BlackListPardonRequestService blackListPardonRequestService;
    private final BlackListService blackListService;
    private final ProductService productService;
    private final ReturnRequestService returnRequestService;
    private final DealRepository dealRepository;

    public void register(VendorInDTO vendorInDTO) {
        // Validate uniqueness of MyUser
        if (authRepository.existsByUsername(vendorInDTO.getUsername())) {
            throw new ApiException("Username already exists.");
        }
        if (authRepository.existsByEmail(vendorInDTO.getEmail())) {
            throw new ApiException("Email already exists.");
        }

        // Validate uniqueness of VendorProfile
        if (vendorRepository.existsByCommercialRegistration(vendorInDTO.getCommercialRegistration())) {
            throw new ApiException("Commercial Registration already exists.");
        }
        if (vendorRepository.existsByMyUser_PhoneNumber(vendorInDTO.getPhoneNumber())) {
            throw new ApiException("Phone number already exists.");
        }

        // Create and set up MyUser
        MyUser myUser1 = new MyUser();
        myUser1.setFullName(vendorInDTO.getFullName());
        myUser1.setUsername(vendorInDTO.getUsername());
        myUser1.setPhoneNumber(vendorInDTO.getPhoneNumber());
        myUser1.setEmail(vendorInDTO.getEmail());
        String hashPassword = new BCryptPasswordEncoder().encode(vendorInDTO.getPassword());
        myUser1.setPassword(hashPassword);
        myUser1.setRole("VENDOR");

        // Create and set up Vendor
        Vendor vendor = new Vendor();
        vendor.setCommercialRegistration(vendorInDTO.getCommercialRegistration());
        vendor.setStatus("Inactive"); // Set the status as inactive
        vendor.setMyUser(myUser1);

        // Create VendorProfile
        VendorProfile vendorProfile = new VendorProfile();
        vendorProfile.setVendor(vendor);
        vendor.setVendorProfile(vendorProfile);

        // Create and set up Inventory
        Inventory inventory = new Inventory();
        inventory.setAvailableQuantity(0); // Initialize with zero available quantity
        inventory.setSoldQuantity(0); // Initialize with zero sold quantity
        inventory.setVendorProfile(vendorProfile);

        // Associate Inventory with VendorProfile
        vendorProfile.setInventory(inventory);

        // Save all entities (MyUser, Vendor, VendorProfile, and Inventory)
        authRepository.save(myUser1);
        vendorRepository.save(vendor);
        vendorProfileRepository.save(vendorProfile);
        inventoryRepository.save(inventory);
    }

    public void updateVendor(Integer vendorId, VendorInDTO vendorInDTO) {

        MyUser oldUser = authRepository.findMyUserById(vendorId);
        if (oldUser == null) {
            throw new ApiException("Vendor Not Found.");
        }

        oldUser.setFullName(vendorInDTO.getFullName());
        oldUser.setUsername(vendorInDTO.getUsername());
        oldUser.setEmail(vendorInDTO.getEmail());
        String hashPassword = new BCryptPasswordEncoder().encode(vendorInDTO.getPassword());
        oldUser.setPassword(hashPassword);
        oldUser.setPhoneNumber(vendorInDTO.getPhoneNumber());
        oldUser.getVendor().setCommercialRegistration(vendorInDTO.getCommercialRegistration());

        authRepository.save(oldUser);
    }

    public void deleteMyAccount(Integer vendorId) {
        MyUser vendor = authRepository.findMyUserById(vendorId);
        if (vendor == null) {
            throw new ApiException("Can't Find This Account.");
        }
        VendorProfile vendorProfile = vendorProfileRepository.findVendorProfileById(vendorId);
        if (vendorProfile != null) {
            // Fetch and delete the associated inventory
            Inventory inventory = inventoryRepository.findInventoryByVendorProfile(vendorProfile);
            if (inventory != null) {
                inventoryRepository.delete(inventory);
            }
            // Delete vendor profile
            vendorProfileRepository.delete(vendorProfile);
        }
        // Delete the account
        authRepository.delete(vendor);
    }

    // Extra endpoints
    // 1. get all open deals for vendor products
    public List<DealOutDTO> viewVendorsOpenDeals(Integer vendorId) { //Waleed
        VendorProfile vendorProfile = vendorProfileRepository.findVendorProfileById(vendorId);
        if (vendorProfile == null) throw new ApiException("Vendor not found");

        return dealService.viewVendorsOpenDeals(vendorProfile);
    }

    // 2. Activate vendor (admin)
    public void activateVendor(Integer authId, Integer vendorId) { //Renad
        MyUser auth = authRepository.findMyUserById(authId);
        if (auth == null) {
            throw new ApiException("Admin was not found");
        }

        if (!auth.getRole().equalsIgnoreCase("ADMIN")) {
            throw new ApiException("You don't have the permission to access this endpoint");
        }

        Vendor vendor = vendorRepository.findVendorById(vendorId);
        if (vendor == null) {
            throw new ApiException("Vendor Not Found.");
        }

        vendor.setStatus("Active");
        vendorRepository.save(vendor);
    }

    // 3. Get vendor by id
    public VendorProfileOutDTO getVendorProfileById(Integer vendorId) { //Renad
        VendorProfile vendor = vendorProfileRepository.findVendorProfileById(vendorId);
        if (vendor == null) {
            throw new ApiException("Vendor Not Found.");
        }

        List<VendorReviewOutDTO> reviewOutDTOS = new ArrayList<>();
        for (VendorReview vendorReview : vendor.getVendorReviews()) {
            VendorReviewOutDTO reviewOutDTO = new VendorReviewOutDTO(vendorReview.getOverallRating(), vendorReview.getServiceRating(), vendorReview.getProductQualityRating(), vendorReview.getReturnPolicyRating(), vendorReview.getDeliveryRating(), vendorReview.getComment(), vendorReview.getCustomer().getMyUser().getFullName());
            reviewOutDTOS.add(reviewOutDTO);
        }

        List<ProductOutDTO> productOutDTOS = new ArrayList<>();
        for (Product product : vendor.getInventory().getProducts()) {
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

        return new VendorProfileOutDTO(vendor.getVendor().getMyUser().getFullName(), vendor.getCompanyName(), vendor.getVendor().getMyUser().getPhoneNumber(), vendor.getCity(), reviewOutDTOS, productOutDTOS);
    }

    // 4. Get vendor inventory
    public InventoryOutDTO getVendorInventory(Integer vendorId) { //Renad
        VendorProfile vendor = vendorProfileRepository.findVendorProfileById(vendorId);

        if (vendor == null) {
            throw new ApiException("Vendor Not Found.");
        }

        Inventory inventory = vendor.getInventory();

        List<ProductOutDTO> productOutDTOS = new ArrayList<>();
        for (Product product : inventory.getProducts()) {
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
        return new InventoryOutDTO(inventory.getAvailableQuantity(), inventory.getSoldQuantity(), inventory.getUpdatedAt(), productOutDTOS);
    }

    // 5. Get vendor return requests
    public List<ReturnRequestOutDTO> getVendorReturnRequests(Integer vendorId) {//Renad
        VendorProfile vendor = vendorProfileRepository.findVendorProfileById(vendorId);
        if (vendor == null) {
            throw new ApiException("Vendor Not Found.");
        }

        Set<ReturnRequest> returnRequests = vendor.getReturnRequests();

        if (returnRequests.isEmpty()) {
            throw new ApiException("There Are No Return Requests for Vendor with ID:" + vendorId);
        }

        List<ReturnRequestOutDTO> returnRequestOutDTOS = new ArrayList<>();
        for (ReturnRequest returnRequest : returnRequests) {
            ReturnRequestOutDTO returnRequestOutDTO = new ReturnRequestOutDTO(returnRequest.getReason(),
                    returnRequest.getResponse(),
                    returnRequest.getStatus(),
                    returnRequest.getRequestDate(),
                    returnRequest.getProduct().getBrand(),
                    returnRequest.getProduct().getName(),
                    returnRequest.getProduct().getPrice(),
                    returnRequest.getVendorProfile().getVendor().getMyUser().getFullName());
            returnRequestOutDTOS.add(returnRequestOutDTO);
        }
        return returnRequestOutDTOS;
    }

    // 6. get vendor blacklist
    public List<BlackListOutDTO> getVendorBlackList(Integer vendorId) {//Waleed
        VendorProfile vendorProfile = vendorProfileRepository.findVendorProfileById(vendorId);
        if (vendorProfile == null) throw new ApiException("Vendor not found");

        return blackListService.getVendorBlackList(vendorProfile);
    }

    // 7. add customer to blacklist
    public void addCustomerToBlacklist(Integer vendorId, Integer customerId, BlackListInDTO blackListInDTO) {//Waleed
        VendorProfile vendorProfile = vendorProfileRepository.findVendorProfileById(vendorId);
        if (vendorProfile == null) throw new ApiException("Vendor not found");

        blackListService.addCustomerToBlacklist(vendorProfile, customerId, blackListInDTO);
    }

    public void removeCustomerFromBlackList(Integer vendorId, Integer customerId) {//EBTEHAL
        VendorProfile vendorProfile = vendorProfileRepository.findVendorProfileById(vendorId);
        if (vendorProfile == null) throw new ApiException("Vendor not found");

        blackListService.removeCustomerFromBlackList(vendorProfile, customerId);
    }

    public List<BlackListPardonRequestOutDTO> getVendorBlackListPardonRequests(Integer vendorId) {//Waleed
        VendorProfile vendorProfile = vendorProfileRepository.findVendorProfileById(vendorId);
        if (vendorProfile == null) throw new ApiException("Vendor not found");

        return blackListPardonRequestService.getVendorBlackListPardonRequests(vendorProfile);
    }

    public void approvePardonRequest(Integer vendorId, Integer pardonRequestId, BlackListPardonRequestVendorInDTO blackListPardonRequestVendorInDTO) {//Waleed
        VendorProfile vendorProfile = vendorProfileRepository.findVendorProfileById(vendorId);
        if (vendorProfile == null) throw new ApiException("Vendor not found");

        blackListPardonRequestService.approvePardonRequest(vendorProfile, pardonRequestId, blackListPardonRequestVendorInDTO);
    }

    public void rejectPardonRequest(Integer vendorId, Integer pardonRequestId, BlackListPardonRequestVendorInDTO blackListPardonRequestVendorInDTO) {//Waleed
        VendorProfile vendorProfile = vendorProfileRepository.findVendorProfileById(vendorId);
        if (vendorProfile == null) throw new ApiException("Vendor not found");

        blackListPardonRequestService.rejectPardonRequest(vendorProfile, pardonRequestId, blackListPardonRequestVendorInDTO);
    }

    public void updateProductImages(Integer vendorId, Integer productId, ImageInDTO imageInDTO) { //Renad
        productService.updateProductImages(vendorId, productId, imageInDTO);
    }

    public void acceptReturnRequest(Integer vendorId, Integer returnRequestId) { // Renad
        returnRequestService.acceptReturnRequest(vendorId, returnRequestId);
    }

    public void rejectReturnRequest(Integer vendorId, Integer returnRequestId) { // Renad
        returnRequestService.rejectReturnRequest(vendorId, returnRequestId);
    }
}