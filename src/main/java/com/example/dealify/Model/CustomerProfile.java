package com.example.dealify.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class CustomerProfile { //Ebtehal
    @Id
    private Integer id;

    @Column(columnDefinition = "varchar(10)")
    @Size(min = 3, max = 10, message = " city name can not be less than 3 or more than 10")
    private String city;

    @Column(columnDefinition = "varchar(10)")
    @Size(min = 3, max = 10, message = "Street name can not be less than 3 or more than 10")
    private String street;

    @Column(columnDefinition = "varchar(10)")
    @Size(min = 3, max = 10, message = "Street name can not be less than 3 or more than 10")
    private String district;

    @OneToOne
    @MapsId
    @JsonIgnore
    private Customer customer;

    @OneToMany(mappedBy = "creator")
    private Set<Deal> deals;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "customer")
    private Set<ReturnRequest> returnRequests;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "customer")
    private Set<CustomerReview> customerReview;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "customer")
    private Set<Favorite> favorites;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "customer")
    private Set<CustomerDeal> customerDeals;
}