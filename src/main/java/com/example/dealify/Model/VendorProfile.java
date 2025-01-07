package com.example.dealify.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
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
public class VendorProfile {// Ebtehal

    @Id
    private Integer id;

    @Column(columnDefinition = "varchar(20)")
    private String companyName;

    @Column(columnDefinition = "varchar(10)")
    private String fundamentalFile;

    @Column(columnDefinition = "varchar(10)")
    private String city;

    @OneToOne
    @MapsId
    @JsonIgnore
    private Vendor vendor;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "vendorProfile")
    private Set<ReturnRequest> returnRequests;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "vendorProfile")
    @PrimaryKeyJoinColumn
    private Inventory inventory;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "vendor")
    private Set<VendorReview> vendorReviews;


    @OneToMany(cascade = CascadeType.ALL,mappedBy = "vendor")
    private Set<Blacklist> blackList;
}