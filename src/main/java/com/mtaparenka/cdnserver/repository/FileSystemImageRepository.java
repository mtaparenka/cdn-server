package com.mtaparenka.cdnserver.repository;

import com.mtaparenka.cdnserver.config.ContentProperties;
import com.mtaparenka.cdnserver.metadata.JpegMetadata;
import com.mtaparenka.cdnserver.model.UploadData;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.UUID;

@Component
public class FileSystemImageRepository implements ImageRepository {
    private final ContentProperties contentProperties;

    public FileSystemImageRepository(ContentProperties contentProperties) {
        this.contentProperties = contentProperties;
    }

    @Override
    public byte[] readImage(String fileName) {
        var file = new File(contentProperties.images().path() + "/" + fileName);

        try (var stream = new FileInputStream(file)) {
            var bytes = stream.readAllBytes();
            var metadata = JpegMetadata.read(bytes);
            return bytes;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void writeImage(UploadData uploadData) {
        var fileName = UUID.randomUUID() + "." + uploadData.fileExtension();

        try (var stream = new FileOutputStream(
                "%s/%s".formatted(
                        contentProperties.images().path(),
                        fileName))) {
            stream.write(uploadData.content());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
