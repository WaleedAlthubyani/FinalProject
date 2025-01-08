package com.example.dealify.Service;

import com.example.dealify.Api.ApiException;
import com.example.dealify.Model.Image;
import com.example.dealify.Repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;

    public List<Image> getAllImages(){//Waleed
        return imageRepository.findAll();
    }

    public void deleteImage(Integer imageId){//Waleed
        Image image=imageRepository.findImageById(imageId);
        if (image==null) throw new ApiException("Image not found");
    }
}
