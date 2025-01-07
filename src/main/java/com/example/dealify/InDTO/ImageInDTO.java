package com.example.dealify.InDTO;

import com.example.dealify.Model.Image;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
public class ImageInDTO {// Waleed
    private Set<Image> images;
}