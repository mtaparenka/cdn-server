package com.mtaparenka.cdnserver.repository;

import com.mtaparenka.cdnserver.config.ContentProperties;
import com.mtaparenka.cdnserver.model.UploadData;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Stream;

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
            readMetadata(bytes);
            return bytes;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void readMetadata(byte[] bytes) {
        int app1Start = 0;
        int i = 0;
        var hexes = new String[bytes.length];

        for (int j = 0; j < bytes.length; j++) {
            hexes[j] = String.format("%02x", bytes[j]);
        }

        while (app1Start == 0) {
            if (Byte.toUnsignedInt(bytes[i]) == 0xff && Byte.toUnsignedInt(bytes[i+1]) == 0xe1) {
                app1Start = i;
            }
            i++;
        }

        int app1Length = ((bytes[app1Start + 2] & 0xff) << 8) | (bytes[app1Start + 3] & 0xff);


        var string = new String(Arrays.copyOfRange(bytes, app1Start, app1Length), StandardCharsets.UTF_8);
        System.out.println(string);
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
