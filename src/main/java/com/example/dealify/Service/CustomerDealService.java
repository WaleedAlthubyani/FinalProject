package com.example.dealify.Service;

import com.example.dealify.Api.ApiException;
import com.example.dealify.InDTO.DealJoinInDTO;
import com.example.dealify.Model.*;
import com.example.dealify.OutDTO.DealOutDTO;
import com.example.dealify.Repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CustomerDealService {

    private final CustomerDealRepository customerDealRepository;
    private final DealRepository dealRepository;
    private final BlackListRepository blackListRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;
    private final EmailService emailService;

    //Waleed
    public void joinDeal(CustomerProfile customerProfile, DealJoinInDTO dealJoinInDTO, Integer dealId) {
        Deal deal = dealRepository.findDealById(dealId);
        if (deal == null) throw new ApiException("Deal not found");
        if (!deal.getStatus().equalsIgnoreCase("Open"))
            throw new ApiException("Can't join a completed deal");

        if (blackListRepository.findBlackListByCustomerAndVendor(customerProfile.getCustomer(), deal.getProduct().getInventory().getVendorProfile()) != null)
            throw new ApiException("Customer is in this vendor blacklist");

        if (Objects.equals(deal.getCurrentParticipants(), deal.getParticipantsLimit())) {
            throw new ApiException("Deal participants limit has been reached");
        }

        if (customerDealRepository.findCustomerDealByCustomerAndDeal(customerProfile, deal) != null)
            throw new ApiException("Can't join the same deal twice");

        CustomerDeal customerDeal = new CustomerDeal();
        customerDeal.setCustomer(customerProfile);
        customerDeal.setDeal(deal);

        Double discount = calculateDiscount(deal);
        customerDeal.setQuantity(dealJoinInDTO.getQuantity());
        customerDeal.setOriginalPrice(deal.getProduct().getPrice() * customerDeal.getQuantity());
        customerDeal.setDiscountedPrice(customerDeal.getOriginalPrice() - (customerDeal.getOriginalPrice() * discount));
        customerDeal.setJoinedAt(LocalDateTime.now());

        customerDealRepository.save(customerDeal);
        deal.setCurrentParticipants(deal.getCurrentParticipants() + 1);
        deal.setQuantity(deal.getQuantity() + dealJoinInDTO.getQuantity());
        dealRepository.save(deal);

        if (Objects.equals(deal.getParticipantsLimit(), deal.getCurrentParticipants())) {
            List<CustomerDeal> customerDeals = customerDealRepository.findCustomerDealsByDeal(deal);

            for (CustomerDeal c : customerDeals) {
                String customerEmail = c.getCustomer().getCustomer().getMyUser().getEmail();
                String productName = deal.getProduct().getName();
                DealOutDTO dealDetails = new DealOutDTO(
                        deal.getCurrentParticipants(),
                        deal.getParticipantsLimit(),
                        deal.getStatus(),
                        deal.getQuantity(),
                        deal.getStartedAt(),
                        deal.getEndsAt()
                );

                String subject = "The deal on: " + productName + " has reached its participants limit";
                String body = String.format(
                        "Dear Customer,\n\n" +
                                "The deal on: %s has reached its participants limit and is now ready for you to pay.\n\n" +
                                "Deal Details:\n" +
                                " - Current Participants: %d\n" +
                                " - Participants Limit: %d\n" +
                                " - Status: %s\n" +
                                " - Quantity: %d\n" +
                                " - Start Date: %s\n" +
                                " - End Date: %s\n\n" +
                                "Please make sure to pay for it as soon as possible to secure your spot.\n\n" +
                                "Best regards,\n" +
                                "Dealify Team",
                        productName,
                        dealDetails.getCurrentParticipants(),
                        dealDetails.getParticipantsLimit(),
                        dealDetails.getStatus(),
                        dealDetails.getQuantity(),
                        dealDetails.getStartedAt(),
                        dealDetails.getEndsAt()
                );

                emailService.sendEmail(customerEmail, subject, body);
            }
        }
    }

    //Waleed
    public void updateQuantity(CustomerProfile customerProfile, Integer customerDealId, DealJoinInDTO dealJoinInDTO) {

        CustomerDeal customerDeal = customerDealRepository.findCustomerDealById(customerDealId);
        if (customerDeal == null) throw new ApiException("Couldn't find deal joining details");

        if (!customerDeal.getCustomer().equals(customerProfile))
            throw new ApiException("Unauthorized to update another customer's quantity");


        Deal deal = dealRepository.findDealById(customerDeal.getDeal().getId());
        if (deal.getStatus().equalsIgnoreCase("Completed")) throw new ApiException("Can't edit a completed deal");

        deal.setQuantity(deal.getQuantity() - customerDeal.getQuantity());
        customerDeal.setQuantity(dealJoinInDTO.getQuantity());
        customerDeal.setOriginalPrice(deal.getProduct().getPrice() * customerDeal.getQuantity());
        customerDeal.setDiscountedPrice(customerDeal.getOriginalPrice() - (customerDeal.getOriginalPrice() * calculateDiscount(deal)));

        customerDealRepository.save(customerDeal);

        deal.setQuantity(deal.getQuantity() + customerDeal.getQuantity());
    }

    //Waleed
    public void leaveDeal(CustomerProfile customerProfile, Integer customerDealId) {
        CustomerDeal customerDeal = customerDealRepository.findCustomerDealById(customerDealId);
        if (customerDeal == null) throw new ApiException("Couldn't find deal joining details");

        if (customerDeal.getDeal().getCurrentParticipants().equals(customerDeal.getDeal().getParticipantsLimit()))
            rejectPay(customerProfile, customerDeal.getDeal().getId());

        if (!customerDeal.getCustomer().equals(customerProfile))
            throw new ApiException("Unauthorized to do this action");

        Deal deal = dealRepository.findDealById(customerDeal.getDeal().getId());

        if (deal.getStatus().equalsIgnoreCase("Completed")) throw new ApiException("Can't leave a completed deal");

        if (deal.getCurrentParticipants() == 1) {
            customerDealRepository.delete(customerDeal);
            dealRepository.delete(deal);
        }

        if (deal.getCreator().equals(customerProfile)) {
            deal.setCreator(customerDealRepository.findCustomerDealsByDealAndJoinedAtAfterOrderByJoinedAtAsc(deal, customerDeal.getJoinedAt()).get(0).getCustomer());
        }

        deal.setQuantity(deal.getQuantity() - customerDeal.getQuantity());
        deal.setCurrentParticipants(deal.getCurrentParticipants() - 1);
        customerDealRepository.delete(customerDeal);
        dealRepository.save(deal);
    }

    public void pay(CustomerProfile customerProfile, Integer dealId) {
        Deal deal = dealRepository.findDealById(dealId);
        if (deal == null) throw new ApiException("Deal not found");
        if (!Objects.equals(deal.getCurrentParticipants(), deal.getParticipantsLimit()))
            throw new ApiException("You can't do this yet");
        if (!deal.getStatus().equalsIgnoreCase("Open")) throw new ApiException("Deal is completed already");

        CustomerDeal customerDeal = customerDealRepository.findCustomerDealByCustomerAndDeal(customerProfile, deal);
        if (customerDeal == null) throw new ApiException("You are not part of this deal");

        customerDeal.setPayMethod("Card");
        customerDealRepository.save(customerDeal);

        checkAllAgree(dealId);
    }

    public void payWithPoints(CustomerProfile customerProfile, Integer dealId) {
        Deal deal = dealRepository.findDealById(dealId);
        if (deal == null) throw new ApiException("Deal is not found");
        if (!deal.getStatus().equalsIgnoreCase("Open")) throw new ApiException("Deal is completed already");
        if (!Objects.equals(deal.getCurrentParticipants(), deal.getParticipantsLimit()))
            throw new ApiException("You can't do this yet");

        CustomerDeal customerDeal = customerDealRepository.findCustomerDealByCustomerAndDeal(customerProfile, deal);
        if (customerDeal == null) throw new ApiException("You are not part of this deal");
        if (customerProfile.getCustomer().getPoint() < customerDeal.getDiscountedPrice())
            throw new ApiException("You don't have enough points to do this action");

        List<CustomerDeal> customerDeals = customerDealRepository.findCustomerDealsByCustomerAndPayMethod(customerProfile, "Points");
        int total = 0;

        for (CustomerDeal c : customerDeals) {
            total += c.getDiscountedPrice();
        }

        if (total > customerProfile.getCustomer().getPoint())
            throw new ApiException("You don't have enough points to spend on all your deals");

        customerDeal.setPayMethod("Points");
        customerDealRepository.save(customerDeal);

        checkAllAgree(dealId);

    }

    public void checkAllAgree(Integer dealId) {
        Deal deal = dealRepository.findDealById(dealId);
        List<CustomerDeal> customerDeals = customerDealRepository.findCustomerDealsByDeal(deal);

        int counter = 0;

        for (CustomerDeal d : customerDeals) {
            if (d.getPayMethod().equalsIgnoreCase("Pending"))
                break;

            counter++;
        }

        if (counter < customerDeals.size())
            return;

        payNow(dealId);
    }

    public void payNow(Integer dealId) {

        Deal deal = dealRepository.findDealById(dealId);

        Product product = productRepository.findProductById(deal.getProduct().getId());
        product.setStock(product.getStock() - deal.getQuantity());
        productRepository.save(product);

        List<CustomerDeal> customerDeals = customerDealRepository.findCustomerDealsByDeal(deal);

        int points;
        for (CustomerDeal d : customerDeals) {

            Customer customer = customerRepository.findCustomerById(d.getCustomer().getId());

            points = d.getQuantity() / 10;

            if (d.getPayMethod().equalsIgnoreCase("card"))
                customer.setPoint(customer.getPoint() + points);
            else
                customer.setPoint((int) (customer.getPoint() - d.getDiscountedPrice()));

            if (deal.getCreator().equals(customer.getCustomerProfile()))
                customer.setPoint(customer.getPoint() + 100);
            customerRepository.save(customer);
        }

        deal.setStatus("Complete");
        dealRepository.save(deal);
    }

    public void rejectPay(CustomerProfile customerProfile, Integer dealId) {
        Deal deal = dealRepository.findDealById(dealId);
        if (deal == null) throw new ApiException("Deal isn't found");
        if (!Objects.equals(deal.getCurrentParticipants(), deal.getParticipantsLimit())) {
            throw new ApiException("You can not do this yet");
        }
        if (!deal.getStatus().equalsIgnoreCase("Open")) throw new ApiException("Deal is completed already");

        CustomerDeal customerDeal = customerDealRepository.findCustomerDealByCustomerAndDeal(customerProfile, deal);
        if (customerDeal == null) throw new ApiException("You are not part of this deal");

        deal.setCurrentParticipants(deal.getCurrentParticipants()-1);
        List<CustomerDeal> customerDeals = customerDealRepository.findCustomerDealsByDeal(deal);

        for (int i = 0; i < customerDeals.size(); i++) {
            // Retrieve customer deal by ID
            CustomerDeal customerDeal1 = customerDealRepository.findCustomerDealById(customerDeals.get(i).getId());

            // Update the payment method to "Pending"
            customerDeal1.setPayMethod("Pending");

            // Compose the email message
            String subject = "Unfortunately " + customerDeal.getCustomer().getCustomer().getMyUser().getName() + " Has rejected to pay";
            String body = String.format(
                    "Hello %s,\n\n" +
                            "We are sorry to inform you that %s has decided not to continue with the deal and has left.\n" +
                            "Once someone else joins in their place, you will receive an email.\n\n" +
                            "Best regards,\n" +
                            "Dealify team",
                    customerDeal1.getCustomer().getCustomer().getMyUser().getName(),
                    customerDeal.getCustomer().getCustomer().getMyUser().getName()
            );

            // Send the email notification
            emailService.sendEmail(
                    customerDeal1.getCustomer().getCustomer().getMyUser().getEmail(),
                    subject,
                    body
            );

            // Save the updated customer deal
            customerDealRepository.save(customerDeal1);
        }

    }

    //Waleed
    // Helper method
    private Double calculateDiscount(Deal deal) {
        return switch (deal.getParticipantsLimit()) {
            case 3 -> 0.05;
            case 5 -> 0.10;
            case 10 -> 0.15;
            case 15 -> 0.20;
            case 20 -> 0.25;
            default -> 0.0;
        };
    }
}