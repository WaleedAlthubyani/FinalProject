package com.example.dealify.Repository;

import com.example.dealify.Model.Product;
import com.example.dealify.Model.ProductReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

//Waleed
@Repository
public interface ProductReviewRepository extends JpaRepository<ProductReview,Integer> {//Waleed

    ProductReview findProductReviewById(Integer id);

    List<ProductReview> findProductReviewsByProduct(Product product);
}