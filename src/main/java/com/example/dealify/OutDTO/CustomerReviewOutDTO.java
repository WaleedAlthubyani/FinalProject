package com.example.dealify.OutDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
public class CustomerReviewOutDTO { //Renad
    private String sender;

    private Integer rating;

    private String comment;

    private LocalDate createdAt;
}