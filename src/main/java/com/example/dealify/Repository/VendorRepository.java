package com.example.dealify.Repository;

import com.example.dealify.Model.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VendorRepository extends JpaRepository<Vendor,Integer> {
    Vendor findVendorById(Integer id);

    boolean existsByCommercialRegistration(String commercialRegistration);
    boolean existsByMyUser_PhoneNumber(String phoneNumber);
}