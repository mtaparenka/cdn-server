package com.mtaparenka.cdnserver.model;

public record UploadData(byte[] content, String fileExtension) {
}
