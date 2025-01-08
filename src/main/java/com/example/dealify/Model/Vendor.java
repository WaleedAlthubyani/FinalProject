package com.example.dealify.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity

public class Vendor { //Ebtehal
    @Id
    private Integer id;

    @Column(columnDefinition = "varchar(10) not null unique")
    private String commercialRegistration;

    @Column(columnDefinition = "varchar(10)")
    @Pattern(regexp = "^(?i)(Active|Inactive)$")
    private String status;

    @OneToOne
    @MapsId
    @JsonIgnore
    private MyUser myUser;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "vendor")
    @PrimaryKeyJoinColumn
    private VendorProfile vendorProfile;
}