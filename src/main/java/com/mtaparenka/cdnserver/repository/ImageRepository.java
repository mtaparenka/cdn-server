package com.mtaparenka.cdnserver.repository;

import com.mtaparenka.cdnserver.model.UploadData;

public interface ImageRepository {
    byte[] readImage(String fileName);

    void writeImage(UploadData uploadData);
}
