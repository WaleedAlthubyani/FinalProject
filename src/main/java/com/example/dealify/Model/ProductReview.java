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
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class ProductReview {//Waleed

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "double not null")
    private Double overallRating;

    @Column(columnDefinition = "int not null")
    private Integer qualityRating;

    @Column(columnDefinition = "int not null")
    private Integer packageRating;

    @Column(columnDefinition = "varchar(255) not null")
    private String comment;

    @Column(columnDefinition = "date not null")
    private LocalDate createdAt;

    @Column(columnDefinition = "timestamp")
    private LocalDate updatedAt;

    @ManyToOne
    private Customer customer;

    @ManyToOne
    @JsonIgnore
    private Product product;
}
