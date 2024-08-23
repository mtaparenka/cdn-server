package com.mtaparenka.cdnserver.service;

import com.mtaparenka.cdnserver.config.ContentProperties;
import com.mtaparenka.cdnserver.model.UploadData;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

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

    public String writeImage(UploadData uploadData) {
        var fileName = UUID.randomUUID() + "." + uploadData.fileExtension();

        try (var stream = new FileOutputStream(
                "%s/%s".formatted(
                        contentProperties.images().path(),
                        fileName))) {
            stream.write(uploadData.content());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return fileName;
    }
}
