package com.example.dealify.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class CustomerReview { //Renad
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "Rating is required.")
    @Min(value = 1, message = "Rating must be at least 1.")
    @Max(value = 5, message = "Rating must be no more than 5.")
    @Column(columnDefinition = "int not null")
    private Integer rating;

    @Size(max = 500, message = "Comment can't exceed 500 characters.")
    @Column(columnDefinition = "varchar(500)")
    private String comment;

    @Column(columnDefinition = "timestamp")
    private LocalDate createdAt;

    @Column(columnDefinition = "timestamp")
    private LocalDate updatedAt;

    @ManyToOne
    @JsonIgnore
    private CustomerProfile customer; // Who's being reviewed

    @ManyToOne
    private MyUser sender; // Who review
}