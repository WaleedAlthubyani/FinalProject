package com.example.dealify.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Inventory { //Renad
    @Id
    private Integer id;

    @Column(columnDefinition = "int")
    private Integer availableQuantity;

    @Column(columnDefinition = "int")
    private Integer soldQuantity;

    @Column(columnDefinition = "timestamp")
    private LocalDateTime updatedAt;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "inventory")
    private Set<Product> products;

    @OneToOne
    @MapsId
    @JsonIgnore
    private VendorProfile vendorProfile;
}