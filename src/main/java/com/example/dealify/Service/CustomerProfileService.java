package com.example.dealify.Service;

import com.example.dealify.Api.ApiException;
import com.example.dealify.InDTO.CustomerProfileInDTO;
import com.example.dealify.Model.Customer;
import com.example.dealify.Model.CustomerProfile;
import com.example.dealify.Repository.CustomerProfileRepository;
import com.example.dealify.Repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerProfileService { //Ebtehal
    private final CustomerRepository customerRepository;
    private final CustomerProfileRepository customerProfileRepository;

    public void updateCustomerProfile(Integer customerId, CustomerProfileInDTO customerProfileInDTO) {

        Customer customer = customerRepository.findCustomerById(customerId);
        if (customer == null) {
            throw new ApiException("Customer not found");
        }

        CustomerProfile customerProfile = customerProfileRepository.findCustomerProfileById(customerId);
        if (customerProfile == null) {
            throw new ApiException("Customer profile not found");
        }

        customerProfile.setCity(customerProfileInDTO.getCity());
        customerProfile.setStreet(customerProfileInDTO.getStreet());
        customerProfile.setDistrict(customerProfileInDTO.getDistrict());
        customerProfileRepository.save(customerProfile);
    }
}