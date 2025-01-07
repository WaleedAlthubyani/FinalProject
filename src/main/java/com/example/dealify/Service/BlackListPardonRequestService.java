package com.example.dealify.Service;

import com.example.dealify.Api.ApiException;
import com.example.dealify.InDTO.BlackListPardonRequestCustomerInDTO;
import com.example.dealify.InDTO.BlackListPardonRequestVendorInDTO;
import com.example.dealify.Model.Blacklist;
import com.example.dealify.Model.BlackListPardonRequest;
import com.example.dealify.Model.Customer;
import com.example.dealify.Model.VendorProfile;
import com.example.dealify.OutDTO.BlackListPardonRequestOutDTO;
import com.example.dealify.Repository.BlackListPardonRequestRepository;
import com.example.dealify.Repository.BlackListRepository;
import com.example.dealify.Repository.VendorProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BlackListPardonRequestService {

    private final BlackListPardonRequestRepository blackListPardonRequestRepository;
    private final VendorProfileRepository vendorProfileRepository;
    private final BlackListRepository blackListRepository;

    public List<BlackListPardonRequestOutDTO> getVendorBlackListPardonRequests(VendorProfile vendor){
        List<BlackListPardonRequest> blackListPardonRequests=blackListPardonRequestRepository.findBlackListPardonRequestsByVendor(vendor);

        List<BlackListPardonRequestOutDTO> blackListPardonRequestOutDTOS=new ArrayList<>();
        for (BlackListPardonRequest b:blackListPardonRequests){
            blackListPardonRequestOutDTOS.add(new BlackListPardonRequestOutDTO(b.getCustomer().getMyUser().getName(),b.getReason(),b.getStatus(),b.getVendor().getVendor().getMyUser().getName(),b.getResponse(),b.getRequestDate(),b.getResponseDate()));
        }

        return blackListPardonRequestOutDTOS;
    }


    public List<BlackListPardonRequestOutDTO> getCustomerBlackListPardonRequests(Customer customer){

        List<BlackListPardonRequest> blackListPardonRequests=blackListPardonRequestRepository.findBlackListPardonRequestsByCustomer(customer);

        List<BlackListPardonRequestOutDTO> blackListPardonRequestOutDTOS=new ArrayList<>();
        for (BlackListPardonRequest b:blackListPardonRequests){
            blackListPardonRequestOutDTOS.add(new BlackListPardonRequestOutDTO(b.getCustomer().getMyUser().getName(),b.getReason(),b.getStatus(),b.getVendor().getVendor().getMyUser().getName(),b.getResponse(),b.getRequestDate(),b.getResponseDate()));
        }

        return blackListPardonRequestOutDTOS;
    }

    public void requestAPardon(Customer customer, Integer vendorId, BlackListPardonRequestCustomerInDTO blackListPardonRequestCustomerInDTO){ //EBTEHAL
        VendorProfile vendorProfile= vendorProfileRepository.findVendorProfileById(vendorId);
        Blacklist blackList=blackListRepository.findBlackListByCustomerAndVendor(customer,vendorProfile);

        if (blackList==null) throw new ApiException("Customer is not blacklisted by this vendor");

        if (blackListPardonRequestRepository.findBlackListPardonRequestByCustomerAndVendor(customer,vendorProfile)!=null)
            throw new ApiException("Can't send another pardon request");

        BlackListPardonRequest blackListPardonRequest = new BlackListPardonRequest();

        blackListPardonRequest.setCustomer(customer);
        blackListPardonRequest.setVendor(vendorProfile);
        blackListPardonRequest.setReason(blackListPardonRequestCustomerInDTO.getReason());
        blackListPardonRequest.setStatus("Pending");
        blackListPardonRequest.setRequestDate(LocalDateTime.now());
        blackListPardonRequest.setResponse("");

        blackListPardonRequestRepository.save(blackListPardonRequest);
    }

    public void updateAPardonRequest(Customer customer,Integer pardonId, BlackListPardonRequestCustomerInDTO blackListPardonRequestCustomerInDTO){
        BlackListPardonRequest blackListPardonRequest=blackListPardonRequestRepository.findBlackListPardonRequestById(pardonId);
        if (blackListPardonRequest==null) throw new ApiException("Pardon request not found");

        if (!blackListPardonRequest.getCustomer().equals(customer))
            throw new ApiException("Can't update another customer request");

        if (!blackListPardonRequest.getStatus().equalsIgnoreCase("Pending"))
            throw new ApiException("Can't update a request already decided on");

        blackListPardonRequest.setReason(blackListPardonRequestCustomerInDTO.getReason());

        blackListPardonRequestRepository.save(blackListPardonRequest);
    }

    public void approvePardonRequest(VendorProfile vendor, Integer pardonRequestId, BlackListPardonRequestVendorInDTO blackListPardonRequestVendorInDTO){
        BlackListPardonRequest blackListPardonRequest=blackListPardonRequestRepository.findBlackListPardonRequestById(pardonRequestId);
        if (blackListPardonRequest==null) throw new ApiException("Pardon request not found");

        if (!blackListPardonRequest.getVendor().equals(vendor))
            throw new ApiException("Can't accept a pardon request of another vendor");

        blackListPardonRequest.setResponse(blackListPardonRequestVendorInDTO.getResponse());
        blackListPardonRequest.setResponseDate(LocalDateTime.now());
        blackListPardonRequest.setStatus("Approved");

        blackListPardonRequestRepository.save(blackListPardonRequest);

        Blacklist blackList=blackListRepository.findBlackListByCustomerAndVendor(blackListPardonRequest.getCustomer(),blackListPardonRequest.getVendor());
        blackListRepository.delete(blackList);
    }

    public void rejectPardonRequest(VendorProfile vendor,Integer pardonRequestId,BlackListPardonRequestVendorInDTO blackListPardonRequestVendorInDTO){
        BlackListPardonRequest blackListPardonRequest=blackListPardonRequestRepository.findBlackListPardonRequestById(pardonRequestId);

        if (blackListPardonRequest==null) throw new ApiException("Pardon request not found");

        if (!blackListPardonRequest.getVendor().equals(vendor))
            throw new ApiException("Can't reject another vendor pardon request");

        blackListPardonRequest.setResponse(blackListPardonRequestVendorInDTO.getResponse());
        blackListPardonRequest.setResponseDate(LocalDateTime.now());
        blackListPardonRequest.setStatus("Rejected");

        blackListPardonRequestRepository.save(blackListPardonRequest);
    }

}
