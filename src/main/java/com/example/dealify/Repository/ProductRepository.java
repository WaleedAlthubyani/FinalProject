package com.example.dealify.Repository;

import com.example.dealify.Model.Inventory;
import com.example.dealify.Model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

//Renad
@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    Product findProductById(Integer id);
    @Query("SELECT p FROM Product p WHERE p.category.name = :categoryName")
    List<Product> findProductsByCategoryName(@Param("categoryName") String categoryName);
    List<Product> findByStockLessThan(Integer stock);
    List<Product> findProductByInventory(Inventory inventory);
    List<Product> findByPriceBetween(Double minPrice, Double maxPrice);
}