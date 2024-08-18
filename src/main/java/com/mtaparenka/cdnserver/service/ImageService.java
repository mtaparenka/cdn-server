package com.mtaparenka.cdnserver.service;

import com.mtaparenka.cdnserver.config.ContentProperties;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@Service
public class ImageService {
    private final ContentProperties contentProperties;

    public ImageService(ContentProperties contentProperties) {
        this.contentProperties = contentProperties;
    }

    public byte[] readImage(String name) {
        var file = new File(contentProperties.images().path() + "/" + name);

        try (var stream = new FileInputStream(file)) {
            return stream.readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
