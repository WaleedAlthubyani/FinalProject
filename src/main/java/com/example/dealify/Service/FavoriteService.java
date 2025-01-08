package com.example.dealify.Service;

import com.example.dealify.Api.ApiException;
import com.example.dealify.Model.CustomerProfile;
import com.example.dealify.Model.Favorite;
import com.example.dealify.Model.Product;
import com.example.dealify.OutDTO.FavoriteOutDTO;
import com.example.dealify.OutDTO.ProductOutCU;
import com.example.dealify.Repository.FavoriteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;

    public List<Favorite> getAllFavorites(){//Waleed
        return favoriteRepository.findAll();
    }

    public FavoriteOutDTO getCustomerFavorites(CustomerProfile customerProfile){//Waleed
        return convertFavoriteToDTO(favoriteRepository.findFavoriteByCustomer(customerProfile).getProduct());
    }

    public void addProductToFavorite(CustomerProfile customer,Product product){//Waleed
        Favorite favorite=favoriteRepository.findFavoriteByCustomer(customer);

        if (favorite==null) {favorite=new Favorite(); favorite.setCustomer(customer);}

        for (Product p:favorite.getProduct()){
            if (p.equals(product))
                throw new ApiException("This product is already in your favorite list");
        }

        favorite.getProduct().add(product);
        favoriteRepository.save(favorite);
    }

    public void removeProductFromFavorite(CustomerProfile customer, Product product){//Waleed
        Favorite favorite=favoriteRepository.findFavoriteByCustomer(customer);

        if (!favorite.getProduct().contains(product))
            throw new ApiException("Product is not in favorites list");

        favorite.getProduct().remove(product);

        favoriteRepository.save(favorite);
    }

    public FavoriteOutDTO convertFavoriteToDTO(Collection<Product> products){//Waleed
        List<ProductOutCU> productsDTO=new ArrayList<>();

        for (Product p:products){
            productsDTO.add(new ProductOutCU(p.getName(),p.getPrimaryImage()));
        }

        return new FavoriteOutDTO(productsDTO);
    }
}
