package com.example.dealify.Service;

import com.example.dealify.Api.ApiException;
import com.example.dealify.InDTO.DealCreationInDTO;
import com.example.dealify.InDTO.DealJoinInDTO;
import com.example.dealify.Model.*;
import com.example.dealify.OutDTO.DealOutDTO;
import com.example.dealify.Repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DealService {

    private final DealRepository dealRepository;
    private final ProductRepository productRepository;
    private final CustomerDealService customerDealService;
    private final BlackListRepository blackListRepository;

    public void createDeal(CustomerProfile customer, Integer productId, DealCreationInDTO dealDTO) { //Waleed

        Product product = productRepository.findProductById(productId);
        if (product == null) throw new ApiException("Product not found");

        if (blackListRepository.findBlackListByCustomerAndVendor(customer.getCustomer(),product.getInventory().getVendorProfile())!=null)
            throw new ApiException("Customer is in this vendor blacklist");

        if (dealRepository.findDealByProductAndParticipantsLimitAndStatus(product,Integer.parseInt(dealDTO.getParticipantsLimit()),"Open")!=null){
            throw new ApiException("A deal with these many participants already exists");
        }

        Deal deal = new Deal();

        deal.setCreator(customer);
        deal.setParticipantsLimit(Integer.parseInt(dealDTO.getParticipantsLimit()));
        deal.setCurrentParticipants(0);
        deal.setQuantity(0);
        deal.setProduct(product);
        deal.setStatus("open");
        deal.setStartedAt(LocalDateTime.now());
        deal.setEndsAt(LocalDateTime.now().plusDays(5));
        dealRepository.save(deal);

        customerDealService.joinDeal(customer, new DealJoinInDTO(dealDTO.getQuantity()), deal.getId());
    }

    public List<DealOutDTO> viewProductDeals(Integer productId) {//Waleed
        Product product = productRepository.findProductById(productId);
        if (product == null) throw new ApiException("product not found");
        return convertDealsToDTO(dealRepository.findDealsByProductAndStatus(product, "open"));
    }

    public List<DealOutDTO> viewCustomerOpenedDeals(CustomerProfile customerProfile) {//Waleed
        return convertDealsToDTO(dealRepository.findDealsByCustomerAndOpen(customerProfile, "open"));
    }

    public List<DealOutDTO> viewCustomerCompletedDeals(CustomerProfile customerProfile) {//Waleed
        return convertDealsToDTO(dealRepository.findDealsByCustomerAndCompleted(customerProfile, "completed"));
    }

    public List<DealOutDTO> viewVendorsOpenDeals(VendorProfile vendorProfile) {//Waleed

        return convertDealsToDTO(dealRepository.findDealsByVendorAndOpen(vendorProfile.getId(), "open"));
    }

    @Scheduled(fixedRate = 60000) //runs every minute and delete all expired open deals
    public void deleteExpiredDeals() {//Waleed
        List<Deal> deals = dealRepository.findDealsByStatusAndEndsAtIsBetween("Open", LocalDateTime.now().minusMinutes(1), LocalDateTime.now());

        for (Deal d : deals) {
            if (d.getEndsAt().isBefore(LocalDateTime.now()) && d.getStatus().equalsIgnoreCase("open")) {
                dealRepository.delete(d);
            }
        }
    }

    // Get deals by product category
    public List<DealOutDTO> viewDealsByProductCategory(String categoryName) { //Renad
        List<Product> products = productRepository.findProductsByCategoryName(categoryName);

        List<DealOutDTO> dealOutDTOS = new ArrayList<>();
        for (Product product : products) {
            List<Deal> deals = dealRepository.findDealsByProduct(product);
            for (Deal deal : deals) {
                DealOutDTO dealOutDTO = new DealOutDTO(deal.getCurrentParticipants(), deal.getParticipantsLimit(), deal.getStatus(), deal.getQuantity(), deal.getStartedAt(), deal.getEndsAt());
                dealOutDTOS.add(dealOutDTO);
            }
        }
        return dealOutDTOS;
    }

    public List<DealOutDTO> convertDealsToDTO(Collection<Deal> deals) { //Waleed
        List<DealOutDTO> dealOutDTOS = new ArrayList<>();

        for (Deal d : deals) {
            dealOutDTOS.add(new DealOutDTO(d.getCurrentParticipants(), d.getParticipantsLimit(), d.getStatus(), d.getQuantity(), d.getStartedAt(), d.getEndsAt()));
        }

        return dealOutDTOS;
    }

}