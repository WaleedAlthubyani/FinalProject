package com.example.dealify.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Product { //Renad
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "varchar(50) not null")
    private String name;

    @Column(columnDefinition = "varchar(30) not null")
    private String brand;

    @Column(columnDefinition = "varchar(800) not null")
    private String description;

    @Column(columnDefinition = "varchar(800) not null")
    private String primaryImage;

    @Column(columnDefinition = "double not null")
    private Double price;

    @Column(columnDefinition = "int not null")
    private Integer stock;

    // Unique identifier for the product used for inventory tracking and management (auto generated)
    @Column(columnDefinition = "varchar(50) unique")
    private String SKU;

    @Column(columnDefinition = "boolean default true")
    @AssertTrue
    private Boolean isAvailable = true;

    @ManyToOne
    @JsonIgnore
    private Category category;

    @ManyToOne
    @JsonIgnore
    private Inventory inventory;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "product")
    private Set<Image> images;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "product")
    private Set<Deal> deals;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "product")
    private Set<ReturnRequest> returnRequest;

    @ManyToMany
    @JsonIgnore
    private Set<Favorite> favorites;
}