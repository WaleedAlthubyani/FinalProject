package com.example.dealify.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Image {//Waleed
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

//    @NotEmpty(message = "Image URL is required.")
//    @Column(columnDefinition = "varchar(60) not null")
    private String imageUrl;

//    @NotEmpty(message = "Description is required.")
//    @Column(columnDefinition = "varchar(255) null")
    private String description;

    @ManyToOne
    @JsonIgnore
    private Product product;
}