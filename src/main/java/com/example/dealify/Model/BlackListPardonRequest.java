package com.example.dealify.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
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
public class BlackListPardonRequest { //Waleed

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "varchar(255) not null")
    private String reason;

    // Pending - Approved - Rejected
    @Column(columnDefinition = "varchar(8) not null")
    @Pattern(regexp = "^(?i)(Pending|Approved|Rejected)$")
    private String status;

    @Column(columnDefinition = "varchar(255) not null")
    private String response;

    @CreationTimestamp
    @Column(columnDefinition = "timestamp")
    private LocalDateTime requestDate;

    @Column(columnDefinition = "timestamp")
    private LocalDateTime responseDate;

    @ManyToOne
    private VendorProfile vendor;

    @ManyToOne
    private Customer customer;
}