package com.example.dealify.Repository;

import com.example.dealify.Model.CustomerProfile;
import com.example.dealify.Model.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite,Integer> {//Waleed

    Favorite findFavoriteByCustomer(CustomerProfile customer);

}
