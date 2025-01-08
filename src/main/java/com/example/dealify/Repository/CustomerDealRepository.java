package com.example.dealify.Repository;

import com.example.dealify.Model.CustomerDeal;
import com.example.dealify.Model.CustomerProfile;
import com.example.dealify.Model.Deal;
import com.example.dealify.Model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

// Waleed
@Repository
public interface CustomerDealRepository extends JpaRepository<CustomerDeal,Integer> {

    CustomerDeal findCustomerDealById(Integer id);

    List<CustomerDeal> findCustomerDealsByDealAndJoinedAtAfterOrderByJoinedAtAsc(Deal deal, LocalDateTime localDateTime);

    @Query("select case when count(cd.deal)>0 then true else false end " +
            "from CustomerDeal cd where cd.customer in (?1,?2) and cd.deal.status='ompleted'" +
            "group by cd.deal having count(distinct cd.customer)>=3")
    Boolean haveCustomersJoinedSameCompletedDeal(CustomerProfile customerProfile1,CustomerProfile customerProfile2);

    CustomerDeal findCustomerDealByCustomerAndDeal(CustomerProfile customerProfile,Deal deal);

    List<CustomerDeal> findCustomerDealsByDealAndStatus(Deal deal,String status);

    List<CustomerDeal> findCustomerDealsByCustomerAndPayMethod(CustomerProfile customer,String payMethod);

    @Query("select case when count(cd)>0 then true else false end " +
            "from CustomerDeal cd where cd.customer=?1 and cd.status='Joined' and cd.deal.status='Open' and cd.deal.product=?2")
    Boolean checkIfCustomerJoinedAnotherDealForThisProduct(CustomerProfile customer, Product product);
}
