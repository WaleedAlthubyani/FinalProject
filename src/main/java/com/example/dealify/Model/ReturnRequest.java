package com.example.dealify.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ReturnRequest { //Renad
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "varchar(300) not null")
    private String reason;

    @Column(columnDefinition = "varchar(300) not null")
    private String response;

    // Pending - Approved - Rejected
    @Column(columnDefinition = "varchar(8)")
    @Pattern(regexp = "^(?i)(Pending|Approved|Rejected)$")
    private String status;

    @Column(columnDefinition = "timestamp")
    private LocalDateTime requestDate;

    @Column(columnDefinition = "timestamp")
    private LocalDateTime responseDate;

    @ManyToOne
    @JsonIgnore
    private CustomerProfile customer;

    @ManyToOne
    @JsonIgnore
    private VendorProfile vendorProfile;

    @ManyToOne
    @JsonIgnore
    private Deal deal;

    @ManyToOne
    @JsonIgnore
    private Product product;
}