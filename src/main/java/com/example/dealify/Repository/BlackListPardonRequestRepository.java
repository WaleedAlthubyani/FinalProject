package com.example.dealify.Repository;

import com.example.dealify.Model.BlackListPardonRequest;
import com.example.dealify.Model.Customer;
import com.example.dealify.Model.VendorProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

//Waleed
@Repository
public interface BlackListPardonRequestRepository extends JpaRepository<BlackListPardonRequest,Integer> {

    List<BlackListPardonRequest> findBlackListPardonRequestsByCustomer(Customer customer);

    List<BlackListPardonRequest> findBlackListPardonRequestsByVendor(VendorProfile vendor);

    BlackListPardonRequest findBlackListPardonRequestByCustomerAndVendor(Customer customer,VendorProfile vendor);

    BlackListPardonRequest findBlackListPardonRequestById(Integer id);
}
