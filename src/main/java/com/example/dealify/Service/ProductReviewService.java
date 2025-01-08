package com.example.dealify.Service;

import com.example.dealify.Api.ApiException;
import com.example.dealify.InDTO.ProductReviewInDTO;
import com.example.dealify.Model.Customer;
import com.example.dealify.Model.Product;
import com.example.dealify.Model.ProductReview;
import com.example.dealify.OutDTO.ProductReviewOutDTO;
import com.example.dealify.Repository.ProductRepository;
import com.example.dealify.Repository.ProductReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductReviewService {

    private final ProductReviewRepository productReviewRepository;
    private final ProductRepository productRepository;

    public List<ProductReview> getProductReviews() {//Waleed
        return productReviewRepository.findAll();
    }

    public void deleteAProductReview(Integer productReviewId) {//Waleed
        ProductReview productReview = productReviewRepository.findProductReviewById(productReviewId);
        if (productReview == null) throw new ApiException("Product review not found");

        productReviewRepository.delete(productReview);
    }

    public List<ProductReviewOutDTO> getAllProductReviews(Product product) {
        List<ProductReview> productReviews = productReviewRepository.findProductReviewsByProduct(product);
        if (productReviews == null) throw new ApiException("Product reviews not found");

        List<ProductReviewOutDTO> productReviewOutDTOS = new ArrayList<>();
        for (ProductReview pr : productReviews) {
            productReviewOutDTOS.add(new ProductReviewOutDTO(pr.getCustomer().getMyUser().getFullName(), pr.getOverallRating(), pr.getQualityRating(), pr.getPackageRating(), pr.getComment()));
        }

        return productReviewOutDTOS;
    }

    public void reviewAProduct(Customer customer, Integer productId, ProductReviewInDTO productReviewInDTO) { // Ebtehal
        Product product = productRepository.findProductById(productId);
        if (product == null) throw new ApiException("Product not found");

        ProductReview productReview = new ProductReview();
        productReview.setProduct(product);
        productReview.setComment(productReviewInDTO.getComment());
        productReview.setQualityRating(productReviewInDTO.getQualityRating());
        productReview.setPackageRating(productReviewInDTO.getPackageRating());
        productReview.setOverallRating(((double) productReviewInDTO.getPackageRating() + (double) productReviewInDTO.getQualityRating()) / 2.0);
        productReview.setCreatedAt(LocalDate.now());
        productReview.setCustomer(customer);

        productReviewRepository.save(productReview);
    }

    public void updateReview(Customer customer, Integer productReviewId, ProductReviewInDTO productReviewInDTO) {

        ProductReview productReview = productReviewRepository.findProductReviewById(productReviewId);
        if (productReview == null) throw new ApiException("Review not found");

        if (!productReview.getCustomer().equals(customer))
            throw new ApiException("You can't update someone else's review");

        productReview.setUpdatedAt(LocalDate.now());
        productReview.setPackageRating(productReviewInDTO.getPackageRating());
        productReview.setQualityRating(productReviewInDTO.getQualityRating());
        productReview.setOverallRating(((double) productReviewInDTO.getPackageRating() + (double) productReviewInDTO.getQualityRating()) / 2.0);
        productReview.setComment(productReviewInDTO.getComment());

        productReviewRepository.save(productReview);
    }
}
