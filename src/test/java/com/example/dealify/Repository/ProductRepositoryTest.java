package com.example.dealify.Repository;

import com.example.dealify.Model.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;


import java.time.LocalDateTime;
import java.util.List;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ProductRepositoryTest {

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    AuthRepository authRepository;

    @Autowired
    VendorRepository vendorRepository;

    @Autowired
    VendorProfileRepository vendorProfileRepository;

    @Autowired
    InventoryRepository inventoryRepository;

    Product product,product1,product2,product3;

    Category category;

    List<Product> products;

    MyUser myUser;
    Vendor vendor;
    VendorProfile vendorProfile;
    Inventory inventory;

    @BeforeEach
    void setUp(){

        category=new Category(null,"Electronic AP",null);
        categoryRepository.save(category);

        product=new Product(null,"TV","LG","60' LED TV","www.google.com",1400.5,25,"12512512",true,category,null,null,null,null,null);
        product1=new Product(null,"Fridge","LG","Large refrigerator","www.google.com",2558.0,5,"12512513",true,category,null,null,null,null,null);
        product2=new Product(null,"Air Conditioner","LG","split","www.google.com",140.5,50,"12512514",true,category,null,null,null,null,null);
        productRepository.save(product);
        productRepository.save(product1);
        productRepository.save(product2);


        myUser=new MyUser(null,"Mohammed","MohammedLi","Mohammed@gmail.com","123456ABCd@","VENDOR",null,null,null);
        authRepository.save(myUser);
        vendor=new Vendor(null,"0592787142","123456789","Active",myUser,null);
        vendorRepository.save(vendor);
        vendorProfile=new VendorProfile(null,"ElectoDeal","NOEASCA","Riyadh",vendor,null,null,null,null);
        vendorProfileRepository.save(vendorProfile);
        inventory=new Inventory(null,55,205, LocalDateTime.now(),null,vendorProfile);
        inventoryRepository.save(inventory);

        product3=new Product(null,"Cooler","LG","Compact","www.google.com",221.5,55,"12812514",true,category,inventory,null,null,null,null);
        productRepository.save(product3);


    }

    @Test
    void findProductByIdTesting() {
        Product productA=productRepository.findProductById(product.getId());
        Assertions.assertThat(productA).isEqualTo(product);
    }

    @Test
    void findProductsByCategoryNameTesting() {
        products=productRepository.findProductsByCategoryName(category.getName());
        Assertions.assertThat(products.get(0).getId()).isEqualTo(product.getId());
    }

    @Test
    void findByStockLessThanTesting() {
        products=productRepository.findByStockLessThan(10);
        Assertions.assertThat(products.get(0).getId()).isEqualTo(product1.getId());

    }

    @Test
    void findByPriceBetweenTesting() {
        products=productRepository.findByPriceBetween(100.0,200.5);
        Assertions.assertThat(products.get(0).getId()).isEqualTo(product2.getId());

    }

    @Test
    void findProductByInventory() {
        products=productRepository.findProductByInventory(inventory);
        Assertions.assertThat(products.get(0).getId()).isEqualTo(product3.getId());
    }
}
