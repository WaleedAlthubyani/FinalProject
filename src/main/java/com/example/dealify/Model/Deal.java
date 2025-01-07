package com.example.dealify.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
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
public class Deal {//Waleed
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "int not null")
    private Integer currentParticipants;

    @Column(columnDefinition = "int not null")
    private Integer participantsLimit;

    @Column(columnDefinition = "varchar(9) not null")
    @Pattern(regexp = "^(?i)(Open|Completed)$")
    private String status;

    @Column(columnDefinition = "int not null")
    private Integer quantity;

    @Column(columnDefinition = "timestamp not null")
    private LocalDateTime startedAt;

    @Column(columnDefinition = "timestamp not null")
    private LocalDateTime endsAt;

    @ManyToOne
    @JsonIgnore
    private Product product;

    @OneToMany(cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<ReturnRequest> returnRequests;

    @ManyToOne
    @JsonIgnore
    private CustomerProfile creator;

    @OneToMany(cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<CustomerDeal> customerDeals;
}