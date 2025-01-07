package com.example.dealify.Repository;

import com.example.dealify.Model.Blacklist;
import com.example.dealify.Model.Customer;
import com.example.dealify.Model.VendorProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

//Waleed
@Repository
public interface BlackListRepository extends JpaRepository<Blacklist,Integer> {

    Blacklist findBlackListByCustomerAndVendor(Customer customer, VendorProfile vendorProfile);

    List<Blacklist> findBlackListsByVendor(VendorProfile vendorProfile);
}
