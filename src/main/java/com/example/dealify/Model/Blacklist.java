package com.example.dealify.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Blacklist { //Waleed

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "varchar(255) not null")
    private String reason;

    @Column(columnDefinition = "timestamp not null")
    private LocalDate addedDate;

    @ManyToOne
    @JsonIgnore
    private VendorProfile vendor;

    @ManyToOne
    private Customer customer;
}