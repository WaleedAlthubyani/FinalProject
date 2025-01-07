package com.example.dealify.Repository;

import com.example.dealify.Model.VendorProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VendorProfileRepository extends JpaRepository<VendorProfile, Integer> {
    VendorProfile findVendorProfileById(Integer vendorId);

    List<VendorProfile> findVendorProfileByCity(String city);
}