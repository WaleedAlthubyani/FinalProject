package com.example.dealify;

import com.example.dealify.InDTO.VendorInDTO;
import com.example.dealify.Model.*;
import com.example.dealify.Repository.*;
import com.example.dealify.Service.BlackListService;
import com.example.dealify.Service.DealService;
import com.example.dealify.Service.VendorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VendorServiceTest {

    @InjectMocks
    private VendorService vendorService;

    @Mock
    private AuthRepository authRepository;

    @Mock
    private VendorRepository vendorRepository;

    @Mock
    private VendorProfileRepository vendorProfileRepository;

    @Mock
    private InventoryRepository inventoryRepository;

    @Mock
    private DealService dealService;

    @Mock
    private BlackListService blackListService;

    private VendorInDTO vendorInDTO;
    private Vendor vendor;
    private MyUser myUser;
    private VendorProfile vendorProfile;


    //Ebtehal
    @BeforeEach
    void setUp() {
        vendorInDTO = new VendorInDTO();
        vendorInDTO.setName("John Vendor");
        vendorInDTO.setUsername("johnvendor");
        vendorInDTO.setEmail("john.vendor@example.com");
        vendorInDTO.setPassword("password");
        vendorInDTO.setPhoneNumber("+966512345678");
        vendorInDTO.setCommercialRegistration("CR123456");

        myUser = new MyUser();
        myUser.setId(1);
        myUser.setName("John Vendor");
        myUser.setUsername("johnvendor");
        myUser.setEmail("john.vendor@example.com");
        myUser.setRole("VENDOR");

        vendor = new Vendor();
        vendor.setId(1);
        vendor.setPhoneNumber("+966512345678");
        vendor.setCommercialRegistration("CR123456");
        vendor.setStatus("Inactive");
        vendor.setMyUser(myUser);
        myUser.setVendor(vendor);

        vendorProfile = new VendorProfile();
        vendorProfile.setVendor(vendor);
        vendor.setVendorProfile(vendorProfile);
    }

    @Test
    public void registerVendorTest() {
        when(authRepository.existsByUsername(any(String.class))).thenReturn(false);
        when(authRepository.existsByEmail(any(String.class))).thenReturn(false);
        when(vendorRepository.existsByCommercialRegistration(any(String.class))).thenReturn(false);
        when(vendorRepository.existsByPhoneNumber(any(String.class))).thenReturn(false);

        vendorService.register(vendorInDTO);

        verify(authRepository, times(1)).existsByUsername(any(String.class));
        verify(authRepository, times(1)).existsByEmail(any(String.class));
        verify(vendorRepository, times(1)).existsByCommercialRegistration(any(String.class));
        verify(vendorRepository, times(1)).existsByPhoneNumber(any(String.class));
        verify(authRepository, times(1)).save(any(MyUser.class));
        verify(vendorRepository, times(1)).save(any(Vendor.class));
        verify(vendorProfileRepository, times(1)).save(any(VendorProfile.class));
        verify(inventoryRepository, times(1)).save(any(Inventory.class));
    }

    //Renad
    @Test
    public void updateVendorTest() {
        when(authRepository.findMyUserById(any(Integer.class))).thenReturn(myUser);

        VendorInDTO updatedVendorInDTO = new VendorInDTO();
        updatedVendorInDTO.setName("Updated Name");
        updatedVendorInDTO.setUsername("updatedusername");
        updatedVendorInDTO.setEmail("updated.email@example.com");
        updatedVendorInDTO.setPassword("updatedpassword");
        updatedVendorInDTO.setPhoneNumber("+966512345679");
        updatedVendorInDTO.setCommercialRegistration("CR654321");

        vendorService.updateVendor(myUser.getId(), updatedVendorInDTO);

        verify(authRepository, times(1)).findMyUserById(any(Integer.class));
        verify(authRepository, times(1)).save(any(MyUser.class));
    }

    @Test
    public void deleteVendorTest() {
        when(authRepository.findMyUserById(any(Integer.class))).thenReturn(myUser);
        when(vendorProfileRepository.findVendorProfileById(any(Integer.class))).thenReturn(vendorProfile);
        when(inventoryRepository.findInventoryByVendorProfile(any(VendorProfile.class))).thenReturn(new Inventory());

        vendorService.deleteMyAccount(myUser.getId());

        verify(authRepository, times(1)).findMyUserById(any(Integer.class));
        verify(vendorProfileRepository, times(1)).findVendorProfileById(any(Integer.class));
        verify(inventoryRepository, times(1)).findInventoryByVendorProfile(any(VendorProfile.class));
        verify(inventoryRepository, times(1)).delete(any(Inventory.class));
        verify(vendorProfileRepository, times(1)).delete(any(VendorProfile.class));
        verify(authRepository, times(1)).delete(any(MyUser.class));
    }

    @Test
    public void viewVendorsOpenDealsTest() {
        when(vendorProfileRepository.findVendorProfileById(any(Integer.class))).thenReturn(vendorProfile);

        vendorService.viewVendorsOpenDeals(vendorProfile.getVendor().getId());

        verify(vendorProfileRepository, times(1)).findVendorProfileById(any(Integer.class));
        verify(dealService, times(1)).viewVendorsOpenDeals(any(VendorProfile.class));
    }

    @Test
    public void activateVendorTest() {
        MyUser admin = new MyUser();
        admin.setId(1);
        admin.setRole("ADMIN");

        when(authRepository.findMyUserById(any(Integer.class))).thenReturn(admin);
        when(vendorRepository.findVendorById(any(Integer.class))).thenReturn(vendor);

        vendorService.activateVendor(admin.getId(), vendor.getId());

        verify(authRepository, times(1)).findMyUserById(any(Integer.class));
        verify(vendorRepository, times(1)).findVendorById(any(Integer.class));
        verify(vendorRepository, times(1)).save(any(Vendor.class));
    }}