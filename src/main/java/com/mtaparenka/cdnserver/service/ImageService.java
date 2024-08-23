package com.mtaparenka.cdnserver.service;

import com.mtaparenka.cdnserver.model.UploadData;
import com.mtaparenka.cdnserver.repository.ImageRepository;
import org.springframework.stereotype.Service;

@Service
public class ImageService {
    private final ImageRepository imageRepository;

    public ImageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    public byte[] readImage(String fileName) {
        return imageRepository.readImage(fileName);
    }

    public void writeImage(UploadData uploadData) {
        imageRepository.writeImage(uploadData);
    }
}
