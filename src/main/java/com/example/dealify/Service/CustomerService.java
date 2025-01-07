package com.example.dealify.Service;

import com.example.dealify.Api.ApiException;
import com.example.dealify.InDTO.*;
import com.example.dealify.Model.*;
import com.example.dealify.OutDTO.*;
import com.example.dealify.Repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final AuthRepository authRepository;
    private final CustomerRepository customerRepository;
    private final CustomerProfileRepository customerProfileRepository;
    private final DealService dealService;
    private final CustomerDealService customerDealService;
    private final FavoriteService favoriteService;
    private final ProductRepository productRepository;
    private final CustomerReviewService customerReviewService;
    private final EmailService emailService;
    private final CustomerDealRepository customerDealRepository;
    private final ProductReviewService productReviewService;
    private final BlackListPardonRequestService blackListPardonRequestService;
    private final DealRepository dealRepository;
    private final ProductService productService;
    private final ReturnRequestService returnRequestService;

    //get all user for Admin
    public List<MyUser> getAllCustomers(Integer authId) {  //Ebtehal
        MyUser auth = authRepository.findMyUserById(authId);
        if (auth == null) {
            throw new ApiException("Admin was not found");
        }

        if (!auth.getRole().equalsIgnoreCase("ADMIN")) {
            throw new ApiException("You don't have the permission to access this endpoint");
        }

        return authRepository.findAll();
    }

    //register new customer
    public void register(CustomerInDTO customerDTO) { //Ebtehal
        // تحقق من وجود المستخدم
        MyUser myUser = authRepository.findMyUserByUsername(customerDTO.getUsername());
        if (myUser != null) {
            throw new ApiException("User already exists");
        }

        // إنشاء كيان MyUser
        MyUser myUser1 = new MyUser();
        myUser1.setName(customerDTO.getName());
        myUser1.setUsername(customerDTO.getUsername());
        myUser1.setEmail(customerDTO.getEmail());
        String hashPassword = new BCryptPasswordEncoder().encode(customerDTO.getPassword());
        myUser1.setPassword(hashPassword);
        myUser1.setRole("CUSTOMER");

        // إنشاء كيان Customer
        Customer customer = new Customer();
        customer.setMyUser(myUser1);

        // إنشاء كيان CustomerProfile وربطه بـ Customer
        CustomerProfile customerProfile = new CustomerProfile();
        customerProfile.setCustomer(customer);
        customer.setCustomerProfile(customerProfile);

        // حفظ البيانات في قاعدة البيانات
        authRepository.save(myUser1);
        customerRepository.save(customer);
        customerProfileRepository.save(customerProfile);
    }

    public void updateCustomer(Integer customerId, CustomerInDTO customerInDTO) { //Ebtehal
        MyUser oldUser = authRepository.findMyUserById(customerId);
        if (oldUser == null) {
            throw new ApiException(" customer not found");
        }

        oldUser.setName(customerInDTO.getName());
        oldUser.setUsername(customerInDTO.getUsername());
        oldUser.setEmail(customerInDTO.getEmail());
        oldUser.setPassword((customerInDTO.getPassword()));

        authRepository.save(oldUser);
    }

    public void deleteMyAccount(Integer customerId) {  //Ebtehal
        MyUser customer = authRepository.findMyUserById(customerId);
        if (customer == null) {
            throw new ApiException(" can not found this account");
        }
        CustomerProfile customerProfile = customerProfileRepository.findCustomerProfileById(customerId);
        customer.getCustomer().setCustomerProfile(null);
        customerProfileRepository.delete(customerProfile);
        authRepository.delete(customer);
    }

    public CustomerOutDTO getCustomerById(Integer id) { //Ebtehal
        CustomerProfile customerProfile = customerProfileRepository.findCustomerProfileById(id);
        if (customerProfile == null) throw new ApiException("Customer not found");

        return new CustomerOutDTO(customerProfile.getCustomer().getMyUser().getUsername(), customerProfile.getCustomer().getMyUser().getName(), customerProfile.getCity(), customerProfile.getDistrict(), customerProfile.getStreet());
    }

    //Waleed
    public void createADeal(Integer customerId, Integer productId, DealCreationInDTO dealCreationInDTO) {
        CustomerProfile customerProfile = customerProfileRepository.findCustomerProfileById(customerId);
        if (customerProfile == null) throw new ApiException("customer not found");

        dealService.createDeal(customerProfile, productId, dealCreationInDTO);
    }

    //Waleed
    public List<DealOutDTO> viewCustomerOpenedDeals(Integer id) {
        CustomerProfile customerProfile = customerProfileRepository.findCustomerProfileById(id);
        if (customerProfile == null) throw new ApiException("Customer not found");

        return dealService.viewCustomerOpenedDeals(customerProfile);
    }

    //Waleed
    public List<DealOutDTO> viewCustomerCompletedDeals(Integer id) {
        CustomerProfile customerProfile = customerProfileRepository.findCustomerProfileById(id);
        if (customerProfile == null) throw new ApiException("Customer not found");

        return dealService.viewCustomerCompletedDeals(customerProfile);
    }

    // Renad
    public List<DealOutDTO> viewDealsByProductCategory(String categoryName) {
        return dealService.viewDealsByProductCategory(categoryName);
    }

    //Waleed
    public void joinADeal(Integer customerId, Integer dealId, DealJoinInDTO dealJoinInDTO) {
        CustomerProfile customerProfile = customerProfileRepository.findCustomerProfileById(customerId);
        if (customerProfile == null) throw new ApiException("customer not found");

        customerDealService.joinDeal(customerProfile, dealJoinInDTO, dealId);
    }

    //Waleed
    public void updateQuantity(Integer customerId, Integer customerDealId, DealJoinInDTO dealJoinInDTO) {
        CustomerProfile customerProfile = customerProfileRepository.findCustomerProfileById(customerId);
        if (customerProfile == null) throw new ApiException("customer not found");

        customerDealService.updateQuantity(customerProfile, customerDealId, dealJoinInDTO);
    }

    //Waleed
    public void leaveADeal(Integer customerId, Integer customerDealId) {
        CustomerProfile customerProfile = customerProfileRepository.findCustomerProfileById(customerId);
        if (customerProfile == null) throw new ApiException("customer not found");

        customerDealService.leaveDeal(customerProfile, customerDealId);
    }

    //Waleed
    public FavoriteOutDTO getCustomerFavorites(Integer id) {
        CustomerProfile customerProfile = customerProfileRepository.findCustomerProfileById(id);

        if (customerProfile == null) throw new ApiException("Customer not found");
        return favoriteService.getCustomerFavorites(customerProfile);
    }

    //Waleed
    public void addProductToFavorite(Integer customerId, Integer productId) {
        CustomerProfile customerProfile = customerProfileRepository.findCustomerProfileById(customerId);
        if (customerProfile == null) throw new ApiException("Customer not found");

        Product product = productRepository.findProductById(productId);
        if (product == null) throw new ApiException("Product not found");

        favoriteService.addProductToFavorite(customerProfile, product);
    }

    //Waleed
    public void removeProductFromFavorite(Integer customerId, Integer productId) {
        CustomerProfile customerProfile = customerProfileRepository.findCustomerProfileById(customerId);
        if (customerProfile == null) throw new ApiException("Customer not found");

        Product product = productRepository.findProductById(productId);
        if (product == null) throw new ApiException("Product not found");

        favoriteService.removeProductFromFavorite(customerProfile, product);
    }

    //Waleed
    public List<CustomerReviewOutDTO> getCustomerReviews(Integer id) {
        CustomerProfile customerProfile = customerProfileRepository.findCustomerProfileById(id);
        if (customerProfile == null) throw new ApiException("customer not found");

        return customerReviewService.getCustomerReviews(customerProfile);
    }

    //Waleed
    public void reviewACustomer(Integer reviewerId, Integer reviewedId, CustomerReviewInDTO customerReview) {
        Customer reviewer = customerRepository.findCustomerById(reviewerId);
        if (reviewer == null) throw new ApiException("Reviewer not found");
        CustomerProfile reviewed = customerProfileRepository.findCustomerProfileById(reviewedId);
        if (reviewed == null) throw new ApiException("Reviewed customer not found");

        if (reviewer.getId().equals(reviewed.getId()))
            throw new ApiException("You can't review yourself");

        if (!customerDealRepository.haveCustomersJoinedSameCompletedDeal(reviewer.getCustomerProfile(), reviewed)) {
            throw new ApiException("You didn't join this customer in completed deal yet");
        }

        customerReviewService.reviewACustomer(reviewer.getMyUser(), reviewed, customerReview);
    }

    //Waleed
    public void updateACustomerReview(Integer reviewerId, Integer customerReviewId, CustomerReviewInDTO customerReviewInDTO) {
        Customer customer = customerRepository.findCustomerById(reviewerId);
        if (customer == null) throw new ApiException("Customer not found");

        customerReviewService.updateCustomerReview(customer.getMyUser(), customerReviewId, customerReviewInDTO);
    }

    // Renad
    public void deleteACustomerReview(Integer reviewerId, Integer customerReviewId) {
        Customer customer = customerRepository.findCustomerById(reviewerId);
        if (customer == null) throw new ApiException("Customer not found");

        customerReviewService.deleteCustomerReview(customer.getMyUser(), customerReviewId);
    }

    //  Send invite to deal
    public void inviteToDeal(Integer inviterCustomerId, Integer dealId, Integer inviteeCustomerId) {  //Ebtehal
        CustomerProfile inviterProfile = customerProfileRepository.findCustomerProfileById(inviterCustomerId);

        if (inviterProfile == null) {
            throw new ApiException("Inviter not found");
        }

        CustomerProfile inviteeProfile = customerProfileRepository.findCustomerProfileById(inviteeCustomerId);
        if (inviteeProfile == null) {
            throw new ApiException("Invitee not found");
        }

        sendInvite(inviterProfile, inviteeProfile, dealId);
    }

    // Helper method
    private void sendInvite(CustomerProfile inviterProfile, CustomerProfile inviteeProfile, Integer dealId) {  //Ebtehal
        String inviterName = inviterProfile.getCustomer().getMyUser().getName();
        String inviteeEmail = inviteeProfile.getCustomer().getMyUser().getEmail();
        Deal deal = dealRepository.findDealById(dealId);
        if (deal == null) {
            throw new ApiException("Deal not found");
        }

        if (deal.getCurrentParticipants().equals(deal.getParticipantsLimit())){
            throw new ApiException("Deal participation limit has been reached. Can't invite anyone to this deal.");
        }

        if (!deal.getStatus().equalsIgnoreCase("Open")) {
            throw new ApiException("Deal is completed. Can't invite anyone to this deal.");
        }

        String subject = "Invitation to Join a Deal";
        String body = String.format(
                "Dear Customer,\n\n" +
                        "You have been invited by %s to join a deal for the product '%s'.\n\n" +
                        "Deal Details:\n" +
                        " - Current Participants: %d\n" +
                        " - Participants Limit: %d\n" +
                        " - Status: %s\n" +
                        " - Quantity: %d\n" +
                        " - Start Date: %s\n" +
                        " - End Date: %s\n\n" +
                        "Best regards,\n" +
                        "Dealify Team",
                inviterName,
                deal.getProduct().getName(),
                deal.getCurrentParticipants(),
                deal.getParticipantsLimit(),
                deal.getStatus(),
                deal.getQuantity(),
                deal.getStartedAt(),
                deal.getEndsAt()
        );

        emailService.sendEmail(inviteeEmail, subject, body);
    }

    public ProductOutDTO viewProductById(Integer productId) { //Renad
        return productService.getProductById(productId);
    }

    public List<ImageOutDTO> viewProductImages(Integer productId) { //Renad
        return productService.getProductImages(productId);
    }

    public List<ProductOutDTO> viewProductsByVendor(Integer vendorId) {  // Ebtehal
        return productService.getProductsByVendor(vendorId);
    }

    public List<ProductReviewOutDTO> viewProductReviews(Integer productId) { //Waleed
        return productService.getProductReviews(productId);
    }

    public void creatReturnRequest(Integer customerId, Integer productId, Integer dealId, ReturnRequestCustomerInDTO returnRequestCustomerInDTO) {//Renad
        returnRequestService.creatReturnRequest(customerId, productId, dealId, returnRequestCustomerInDTO);
    }

    public List<ReturnRequestOutDTO> viewCustomerReturnRequests(Integer customerId) { //Renad
        return returnRequestService.getCustomerReturnRequests(customerId);
    }

    public void reviewAProduct(Integer customerId, Integer productId, ProductReviewInDTO productReviewInDTO) {//Waleed
        Customer customer = customerRepository.findCustomerById(customerId);
        if (customer == null) throw new ApiException("Customer not found");

        productReviewService.reviewAProduct(customer, productId, productReviewInDTO);
    }

    public void updateAProductReview(Integer customerId, Integer productReviewId, ProductReviewInDTO
            productReviewInDTO) {//Waleed
        Customer customer = customerRepository.findCustomerById(customerId);
        if (customer == null) throw new ApiException("Customer not found");

        productReviewService.updateReview(customer, productReviewId, productReviewInDTO);
    }

    public List<BlackListPardonRequestOutDTO> getCustomerBlackListPardonRequests(Integer customerId) {//Waleed
        Customer customer = customerRepository.findCustomerById(customerId);
        if (customer == null) throw new ApiException("Customer not found");

        return blackListPardonRequestService.getCustomerBlackListPardonRequests(customer);
    }

    public void requestAPardon(Integer customerId, Integer vendorId, BlackListPardonRequestCustomerInDTO
            blackListPardonRequestCustomerInDTO) {//Waleed
        Customer customer = customerRepository.findCustomerById(customerId);
        if (customer == null) throw new ApiException("Customer not found");

        blackListPardonRequestService.requestAPardon(customer, vendorId, blackListPardonRequestCustomerInDTO);
    }

    public void updateAPardonRequest(Integer customerId, Integer pardonId, BlackListPardonRequestCustomerInDTO
            blackListPardonRequestCustomerInDTO) {//Waleed
        Customer customer = customerRepository.findCustomerById(customerId);
        if (customer == null) throw new ApiException("Customer not found");

        blackListPardonRequestService.updateAPardonRequest(customer, pardonId, blackListPardonRequestCustomerInDTO);
    }

    public void pay(Integer customerId, Integer dealId) {//Waleed
        CustomerProfile customer = customerProfileRepository.findCustomerProfileById(customerId);
        if (customer == null) throw new ApiException("Customer not found");

        customerDealService.pay(customer, dealId);
    }

    public void payWithPoints(Integer customerId, Integer dealId) {//Waleed
        CustomerProfile customer = customerProfileRepository.findCustomerProfileById(customerId);
        if (customer == null) throw new ApiException("Customer not found");

        customerDealService.payWithPoints(customer, dealId);
    }

    public void declineToPay(Integer customerId, Integer dealId) {//Waleed
        CustomerProfile customer = customerProfileRepository.findCustomerProfileById(customerId);
        if (customer == null) throw new ApiException("Customer not found");

        customerDealService.rejectPay(customer, dealId);
    }




}