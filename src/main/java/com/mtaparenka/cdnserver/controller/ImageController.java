package com.mtaparenka.cdnserver.controller;

import com.mtaparenka.cdnserver.model.UploadData;
import com.mtaparenka.cdnserver.service.ImageService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ImageController {
    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @GetMapping(value = "/content/images/{imageName}", produces = {MediaType.IMAGE_JPEG_VALUE})
    public byte[] getImage(@PathVariable String imageName) {
        return imageService.readImage(imageName);
    }

    @PostMapping(value = "/content/images/upload", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public void uploadImage(@RequestBody UploadData uploadData) {
        imageService.writeImage(uploadData);
    }
}
