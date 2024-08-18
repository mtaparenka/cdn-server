package com.mtaparenka.cdnserver.controller;

import com.mtaparenka.cdnserver.service.ImageService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ImageController {
    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @GetMapping(value = "/{imageName}", produces = {MediaType.IMAGE_JPEG_VALUE})
    public byte[] getFile(@PathVariable String imageName) {
        return imageService.readImage(imageName);
    }
}
