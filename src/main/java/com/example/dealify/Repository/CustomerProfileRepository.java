package com.example.dealify.Repository;

import com.example.dealify.Model.CustomerProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//Ebtehal
@Repository
public interface CustomerProfileRepository extends JpaRepository<CustomerProfile, Integer> {
    CustomerProfile findCustomerProfileById(Integer id);
}