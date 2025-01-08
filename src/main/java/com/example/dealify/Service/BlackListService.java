package com.example.dealify.Service;

import com.example.dealify.Api.ApiException;
import com.example.dealify.InDTO.BlackListInDTO;
import com.example.dealify.Model.Blacklist;
import com.example.dealify.Model.Customer;
import com.example.dealify.Model.VendorProfile;
import com.example.dealify.OutDTO.BlackListOutDTO;
import com.example.dealify.Repository.BlackListRepository;
import com.example.dealify.Repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BlackListService {

    private final BlackListRepository blackListRepository;
    private final CustomerRepository customerRepository;

    public List<Blacklist> getAllBlacklists(){//Waleed
        return blackListRepository.findAll();
    }

    public List<BlackListOutDTO> getVendorBlackList(VendorProfile vendorProfile){//Waleed

        List<Blacklist> blackLists=blackListRepository.findBlackListsByVendor(vendorProfile);
        if (blackLists==null) throw new ApiException("Black list not found");

        List<BlackListOutDTO> blackListOutDTOS=new ArrayList<>();
        for (Blacklist bl:blackLists){
            blackListOutDTOS.add(new BlackListOutDTO(bl.getCustomer().getMyUser().getFullName(),bl.getReason(),bl.getAddedDate()));
        }

        return blackListOutDTOS;
    }

    public void addCustomerToBlacklist(VendorProfile vendorProfile, Integer customerId, BlackListInDTO blackListInDTO){
        Customer customer=customerRepository.findCustomerById(customerId);
        if (customer==null) throw new ApiException("Customer not found");

        Blacklist blackList=new Blacklist();

        blackList.setVendor(vendorProfile);
        blackList.setCustomer(customer);
        blackList.setReason((blackListInDTO.getReason()));
        blackList.setAddedDate(LocalDate.now());

        blackListRepository.save(blackList);
    }

    public void removeCustomerFromBlackList(VendorProfile vendorProfile,Integer customerId){
        Customer customer=customerRepository.findCustomerById(customerId);

        Blacklist blackList=blackListRepository.findBlackListByCustomerAndVendor(customer,vendorProfile);

        if (blackList==null) throw new ApiException("Customer is not in the blacklist");

        blackListRepository.delete(blackList);
    }
}
