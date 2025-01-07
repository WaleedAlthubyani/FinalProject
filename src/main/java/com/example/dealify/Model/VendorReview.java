package com.example.dealify.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@Entity
@NoArgsConstructor
public class VendorReview {//Waleed

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "double not null")
    private Double overallRating;

    @Column(columnDefinition = "int not null")
    private Integer serviceRating;

    @Column(columnDefinition = "int not null")
    private Integer productQualityRating;

    @Column(columnDefinition = "int not null")
    private Integer deliveryRating;

    @Column(columnDefinition = "int not null")
    private Integer returnPolicyRating;

    @Column(columnDefinition = "varchar(255) not null")
    private String comment;

    @Column(columnDefinition = "timestamp not null")
    private LocalDate createdAt;

    @Column(columnDefinition = "timestamp null")
    private LocalDate updatedAt;

    @ManyToOne
    private Customer customer;

    @ManyToOne
    @JsonIgnore
    private VendorProfile vendor;
}