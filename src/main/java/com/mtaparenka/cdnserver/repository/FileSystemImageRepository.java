package com.mtaparenka.cdnserver.repository;

import com.mtaparenka.cdnserver.config.ContentProperties;
import com.mtaparenka.cdnserver.model.UploadData;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;
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
            return stream.readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void writeImage(UploadData uploadData) {
        var content = Base64.getDecoder().decode(uploadData.base64image());
        var fileName = UUID.randomUUID() + "." + getFileExtension(content);

        try (var stream = new FileOutputStream(
                "%s/%s".formatted(
                        contentProperties.images().path(),
                        fileName))) {

            stream.write(content);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String getFileExtension(byte[] content) {
        if (content.length < 4) {
            throw new RuntimeException("invalid file");
        }

        //ff d8...ff d9
        if (content[0] == -1 && content[1] == -40 && content[content.length-2] == -1 && content[content.length-1] == -39) {
            return "jpg";
        }

        throw new RuntimeException("unsupported file format");
    }
}
