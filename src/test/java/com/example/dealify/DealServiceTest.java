package com.example.dealify;
import com.example.dealify.InDTO.DealCreationInDTO;
import com.example.dealify.InDTO.DealJoinInDTO;
import com.example.dealify.Model.*;
import com.example.dealify.OutDTO.DealOutDTO;
import com.example.dealify.Repository.BlackListRepository;
import com.example.dealify.Repository.DealRepository;
import com.example.dealify.Repository.ProductRepository;
import com.example.dealify.Service.CustomerDealService;
import com.example.dealify.Service.DealService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DealServiceTest { // Renad

    @InjectMocks
    private DealService dealService;

    @Mock
    private DealRepository dealRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private BlackListRepository blackListRepository;

    @Mock
    private CustomerDealService customerDealService;

    private Product product;
    private CustomerProfile customerProfile;
    private VendorProfile vendorProfile;
    private Inventory inventory;
    private Deal deal1, deal2;
    private List<Deal> deals;

    @BeforeEach
    void setUp() {
        vendorProfile = new VendorProfile();
        vendorProfile.setId(1);

        inventory = new Inventory();
        inventory.setVendorProfile(vendorProfile);

        product = new Product();
        product.setId(1);
        product.setInventory(inventory);

        customerProfile = new CustomerProfile();
        customerProfile.setId(1);

        deal1 = new Deal();
        deal1.setId(1);

        deal2 = new Deal();
        deal2.setId(2);

        deals = new ArrayList<>();
        deals.add(deal1);
        deals.add(deal2);
    }


//    @Test
//    public void createDealTest() {
//        when(productRepository.findProductById(product.getId())).thenReturn(product);
//        when(blackListRepository.findBlackListByCustomerAndVendor(any(), any())).thenReturn(null);
//        when(dealRepository.findDealByProductAndParticipantsLimitAndStatus(any(Product.class), anyInt(), eq("Open"))).thenReturn(null);
//
//        DealCreationInDTO dealDTO = new DealCreationInDTO(500,"10");
//        dealService.createDeal(customerProfile, product.getId(), dealDTO);
//
//        verify(dealRepository, times(1)).save(any(Deal.class));
//        verify(customerDealService, times(1)).joinDeal(any(CustomerProfile.class), any(DealJoinInDTO.class), anyInt());
//    }

    @Test
    public void viewProductDealsTest() {
        when(productRepository.findProductById(product.getId())).thenReturn(product);
        when(dealRepository.findDealsByProductAndStatus(product, "open")).thenReturn(deals);

        List<DealOutDTO> productDeals = dealService.viewProductDeals(product.getId());
        assertEquals(2, productDeals.size());
        verify(dealRepository, times(1)).findDealsByProductAndStatus(product, "open");
    }

    @Test
    public void viewCustomerOpenedDealsTest() {
        when(dealRepository.findDealsByCustomerAndOpen(customerProfile, "open")).thenReturn(deals);

        List<DealOutDTO> openedDeals = dealService.viewCustomerOpenedDeals(customerProfile);
        assertEquals(2, openedDeals.size());
        verify(dealRepository, times(1)).findDealsByCustomerAndOpen(customerProfile, "open");
    }

    @Test
    public void viewVendorsOpenDealsTest() { when(dealRepository.findDealsByVendorAndOpen(anyInt(), eq("open"))).thenReturn(deals); List<DealOutDTO> openDeals = dealService.viewVendorsOpenDeals(vendorProfile); assertEquals(2, openDeals.size()); verify(dealRepository, times(1)).findDealsByVendorAndOpen(anyInt(), eq("open")); }
}


