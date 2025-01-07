package com.example.dealify.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Customer { //Ebtehal
    @Id
    private Integer id;

    @CreationTimestamp
    @Column(columnDefinition = "timestamp not null")
    private LocalDate joinDate;

    @Column(columnDefinition = "int default 0")
    private Integer point = 0;

    @OneToOne
    @MapsId
    @JsonIgnore
    private MyUser myUser;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "customer")
    private CustomerProfile customerProfile;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "customer")
    @JsonIgnore
    private Set<VendorReview> vendorReviews;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "customer")
    @JsonIgnore
    private Set<ProductReview> productReviews;

    @OneToMany(cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Blacklist> blackList;
}