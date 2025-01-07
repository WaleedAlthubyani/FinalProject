package com.example.dealify.Repository;

import com.example.dealify.Model.CustomerProfile;
import com.example.dealify.Model.ReturnRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

//Renad
@Repository
public interface ReturnRequestRepository extends JpaRepository<ReturnRequest, Integer> {
    ReturnRequest findReturnRequestById(Integer id);

    List<ReturnRequest> findReturnRequestByCustomer(CustomerProfile customer);
}