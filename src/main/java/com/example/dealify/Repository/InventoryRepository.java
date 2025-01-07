package com.example.dealify.Repository;

import com.example.dealify.Model.Inventory;
import com.example.dealify.Model.VendorProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//Renad
@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Integer> {

    Inventory findInventoryByVendorProfile(VendorProfile vendorProfile);
}