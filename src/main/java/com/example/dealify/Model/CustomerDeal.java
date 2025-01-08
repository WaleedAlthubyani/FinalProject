package com.example.dealify.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Entity
@NoArgsConstructor
public class CustomerDeal {  //Waleed
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Integer id;

    @Column(columnDefinition = "int not null")
    private Integer quantity;

    @Column(columnDefinition = "double not null")
    private Double originalPrice;

    @Column(columnDefinition = "double not null")
    private Double discountedPrice;

    //Pending-Normal-Points
    @Column(columnDefinition = "varchar(7)")
    private String payMethod="Pending";

    @CreationTimestamp
    @Column(columnDefinition = "timestamp not null")
    private LocalDateTime joinedAt;

    @Column(columnDefinition = "timestamp")
    private LocalDateTime updatedAt;

    //Joined-Left
    @Column(columnDefinition = "varchar(6)")
    private String status;

    @ManyToOne
    @JsonIgnore
    private CustomerProfile customer;

    @ManyToOne
    @JsonIgnore
    private Deal deal;
}