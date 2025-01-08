package com.example.dealify.Repository;

import com.example.dealify.Model.CustomerProfile;
import com.example.dealify.Model.Deal;
import com.example.dealify.Model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DealRepository extends JpaRepository<Deal, Integer> {//Waleed

    List<Deal> findDealsByStatusAndEndsAtIsBetween(String status, LocalDateTime localDateTime, LocalDateTime localDateTime1);

    Deal findDealById(Integer id);

    List<Deal> findDealsByProduct(Product product);

    List<Deal> findDealsByProductAndStatus(Product product, String status);

    @Query("select d from Deal d join d.customerDeals c where c.customer=?1 and d.status=?2")
    List<Deal> findDealsByCustomerAndOpen(CustomerProfile customer, String status);

    @Query("select d from Deal d join d.customerDeals c where c.customer=?1 and d.status=?2")
    List<Deal> findDealsByCustomerAndCompleted(CustomerProfile customer, String status);

    @Query("select d from Deal d join d.product p where p.inventory.id=?1 and d.status=?2")
    List<Deal> findDealsByVendorAndOpen(Integer id, String status);

    Deal findDealByProductAndParticipantsLimitAndStatus(Product product, Integer participantsLimit,String status);
}